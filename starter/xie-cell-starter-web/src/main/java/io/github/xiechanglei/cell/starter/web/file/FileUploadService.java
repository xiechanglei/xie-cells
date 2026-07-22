package io.github.xiechanglei.cell.starter.web.file;

import io.github.xiechanglei.cell.common.bean.exception.BusinessException;
import io.github.xiechanglei.cell.common.lang.io.FileHandler;
import io.github.xiechanglei.cell.common.lang.io.StreamHelper;
import io.github.xiechanglei.cell.starter.web.utils.RequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传的服务类,需要自行编写controller,因为可能在不同的场景下需要使用不同的鉴权策略与数据记录,这里值提供文件的存储以及获取和流操作
 * 前置鉴权请自行开发代码
 *
 * @author xie
 * @date 2026/7/21
 */
@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final FileUploadProperties fileUploadProperties;
    public static final int DEFAULT_BUFFER_SIZE = 8192;


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
                storeFileHandler.deleteFile();
            }
            throw new RuntimeException(e);
        } finally {
            tempFileHandler.deleteFile();
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
    public void asResponse(String md5, String fileName) {
        HttpServletResponse response = RequestHandler.getCurrentResponse();
        FileHandler fileHandler = getFile(md5);
        if (fileHandler.exists()) {
            // attachment 以及中文文件名处理
            try (java.io.FileInputStream fis = new java.io.FileInputStream(fileHandler.getFile())) {
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                response.addHeader("Content-Type", "application/octet-stream");
                response.addHeader("Content-Length", String.valueOf(fileHandler.getFile().length()));

                // 使用缓冲区复制文件内容到响应输出流
                StreamHelper.transfer(fis, response.getOutputStream(), 1024 * 8);
                // 确保所有数据都被写入响应
                response.flushBuffer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * 图片服务,将md5值对应的文件写入响应,主要用于图片展示,
     * 1. 处理404
     * 2. 处理缓存机制
     * 3. 处理文件的缩略图生成
     */
    public void asImageResponse(String md5, Integer maxWidth, Integer maxHeight) throws IOException {
        FileHandler fileHandler = getImageFileHandler(md5, maxWidth, maxHeight);
        HttpServletResponse response = RequestHandler.getCurrentResponse();
        HttpServletRequest request = RequestHandler.getCurrentRequest();
        if (fileHandler.exists()) {
            // 缓存判断
            // ETag 使用文件名（MD5部分）
            String etag = "\"" + md5 + "\"";

            // 检查 If-None-Match，命中则返回 304
            String ifNoneMatch = request.getHeader(HttpHeaders.IF_NONE_MATCH);
            if (etag.equals(ifNoneMatch)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }

            // 设置缓存头
            response.setHeader(HttpHeaders.ETAG, etag);
            response.setHeader(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000, immutable");

            // Last-Modified
            long lastModified = fileHandler.getFile().lastModified();
            response.setHeader(HttpHeaders.LAST_MODIFIED, HTTP_DATE_FORMAT.format(Instant.ofEpochMilli(lastModified)));

            // Content-Type

            response.setContentType(FileUploadUtil.getImageContentType(fileHandler));
            // set content length
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileHandler.size()));

            // 写入响应体
            try (InputStream is = new FileInputStream(fileHandler.getFile())) {
                StreamHelper.transfer(is, response.getOutputStream(), 1024 * 8);
            }
            response.flushBuffer();

        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

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
                fileHandlers.get(i).deleteFile();
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
