package io.github.xiechanglei.cell.common.lang.bytes;

/**
 * 字节操作辅助类
 * 提供字节与十六进制字符串之间的相互转换功能
 */
public class ByteHelper {

    /**
     * 将两位十六进制字符串转换为字节
     *
     * @param hexString 包含两位十六进制字符的字符串，例如 "FF", "0A"
     * @return 转换后的字节值
     * @throws IllegalArgumentException 如果输入字符串为null或长度不等于2
     */
    static byte hexToByte(String hexString) {
        if (hexString == null || hexString.length() != 2) {
            throw new IllegalArgumentException("hexString length must be 2");
        }
        return (byte) Integer.parseInt(hexString, 16);
    }

    /**
     * 将字节转换为两位十六进制字符串
     *
     * @param b 待转换的字节
     * @return 表示该字节的两位十六进制字符串，例如对于字节 0xFF 返回 "FF"
     */
    static String byteToHex(byte b) {
        return String.format("%02X", b);
    }
}