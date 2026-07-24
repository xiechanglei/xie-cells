package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public interface FileIoHandler extends FileOperateHandler {

    /**
     * 接收输入流
     *
     * @param inputStream 输入流
     * @return 文件工具类
     * @throws IOException 输入流异常
     */
    default FileHandler receiveStream(InputStream inputStream) throws IOException {
        File file = getFile();
        createFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            StreamHelper.transfer(inputStream, fos, 1024 * 8);
        }
        return (FileHandler) this;
    }
}
