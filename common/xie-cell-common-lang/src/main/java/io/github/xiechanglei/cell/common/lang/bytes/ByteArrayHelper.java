package io.github.xiechanglei.cell.common.lang.bytes;

/**
 * 字节数组工具类
 * 提供字节数组转换和操作的静态方法
 * 该类聚合了ByteArrayConvertor（转换功能）和ActionsForByteArray（操作功能）的功能
 * 通过此类可以访问所有的字节数组相关工具方法
 *
 * @author xie
 * @date 2026/2/11
 */
public class ByteArrayHelper {
    
    // 从ByteArrayConvertor继承的方法（实际上是委托调用）
    /**
     * 构建字节数组，将整数数组转换为字节数组
     *
     * @param bytes 整数形式的字节值数组
     * @return 转换后的字节数组
     */
    public static byte[] build(int... bytes) {
        return ByteArrayConvertor.build(bytes);
    }

    /**
     * 将十六进制字符串转换为字节数组
     * 十六进制字符串可以包含空格或换行符，会被自动忽略
     *
     * @param hexString 十六进制字符串，例如 "FF0A1B" 或 "FF 0A 1B"
     * @return 转换后的字节数组
     * @throws IllegalArgumentException 如果十六进制字符串长度不是偶数
     */
    public static byte[] hexToByteArray(String hexString) {
        return ByteArrayConvertor.hexToByteArray(hexString);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @return 表示字节数组的十六进制字符串
     */
    public static String toHexString(byte[] bytes) {
        return ByteArrayConvertor.toHexString(bytes);
    }

    /**
     * 将字节数组的一部分转换为十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @param end   结束索引（不包含）
     * @return 表示字节数组前end个元素的十六进制字符串
     */
    public static String toHexString(byte[] bytes, int end) {
        return ByteArrayConvertor.toHexString(bytes, end);
    }

    /**
     * 将字节数组指定范围内的部分转换为十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @param start 开始索引
     * @param end   结束索引（不包含）
     * @return 表示字节数组指定范围内元素的十六进制字符串
     */
    public static String toHexString(byte[] bytes, int start, int end) {
        return ByteArrayConvertor.toHexString(bytes, start, end);
    }
    
    // 私有构造函数防止实例化
    private ByteArrayHelper() {
        // 工具类不需要实例化
    }
}
