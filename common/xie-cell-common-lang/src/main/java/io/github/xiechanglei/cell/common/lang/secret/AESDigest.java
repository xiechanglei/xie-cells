package io.github.xiechanglei.cell.common.lang.secret;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//AES 加密算法
public class AESDigest {
    /**
     * AES/CBC/PKCS5Padding 算法
     * todo(daiweipeng): 其他的算法集成与兼容
     */
    public static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    /**
     * AES加密，
     * 使用默认的 AES/CBC/PKCS5Padding 算法，
     * 参数说明: {@link AESDigest#aes(byte[], byte[], byte[], int, String)}
     */
    public static byte[] encode(byte[] byteContent, byte[] password, byte[] iv, String paddingType) throws Exception {
        return aes(byteContent, password, iv, Cipher.ENCRYPT_MODE, paddingType);
    }

    /**
     * AES解密
     * 使用默认的 AES/CBC/PKCS5Padding 算法，
     * 参数说明: {@link AESDigest#aes(byte[], byte[], byte[], int, String)}
     */
    public static byte[] decode(byte[] byteContent, byte[] password, byte[] iv, String paddingType) throws Exception {
        return aes(byteContent, password, iv, Cipher.DECRYPT_MODE, paddingType);
    }

    /**
     * aes编码过程，使用 AES/CBC/PKCS5Padding 算法，
     * 密码与偏移量可以使用 {@link AESHelper#formatPass(byte[]) 密钥格式化} 与 {@link AESHelper#formatIv(byte[]) iv格式化} 进行处理
     *
     * @param byteContent 待aes编码字节数组
     * @param password    密码字节数组，长度必须是16字节，24字节，或者是32字节
     * @param iv          偏移量字节数组 16字节
     * @param encryptMode aes模式  加密/解密
     * @return aes编码结果字节数组
     * @throws Exception aes编码异常
     */
    private static byte[] aes(byte[] byteContent, byte[] password, byte[] iv, int encryptMode, String paddingType) throws Exception {
        Cipher cipher = Cipher.getInstance(paddingType);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(password, "AES");
        cipher.init(encryptMode, key, zeroIv);
        return cipher.doFinal(byteContent);
    }
}
