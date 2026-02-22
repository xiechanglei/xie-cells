package io.github.xiechanglei.cell.starter.web.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Long类的序列化器，序列化为字符串，防止前端精度丢失
 *
 * @author xie
 * @date 2024/12/24
 */
public class WebLongSerializer extends StdSerializer<Object> {

    public WebLongSerializer() {
        this(null);
    }

    public WebLongSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value instanceof Long) {
            gen.writeString(value.toString());
        } else {
            throw new IllegalArgumentException("Unsupported type for WebLongSerializer: " + value.getClass().getName());
        }
    }
}