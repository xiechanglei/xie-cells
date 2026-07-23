package io.github.xiechanglei.cell.common.lang.http;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * http 响应类
 *
 * @author xie
 * @date 2026/7/23
 */
public class HttpResponse {
    private final java.net.http.HttpResponse<InputStream> response;
    private boolean hasRead = false;

    public HttpResponse(java.net.http.HttpResponse<InputStream> response) {
        this.response = response;
    }

    /**
     * 获取响应的状态码
     */
    public int statusCode() {
        return response.statusCode();
    }

    /**
     * 获取响应的体
     */
    public String body() {
        if (!hasRead) {

        }
        return "";
    }

    public HttpResponse writeTo(OutputStream outputStream) {
        return this;
    }

    public HttpResponse writeTo(String filePath) {
        return this;
    }

    private void readResponseStream() {

    }

    /**
     * 获取响应的头
     */
    public String header(String name) {
        return "";
    }

    /**
     * 获取响应的Cookie
     */
    public String Cookie() {
        return "";
    }
}
