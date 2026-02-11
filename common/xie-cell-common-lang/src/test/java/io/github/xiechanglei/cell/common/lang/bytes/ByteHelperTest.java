package io.github.xiechanglei.cell.common.lang.bytes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ByteHelper单元测试
 */
class ByteHelperTest {

    @Test
    void testByteToHex() {
        // 测试正常情况
        assertEquals("01", ByteHelper.byteToHex((byte) 0x01));
        assertEquals("AB", ByteHelper.byteToHex((byte) 0xAB));

        // 测试边界值
        assertEquals("00", ByteHelper.byteToHex((byte) 0x00));
        assertEquals("FF", ByteHelper.byteToHex((byte) 0xFF));

        // 测试负数值（补码表示）
        assertEquals("80", ByteHelper.byteToHex((byte) 0x80)); // -128
        assertEquals("FE", ByteHelper.byteToHex((byte) 0xFE)); // -2
    }

    @Test
    void testHexToByte() {
        // 测试正常情况
        assertEquals((byte) 0x01, ByteHelper.hexToByte("01"));
        assertEquals((byte) 0xAB, ByteHelper.hexToByte("AB"));
        assertEquals((byte) 0xCD, ByteHelper.hexToByte("CD"));

        // 测试小写情况
        assertEquals((byte) 0xAB, ByteHelper.hexToByte("ab"));
        assertEquals((byte) 0xCD, ByteHelper.hexToByte("cd"));

        // 测试混合大小写
        assertEquals((byte) 0xA1, ByteHelper.hexToByte("A1"));
        assertEquals((byte) 0x2F, ByteHelper.hexToByte("2f"));
    }

    @Test
    void testRoundTripConversion() {
        // 测试从字节转换为十六进制再转换回字节的过程
        byte originalByte = (byte) 0xAB;
        String hexString = ByteHelper.byteToHex(originalByte);
        byte convertedByte = ByteHelper.hexToByte(hexString);

        assertEquals(originalByte, convertedByte);
    }

    @Test
    void testInvalidInput() {
        // 测试null输入
        assertThrows(IllegalArgumentException.class, () -> {
            ByteHelper.hexToByte(null);
        });

        // 测试长度小于2的字符串
        assertThrows(IllegalArgumentException.class, () -> {
            ByteHelper.hexToByte("F");
        });

        // 测试长度大于2的字符串
        assertThrows(IllegalArgumentException.class, () -> {
            ByteHelper.hexToByte("FFF");
        });

        // 测试无效的十六进制字符
        assertThrows(NumberFormatException.class, () -> {
            ByteHelper.hexToByte("FG"); // 'G' 不是有效的十六进制字符
        });
    }
}