package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    /**
     * 获取文件的大小,如果是文件,则直接返回文件大小,如果是文件夹则返回文件夹下所有文件的大小之和
     */
    @Override
    public long size() {
        if (!file.exists()) {
            return 0;
        }
        if (file.isFile()) {
            return file.length();
        }
        if (file.isDirectory()) {
            long total = 0;
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    total += FileHandler.withFile(f).size();
                }
            }
            return total;
        }
        return 0;
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public List<FileHandler> filterChildren(Predicate<File> filter) {
        File[] files = file.listFiles();
        List<FileHandler> fileHandlers = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                if (filter.test(f)) {
                    fileHandlers.add(FileHandler.withFile(f));
                }
            }
        }
        return fileHandlers;
    }

    @Override
    public FileHandler createFile() throws IOException {
        makeFile(file);
        return this;
    }

    @Override
    public FileHandler createSubFile(String... paths) throws IOException {
        makeFile(new File(file, String.join(File.separator, paths)));
        return this;
    }

    @Override
    public FileHandler deleteFile() throws IOException {
        if (file.exists() && file.isFile()) {
            boolean delete = file.delete();
            if (!delete) {
                throw new IOException("Failed to delete file: " + file.getAbsolutePath());
            }
        }
        return this;
    }

    @Override
    public FileHandler removeChildren() throws IOException {
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
                    FileHandler.withFile(f).removeChildren();
                } else {
                    FileHandler.withFile(f).deleteFile();
                }
            }
        }
        return this;
    }

    @Override
    public FileHandler moveFrom(FileHandler fileHandler) throws IOException {
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
        return this;
    }

    @Override
    public FileHandler createDir() throws IOException {
        makeDir(file);
        return this;
    }

    @Override
    public FileHandler createSubDir(String... paths) throws IOException {
        makeDir(new File(file, String.join(File.separator, paths)));
        return this;
    }

    @Override
    public FileHandler withSubPath(String... paths) {
        return new FileHandlerImpl(new File(file, String.join(File.separator, paths)));
    }

    @Override
    public FileHandler withParent() {
        return new FileHandlerImpl(file.getParentFile());
    }


    private void makeFile(File file) throws IOException {
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

    private void makeDir(File file) throws IOException {
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