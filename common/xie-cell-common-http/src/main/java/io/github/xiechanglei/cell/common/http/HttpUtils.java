package io.github.xiechanglei.cell.common.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 *
 * @author xie
 * @date 2026/7/24
 */
public class HttpUtils {
    /**
     * 将cookie字符串转换为map
     * cookies 的格式规范符合http 标准协议：
     * Set-Cookie: name1=value1; Path=/; Expires=Wed, 21 Oct 2025 07:28:00 GMT; Secure; HttpOnly
     */
    public static Map<String, String> covertSetCookies(List<String> cookieContent) {
        Map<String, String> cookieMap = new HashMap<>();
        cookieContent.forEach(cookie -> {
            String[] split = cookie.split(";");
            if (split.length > 0) {
                String[] keyValue = split[0].split("=");
                if (keyValue.length == 2) {
                    cookieMap.put(keyValue[0], keyValue[1]);
                }
            }
        });
        return cookieMap;
    }
}
