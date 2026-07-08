package io.github.xiechanglei.cell.starter.web.serialize;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义对象格式输出
 *
 * @author xie
 * @date 2024/12/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface WebSerializer {
    /**
     * Bean 名称，默认为空（使用类名作为 Bean 名称）
     *
     * @return Bean 名称
     */
    @SuppressWarnings("UnusedReturnValue")
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * 目标类型，即该序列化器适用于哪个类型的对象。
     */
    Class<?>[] targetType();
}
