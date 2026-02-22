package io.github.xiechanglei.cell.starter.web.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * HTTP 请求处理工具类
 * <p>
 * 提供了一系列静态方法用于在当前请求上下文中获取 HTTP 请求相关信息，包括：
 * </p>
 * <ul>
 * <li>获取当前 HTTP 请求对象 {@link HttpServletRequest}</li>
 * <li>获取当前 HTTP 响应对象 {@link HttpServletResponse}</li>
 * <li>获取客户端 IP 地址（支持代理服务器场景）</li>
 * <li>获取 Cookie 值</li>
 * </ul>
 * <p>
 * 该类依赖于 Spring 的 {@link RequestContextHolder} 来获取当前线程绑定的请求上下文，
 * 因此只能在 Web 请求处理线程中调用。
 * </p>
 *
 * @author xie
 * @date 2026/2/22
 */
public class RequestHandler {
    /**
     * 获取当前 HTTP 请求对象。
     * <p>
     * 该方法从 Spring 的 {@link RequestContextHolder} 中获取当前的 {@link ServletRequestAttributes}，
     * 然后提取 {@link HttpServletRequest} 对象。如果没有找到请求上下文或请求对象为空，
     * 则会抛出 {@link NullPointerException}。
     * </p>
     *
     * @return 当前的 {@link HttpServletRequest} 对象
     * @throws NullPointerException 当请求上下文为空或请求对象为空时抛出
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取当前 HTTP 响应对象。
     * <p>
     * 该方法从 Spring 的 {@link RequestContextHolder} 中获取当前的 {@link ServletRequestAttributes}，
     * 然后提取 {@link HttpServletResponse} 对象。如果没有找到请求上下文或响应对象为空，
     * 则会抛出 {@link NullPointerException}。
     * </p>
     *
     * @return 当前的 {@link HttpServletResponse} 对象
     * @throws NullPointerException 当请求上下文为空或响应对象为空时抛出
     */
    public static HttpServletResponse getCurrentResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * 获取当前请求方的 IP 地址。
     * <p>
     * 该方法按照以下优先级尝试获取客户端真实 IP 地址：
     * <ol>
     * <li>首先从 HTTP 头部中的 {@code x-forwarded-for} 获取，该头部通常用于在代理服务器后面时传递原始 IP 地址</li>
     * <li>如果无效，则尝试从 {@code Proxy-Client-IP} 头部获取</li>
     * <li>如果仍无效，则尝试从 {@code WL-Proxy-Client-IP} 头部获取</li>
     * <li>如果所有头部都无效，则返回 {@code request.getRemoteAddr()}，即直接连接到服务器的 IP 地址</li>
     * </ol>
     * </p>
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
     * </p>
     *
     * @param request 当前的 HTTP 请求对象，用于获取请求中的 Cookie
     * @param key     要查找的 Cookie 名称
     * @return 如果找到匹配的 Cookie，则返回其值；否则返回 null
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
     * 从当前请求上下文中获取指定名称的 Cookie 值。
     * <p>
     * 该方法适用于不方便传递 {@link HttpServletRequest} 对象的场景，
     * 内部通过 {@link #getCurrentRequest()} 获取当前请求对象。
     * </p>
     *
     * @param key Cookie 的名称
     * @return 如果找到匹配的 Cookie 则返回其值，否则返回 null
     * @see #getCurrentRequest()
     * @see #getCookie(HttpServletRequest, String)
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
