package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.NonNull;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 范围资源类，用于实现 HTTP 范围请求的资源包装器。
 * <p>
 * 该类包装了原始资源，并指定了资源的起始位置和长度，
 * 用于支持 HTTP 206 Partial Content 响应，实现视频拖拽、断点续传等功能。
 * </p>
 *
 * @author xie
 * @date 2026/2/20
 */
public class RangeResource implements Resource {
    /**
     * 原始资源对象
     */
    private final Resource source;

    /**
     * 范围起始位置（字节）
     */
    private final long start;

    /**
     * 范围长度（字节）
     */
    private final long length;

    /**
     * 构造范围资源对象。
     *
     * @param source 原始资源对象
     * @param start  起始字节位置
     * @param length 范围长度（字节数）
     */
    RangeResource(Resource source, long start, long length) {
        this.source = source;
        this.start = start;
        this.length = length;
    }

    @Override
    public boolean exists() {
        return source.exists();
    }

    @Override
    @NonNull
    public URL getURL() throws IOException {
        return source.getURL();
    }

    @Override
    @NonNull
    public URI getURI() throws IOException {
        return source.getURI();
    }

    @Override
    @NonNull
    public File getFile() throws IOException {
        return source.getFile();
    }

    @Override
    public long contentLength() {
        return length;
    }

    @Override
    public long lastModified() throws IOException {
        return source.lastModified();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        return source.createRelative(relativePath);
    }

    @Override
    public String getFilename() {
        return source.getFilename();
    }

    @Override
    public String getDescription() {
        return source.getDescription();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = source.getInputStream();
        return new LimitedInputStream(inputStream, start, length);
    }
}
