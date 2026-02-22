package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * 分段请求信息类
 *
 * @author xie
 * @date 2026/2/22
 */
@Getter
public class RangeRequestInfo {
    private boolean support;
    private long start;
    private long end;

    /**
     * 从当前请求获取 Range 头
     */
    public static RangeRequestInfo parse(ResourceInfo resourceInfo)  {
        RangeRequestInfo info = new RangeRequestInfo();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String range = attributes.getRequest().getHeader("Range");
            if (range != null && range.startsWith("bytes=")) {
                String[] parts = range.substring(6).split("-");
                try {
                    long contentLength = resourceInfo.resource() != null ? resourceInfo.resource().contentLength() : 0;
                    info.start = Long.parseLong(parts[0]);
                    if (parts.length > 1) {
                        info.end = Long.parseLong(parts[1]);
                    } else {
                        info.end = Math.min(info.start + resourceInfo.rangeSize(), contentLength - 1);
                    }
                    info.support = true;
                } catch (NumberFormatException | IOException ignored) {
                }
            }
        }
        return info;
    }
}
