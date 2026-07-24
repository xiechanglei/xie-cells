package io.github.xiechanglei.cell.common.http;

import lombok.RequiredArgsConstructor;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/7/24
 */
@RequiredArgsConstructor
public class FakeHttpStringResponse implements HttpResponse<String> {
    private final HttpResponse<?> response;
    private final String body;

    @Override
    public int statusCode() {
        return response.statusCode();
    }

    @Override
    public HttpRequest request() {
        return response.request();
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return response.headers();
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return response.sslSession();
    }

    @Override
    public URI uri() {
        return response.uri();
    }

    @Override
    public HttpClient.Version version() {
        return response.version();
    }
}
