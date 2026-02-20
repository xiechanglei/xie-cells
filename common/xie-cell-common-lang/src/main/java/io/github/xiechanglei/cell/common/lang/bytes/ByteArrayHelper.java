package io.github.xiechanglei.cell.common.lang.bytes;

/**
 * 字节数组工具类
 *
 * @author xie
 * @date 2026/2/11
 */
public interface ByteArrayHelper {
    /**
     * 构建字节数组，将整数数组转换为字节数组
     *
     * @param bytes 整数形式的字节值数组
     * @return 转换后的字节数组
     */
    static byte[] build(int... bytes) {
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (byte) bytes[i];
        }
        return result;
    }

    /**
     * 将十六进制字符串转换为字节数组
     * 十六进制字符串可以包含空格或换行符，会被自动忽略
     *
     * @param hexString 十六进制字符串，例如 "FF0A1B" 或 "FF 0A 1B"
     * @return 转换后的字节数组
     * @throws IllegalArgumentException 如果十六进制字符串长度不是偶数
     */
    static byte[] hexToByteArray(String hexString) {
        if (hexString == null) {
            return new byte[0];
        }
        hexString = hexString.replaceAll("[\\s\r]+", "");
        if (hexString.isEmpty()) {
            return new byte[0];
        }
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("hexString length must be even");
        }
        byte[] result = new byte[hexString.length() / 2];
        char[] charArray = hexString.toCharArray();
        for (int i = 0; i < hexString.length(); i += 2) {
            result[i / 2] = (byte) Integer.parseInt(new String(new char[]{charArray[i], charArray[i + 1]}), 16);
        }
        return result;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @return 表示字节数组的十六进制字符串
     */
    static String toHexString(byte[] bytes) {
        return toHexString(bytes, 0, bytes.length);
    }

    /**
     * 将字节数组的一部分转换为十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @param end   结束索引（不包含）
     * @return 表示字节数组前end个元素的十六进制字符串
     */
    static String toHexString(byte[] bytes, int end) {
        return toHexString(bytes, 0, end);
    }

    /**
     * 将字节数组指定范围内的部分转换为十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @param start 开始索引
     * @param end   结束索引（不包含）
     * @return 表示字节数组指定范围内元素的十六进制字符串
     */
    static String toHexString(byte[] bytes, int start, int end) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        end = Math.min(end, bytes.length);
        for (int i = start; i < end; i++) {
            sb.append(ByteHelper.byteToHex(bytes[i]));
        }
        return sb.toString();
    }
}
