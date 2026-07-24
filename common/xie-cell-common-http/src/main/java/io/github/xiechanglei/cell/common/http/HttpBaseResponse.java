package io.github.xiechanglei.cell.common.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

/**
 * http 响应类
 *
 * @author xie
 * @date 2026/7/23
 */
@Getter
@RequiredArgsConstructor
public abstract class HttpBaseResponse<T> {
    protected final HttpResponse<T> response;

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

}
