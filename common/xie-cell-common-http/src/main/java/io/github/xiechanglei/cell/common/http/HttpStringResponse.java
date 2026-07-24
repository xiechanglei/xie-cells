package io.github.xiechanglei.cell.common.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.internal.ParseContextImpl;
import io.github.xiechanglei.cell.common.json.JsonHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpResponse;

/**
 * http 响应类  String 类型
 *
 * @author xie
 * @date 2026/7/23
 */
@RequiredArgsConstructor
public class HttpStringResponse implements HttpResponseAble<String> {
    private DocumentContext parse;
    @Getter
    private final HttpResponse<String> response;

    /**
     * 获取响应的体
     */
    public String body() {
        return response.body();
    }

    /**
     * 将响应体转换为指定类型的对象
     *
     * @param clazz 目标类型
     * @return 转换后的对象
     */
    public <T> T as(Class<T> clazz) {
        return JsonHelper.fromJson(response.body(), clazz);
    }

    /**
     * 将响应体转换为指定类型的对象 , TypeReference<T>
     */
    public <T> T as(TypeReference<T> typeReference) {
        return JsonHelper.fromJson(response.body(), typeReference);
    }

    /**
     * 使用 JSONPath 解析响应体
     *
     * @param jsonPath JSONPath 表达式
     */
    public Object read(String jsonPath) {
        if (this.parse == null) {
            this.parse = new ParseContextImpl().parse(this.body());
        }
        return this.parse.read(jsonPath);
    }
}
