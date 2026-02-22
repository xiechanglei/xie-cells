package io.github.xiechanglei.cell.starter.web.resolver;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记一个类为WebConverter
 * 该注解用于标识一个类是Web参数解析器，通常用于处理请求和响应的转换逻辑，会自动被添加到Spring的上下文中。
 *
 * @author xie
 * @date 2024/12/26
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface WebConverter {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
