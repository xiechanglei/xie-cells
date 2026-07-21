package io.github.xiechanglei.cell.starter.web.file;

import io.github.xiechanglei.cell.common.lang.io.FileHandler;
import org.apache.tika.Tika;
import org.springframework.lang.NonNull;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 * 文件上传工具类
 *
 * @author xie
 * @date 2026/7/21
 */
public class FileUploadUtil {


    @NonNull
    public static String copyAndGetMd5(MultipartFile file, FileHandler tempFileHandler) throws IOException, NoSuchAlgorithmException {
        String md5;
        // copy and calculate md5
        try (InputStream is = file.getInputStream();
             java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFileHandler.getFile())) {

            // 先将上传的文件内容写入临时文件，并同时计算MD5
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(is, md5Digest);

            // 将数据从输入流复制到临时文件
            StreamUtils.copy(dis, fos);
            // 获取MD5值
            byte[] digest = md5Digest.digest();
            md5 = DigestUtils.md5DigestAsHex(digest);
        }
        return md5;
    }

    /**
     * 判断是否是一个正确的尺寸大小数字
     *
     * @param size 尺寸大小
     * @return 是否是一个正确的尺寸大小数字
     */
    public static boolean isValidateSize(Integer size) {
        return size != null && size > 0;
    }


    /**
     * 获取文件所在的子目录
     *
     * @param md5 文件的md5
     * @return 文件所在的子目录
     */
    @NonNull
    public static FileHandler getSubDirByCode(FileHandler parent, String md5) {
        return parent.withSubPath(md5.substring(0, 2), md5);
    }

    public static String getImageContentType(FileHandler fileHandler) throws IOException {
        Tika tika = new Tika();
        return tika.detect(fileHandler.getFile());
    }

    public static int[] getImageSize(FileHandler fileHandler) throws IOException {
        try (ImageInputStream iis = ImageIO.createImageInputStream(fileHandler.getFile())) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(iis);
                    // 仅读取宽和高，不解码像素
                    int width = reader.getWidth(0);
                    int height = reader.getHeight(0);
                    return new int[]{width, height};
                } finally {
                    reader.dispose();
                }
            }
        }
        throw new IOException("Failed to read image size: " + fileHandler.getFile());
    }

    public static FileHandler getImageFileWithSize(FileHandler file, Integer width, Integer height) {
        return file.withSubPath(width + "_" + height);
    }

    public static FileHandler getImageDescFile(FileHandler file) {
        return file.withSubPath("desc.txt");
    }
}
