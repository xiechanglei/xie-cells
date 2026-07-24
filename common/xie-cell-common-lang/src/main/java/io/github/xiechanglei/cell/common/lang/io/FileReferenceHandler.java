package io.github.xiechanglei.cell.common.lang.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 文件工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public interface FileReferenceHandler extends FileStatusHandler {
    /**
     * 获取子路径
     *
     * @param paths 子路径
     * @return FileHandler
     */
    default FileHandler withSubPath(String... paths){
        return new FileHandlerImpl(new File(getFile(), String.join(File.separator, paths)));
    }

    /**
     * 获取父路径
     *
     * @return FileHandler
     */
    default FileHandler withParent() {
        return new FileHandlerImpl(getFile().getParentFile());
    }


    /**
     * 过滤获取子文件
     *
     * @param filter 过滤条件lamda表达式函数,函数接受File对象,返回boolean
     * @return 过滤后的子文件
     */
    default List<FileHandler> filterChildren(Predicate<File> filter) {
        File[] files = getFile().listFiles();
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
}
