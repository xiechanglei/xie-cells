package io.github.xiechanglei.cell.starter.web.file;

import io.github.xiechanglei.cell.common.lang.io.FileHandler;
import io.github.xiechanglei.cell.common.lang.string.StringHelper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传属性配置
 * 约定:
 * 1. 上传目录下的file目录用于存储文件,文件名称使用md5分级存储,md5的前两位作为一级文件夹,然后文件存在该文件夹下,文件名使用md5值,后缀名使用原文件后缀名
 * 2. 上传目录下的image-temp目录用于存储图片的缩略图,与原始文件的存储目录结构相同:
 * 如原始文件为 3c/3c423423423423423423423423423423,
 * 则缩略的路径为 3c/3c423423423423423423423423423423/320_480
 * 图片的描述信息为 3c/3c423423423423423423423423423423/desc.txt  里面存储了文件的长度,宽度等信息
 *
 * @author xie
 * @date 2026/7/21
 */
@Component
@ConfigurationProperties(prefix = "cell.web.file")
@Log4j2
public class FileUploadProperties {
    private FileHandler STORE_DIR = null;
    private FileHandler IMAGE_TEMP_DIR = null;
    private FileHandler TEMP_DIR = null;
    /**
     * 上传的目录
     */
    @Getter
    @Setter
    private String uploadDir;

    /**
     * 最多对一个图片存在的缩略图的数量
     */
    @Getter
    @Setter
    private Integer maxThumbnailCount = 10;

    /**
     * 初始化目录
     */
    @PostConstruct
    public void init() {
        // maxThumbnailCount 最低为1
        maxThumbnailCount = maxThumbnailCount == null ? 10 : maxThumbnailCount;
        maxThumbnailCount = Math.max(maxThumbnailCount, 1);
        // 如果用户没有配置上传普目录,则默认存放在用户目录下的xie-cell-upload目录下
        if (StringHelper.isBlank(uploadDir)) {
            uploadDir = System.getProperty("user.home") + File.separator + "xie-cell-upload";
        }
        // 初始化上传目录
        try {
            STORE_DIR = FileHandler.withPath(uploadDir, "file").createDir();
            IMAGE_TEMP_DIR = FileHandler.withPath(uploadDir, "image-temp").createDir();
            TEMP_DIR = FileHandler.withPath(uploadDir, "temp").createDir();
            emptyTempDir();
        } catch (IOException e) {
            log.error("初始化上传目录失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件的存储路径
     */
    public FileHandler getStoreDir() {
        return STORE_DIR;
    }

    /**
     * 获取文件的临时图片路径
     */
    public FileHandler getImageTempDir() {
        return IMAGE_TEMP_DIR;
    }

    /**
     * 获取临时文件目录
     */
    public FileHandler getTempDir() {
        return TEMP_DIR;
    }

    /**
     * 清空用于上传的临时目录下的所有文件以及文件夹
     */
    private void emptyTempDir() {
        try {
            TEMP_DIR.removeChildren();
        } catch (IOException e) {
            log.error("清空临时目录失败", e);
        }
    }

//    static void main() throws IOException {
//        FileUploadProperties fileUploadProperties = new FileUploadProperties();
//        fileUploadProperties.init();
//        FileUploadService fileUploadService = new FileUploadService(fileUploadProperties);
//        fileUploadService.asImageResponse("xxx",100,300);
//    }
}
