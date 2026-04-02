package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.EntityInfo;
import io.github.xiechanglei.cell.starter.jpa.auto.base.ForkMethodHandler;
import io.github.xiechanglei.cell.starter.jpa.auto.base.JpaForkTask;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * findAll对应注解的实际处理类
 */
@Component
@RequiredArgsConstructor
public class FindAllJpaForkTask implements JpaForkTask {
    private final EntityManager em;

    @Override
    public Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable {
        // 获取注解
        FindAll findById = method.getAnnotation(FindAll.class);
        // 确定实体类
        EntityInfo entityInfo = ForkMethodHandler.resolveEntityClass(method, findById.value());
        // 查询实体对象

        EntityGraph<?> entityGraph = em.createEntityGraph(entityInfo.entityClass());
        entityGraph.addAttributeNodes("id");

        Object result = em.createQuery("SELECT e FROM " + entityInfo.entityClass().getName() + " e", entityInfo.entityClass())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
        return invokeOriginalMethod(joinPoint, method, result);
    }
}
