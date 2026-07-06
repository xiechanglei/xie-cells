package io.github.xiechanglei.cell.starter.rbac.core.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 拦截器，拦截带有@RbacPermission 注解的方法，对当前登陆的用户进行权限校验，
 * 如果用户没有登陆，则返回未401状态码
 * 如果用户已登陆但没有权限，则返回403状态码
 *
 * @author xie
 * @date 2026/7/1
 */
@Order(0)
@Aspect
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "filter-auth", havingValue = "true", matchIfMissing = true)
@Log4j2
public class CellRbacPermissionAop {

    @Before("@annotation(io.github.xiechanglei.cell.starter.rbac.core.provide.RbacPermission)")
    public void before() {
        log.info("执行了CellRbacPermissionAop:before");
    }
}
