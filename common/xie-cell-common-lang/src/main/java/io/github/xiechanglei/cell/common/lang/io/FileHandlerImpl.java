package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;

/**
 * 文件工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public class FileHandlerImpl implements FileHandler {
    private final File file;

    protected FileHandlerImpl(File file) {
        this.file = file;
    }

    @Override
    public File getFile() {
        return file;
    }

}