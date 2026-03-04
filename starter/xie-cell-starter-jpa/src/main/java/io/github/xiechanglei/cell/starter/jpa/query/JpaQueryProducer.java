package io.github.xiechanglei.cell.starter.jpa.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * jpa 查询的构建器,主要负责对查询条件进行构建
 *
 * @author xie
 * @date 2025/3/24
 */
public interface JpaQueryProducer {


    /**
     * 添加查询条件, 本质还是在拼接aql，不要有太多的期望
     * 注意，约定使用命名参数的形式进行参数绑定：   query.withCondition("and l.userId = :userId", userId);
     * 注意动态参数的个数 与顺序应该与condition中的命名参数一致
     * 不需要在condition前后添加空格，会自动添加
     * 也可以不添加参数，表达的是直接拼接sql: withCondition("and l.userId = 1")
     *
     * @param condition    查询条件
     * @param paramsValues 参数
     */
    JpaQueryProducer withCondition(String condition, Object... paramsValues);


    /**
     * 根据布尔值判断是否添加查询条件，注意动态参数的个数 与顺序应该与condition中的命名参数一致
     *
     * @param filter       是否添加
     * @param condition    查询条件
     * @param paramsValues 参数
     */
    default JpaQueryProducer withBoolCheckCondition(boolean filter, String condition, Object... paramsValues) {
        if (filter) {
            return withCondition(condition, paramsValues);
        }
        return this;
    }

    /**
     * 设置查询参数,可以传递多个，需要注意的是参数的格式应该是偶数个，奇数位是参数名，偶数位是参数值
     * 注意，约定使用命名参数的形式进行参数绑定：   query.param("userId", userId);  query.param("userId", userId, "name", name);
     */
    JpaQueryProducer param(Object... paramAndValue);

    /**
     * 组合设置查询条件，用以封装复杂的查询条件逻辑，进行复用
     */
    default JpaQueryProducer withConditionGroup(Consumer<JpaQueryProducer> conditionGroup) {
        conditionGroup.accept(this);
        return this;
    }

    /**
     * 执行更新操作
     */
    int executeUpdate();


    /**
     * 获取满足查询结果的总数
     */
    Long count();

    /**
     * 获取指定大小的结果集
     *
     * @param clazz 结果类型
     * @param skip  跳过的数量
     * @param limit 获取的数量
     * @param <T>   结果类型
     * @return 结果集
     */
    <T> List<T> getList(Class<T> clazz, int skip, int limit);


    /**
     * 获取一个结果，并且仅仅只能有一个结果，如果返回了多条，则抛出异常
     */
    default <T> Optional<T> getOnlyOne(Class<T> clazz) {
        List<T> list = getList(clazz, 0, 2);
        if (list.size() > 1) {
            throw new RuntimeException("expect one result, but found " + list.size());
        }
        return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
    }

    /**
     * 执行查询操作，获取一个结果，如果没有数据则返回null
     *
     * @param clazz 结果类型
     */
    default <T> Optional<T> getOne(Class<T> clazz) {
        return getOne(clazz, 0);
    }

    /**
     * 执行查询操作，跳过指定的数量之后，获取一个结果，如果没有数据则返回null
     */
    default <T> Optional<T> getOne(Class<T> clazz, int skip) {
        List<T> resultList = getList(clazz, skip, 1);
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.getFirst());
    }

    /**
     * 执行查询操作，获取一个结果，如果没有数据则返回null
     *
     * @param tupleConvertor 元组转换器
     */
    default <T> Optional<T> getTupleOne(TupleConvertor<T> tupleConvertor) {
        return getTupleOne(tupleConvertor, 0);
    }

    default <T> Optional<T> getTupleOne(TupleConvertor<T> tupleConvertor, int skip) {
        Optional<Object[]> one = getOne(null, skip);
        return one.map(tupleConvertor::convert);
    }

    default <T> Optional<T> getTupleOnlyOne(TupleConvertor<T> tupleConvertor) {
        Optional<Object[]> one = getOnlyOne(null);
        return one.map(tupleConvertor::convert);
    }

    /**
     * 执行查询操作，获取结果List
     *
     * @param clazz 结果类型
     */
    default <T> List<T> getList(Class<T> clazz) {
        return getList(clazz, 0);
    }

    default <T> List<T> getList(Class<T> clazz, int skip) {
        return getList(clazz, skip, -1);
    }

    /**
     * 执行查询操作，获取结果List
     *
     * @param tupleConvertor 元组转换器
     */
    default <T> List<T> getTupleList(TupleConvertor<T> tupleConvertor) {
        return tupleConvertor.convertList(getList(null));
    }

    default <T> List<T> getTupleList(TupleConvertor<T> tupleConvertor, int skip) {
        return tupleConvertor.convertList(getList(null, skip));
    }

    default <T> List<T> getTupleList(TupleConvertor<T> tupleConvertor, int skip, int limit) {
        return tupleConvertor.convertList(getList(null, skip, limit));
    }

    /**
     * 获取一个分页结果,注意，这里不会处理排序逻辑，需要自己在sql中提供
     * String.join(",", pageRequest.getSort().stream().map(order -> order.getProperty() + " " + order.getDirection()).toList());
     *
     * @param clazz       结果类型
     * @param pageRequest 分页请求
     */
    default <T> Page<T> getPage(Class<T> clazz, PageRequest pageRequest) {
        int skip = pageRequest.getPageNumber() * pageRequest.getPageSize();
        int limit = pageRequest.getPageSize();
        return new PageImpl<>(this.getList(clazz, skip, limit), pageRequest, this.count());
    }

    /**
     * 获取一个分页结果,注意，这里不会处理排序逻辑，需要自己在sql中提供
     * String.join(",", pageRequest.getSort().stream().map(order -> order.getProperty() + " " + order.getDirection()).toList());
     *
     * @param tupleConvertor 元组转换器
     * @param pageRequest    分页请求
     */
    default <T> Page<T> getTuplePage(TupleConvertor<T> tupleConvertor, PageRequest pageRequest) {
        Page<Object> page = getPage(null, pageRequest);
        return new PageImpl<>(tupleConvertor.convertList(page.getContent()), pageRequest, page.getTotalElements());
    }

}
