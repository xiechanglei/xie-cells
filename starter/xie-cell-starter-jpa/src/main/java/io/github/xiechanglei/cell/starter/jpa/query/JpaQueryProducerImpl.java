package io.github.xiechanglei.cell.starter.jpa.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JPA查询帮助类，主要是为了使动态查询更加方便，
 *
 * @author xie
 * @date 2025/2/14
 */
@SuppressWarnings("all")
public class JpaQueryProducerImpl implements JpaQueryProducer {
    /**
     * jpa的entityManager，需要提供
     **/
    private final EntityManager entityManager;
    /**
     * 是否是原生sql
     **/
    private final boolean isNative;
    /**
     * 查询语句
     **/
    private String queryString;
    /**
     * 查询参数
     **/
    private final Map<String, Object> params = new HashMap<>();

    protected JpaQueryProducerImpl(EntityManager entityManager, boolean isNative, String queryString) {
        this.entityManager = entityManager;
        this.isNative = isNative;
        this.queryString = queryString;
    }


    public JpaQueryProducer withCondition(String condition, Object... paramsValues) {
        List<String> strings = extractNamedParameters(condition);
        for (int i = 0; i < paramsValues.length; i++) {
            params.put(strings.get(i), paramsValues[i]);
        }
        this.queryString += condition.startsWith(" ") ? condition : " " + condition;
        return this;
    }

    @Override
    public JpaQueryProducer param(Object... value) {
        if (value.length % 2 != 0) {
            throw new IllegalArgumentException("The number of parameters must be even.");
        }
        for (int i = 0; i < value.length; i += 2) {
            params.put(value[i].toString(), value[i + 1]);
        }
        return this;
    }

    @Override
    public int executeUpdate() {
        return createJpaQuery(this.queryString, null).executeUpdate();
    }


    @Override
    public Long count() {
        return (Long) createJpaQuery(getCountQueryString(), null).getSingleResult();
    }

    @Override
    public <T> List<T> getList(Class<T> clazz, int skip, int limit) {
        Query jpaQuery = createJpaQuery(this.queryString, clazz);
        if (skip > 0) {
            jpaQuery.setFirstResult(skip);
        }
        if (limit != -1) {
            jpaQuery.setMaxResults(limit);
        }
        return jpaQuery.getResultList();
    }

    /**
     * 构建一个分页查询获取总数的查询
     */
    private String getCountQueryString() {
        String lowerCaseQuery = this.queryString.toLowerCase();
        int fromIndex = lowerCaseQuery.indexOf("from");
        int orderByIndex = lowerCaseQuery.lastIndexOf(" order ");
        if (fromIndex == -1) {
            throw new IllegalArgumentException("The query does not contain a FROM clause.");
        }
        return "SELECT COUNT(*) " + this.queryString.substring(fromIndex, orderByIndex == -1 ? this.queryString.length() : orderByIndex);
    }

    /**
     * 创建jpa查询对象
     *
     * @param clazz 结果类型
     * @return jpa查询对象
     */
    private Query createJpaQuery(String sql, Class<?> clazz) {
        Query query;
        if (isNative) {
            if (clazz == null) {
                query = entityManager.createNativeQuery(sql);
            } else {
                query = entityManager.createNativeQuery(sql, clazz);
            }
        } else {
            if (clazz == null) {
                query = entityManager.createQuery(sql);
            } else {
                query = entityManager.createQuery(sql, clazz);
            }
        }
        params.forEach(query::setParameter);
        return query;
    }

    /**
     * 提取condition中的命名参数
     *
     * @param sql the SQL string
     * @return a list of named parameters
     */
    private static List<String> extractNamedParameters(String sql) {
        List<String> namedParameters = new ArrayList<>();
        Pattern pattern = Pattern.compile(":(\\w+)");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String name = matcher.group(1);
            if (!namedParameters.contains(name)) {
                namedParameters.add(name);
            }
        }
        return namedParameters;
    }

}
