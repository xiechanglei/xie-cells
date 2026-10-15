package io.github.xiechanglei.cell.starter.web.file;

import io.github.xiechanglei.cell.common.bean.exception.BusinessException;
import io.github.xiechanglei.cell.common.lang.io.FileHandler;
import io.github.xiechanglei.cell.starter.web.resoure.CellResourceResponseHandler;
import io.github.xiechanglei.cell.starter.web.resoure.ResourceInfo;
import io.github.xiechanglei.cell.starter.web.resoure.ResourceMediaType;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传的服务类,需要自行编写controller,因为可能在不同的场景下需要使用不同的鉴权策略与数据记录,这里值提供文件的存储以及获取和流操作
 * 前置鉴权请自行开发代码
 * todo 文件下载的部分应该使用resourceHandler那边的代码进行代理执行
 *
 * @author xie
 * @date 2026/7/21
 */
@Service
@RequiredArgsConstructor
public class FileUploadService {
    public  static  final String THUMBNAIL_SUFFIX = "png";

    private final FileUploadProperties fileUploadProperties;

    private static final DateTimeFormatter HTTP_DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                    .withZone(ZoneId.of("GMT"));

    /**
     * 存储文件,并且返回文件的签名md5
     */
    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw BusinessException.of("上传文件不能为空");
        }

        // 使用配置的临时目录创建临时文件
        FileHandler tempFileHandler = fileUploadProperties.getTempDir().withSubPath(UUID.randomUUID().toString()).createFile();
        FileHandler storeFileHandler = null;
        try {
            String md5 = FileUploadUtil.copyAndGetMd5(file, tempFileHandler);
            storeFileHandler = FileUploadUtil.getSubDirByCode(fileUploadProperties.getStoreDir(), md5);
            if (!storeFileHandler.exists()) {
                storeFileHandler.moveFrom(tempFileHandler);
            }
            return md5;
        } catch (IOException | NoSuchAlgorithmException e) {
            if (storeFileHandler != null) {
                storeFileHandler.delete();
            }
            throw new RuntimeException(e);
        } finally {
            tempFileHandler.delete();
        }
    }

    /**
     * 根据md5获取文件
     *
     * @param md5 文件的md5
     * @return 文件的FileHandler
     */
    public FileHandler getFile(String md5) {
        return FileUploadUtil.getSubDirByCode(fileUploadProperties.getStoreDir(), md5);
    }


    /***
     * 将md5值对应的文件写入响应,主要用于下载
     * @param md5 文件的md5
     */
    public ResponseEntity<?> asResponse(String md5) {
        FileHandler fileHandler = getFile(md5);
        return CellResourceResponseHandler.useFile(fileHandler.getFile().getAbsolutePath());
    }

    /**
     * 图片服务,将md5值对应的文件写入响应,主要用于图片展示,
     * 1. 处理404
     * 2. 处理缓存机制
     * 3. 处理文件的缩略图生成
     *
     * @param md5       文件的md5
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     */
    public ResponseEntity<?> asImageResponse(String md5, Integer maxWidth, Integer maxHeight) throws IOException {
        FileHandler fileHandler = getImageFileHandler(md5, maxWidth, maxHeight);
        ResourceInfo resourceInfo = ResourceInfo.withFilePath(fileHandler.getFile().getAbsolutePath()).mediaType(ResourceMediaType.JPEG);
        return CellResourceResponseHandler.useResource(resourceInfo);
    }

    public FileHandler getImageFileHandler(String md5, Integer maxWidth, Integer maxHeight) throws IOException {
        boolean validateMaxWidth = FileUploadUtil.isValidateSize(maxWidth);
        boolean validateMaxHeight = FileUploadUtil.isValidateSize(maxHeight);
        FileHandler originalFileHandler = getFile(md5);

        // 如果文件不存在,直接返回原始文件,交给上层去判断
        if (!originalFileHandler.exists()) {
            return originalFileHandler;
        }

        // 如果两个尺寸都是非法的,直接返回原始文件
        if (!validateMaxWidth && !validateMaxHeight) {
            return originalFileHandler;
        }

        // 获取缩略图的工作目录
        FileHandler imageFileDirHandler = FileUploadUtil.getSubDirByCode(fileUploadProperties.getImageTempDir(), md5).createDir();
        // 如果两个尺寸都是合法的,尝试直接获取缩略图文件
        if (validateMaxWidth && validateMaxHeight) {
            FileHandler imageFileWithSize = FileUploadUtil.getImageFileWithSize(imageFileDirHandler, maxWidth, maxHeight);
            if (imageFileWithSize.exists()) {
                return imageFileWithSize;
            }
        }

        // 走到这里,需要真的去读取文件的尺寸然后生成缩略图了
        int[] imageSize = readImageSize(imageFileDirHandler, originalFileHandler);
        if (!validateMaxWidth || maxWidth > imageSize[0]) {
            maxWidth = imageSize[0];
        }
        if (!validateMaxHeight || maxHeight > imageSize[1]) {
            maxHeight = imageSize[1];
        }

        // 再次尝试修正尺寸之后获取缩略图文件,如果存在则直接返回文件
        FileHandler imageFileWithSize = FileUploadUtil.getImageFileWithSize(imageFileDirHandler, maxWidth, maxHeight);
        if (imageFileWithSize.exists()) {
            return imageFileWithSize;
        }

        // 真的真的需要生成并返回了
        Thumbnails.of(originalFileHandler.getFile())
                .size(maxWidth, maxHeight)
                .outputFormat(THUMBNAIL_SUFFIX)
                .toFile(imageFileWithSize.getFile());
        clearImageCache(imageFileDirHandler);
        return imageFileWithSize;
    }

    private void clearImageCache(FileHandler imageFileDirHandler) {
        // 最多只保留FileUploadProperties#maxThumbnailCount个缩略图
        Integer maxThumbnailCount = fileUploadProperties.getMaxThumbnailCount();
        List<FileHandler> fileHandlers = imageFileDirHandler.filterChildren(f -> f.isFile() && !f.getName().endsWith(".txt"));

        if (fileHandlers.size() <= maxThumbnailCount) {
            return;
        }
        // sort
        fileHandlers.sort((f1, f2) -> {
            long diff = f1.getFile().lastModified() - f2.getFile().lastModified();
            return diff > 0 ? 1 : (diff < 0 ? -1 : 0);
        });
        // delete the oldest files
        try {
            for (int i = 0; i < fileHandlers.size() - maxThumbnailCount; i++) {
                fileHandlers.get(i).delete();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private int[] readImageSize(FileHandler imageFileDirHandler, FileHandler originalFileHandler) throws IOException {
        FileHandler descFileHandler = FileUploadUtil.getImageDescFile(imageFileDirHandler);
        if (descFileHandler.exists()) {
            List<String> strings = Files.readAllLines(descFileHandler.getFile().toPath());
            if (strings.size() >= 2) {
                try {
                    int width = Integer.parseInt(strings.get(0));
                    int height = Integer.parseInt(strings.get(1));
                    return new int[]{width, height};
                } catch (Exception _) {
                }
            }
        }
        return generateImageSize(imageFileDirHandler, originalFileHandler);
    }

    private int[] generateImageSize(FileHandler imageFileDirHandler, FileHandler originalFileHandler) throws IOException {
        FileHandler descFileHandler = FileUploadUtil.getImageDescFile(imageFileDirHandler);
        int[] imageSize = FileUploadUtil.getImageSize(originalFileHandler);
        descFileHandler.createFile();
        Files.write(descFileHandler.getFile().toPath(), Arrays.asList(String.valueOf(imageSize[0]), String.valueOf(imageSize[1])));
        return imageSize;
    }

}
