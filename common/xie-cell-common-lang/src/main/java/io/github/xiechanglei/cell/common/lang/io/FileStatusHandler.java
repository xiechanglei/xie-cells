package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;

/**
 * 文件工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public interface FileStatusHandler {


    /**
     * 获取文件
     *
     * @return 文件
     */
    File getFile();

    /**
     * 获取文件的大小,如果是文件,则直接返回文件大小,如果是文件夹则返回文件夹下所有文件的大小之和
     */
    default long size() {
        File file = getFile();
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


    /**
     * 文件是否存在
     *
     * @return 文件是否存在
     */
    default boolean exists() {
        return getFile().exists();
    }
    /**
     * 是否是空文件夹
     */
    default boolean isEmptyDir() {
        File file = getFile();
        if (!file.exists()) {
            return true;
        }
        File[] files = file.listFiles();
        return files == null || files.length == 0;
    }
}
