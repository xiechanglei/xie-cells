package io.github.xiechanglei.cell.starter.rbac.core.provide;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * spring mvc 参数注解，获取当前登陆的用户Id
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserId {
    boolean required() default true;
}
