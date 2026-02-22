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
 * long 类型序列化为字符串，防止js丢失精度
 *
 * @author xie
 * @date 2024/12/24
 */
@Configuration
@RequiredArgsConstructor
public class CellWebJsonSerializeConfig implements ApplicationContextAware {
    private final CellJpaPageSerializer cellJpaPageSerializer;
    private final ObjectMapper objectMapper;
    WebDateSerializer webDateSerializer = new WebDateSerializer();
    WebLongSerializer webLongSerializer = new WebLongSerializer();

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
