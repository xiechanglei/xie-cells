package io.github.xiechanglei.cell.starter.web.serialize;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * JSON 序列化配置类。
 * <p>
 * 该类用于配置全局的 JSON 序列化规则，主要包括：
 * </p>
 * <ul>
 * <li>{@link Long} 类型序列化为字符串，防止前端 JavaScript 精度丢失</li>
 * <li>{@link Date} 类型序列化为时间戳字符串</li>
 * <li>{@link Page} 类型自定义序列化，简化返回结果</li>
 * </ul>
 * <p>
 * 该类通过实现 {@link ApplicationContextAware} 接口，在 Spring 容器初始化时
 * 自动注册自定义序列化器到 {@link ObjectMapper} 中。
 * </p>
 *
 * @author xie
 * @date 2024/12/24
 */
@Configuration
@RequiredArgsConstructor
@Log4j2
public class CellWebJsonSerializeConfig implements ApplicationContextAware {

    /**
     * Jackson ObjectMapper 对象
     */
    private final ObjectMapper objectMapper;

    /**
     * 设置 ApplicationContext 并注册自定义序列化器。
     * <p>
     * 该方法在 Spring 容器初始化时自动调用，用于将自定义序列化器
     * 注册到 ObjectMapper 中，使其在 JSON 序列化时生效。
     * </p>
     *
     * @param applicationContext Spring 应用上下文
     * @throws BeansException 如果获取 Bean 失败
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SimpleModule module = new SimpleModule();

        applicationContext.getBeansWithAnnotation(WebSerializer.class).values().forEach(se -> {
            WebSerializer annotation = se.getClass().getAnnotation(WebSerializer.class);
            if (se instanceof JsonSerializer) {
                log.info("注册自定义 JSON 序列化器: {}，目标类型: {}", se.getClass().getName(), annotation.targetType());
                for (Class<?> aClass : annotation.targetType()) {
                    module.addSerializer(aClass, (JsonSerializer) se);
                }
            } else {
                log.warn("自定义 JSON 序列化器 {} 未实现 JsonSerializer 接口，无法注册", se.getClass().getName());
            }
        });
        objectMapper.registerModule(module);
    }
}
