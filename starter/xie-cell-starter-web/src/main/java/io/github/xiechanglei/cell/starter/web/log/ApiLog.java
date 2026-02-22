package io.github.xiechanglei.cell.starter.web.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于记录 API 请求日志。
 * <p>
 * 该注解用于标记需要记录日志的接口方法。可以指定接口名称和需要入库的参数。
 * </p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLog {
    /**
     * 接口名称
     */

    String value() default "";

    /**
     * 标记需要被入库的参数
     */
    String[] params() default {};
}
