package io.github.xiechanglei.cell.starter.jpa.auto.base;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public interface JpaForkTask {

    default Object service(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return realTask(joinPoint, signature, method);
    }

    Object realTask(ProceedingJoinPoint joinPoint, MethodSignature signature, Method method) throws Throwable;

}
