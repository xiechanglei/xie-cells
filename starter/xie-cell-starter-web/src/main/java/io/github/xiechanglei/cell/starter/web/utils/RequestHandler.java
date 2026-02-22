package io.github.xiechanglei.cell.starter.web.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 实用工具类，用于获取当前 HTTP 请求和请求的 IP 地址。
 * 提供了静态方法来获取请求对象以及解析请求的 IP 地址，
 * 其中包括处理代理服务器可能带来的 IP 地址变化。
 */
public class RequestHandler {
    /**
     * 获取当前 HTTP 请求对象。
     * <p>
     * 该方法从 Spring 的 `RequestContextHolder` 中获取当前的 `ServletRequestAttributes`，
     * 然后提取 `HttpServletRequest` 对象。如果没有找到请求上下文或请求对象为空，
     * 则会抛出 `NullPointerException`。
     *
     * @return 当前的 `HttpServletRequest` 对象
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static HttpServletResponse getCurrentResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * 获取当前请求方的 IP 地址。
     * <p>
     * 该方法首先尝试从 HTTP 头部中的 `x-forwarded-for` 获取客户端 IP 地址，
     * 该头部通常用于在代理服务器后面时传递原始 IP 地址。如果该头部为空或无效，
     * 则尝试从其他常见的代理头部中获取 IP 地址，包括 `Proxy-Client-IP` 和 `WL-Proxy-Client-IP`。
     * 如果所有这些头部都没有提供有效的 IP 地址，则返回 `request.getRemoteAddr()`，
     * 该方法提供的是直接连接到服务器的 IP 地址。
     *
     * @return 请求方的 IP 地址，可能是直接 IP 地址或经过代理的 IP 地址
     */
    public static String getCurrentRequestIp() {
        HttpServletRequest request = getCurrentRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据指定的 Cookie 名称从 HTTP 请求中获取其对应的值。
     * <p>
     * 该方法遍历请求中所有的 Cookie，查找名称匹配的 Cookie。如果找到匹配的 Cookie，
     * 则返回其值；如果没有找到或请求中没有 Cookie，则返回 null。
     *
     * @param request 当前的 HTTP 请求对象，用于获取请求中的 Cookie。
     * @param key     要查找的 Cookie 名称。
     * @return 如果找到匹配的 Cookie，则返回其值；否则返回 null。
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 提供给一些不方便传递 request 的地方使用，从当前请求中获取指定的cookie
     *
     * @param key
     * @return
     */
    public static String getCookie(String key) {
        Cookie[] cookies = getCurrentRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
