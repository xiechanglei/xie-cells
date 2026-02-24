package io.github.xiechanglei.cell.common.lang.secret;

/**
 * Aes加密工具类
 */
public class AESHelper {

    /**
     * 构造一个AesClient，用于加密解密，项目中推荐使用这种方式，而不是直接使用AESHelper，这样可以避免重复的一些字节转换操作
     *
     * @param key 密码
     * @param iv  偏移量
     * @return AesClient
     */
    public static AesClient buildClient(String key, String iv) {
        return new AesClient(key, iv, AESDigest.AES_CBC_PKCS5_PADDING);
    }

    public static AesClient buildClient(String key, String iv, String paddingType) {
        return new AesClient(key, iv, paddingType);
    }

    /**
     * Aes 加密,单纯的测试与验证的一些场景下推荐使用这种方式
     *
     * @param content  明文
     * @param password 密码
     * @param iv       偏移量
     * @return 密文 base64 格式
     * @throws Exception 加密异常
     */
    public static String encode(String content, String password, String iv) throws Exception {
        return buildClient(password, iv).encodeToString(content);
    }

    public static String encode(String content, String password, String iv, String paddingType) throws Exception {
        return buildClient(password, iv, paddingType).encodeToString(content);
    }

    /**
     * Aes 解密,单纯的测试与验证的一些场景下推荐使用这种方式
     *
     * @param content  密文  base64 格式
     * @param password 密码
     * @param iv       偏移量
     * @return 明文
     * @throws Exception 解密异常
     */
    public static String decode(String content, String password, String iv) throws Exception {
        return buildClient(password, iv).decodeToString(content);
    }

    public static String decode(String content, String password, String iv, String paddingType) throws Exception {
        return buildClient(password, iv, paddingType).decodeToString(content);
    }
}
