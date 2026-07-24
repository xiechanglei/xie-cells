package io.github.xiechanglei.cell.common.http;

import lombok.Getter;

import java.net.http.HttpResponse;

/**
 * HTTP状态码异常
 * 当HTTP请求返回的响应状态码不是200时，抛出此异常
 *
 * @author xie
 * @date 2026/7/24
 */
public class HttpStatusException extends Exception implements HttpResponseAble<String> {
    @Getter
    private final HttpResponse<String> response;

    public HttpStatusException(String message, HttpResponse<String> response) {
        super(message);
        this.response = response;
    }

    public String body() {
        return response.body();
    }

}
