package io.github.xiechanglei.cell.common.lang.secret;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 类的详细说明
 * RFC4648
 * @author xie
 * @date 2024/9/3
 */
public class Base64Helper {
    /**
     * @param bytes 需要要编码的字节数组
     * @return 编码后的字节数组
     */
    public static byte[] encode(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    /**
     * @param str 需要要编码的字符串
     * @return 编码后的字节数组
     */
    public static byte[] encode(String str) {
        return Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param bytes 需要要编码的字节数组
     * @return 编码后的字符串
     */
    public static String encodeToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * @param str 需要要编码的字符串
     * @return 编码后的字符串
     */
    public static String encodeToString(String str) {
       return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param bytes 需要要解码的字节数组
     * @return 解码后的字节数组
     */
    public static byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    /**
     * @param base64 需要要解码的字符串
     * @return 解码后的字节数组
     */
    public static byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    /**
     * @param bytes 需要要解码的字节数组
     * @return 解码后的字符串
     */
    public static String decodeToString(byte[] bytes) {
        return new String(Base64.getDecoder().decode(bytes));
    }

    /**
     * @param base64 需要要解码的字符串
     * @return  解码后的字符串
     */
    public static String decodeToString(String base64) {
        return new String(Base64.getDecoder().decode(base64));
    }


}
