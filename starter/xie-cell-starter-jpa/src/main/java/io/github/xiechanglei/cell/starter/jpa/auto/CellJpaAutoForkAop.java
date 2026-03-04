package io.github.xiechanglei.cell.starter.jpa.auto;

import io.github.xiechanglei.cell.starter.jpa.auto.base.ExecuteResult;
import io.github.xiechanglei.cell.starter.jpa.auto.task.FindByIdJpaForkTask;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 根据注解自动实现对应方法的工具类，主要实现一些常用的功能：
 * 1. 根据 id 获取实体对象的方法，
 *
 * @author xie
 * @date 2026/3/4
 */
@Component
@Aspect
@Order
@RequiredArgsConstructor
public class CellJpaAutoForkAop {
    private final FindByIdJpaForkTask findByIdTask;

    /**
     * 处理根据 id 获取实体对象的方法，主要是根据方法的参数类型和参数值来获取对应的实体对象，并返回给调用者
     * <p>
     * 支持两种模式：
     * 1. 方法参数包含 {@link ExecuteResult}：由方法自行处理资源不存在的情况
     * 2. 方法参数不包含 {@link ExecuteResult}：资源不存在时自动抛出异常
     * </p>
     *
     * @param pjp 切点连接点
     * @return 查询到的实体对象或方法自定义的返回值
     */
    @Around("@annotation(io.github.xiechanglei.cell.starter.jpa.auto.task.FindById)")
    public Object aroundFindById(ProceedingJoinPoint pjp) throws Throwable {
        return findByIdTask.service(pjp);
    }
}
