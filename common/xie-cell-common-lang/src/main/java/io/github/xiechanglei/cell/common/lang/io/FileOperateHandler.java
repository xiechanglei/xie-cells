package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public interface FileOperateHandler extends FileStatusHandler {

    /**
     * 创建文件,如果不存在的话
     *
     * @return FileHandler
     */
    default FileHandler createFile() throws IOException {
        FileOperateHandler.makeFile(getFile());
        return (FileHandler) this;
    }

    /**
     * 创建子文件,
     *
     * @param paths 子路径
     * @return 返回的是当前文件的对象
     */
    default FileHandler createSubFile(String... paths) throws IOException {
        makeFile(new File(getFile(), String.join(File.separator, paths)));
        return (FileHandler) this;
    }


    /**
     * 创建文件夹,如果不存在的话
     *
     * @return FileHandler
     */
    default FileHandler createDir() throws IOException {
        makeDir(getFile());
        return (FileHandler) this;
    }

    /**
     * 创建子文件夹,
     *
     * @param paths 子路径
     * @return 返回的是当前文件夹的对象
     */
    default FileHandler createSubDir(String... paths) throws IOException {
        makeDir(new File(getFile(), String.join(File.separator, paths)));
        return (FileHandler) this;
    }


    /**
     * 删除文件,仅仅当前文件是文件的时候才可以
     *
     * @return FileHandler
     */
    default FileHandler delete() throws IOException {
        File file = getFile();
        if (file.exists()) {
            if (file.isFile() || this.isEmptyDir()) {
                boolean delete = file.delete();
                if (!delete) {
                    throw new IOException("Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }
        return (FileHandler) this;
    }

    /**
     * 删除子文件夹以及子文件
     *
     * @return FileHandler
     */
    default FileHandler removeChildren() throws IOException {
        File file = getFile();
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getAbsolutePath());
        }
        if (!file.isDirectory()) {
            throw new IOException("File is not a directory: " + file.getAbsolutePath());
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    FileHandler.withFile(f).removeChildren().delete();
                } else {
                    FileHandler.withFile(f).delete();
                }
            }
        }
        return (FileHandler) this;
    }

    /**
     * 从其他位置移动一个文件过来
     *
     * @param fileHandler 文件位置
     * @return FileHandler
     */
    default FileHandler moveFrom(FileHandler fileHandler) throws IOException {
        File file = getFile();
        // 首先,自己不能存在
        if (file.exists()) {
            throw new IOException("File already exists: " + file.getAbsolutePath());
        }
        // 其次,目标文件不能不存在
        if (!fileHandler.exists()) {
            throw new IOException("File does not exist: " + fileHandler.getFile().getAbsolutePath());
        }
        // 创建自己的父级目录
        makeDir(file.getParentFile());
        // 移动
        boolean rename = fileHandler.getFile().renameTo(file);
        if (!rename) {
            throw new IOException("Failed to move file: " + file.getAbsolutePath());
        }
        return (FileHandler) this;
    }


    private static void makeFile(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File is a directory: " + file.getAbsolutePath());
            }
        } else {
            makeDir(file.getParentFile());
            boolean newFile = file.createNewFile();
            if (!newFile && !file.exists()) { // 如果创建失败并且文件不存在,则抛出异常
                throw new IOException("Failed to create file: " + file.getAbsolutePath());
            }
        }
    }

    private static void makeDir(File file) throws IOException {
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IOException("File is not a directory: " + file.getAbsolutePath());
            }
        } else {
            boolean mkdir = file.mkdirs();
            if (!mkdir && !file.exists()) { // 如果创建失败并且文件不存在,则抛出异常
                throw new IOException("Failed to create directory: " + file.getAbsolutePath());
            }
        }
    }
}
