package io.github.xiechanglei.cell.starter.web.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Date;

/**
 * Date类的序列化器，序列化为时间戳字符串
 *
 * @author xie
 * @date 2024/12/24
 */
public class WebDateSerializer extends StdSerializer<Object> {

    public WebDateSerializer() {
        this(null);
    }

    public WebDateSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value instanceof Date) {
            Date date = (Date) value;
            gen.writeString(Long.toString(date.getTime()));
        } else {
            throw new IllegalArgumentException("Unsupported type for WebLongSerializer: " + value.getClass().getName());
        }
    }
}