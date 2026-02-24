package io.github.xiechanglei.cell.starter.web.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Date;

/**
 * Date 类型序列化器。
 * <p>
 * 该序列化器将 {@link Date} 类型序列化为时间戳字符串（毫秒数），
 * 便于前端 JavaScript 直接使用和处理。
 * </p>
 * <p>
 * 例如：Date 对象会被序列化为字符串 "1609459200000"（2021-01-01 00:00:00 的毫秒时间戳）
 * 前端可以使用 `new Date(timestamp)` 直接还原为 Date 对象。
 * </p>
 *
 * @author xie
 * @date 2024/12/24
 */
public class WebDateSerializer extends StdSerializer<Object> {

    /**
     * 默认构造函数
     */
    public WebDateSerializer() {
        this(null);
    }

    /**
     * 带参数的构造函数。
     *
     * @param t 要序列化的类型
     */
    public WebDateSerializer(Class<Object> t) {
        super(t);
    }

    /**
     * 将 Date 值序列化为时间戳字符串。
     * <p>
     * 该方法将 Date 对象转换为其毫秒时间戳的字符串表示形式，
     * 便于前端 JavaScript 直接使用 `new Date(timestamp)` 还原。
     * </p>
     *
     * @param value    要序列化的值，必须是 Date 类型
     * @param gen      JSON 生成器
     * @param provider 序列化提供者
     * @throws IOException 如果写入 JSON 时发生 IO 错误
     * @throws IllegalArgumentException 如果传入的值不是 Date 类型
     */
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value instanceof Date) {
            Date date = (Date) value;
            gen.writeString(Long.toString(date.getTime()));
        } else {
            throw new IllegalArgumentException("Unsupported type for WebDateSerializer: " + value.getClass().getName());
        }
    }
}
