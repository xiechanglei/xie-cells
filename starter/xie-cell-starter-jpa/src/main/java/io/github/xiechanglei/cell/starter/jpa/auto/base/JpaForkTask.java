package io.github.xiechanglei.cell.starter.jpa.auto.base;

import io.github.xiechanglei.cell.starter.jpa.auto.exception.EntityNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public interface JpaForkTask {

    default Object service(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return realTask(joinPoint, signature, method);
    }

    default Object invokeOriginalMethod(ProceedingJoinPoint joinPoint, Method method, Object result, EntityInfo entityInfo) throws Throwable {
        // 检查方法参数是否有 FindResult 类型
        int findResultParamIndex = ForkMethodHandler.findExecuteResultParameterIndex(method);
        // 方法有 FindResult 参数，让方法对处理查询结果进行二次处理后返回方法的返回值
        if (findResultParamIndex >= 0) {
            joinPoint.getArgs()[findResultParamIndex] = new ExecuteResult<>(result != null, result);
            return joinPoint.proceed(joinPoint.getArgs());
        } else {
            // 方法不包含查询结果,忽略方法返回值,直接返回查询结果,如果查询结果为 null 则抛出异常
            if (result == null) {
                throw EntityNotFoundException.of(entityInfo.entityName() + "不存在");
            }
            return result;
        }
    }

    Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable;

}
