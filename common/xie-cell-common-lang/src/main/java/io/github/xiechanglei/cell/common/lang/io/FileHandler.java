package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

/**
 * 文件工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public interface FileHandler {

    /**
     * 获取文件的大小,如果是文件,则直接返回文件大小,如果是文件夹则返回文件夹下所有文件的大小之和
     */
    long size();


    /**
     * 文件是否存在
     *
     * @return 文件是否存在
     */
    boolean exists();

    /**
     * 获取文件
     *
     * @return 文件
     */
    File getFile();

    /**
     * 过滤获取子文件
     *
     * @param filter 过滤条件lamda表达式函数,函数接受File对象,返回boolean
     * @return 过滤后的子文件
     */
    List<FileHandler> filterChildren(Predicate<File> filter);

    /**
     * 创建文件,如果不存在的话
     *
     * @return FileHandler
     */
    FileHandler createFile() throws IOException;

    /**
     * 创建子文件,
     *
     * @param paths 子路径
     * @return 返回的是当前文件的对象
     */
    FileHandler createSubFile(String... paths) throws IOException;


    /**
     * 创建文件夹,如果不存在的话
     *
     * @return FileHandler
     */
    FileHandler createDir() throws IOException;

    /**
     * 创建子文件夹,
     *
     * @param paths 子路径
     * @return 返回的是当前文件夹的对象
     */
    FileHandler createSubDir(String... paths) throws IOException;

    /**
     * 获取子路径
     *
     * @param paths 子路径
     * @return FileHandler
     */
    FileHandler withSubPath(String... paths);

    /**
     * 获取父路径
     *
     * @return FileHandler
     */
    FileHandler withParent();


    /**
     * 删除文件,仅仅当前文件是文件的时候才可以
     *
     * @return FileHandler
     */
    FileHandler deleteFile() throws IOException;

    /**
     * 删除子文件夹以及子文件
     *
     * @return FileHandler
     */
    FileHandler removeChildren() throws IOException;

    /**
     * 从其他位置移动一个文件过来
     *
     * @param fileHandler 文件位置
     * @return FileHandler
     */
    FileHandler moveFrom(FileHandler fileHandler) throws IOException;

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
