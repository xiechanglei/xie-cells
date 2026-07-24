package io.github.xiechanglei.cell.common.http;

import lombok.Getter;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

/**
 * HTTP状态码异常
 * 当HTTP请求返回的响应状态码不是200时，抛出此异常
 *
 * @author xie
 * @date 2026/7/24
 */
public class HttpStatusException extends Exception {
    @Getter
    private final HttpResponse<String> response;

    public HttpStatusException(String message, HttpResponse<String> response) {
        super(message);
        this.response = response;
    }


    /**
     * 获取响应的状态码
     */
    public int statusCode() {
        return response.statusCode();
    }

    /**
     * 获取所有的响应头
     */
    public HttpHeaders headers() {
        return response.headers();
    }

    /**
     * 获取响应的头
     */
    public String header(String name) {
        return response.headers().firstValue(name).orElse("");
    }

    /**
     * 获取响应的Cookie
     */
    public String Cookie() {
        return response.headers().firstValue("Cookie").orElse("");
    }

    public String body() {
        return response.body();
    }

}
