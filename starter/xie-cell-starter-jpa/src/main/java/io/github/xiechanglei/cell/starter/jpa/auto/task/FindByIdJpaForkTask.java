package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.EntityInfo;
import io.github.xiechanglei.cell.starter.jpa.auto.base.ForkMethodHandler;
import io.github.xiechanglei.cell.starter.jpa.auto.base.JpaForkTask;
import io.github.xiechanglei.cell.starter.jpa.auto.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * findById 对应注解的实际处理类。
 * <p>
 * 支持字段过滤，可以只加载部分字段。
 * </p>
 *
 * @author xie
 * @date 2026/3/4
 */
@Component
@RequiredArgsConstructor
public class FindByIdJpaForkTask implements JpaForkTask {
    private final EntityManager em;

    @Override
    public Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable {
        // 获取注解
        FindById findById = method.getAnnotation(FindById.class);
        // 根据注解配置的参数名获取 ID 参数值
        Object id = ForkMethodHandler.getParamByName(joinPoint, signature, findById.idParamName());
        // 确定实体类
        EntityInfo entityInfo = ForkMethodHandler.resolveEntityClass(method, findById.value());

        String[] onlyFields = findById.onlyFields();
        String[] ignoreFields = findById.ignoreFields();

        // 判断是否需要进行字段过滤
        boolean needFilter = onlyFields.length > 0 || ignoreFields.length > 0;

        Object result;
        if (id == null) {
            result = null;
        } else if (needFilter) {
            // 使用 Criteria API + Tuple 进行字段过滤查询（带 ID 条件）
            String[] finalFields = resolveFinalFields(entityInfo.entityClass(), onlyFields, ignoreFields);
            TypedQuery<Tuple> tupleCriteriaQuery = queryWithFieldFilter(em, entityInfo.entityClass(), finalFields, (cq, root) -> {
                cq.where(em.getCriteriaBuilder().equal(root.get(entityInfo.idPropertyName()), id));
            });
            // add id condition
            result = tupleToEntity(tupleCriteriaQuery.getSingleResult(), finalFields, entityInfo.entityClass());
        } else {
            // 直接查询
            result = em.find(entityInfo.entityClass(), id);
        }

        // 处理查询结果
        if (result == null) {
            throw EntityNotFoundException.of(entityInfo.entityName() + "不存在");
        }

        return invokeOriginalMethod(joinPoint, method, result);
    }
}
