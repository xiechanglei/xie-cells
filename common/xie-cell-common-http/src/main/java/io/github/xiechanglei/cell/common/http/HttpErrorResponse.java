package io.github.xiechanglei.cell.common.http;

import java.net.http.HttpResponse;

/**
 * http 响应类  String 类型
 *
 * @author xie
 * @date 2026/7/23
 */
public class HttpErrorResponse extends HttpBaseResponse<String> {
    public HttpErrorResponse(HttpResponse<String> response) {
        super(response);
    }

    /**
     * 获取响应的体
     */
    public String body() {
        return response.body();
    }
}
