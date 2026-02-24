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
 * JPA Page 对象序列化器。
 * <p>
 * 该序列化器用于自定义 Spring Data JPA 的 {@link Page} 对象的 JSON 序列化规则，
 * 简化返回给前端的数据结构，只保留前端需要的分页信息。
 * </p>
 * <p>
 * 序列化后的 Page 对象包含以下字段：
 * </p>
 * <ul>
 * <li>{@code totalElements} - 总记录数</li>
 * <li>{@code totalPages} - 总页数</li>
 * <li>{@code pageNo} - 当前页码（从 1 开始，参数名可配置）</li>
 * <li>{@code pageSize} - 每页大小（参数名可配置）</li>
 * <li>{@code first} - 是否为第一页</li>
 * <li>{@code last} - 是否为最后一页</li>
 * <li>{@code content} - 当前页的数据列表</li>
 * </ul>
 * <p>
 * 通过这种方式，可以减少不必要的数据传输，同时使前端更容易处理分页数据。
 * </p>
 *
 * @author xie
 * @date 2024/12/24
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("all")
public class CellJpaPageSerializer extends JsonSerializer<Page> {
    /**
     * Web 参数解析器配置属性，用于获取分页参数名称
     */
    private final CellWebResolverConfigProperties webResolverConfigProperties;

    /**
     * 序列化 Page 对象。
     * <p>
     * 该方法将 Page 对象序列化为简化的 JSON 格式，只包含前端需要的分页信息。
     * </p>
     *
     * @param page       要序列化的 Page 对象
     * @param gen        JSON 生成器
     * @param serializers 序列化提供者
     * @throws IOException 如果写入 JSON 时发生 IO 错误
     */
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
