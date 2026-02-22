package io.github.xiechanglei.cell.starter.web.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.xiechanglei.cell.starter.web.resolver.CellWebResolverConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * page对象序列化器，删除不需要的一些字段
 *
 * @author xie
 * @date 2024/12/24
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("all")
public class CellJpaPageSerializer extends JsonSerializer<Page> {
    private final CellWebResolverConfigProperties webResolverConfigProperties;

    @Override
    public void serialize(Page page, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("totalElements", page.getTotalElements());
        gen.writeNumberField("totalPages", page.getTotalPages());
        gen.writeNumberField(webResolverConfigProperties.getPageNoName(), page.getNumber() + 1);
        gen.writeNumberField(webResolverConfigProperties.getPageSizeName(), page.getSize());
        gen.writeBooleanField("first", page.isFirst());
        gen.writeBooleanField("last", page.isLast());
        gen.writeFieldName("content");
        serializers.defaultSerializeValue(page.getContent(), gen);
        gen.writeEndObject();
    }
}
