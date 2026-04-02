package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.EntityInfo;
import io.github.xiechanglei.cell.starter.jpa.auto.base.ForkMethodHandler;
import io.github.xiechanglei.cell.starter.jpa.auto.base.JpaForkTask;
import io.github.xiechanglei.cell.starter.jpa.auto.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * findById对应注解的实际处理类
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
        // 执行查询结果
        Object result = (id == null) ? null : em.find(entityInfo.entityClass(), id);
        // 处理查询结果
        if (result == null) {
            throw EntityNotFoundException.of(entityInfo.entityName() + "不存在");
        }
        return invokeOriginalMethod(joinPoint, method, result);
    }
}
