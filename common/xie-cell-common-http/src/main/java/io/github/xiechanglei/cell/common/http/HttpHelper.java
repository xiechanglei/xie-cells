package io.github.xiechanglei.cell.common.http;


import java.io.IOException;

/**
 * Http 工具类，内部封装jdk内置的HttpClient,使调用简单化
 *
 * @author xie
 * @date 2026/7/23
 */
public class HttpHelper {

    /**
     * 简单的发送一个get请求,并且将结果作为String 解析
     */
    public static HttpStringResponse get(String url) throws IOException, InterruptedException, HttpStatusException {
        return browserClient().method("GET").url(url).execute();
    }

    /**
     * 简单的发送一个post请求，携带请求体,并且将结果作为String 解析
     */
    public static HttpStringResponse post(String url, String body) throws IOException, InterruptedException, HttpStatusException {
        return browserClient().method("POST").url(url).body(body).execute();
    }

    /**
     * 简单的发送一个post请求，携带请求体,请求数据的格式为json，自动将请求数据转换为json格式
     */
    public static HttpStringResponse postJson(String url, Object data) throws IOException, InterruptedException, HttpStatusException {
        return browserClient().method("POST").url(url).json(data).execute();
    }

    /**
     * 创建一个HttpRequest对象，用于更复杂的请求
     */
    public static HttpRequestHandler browserClient() {
        return new HttpRequestHandler().header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
    }

    public static HttpRequestHandler client() {
        return new HttpRequestHandler();
    }

    static void main() throws HttpStatusException, IOException, InterruptedException {
        browserClient().url("https://www.google.com")
                .proxy("127.0.0.1", 58591)
                .followRedirects(true)
                .toFile("/home/xie/google.html");
    }
}
