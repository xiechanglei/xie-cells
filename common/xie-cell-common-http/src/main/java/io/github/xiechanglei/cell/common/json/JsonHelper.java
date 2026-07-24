package io.github.xiechanglei.cell.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 工具类，提供 JSON 序列化和反序列化功能
 *
 * @author xie
 * @date 2026/7/24
 */
public class JsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 配置 objectMapper
    static {
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 不输出 null 属性
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        // 允许使用非标准的 JSON 格式
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    /**
     * 将对象转换为 JSON 字符串
     * @param obj 待转换的对象
     * @return JSON 字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串转换为对象
     *
     * @param json  JSON 字符串
     * @param clazz 目标对象的类
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串转换为对象，type reference，用于处理泛型类型，比如List<String>:
     * JsonHelper.fromJson(json, new TypeReference<List<String>>() {});
     *
     * @param json          JSON 字符串
     * @param typeReference TypeReference 对象，用于指定泛型类型
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
