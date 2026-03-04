package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 资源响应辅助类，用于 Web 请求中的文件输出
 * <p>
 * 提供两种响应方式：
 * 1. useResource: 下载模式，Content-Disposition 为 attachment
 * 2. useMedia: 预览模式，Content-Disposition 为 inline，支持浏览器直接展示
 * </p>
 * <p>
 * useMedia 方法支持：
 * - HTTP 206 Partial Content 范围请求（视频拖拽、断点续传）
 * - HTTP 304 Not Modified 缓存验证
 * </p>
 *
 *
 * todo: 处理一些额外的请求参数，比如：
 * 1. 缩略图，height,width，quality 等参数，生成缩略图并响应
 * @author xie
 * @date 2026/2/12
 */
public class CellResourceResponseHandler {

    /**
     * 使用文件路径响应
     *
     * @param filePath 资源文件路径
     * @return 响应实体
     */
    public static ResponseEntity<?> useFile(String filePath) {
        return useResource(ResourceInfo.withFilePath(filePath));
    }

    /**
     * 使用资源说明进行响应（下载模式，自定义分段阈值）
     * <p>
     * Content-Disposition 为 attachment，浏览器会下载文件
     * 文件超过指定大小时自动启用分段下载支持
     * </p>
     *
     * @param resourceInfo 资源说明
     * @return 响应实体
     */
    public static ResponseEntity<?> useResource(ResourceInfo resourceInfo) {
        try {
            Resource resource = resourceInfo.resource();
            if (resource == null || !resource.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ResponseEntity<?> responseEntity = checkNotModified(resourceInfo);
            if (responseEntity != null) {
                return responseEntity;
            }

            // 检查是否是范围请求
            RangeRequestInfo rangeRequestInfo = RangeRequestInfo.parse(resourceInfo);
            if (rangeRequestInfo.isSupport()) {
                return handleRangeRequest(resourceInfo, rangeRequestInfo);
            }
            return handleFullContentRequest(resourceInfo);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 处理完整内容请求
     */
    private static ResponseEntity<?> handleFullContentRequest(ResourceInfo resourceInfo)
            throws IOException {
        HttpHeaders headers = new HttpHeaders();
        long contentLength = resourceInfo.resource().contentLength();
        long lastModified = resourceInfo.resource().lastModified();
        buildHeader(resourceInfo, lastModified, contentLength, headers);

        return new ResponseEntity<>(resourceInfo.resource(), headers, HttpStatus.OK);
    }

    /**
     * 处理范围请求（HTTP 206 Partial Content）
     */
    private static ResponseEntity<?> handleRangeRequest(ResourceInfo resourceInfo, RangeRequestInfo rangeRequestInfo) throws IOException {
        long start = rangeRequestInfo.getStart();
        long end = rangeRequestInfo.getEnd();
        long contentLength = resourceInfo.resource().contentLength();
        long lastModified = resourceInfo.resource().lastModified();

        long rangeLength = end - start + 1;
        if (start < 0 || end < start || end >= contentLength) {
            HttpHeaders errorHeaders = new HttpHeaders();
            errorHeaders.add(HttpHeaders.CONTENT_RANGE, "bytes */" + contentLength);
            return new ResponseEntity<>(errorHeaders, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + contentLength);
        buildHeader(resourceInfo, lastModified, rangeLength, headers);

        RangeResource rangeResource = new RangeResource(resourceInfo.resource(), start, rangeLength);
        return new ResponseEntity<>(rangeResource, headers, HttpStatus.PARTIAL_CONTENT);
    }

    /**
     * 写入文件响应头
     *
     * @param resourceInfo 资源说明
     * @param lastModified 资源最后修改时间
     * @param rangeLength  响应内容长度（范围请求时为范围长度，完整请求时为资源总长度）
     * @param headers      响应头对象，方法内会添加必要的头信息
     */
    private static void buildHeader(ResourceInfo resourceInfo, long lastModified, long rangeLength, HttpHeaders headers) {
        headers.add(HttpHeaders.CONTENT_TYPE, getMimeType(resourceInfo));
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength));
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.add(HttpHeaders.LAST_MODIFIED, formatLastModified(lastModified));

        String dispositionType = resourceInfo.inline() ? "inline" : "attachment";
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                dispositionType + "; filename=\"" + URLEncoder.encode(getResourceName(resourceInfo), StandardCharsets.UTF_8) + "\"");
    }

    /**
     * 检查资源是否被修改（304 Not Modified）
     */
    private static ResponseEntity<?> checkNotModified(ResourceInfo resourceInfo) throws IOException {
        long lastModified = resourceInfo.resource().lastModified();
        String ifModifiedSince = getIfModifiedSince();
        if (ifModifiedSince != null) {
            try {
                long clientTime = parseIfModifiedSince(ifModifiedSince);
                if (lastModified <= clientTime) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.LAST_MODIFIED, formatLastModified(lastModified));
                    return new ResponseEntity<>(headers, HttpStatus.NOT_MODIFIED);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    /**
     * 从当前请求获取 If-Modified-Since 头
     */
    private static String getIfModifiedSince() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest().getHeader(HttpHeaders.IF_MODIFIED_SINCE);
        }
        return null;
    }

    /**
     * 解析 If-Modified-Since 头（支持 RFC 1123 格式和毫秒时间戳）
     */
    private static long parseIfModifiedSince(String ifModifiedSince) {
        try {
            // 先尝试直接解析毫秒时间戳（兼容旧格式）
            return Long.parseLong(ifModifiedSince);
        } catch (NumberFormatException e) {
            // 尝试解析 RFC 1123 格式
            try {
                return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(ifModifiedSince))
                        .toEpochMilli();
            } catch (Exception ex) {
                return 0;
            }
        }
    }

    /**
     * 格式化 Last-Modified 头为 RFC 1123 格式
     * <p>
     * 例如：Wed, 21 Oct 2015 07:28:00 GMT
     * </p>
     *
     * @param lastModified 毫秒时间戳
     * @return RFC 1123 格式的日期字符串
     */
    private static String formatLastModified(long lastModified) {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(Instant.ofEpochMilli(lastModified).atOffset(ZoneOffset.UTC));
    }

    /**
     * 获取资源名称
     */
    private static @NonNull String getResourceName(ResourceInfo info) {
        String resourceName = info.resourceName();
        if (resourceName == null) {
            resourceName = "UnknownNameResource";
        }
        return resourceName;
    }

    /**
     * 获取资源的 MIME 类型
     */
    private static @NonNull String getMimeType(ResourceInfo info) {
        if (info.mediaType() != null) {
            return info.mediaType().getMimeType();
        }
        return ResourceMediaType.NONE.getMimeType();
    }
}
