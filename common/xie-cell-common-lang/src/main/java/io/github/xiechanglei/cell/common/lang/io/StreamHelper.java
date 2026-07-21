package io.github.xiechanglei.cell.common.lang.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/7/21
 */
public class StreamHelper {
    public static void transfer(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
            outputStream.flush();
        }
    }
}
