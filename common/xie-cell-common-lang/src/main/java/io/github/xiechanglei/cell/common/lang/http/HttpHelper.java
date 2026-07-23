package io.github.xiechanglei.cell.common.lang.http;


/**
 * Http 工具类，内部封装jdk内置的HttpClient,使调用简单化
 *
 * @author xie
 * @date 2026/7/23
 */
public class HttpHelper {

    /**
     * 简单的发送一个get请求
     */
    public static HttpResponse get(String url) {
        return null;
    }

    /**
     * 简单的发送一个post请求，携带请求体
     */
    public static HttpResponse post(String url, String body) {
        return null;
    }

    /**
     * 简单的发送一个post请求，携带请求体,请求数据的格式为json，自动将请求数据转换为json格式
     */
    public static HttpResponse postJson(String url, Object data) {
        return null;
    }

    /**
     * 创建一个HttpRequest对象，用于更复杂的请求
     */
    public static HttpRequest newRequest(boolean browserMode) {
        HttpRequest httpRequest = new HttpRequest();
        if (browserMode) {
            httpRequest.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        }
        return httpRequest;
    }
}
