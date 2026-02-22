package io.github.xiechanglei.cell.starter.web.resoure;

import java.io.IOException;
import java.io.InputStream;

public class LimitedInputStream extends InputStream {
    private final InputStream inputStream;
    private long remaining;

    LimitedInputStream(InputStream inputStream, long limit) {
        this.inputStream = inputStream;
        this.remaining = limit;
    }

    @Override
    public int read() throws IOException {
        if (remaining <= 0) {
            return -1;
        }
        int b = inputStream.read();
        if (b != -1) {
            remaining--;
        }
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (remaining <= 0) {
            return -1;
        }
        int toRead = (int) Math.min(len, remaining);
        int read = inputStream.read(b, off, toRead);
        if (read > 0) {
            remaining -= read;
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        try {
            inputStream.close();
        }catch (IOException e){
            if (!e.getMessage().contains("Broken pipe") && !e.getMessage().contains("ClientAbort")) {
                throw e;
            }
        }
    }
}
