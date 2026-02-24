package io.github.xiechanglei.cell.common.lang.secret;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * Aes加密解密操作类
 * 解决：
 * 1.字符串的统一编码问题
 * 2.base64问题
 * 3.key和iv格式化问题
 */
public class AesClient {
    /**
     * 默认的字符编码，在加密时候，用于将字符串转换为字节数组，解密时候，用于将字节数组转换为字符串
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 密码
     */
    private final byte[] key;

    /**
     * 偏移量
     */
    private final byte[] iv;

    /**
     * 加密填充方式
     */
    private final String paddingType;

    /**
     * 构造一个AesClient，用于加密解密，项目中推荐使用这种方式，而不是直接使用AESHelper，这样可以避免重复的一些字节转换操作
     *
     * @param key         密码
     * @param iv          偏移量
     * @param paddingType 加密填充方式
     */
    public AesClient(String key, String iv, String paddingType) {
        this.key = formatPass(key.getBytes(DEFAULT_CHARSET));
        this.iv = formatIv(iv.getBytes(DEFAULT_CHARSET));
        this.paddingType = paddingType;
    }

    public AesClient(byte[] key, byte[] iv, String paddingType) {
        this.key = formatPass(key);
        this.iv = formatIv(iv);
        this.paddingType = paddingType;
    }


    public byte[] encodeToByte(byte[] content) throws Exception {
        return AESDigest.encode(content, key, iv, paddingType);
    }

    public byte[] encodeToByte(String content) throws Exception {
        return encodeToByte(content.getBytes(StandardCharsets.UTF_8));
    }

    public String encodeToString(byte[] content) throws Exception {
        return Base64.getEncoder().encodeToString(encodeToByte(content));
    }

    public String encodeToString(String content) throws Exception {
        return Base64.getEncoder().encodeToString(encodeToByte(content.getBytes(StandardCharsets.UTF_8)));
    }


    public byte[] decodeToByte(byte[] content) throws Exception {
        return AESDigest.decode(content, key, iv, paddingType);
    }

    public byte[] decodeToByte(String content) throws Exception {
        return decodeToByte(Base64.getDecoder().decode(content));
    }

    public String decodeToString(byte[] content) throws Exception {
        return new String(decodeToByte(content), DEFAULT_CHARSET);
    }

    public String decodeToString(String content) throws Exception {
        return new String(decodeToByte(Base64.getDecoder().decode(content)), DEFAULT_CHARSET);
    }


    /**
     * 密码的长度必须是16字节，24字节，或者是32字节
     */
    private static byte[] formatPass(byte[] bytes) {
        if (bytes.length == 16 || bytes.length == 24 || bytes.length == 32) {
            return bytes;
        }
        if (bytes.length < 16) {
            return padding(bytes, 16 - bytes.length);
        }
        if (bytes.length < 24) {
            return padding(bytes, 24 - bytes.length);
        }
        if (bytes.length < 32) {
            return padding(bytes, 32 - bytes.length);
        }
        return Arrays.copyOf(bytes, 32);
    }

    /**
     * iv 长度大于16，则裁剪，小于16则填充
     */
    private static byte[] formatIv(byte[] bytes) {
        int length = 16 - bytes.length;
        if (length == 0) {
            return bytes;
        }
        if (length < 0) {
            return Arrays.copyOf(bytes, 16);
        }
        return padding(bytes, length);

    }

    /**
     * 填充二进制数组
     */
    private static byte[] padding(byte[] bytes, int length) {
        byte[] res = new byte[bytes.length + length];
        System.arraycopy(bytes, 0, res, 0, bytes.length);
        return res;
    }

}
