package io.github.xiechanglei.cell.starter.web.resolver;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Web 转换器注解。
 * <p>
 * 该注解用于标识一个类是自定义的 Web 转换器，通常用于处理请求参数类型转换或格式化逻辑。
 * 被该注解标记的类会自动被添加到 Spring 的上下文中，并注册到转换器列表中。
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * {@code @WebConverter}
 * {@code @Component}
 * public class CustomConverter implements Converter<String, Date> {
 *     // 实现类型转换逻辑
 * }
 * </pre>
 * </p>
 *
 * @author xie
 * @date 2024/12/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface WebConverter {
    /**
     * Bean 名称，默认为空（使用类名作为 Bean 名称）
     *
     * @return Bean 名称
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
