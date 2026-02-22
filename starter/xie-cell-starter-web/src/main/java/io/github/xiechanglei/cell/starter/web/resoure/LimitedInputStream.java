package io.github.xiechanglei.cell.starter.web.resoure;

import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends InputStream {
    private final InputStream inputStream;
    private long remaining;
    private long start;
    private long readIndex = 0;


    LimitedInputStream(InputStream inputStream, long start, long length) {
        this.inputStream = inputStream;
        this.remaining = length;
        this.start = start;
    }

    @Override
    public int read() throws IOException {
        Integer skipResult = skip();
        if (skipResult != null) return skipResult;
        int b = inputStream.read();
        if (b != -1) {
            remaining--;
        }
        return b;
    }

    @Nullable
    private Integer skip() throws IOException {
        if (remaining <= 0) {
            return -1;
        }
        // SKIP
        while (readIndex < start) {
            long skipped = inputStream.skip(start - readIndex);
            if (skipped <= 0) {
                if (inputStream.read() == -1) {
                    return -1;
                }
                readIndex++;
            } else {
                readIndex += skipped;
            }
        }
        return null;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Integer skipResult = skip();
        if (skipResult != null) return skipResult;
        int toRead = (int) Math.min(len, remaining);
        int read = inputStream.read(b, off, toRead);
        if (read > 0) {
            remaining -= read;
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
