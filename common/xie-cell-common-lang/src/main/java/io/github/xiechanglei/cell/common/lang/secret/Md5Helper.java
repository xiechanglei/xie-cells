package io.github.xiechanglei.cell.common.lang.secret;

import io.github.xiechanglei.cell.common.lang.bytes.ByteArrayHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//Md5 加密工具类
public class Md5Helper {
    /**
     * MD5加密
     *
     * @param str 待加密字符串
     * @return 加密后的字符串
     */
    public static String encode(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : md5) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 从文件中获取MD5
     *
     * @param filePath 文件路径
     * @return md5值
     */
    public static String getMd5FromFile(String filePath) throws IOException, NoSuchAlgorithmException {
        return getMd5FromFile(new File(filePath));
    }

    public static String getMd5FromFile(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        try (InputStream is = Files.newInputStream(file.toPath())) {
            byte[] buffer = new byte[1024 * 100];
            int read;
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        byte[] digest = md.digest();
        return ByteArrayHelper.toHexString(digest);
    }
}
