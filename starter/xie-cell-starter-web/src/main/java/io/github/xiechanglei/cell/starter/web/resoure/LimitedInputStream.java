package io.github.xiechanglei.cell.starter.web.resoure;

import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * 限制输入流类，用于读取输入流的指定范围数据。
 * <p>
 * 该类包装了原始输入流，并限制了可读取的起始位置和长度，
 * 主要用于实现 HTTP 范围请求的数据读取，支持视频拖拽、断点续传等功能。
 * </p>
 *
 * @author xie
 * @date 2026/2/20
 */
public class LimitedInputStream extends InputStream {
    /**
     * 原始输入流
     */
    private final InputStream inputStream;

    /**
     * 剩余可读取的字节数
     */
    private long remaining;

    /**
     * 起始跳过字节数
     */
    private long start;

    /**
     * 已读取的字节索引
     */
    private long readIndex = 0;


    /**
     * 构造限制输入流对象。
     *
     * @param inputStream 原始输入流
     * @param start       起始字节位置
     * @param length      要读取的长度（字节数）
     */
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
