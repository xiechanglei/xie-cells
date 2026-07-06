package io.github.xiechanglei.cell.starter.rbac.core.aop;

/**
 * 拦截器，拦截带有@RbacPermission 注解的方法，对当前登陆的用户进行权限校验，
 * 如果用户没有登陆，则返回未401状态码
 * 如果用户已登陆但没有权限，则返回403状态码
 *
 * @author xie
 * @date 2026/7/1
 */
public class RbacAop {
}
