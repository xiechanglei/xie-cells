package io.github.xiechanglei.cell.common.http;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/7/24
 */
public interface HttpResponseAble<T> {

    HttpResponse<T> getResponse();

    /**
     * 获取响应的状态码
     */
    default int statusCode() {
        return getResponse().statusCode();
    }

    /**
     * 获取所有的响应头
     */
    default HttpHeaders headers() {
        return getResponse().headers();
    }

    /**
     * 获取响应的头
     */
    default String header(String name) {
        return getResponse().headers().firstValue(name).orElse("");
    }

    /**
     * 获取响应的Cookie,找到所有的set-cookie头
     */
    default Map<String, String> cookies() {
        return HttpUtils.covertSetCookies(headers().allValues("set-cookie"));
    }
}
