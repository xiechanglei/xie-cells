package io.github.xiechanglei.cell.starter.jpa.auto;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 根据注解自动实现对应方法的工具类，主要实现一些常用的功能：
 * 1. 根据id获取实体对象的方法，
 *
 * @author xie
 * @date 2026/3/4
 */
@Component
@Aspect
@Order // 设置切面优先级，数值越小优先级越高，这里默认是最大值，表示最低优先级
public class CellJpaAutoForkAop {

    /**
     * 处理根据id获取实体对象的方法，主要是根据方法的参数类型和参数值来获取对应的实体对象，并返回给调用者
     */
    @Around("@annotation(io.github.xiechanglei.cell.starter.jpa.auto.annotation.FindById)")
    public String AroundFindById(ProceedingJoinPoint pjp) {
        return "hello world";
    }
}

