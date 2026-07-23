package io.github.xiechanglei.cell.common.lang.http;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * http 请求类
 *
 * @author xie
 * @date 2026/7/23
 */

@Accessors(chain = true, fluent = true)
public class HttpRequest {
    @Setter
    // 请求的url
    private String url;
    @Setter
    // 请求的方法
    private String method;
    @Setter
    // 请求的参数
    private String body;
    // 请求header
    private Map<String, String> headers = new HashMap<>();

    /**
     * 添加header
     */
    public HttpRequest header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * 添加headers
     */
    public HttpRequest headers(Map<String, String> headers) {
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    /**
     * 设置cookie
     */
    public HttpRequest cookie(String name, String value) {
        headers.put("Cookie", name + "=" + value);
        return this;
    }

    /**
     * 设置referer, 该字段用于标识请求的来源页面
     */
    public HttpRequest referer(String referer) {
        headers.put("Referer", referer);
        return this;
    }

    /**
     * 设置content type
     */
    public HttpRequest contentType(String contentType) {
        headers.put("Content-Type", contentType);
        return this;
    }

    /**
     * 设置为json请求
     */
    public HttpRequest asJsonRequest() {
        return contentType("application/json");
    }

    public HttpResponse execute() throws IOException, InterruptedException {
        java.net.http.HttpRequest.Builder builder = java.net.http.HttpRequest.newBuilder();

        builder.uri(java.net.URI.create(url));
        builder.method(method, java.net.http.HttpRequest.BodyPublishers.ofString(body));

        java.net.http.HttpRequest request = builder.build();
        try (java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient()) {
            java.net.http.HttpResponse<InputStream> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofInputStream());
            return new HttpResponse(response);
        }
    }
}
