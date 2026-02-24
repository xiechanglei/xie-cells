package io.github.xiechanglei.cell.starter.web.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Long 类型序列化器。
 * <p>
 * 该序列化器将 {@link Long} 类型序列化为字符串格式，防止在前端 JavaScript 中
 * 因为 JavaScript 数字精度问题（JavaScript 只能安全表示 -2^53 到 2^53 之间的整数）
 * 导致 Long 类型精度丢失。
 * </p>
 * <p>
 * 例如：Long 值 1234567890123456789 会被序列化为字符串 "1234567890123456789"
 * 而不是数字 1234567890123456789（在 JavaScript 中会丢失精度）
 * </p>
 *
 * @author xie
 * @date 2024/12/24
 */
public class WebLongSerializer extends StdSerializer<Object> {

    /**
     * 默认构造函数
     */
    public WebLongSerializer() {
        this(null);
    }

    /**
     * 带参数的构造函数。
     *
     * @param t 要序列化的类型
     */
    public WebLongSerializer(Class<Object> t) {
        super(t);
    }

    /**
     * 将 Long 值序列化为字符串。
     * <p>
     * 该方法将 Long 对象转换为其字符串表示形式，确保在前端 JavaScript 中
     * 不会因为数字精度问题导致数据丢失。
     * </p>
     *
     * @param value    要序列化的值，必须是 Long 类型
     * @param gen      JSON 生成器
     * @param provider 序列化提供者
     * @throws IOException 如果写入 JSON 时发生 IO 错误
     * @throws IllegalArgumentException 如果传入的值不是 Long 类型
     */
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value instanceof Long) {
            gen.writeString(value.toString());
        } else {
            throw new IllegalArgumentException("Unsupported type for WebLongSerializer: " + value.getClass().getName());
        }
    }
}
