package io.github.xiechanglei.cell.starter.jpa.auto.base;

import io.github.xiechanglei.cell.common.bean.message.GlobalResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

public interface JpaForkTask {

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
        // 如果方法的返回值是 void ,表示不参与返回结算,否则返回方法的返回值
        if (method.getReturnType().equals(void.class)) {
            // 这里处理一下,因为spring mvc 的controller方法如果返回值是void,它会默认返回一个ResponseEntity.ok().build()的响应,
            if (GlobalResult.Result.isBound()) {
                GlobalResult.Result.get().setResult(result);
            }
            return result;
        }
        return proceed;
    }

    Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable;

}
