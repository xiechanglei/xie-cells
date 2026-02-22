package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * 范围请求信息类，用于封装 HTTP Range 请求的详细信息。
 * <p>
 * 当客户端发送范围请求（如视频拖拽、断点续传）时，该类用于解析和存储
 * 请求的起始位置和结束位置信息，支持 HTTP 206 Partial Content 响应。
 * </p>
 *
 * @author xie
 * @date 2026/2/22
 */
@Getter
public class RangeRequestInfo {
    /**
     * 是否支持范围请求
     */
    private boolean support;

    /**
     * 请求的起始字节位置
     */
    private long start;

    /**
     * 请求的结束字节位置
     */
    private long end;

    /**
     * 从当前 HTTP 请求中解析 Range 头信息。
     * <p>
     * 该方法从请求头中获取 Range 信息，格式通常为 "bytes=start-end"，
     * 解析后填充到 {@link RangeRequestInfo} 对象中。
     * </p>
     *
     * @param resourceInfo 资源说明对象，用于获取资源大小等信息
     * @return 解析后的范围请求信息对象
     */
    public static RangeRequestInfo parse(ResourceInfo resourceInfo) {
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
