package io.github.xiechanglei.cell.starter.rbac.core.provide;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * spring mvc 参数注解，用于获取当前登陆的用户
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface CurrentUser {
    // 是否必须
    boolean required() default true;
}
