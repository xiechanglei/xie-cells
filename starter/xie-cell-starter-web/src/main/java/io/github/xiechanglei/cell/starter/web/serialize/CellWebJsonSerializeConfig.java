package io.github.xiechanglei.cell.starter.web.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
public class CellWebJsonSerializeConfig implements ApplicationContextAware {
    /**
     * JPA Page 对象序列化器
     */
    private final CellJpaPageSerializer cellJpaPageSerializer;

    /**
     * Jackson ObjectMapper 对象
     */
    private final ObjectMapper objectMapper;

    /**
     * 日期序列化器
     */
    WebDateSerializer webDateSerializer = new WebDateSerializer();

    /**
     * Long 类型序列化器
     */
    WebLongSerializer webLongSerializer = new WebLongSerializer();

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

        module.addSerializer(Long.class, webLongSerializer);
        module.addSerializer(long.class, webLongSerializer);
        module.addSerializer(Date.class, webDateSerializer);
        module.addSerializer(Page.class, cellJpaPageSerializer);
        objectMapper.registerModule(module);
    }
}
