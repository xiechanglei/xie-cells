package io.github.xiechanglei.cell.common.http;

import io.github.xiechanglei.cell.common.json.JsonHelper;
import io.github.xiechanglei.cell.common.lang.io.FileHandler;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * http 请求类
 *
 * @author xie
 * @date 2026/7/23
 */

@Accessors(chain = true, fluent = true)
public class HttpRequestHandler {
    // 请求的url
    @Setter
    private String url;

    // 请求的方法
    @Setter
    private String method = "GET";

    // 请求的参数
    @Setter
    private String body;

    // 请求的超时时间
    @Setter
    private long readTimeout = 30 * 1000;

    @Setter
    private long connectTimeout = 30 * 1000;

    @Setter
    private boolean followRedirects = false;

    // proxy
    private InetSocketAddress proxyAddress;

    public HttpRequestHandler proxy(String host, int port) {
        this.proxyAddress = new InetSocketAddress(host, port);
        return this;
    }


    // 请求header
    private final Map<String, String> headers = new HashMap<>();

    /**
     * 添加header
     */
    public HttpRequestHandler header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * 添加headers
     */
    public HttpRequestHandler headers(Map<String, String> headers) {
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    /**
     * 设置cookie
     */
    public HttpRequestHandler cookie(String name, String value) {
        headers.put("Cookie", name + "=" + value);
        return this;
    }

    /**
     * 设置referer, 该字段用于标识请求的来源页面
     */
    public HttpRequestHandler referer(String referer) {
        headers.put("Referer", referer);
        return this;
    }

    /**
     * 设置content type
     */
    public HttpRequestHandler contentType(String contentType) {
        headers.put("Content-Type", contentType);
        return this;
    }

    /**
     * 设置为json请求
     */
    public HttpRequestHandler asJsonRequest() {
        return contentType("application/json");
    }

    /**
     * 设置请求体为json格式, 并设置content type为json,会自动将对象转换为json字符串
     *
     * @param data json数据
     * @return this
     */
    public HttpRequestHandler json(Object data) {
        return body(JsonHelper.toJson(data)).asJsonRequest();
    }


    //  === 最后的执行分歧

    /**
     * 执行请求并获取到最终的结果（String）
     */
    public HttpStringResponse execute() throws IOException, InterruptedException, HttpStatusException {
        try (HttpClient client = getClientBuilder().build()) {
            HttpResponse<String> response = client.send(getRequest(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() > 299) {
                throw new HttpStatusException("Http error status code: " + response.statusCode(), response);
            }
            return new HttpStringResponse(response);
        }
    }

    /**
     * 将响应体输出到文件中
     *
     * @param filePath 文件路径
     * @return 文件处理器
     */
    public FileHandler toFile(String filePath) throws IOException, InterruptedException, HttpStatusException {
        try (HttpClient client = getClientBuilder().build()) {
            HttpResponse<InputStream> response = client.send(getRequest(), HttpResponse.BodyHandlers.ofInputStream());
            try (InputStream inputStream = response.body()) {
                if (response.statusCode() < 200 || response.statusCode() > 299) {
                    String body = new String(inputStream.readAllBytes(), guessResponseCharset(response));
                    throw new HttpStatusException("Http error status code: " + response.statusCode(), new FakeHttpStringResponse(response, body));
                } else {
                    return FileHandler.withPath(filePath).receiveStream(inputStream);
                }
            }
        }
    }

    private String guessResponseCharset(HttpResponse<?> response) {
        String contentType = response.headers().firstValue("Content-Type").orElse("");
        String[] params = contentType.split(";");
        for (String param : params) {
            if (param.trim().startsWith("charset=")) {
                return param.trim().substring(8);
            }
        }
        return "UTF-8";
    }


    /**
     * 获取HttpClient.Builder对象
     *
     * @return HttpClient.Builder对象
     */
    private HttpClient.Builder getClientBuilder() {
        HttpClient.Builder clientBuilder = HttpClient.newBuilder();

        // 使用代理  127.0.0.1:1080
        if (proxyAddress != null) {
            clientBuilder.proxy(ProxySelector.of(proxyAddress));
        }
        //
        clientBuilder.connectTimeout(Duration.ofMillis(connectTimeout));
        if (followRedirects) {
            clientBuilder.followRedirects(HttpClient.Redirect.ALWAYS);
        } else {
            clientBuilder.followRedirects(HttpClient.Redirect.NEVER);
        }
        return clientBuilder;
    }

    /**
     * 获取HttpRequest对象
     *
     * @return HttpRequest对象
     */
    private HttpRequest getRequest() {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        // set url
        builder.uri(URI.create(url));
        // set headers
        headers.forEach(builder::header);
        // set method and body  todo  process file request
        if (body != null) {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body));
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }
        // set timeouts
        builder.timeout(java.time.Duration.ofMillis(readTimeout));
        return builder.build();
    }

}
