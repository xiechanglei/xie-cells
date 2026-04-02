package io.github.xiechanglei.cell.starter.jpa.auto.base;

import io.github.xiechanglei.cell.common.bean.message.GlobalResult;
import io.github.xiechanglei.cell.common.lang.reflect.ClassMemberHandler;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * JPA 任务处理接口，提供通用的字段过滤查询方法。
 *
 * @author xie
 * @date 2026/3/4
 */
public interface JpaForkTask {

    Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable;

    @Transactional
    default Object service(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return realTask(joinPoint, signature, method);
    }

    default Object invokeOriginalMethod(ProceedingJoinPoint joinPoint, Method method, Object result) throws Throwable {
        // 检查方法参数是否有 ExecuteResult 类型
        int findResultParamIndex = ForkMethodHandler.findExecuteResultParameterIndex(method);
        // 方法有 ExecuteResult 参数，让方法对处理查询结果进行二次处理后返回方法的返回值
        if (findResultParamIndex >= 0) {
            joinPoint.getArgs()[findResultParamIndex] = new ExecuteResult<>(result != null, result);
        }
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        // 如果方法的返回值是 void ,表示不参与返回结算，否则返回方法的返回值
        if (method.getReturnType().equals(void.class)) {
            // 这里处理一下，因为 spring mvc 的 controller 方法如果返回值是 void，它会默认返回一个 ResponseEntity.ok().build() 的响应
            if (GlobalResult.Result.isBound()) {
                GlobalResult.Result.get().setResult(result);
            }
            return result;
        }
        return proceed;
    }

    /**
     * 确定最终需要查询的字段。
     *
     * @param entityClass  实体类类型
     * @param onlyFields   只加载的字段列表
     * @param ignoreFields 忽略的字段列表
     * @return 最终需要查询的字段数组
     */
    default <T> String[] resolveFinalFields(Class<T> entityClass, String[] onlyFields, String[] ignoreFields) {
        if (onlyFields.length > 0) {
            // onlyFields 优先
            return onlyFields;
        } else {
            List<String> ignoreList = Arrays.asList(ignoreFields);
            // 获取所有字段，然后排除 ignoreFields
            return ClassMemberHandler.of(entityClass)
                    .withOutAnnotation(Transient.class, ManyToMany.class, ManyToOne.class, OneToMany.class, OneToOne.class)
                    .getFields()
                    .stream()
                    .map(Field::getName)
                    .filter(name -> !ignoreList.contains(name) && !name.equals("serialVersionUID"))
                    .toArray(String[]::new);
        }
    }

    /**
     * 使用 Criteria API + Tuple 进行字段过滤查询（查询所有实体）。
     *
     * @param em          实体管理器
     * @param entityClass 实体类类型
     * @param finalFields 需要加载的字段
     * @return 过滤后的实体列表
     */
    default TypedQuery<Tuple> queryWithFieldFilter(EntityManager em, Class<?> entityClass, String[] finalFields, BiConsumer<CriteriaQuery<Tuple>, Root<?>> conditionConsumer) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<?> root = cq.from(entityClass);


        // 构建字段选择
        List<Selection<?>> selections = Arrays.stream(finalFields)
                .map(root::get)
                .collect(Collectors.toList());
        cq.multiselect(selections);
        if (conditionConsumer != null) {
            conditionConsumer.accept(cq, root);
        }
        return em.createQuery(cq);
    }

    /**
     * 将 Tuple 结果映射到实体对象。
     *
     * @param tuple       Tuple 查询结果
     * @param fields      字段名列表
     * @param entityClass 实体类类型
     * @return 映射后的实体对象
     */
    default <T> T tupleToEntity(Tuple tuple, String[] fields, Class<T> entityClass) {
        try {
            // 创建实体实例（使用无参构造函数）
            T entity = entityClass.getDeclaredConstructor().newInstance();
            // 遍历字段并设置值
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i];
                Object value = tuple.get(i);
                ClassMemberHandler.setFieldValue(entity, fieldName, value);
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException("无法将 Tuple 映射到实体对象：" + entityClass.getName(), e);
        }
    }

    default <T> List<T> tuplesToEntity(List<Tuple> tuples, String[] fields, Class<T> entityClass) {
        return tuples.stream()
                .map(tuple -> tupleToEntity(tuple, fields, entityClass))
                .collect(Collectors.toList());
    }


}
