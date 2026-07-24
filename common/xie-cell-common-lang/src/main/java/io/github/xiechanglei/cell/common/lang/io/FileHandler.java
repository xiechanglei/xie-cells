package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;

/**
 * 文件工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public interface FileHandler extends FileIoHandler, FileReferenceHandler {

    /**
     * 构建一个FileHandler
     *
     * @param path  文件路径
     * @param paths 子路径
     * @return FileHandler
     */
    static FileHandler withPath(String path, String... paths) {
        return new FileHandlerImpl(new File(path, String.join(File.separator, paths)));
    }

    /**
     * 构建一个FileHandler
     *
     * @param file  文件
     * @param paths 子路径
     * @return FileHandler
     */
    static FileHandler withFile(File file, String... paths) {
        if (paths != null && paths.length > 0) {
            return new FileHandlerImpl(new File(file, String.join(File.separator, paths)));
        }
        return new FileHandlerImpl(file);
    }
}
