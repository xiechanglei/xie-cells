package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.EntityInfo;
import io.github.xiechanglei.cell.starter.jpa.auto.base.ForkMethodHandler;
import io.github.xiechanglei.cell.starter.jpa.auto.base.JpaForkTask;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * findAll 对应注解的实际处理类。
 * <p>
 * 使用 JPA Criteria API + Tuple 实现动态字段过滤，
 * 只查询指定字段并映射到实体对象返回。
 * </p>
 *
 * @author xie
 * @date 2026/3/4
 */
@Component
@RequiredArgsConstructor
public class FindAllJpaForkTask implements JpaForkTask {
    private final EntityManager em;

    @Override
    public Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable {
        // 获取注解
        FindAll findAll = method.getAnnotation(FindAll.class);
        // 确定实体类
        EntityInfo entityInfo = ForkMethodHandler.resolveEntityClass(method, findAll.value());

        String[] onlyFields = findAll.onlyFields();
        String[] ignoreFields = findAll.ignoreFields();

        // 判断是否需要进行字段过滤
        boolean needFilter = onlyFields.length > 0 || ignoreFields.length > 0;

        Object result;
        if (needFilter) {
            String[] finalFields = resolveFinalFields(entityInfo.entityClass(), onlyFields, ignoreFields);
            TypedQuery<Tuple> tupleCriteriaQuery = queryWithFieldFilter(em, entityInfo.entityClass(), finalFields, null);
            result = tuplesToEntity(tupleCriteriaQuery.getResultList(), finalFields, entityInfo.entityClass());
        } else {
            // 查询所有字段
            result = em.createQuery("SELECT e FROM " + entityInfo.entityClass().getName() + " e", entityInfo.entityClass())
                    .getResultList();
        }

        return invokeOriginalMethod(joinPoint, method, result);
    }
}
