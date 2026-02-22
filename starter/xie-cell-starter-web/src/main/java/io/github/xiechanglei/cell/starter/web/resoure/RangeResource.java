package io.github.xiechanglei.cell.starter.web.resoure;

import lombok.NonNull;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/2/20
 */
public class RangeResource implements Resource {
    private final Resource source;
    private final long start;
    private final long length;

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
        inputStream.skip(start);
        return new LimitedInputStream(inputStream, length);
    }
}
