package io.github.xiechanglei.cell.starter.web.file;

import io.github.xiechanglei.cell.common.bean.exception.BusinessException;
import io.github.xiechanglei.cell.common.lang.io.FileHandler;
import io.github.xiechanglei.cell.common.lang.io.StreamHelper;
import io.github.xiechanglei.cell.starter.web.utils.RequestHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
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

                // 使用缓冲区复制文件内容到响应输出流 todo 这个缓冲区大小是否需要调整,或者设置成参数
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
        boolean validateMaxWidth = FileUploadUtil.isValidateSize(maxWidth);
        boolean validateMaxHeight = FileUploadUtil.isValidateSize(maxHeight);

        FileHandler imageFileHandler = null;
        // 如果最大宽度和最大高度都是无效的,直接给源文件
        if (!validateMaxWidth && !validateMaxHeight) {
            imageFileHandler = getFile(md5);
        } else {
            // 否则生成缩略图
            // 首先将最大宽度和最大高度进行数字确认
            // 查看图片的描述文件是否存在
            FileHandler fileHandler = FileUploadUtil.getSubDirByCode(fileUploadProperties.getImageTempDir(), md5).withSubPath("desc.txt");
            // 如果描述文件存在,则读取文件内容,获取图片原始的宽高,文件的第一行是宽,第二行是高
            if (fileHandler.exists()) {
                List<String> strings = Files.readAllLines(fileHandler.getFile().toPath());
                int width = Integer.parseInt(strings.get(0));
                int height = Integer.parseInt(strings.get(1));
            } else {
                // 如果不存在,则生成描述文件
            }

            imageFileHandler = fileUploadProperties.getImageTempDir().withSubPath(md5);
            if (!imageFileHandler.exists()) {
                imageFileHandler.createFile();
                // 使用图片处理工具生成缩略图
                // 这里省略图片处理逻辑
            }
        }
    }

    private int[] readImageSize(FileHandler fileHandler) throws IOException {
        FileHandler descFileHandler = fileHandler.withSubPath("desc.txt");
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
        return generateImageSize(fileHandler);
    }

    private int[] generateImageSize(FileHandler fileHandler) throws IOException {
        FileHandler descFileHandler = fileHandler.withSubPath("desc.txt");
        BufferedImage img = Thumbnails.of(fileHandler.getFile()).asBufferedImage();
        int width = img.getWidth();
        int height = img.getHeight();

        descFileHandler.createFile();
        Files.write(descFileHandler.getFile().toPath(), Arrays.asList(String.valueOf(width), String.valueOf(height)));
        return new int[]{width, height};
    }

}
