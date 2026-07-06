package io.github.xiechanglei.cell.starter.rbac.web.provide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主要用于提取请求参数中的UserId，并且判断用户id对应的用户是否存在
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UserId {
}
