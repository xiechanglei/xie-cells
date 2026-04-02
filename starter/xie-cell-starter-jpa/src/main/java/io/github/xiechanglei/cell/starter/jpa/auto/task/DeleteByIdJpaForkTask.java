package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.EntityInfo;
import io.github.xiechanglei.cell.starter.jpa.auto.base.ForkMethodHandler;
import io.github.xiechanglei.cell.starter.jpa.auto.base.JpaForkTask;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * deleteById对应注解的实际处理类
 */
@Component
@RequiredArgsConstructor
public class DeleteByIdJpaForkTask implements JpaForkTask {
    private final EntityManager em;

    @Override
    public Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable {
        // 获取注解
        DeleteById deleteById = method.getAnnotation(DeleteById.class);
        // 根据注解配置的参数名获取 ID 参数值
        Object id = ForkMethodHandler.getParamByName(joinPoint, signature, deleteById.idParamName());
        // 确定实体类
        EntityInfo entityInfo = ForkMethodHandler.resolveEntityClass(method, deleteById.value());
        // 删除对象
        em.createQuery("delete from " + entityInfo.entityClass().getName() + " e where e." + entityInfo.idPropertyName() + " = :id")
                .setParameter("id", id)
                .executeUpdate();
        // 删除级联对象
        CascadeDelete[] cascadeDeletes = deleteById.cascadeDelete();
        for (CascadeDelete cascadeDelete : cascadeDeletes) {
            em.createQuery("delete from " + cascadeDelete.entity().getName() + " e where e." + cascadeDelete.property() + " = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        }
        return invokeOriginalMethod(joinPoint, method, null);
    }
}
