package io.github.xiechanglei.cell.common.http;


import java.io.IOException;
import java.util.HashMap;

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
    public static HttpRequestHandler get(String url) {
        return browserClient().method("GET").url(url);
    }

    /**
     * 简单的发送一个post请求，携带请求体,并且将结果作为String 解析
     */
    public static HttpRequestHandler post(String url, String body) {
        return browserClient().method("POST").url(url).body(body);
    }

    /**
     * 简单的发送一个post请求，携带请求体,请求数据的格式为json，自动将请求数据转换为json格式
     */
    public static HttpRequestHandler postJson(String url, Object data) {
        return browserClient().method("POST").url(url).json(data);
    }

    /**
     * 创建一个HttpRequest对象，用于更复杂的请求
     */
    public static HttpRequestHandler browserClient() {
        return new HttpRequestHandler().userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
    }

    public static HttpRequestHandler client() {
        return new HttpRequestHandler();
    }

    static void main() throws HttpStatusException, IOException, InterruptedException {

        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "你好");
        body.put("user_id", "user_hefei_001");
        body.put("agent_name", "滁州");
        postJson("https://taas.it.10086.cn/zyluat/dth5/wl-agent/agent/stream", body)
                .sse(str -> System.out.println("receive: " + str));
    }
}
