package io.github.xiechanglei.cell.starter.web.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置并注册自定义的请求参数解析器。
 * <p>
 * 该类实现了 {@link WebMvcConfigurer} 接口，主要用于向 Spring MVC 框架注册自定义的
 * {@link HandlerMethodArgumentResolver} 实现，用于解析特定类型的请求参数。
 */
@Log4j2
@Configuration
@RequiredArgsConstructor
public class CellWebResolverConfiguration implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    /**
     * 注册自定义的请求参数解析器。
     * <p>
     * 根据配置属性 {@link CellWebResolverConfigProperties} 的设置，决定是否添加日期
     * 类型解析器和分页解析器到解析器列表中。若相关配置项为 true，则对应的解析器将被注册。
     *
     * @param resolvers 当前应用中所有的 {@link HandlerMethodArgumentResolver} 实例的列表。
     *                  在此方法中，能够将自定义的解析器添加到此列表中，使其能在请求处理过程中使用。
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        applicationContext.getBeansWithAnnotation(WebResolver.class).values().forEach(resolver -> {
            if (resolver instanceof HandlerMethodArgumentResolver) {
                resolvers.add((HandlerMethodArgumentResolver) resolver);
                log.info("已注册自定义参数解析器: {}", resolver.getClass().getName());
            } else {
                log.warn("自定义参数解析器 {} 未实现 HandlerMethodArgumentResolver 接口，无法注册", resolver.getClass().getName());
            }
        });
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        applicationContext.getBeansWithAnnotation(WebConverter.class).values().forEach(resolver -> {
            if (resolver instanceof org.springframework.core.convert.converter.Converter) {
                registry.addConverter((org.springframework.core.convert.converter.Converter<?, ?>) resolver);
                log.info("已注册自定义转换器: {}", resolver.getClass().getName());
            } else {
                log.warn("自定义转换器 {} 未实现 Converter 接口，无法注册", resolver.getClass().getName());
            }
        });
    }
}
