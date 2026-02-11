package io.github.xiechanglei.cell.common.lang.bytes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ByteArrayHelper单元测试
 */
class ByteArrayHelperTest {

    @Test
    void testBuild() {
        // 测试正常构建
        byte[] result = ByteArrayHelper.build(0x01, 0x02, 0x03);
        assertEquals(3, result.length);
        assertEquals((byte) 0x01, result[0]);
        assertEquals((byte) 0x02, result[1]);
        assertEquals((byte) 0x03, result[2]);

        // 测试空数组
        byte[] emptyResult = ByteArrayHelper.build();
        assertEquals(0, emptyResult.length);

        // 测试负数转换
        byte[] negativeResult = ByteArrayHelper.build(0xFF, 0x80); // 255, -128
        assertEquals((byte) 0xFF, negativeResult[0]);
        assertEquals((byte) 0x80, negativeResult[1]);
    }

    @Test
    void testHexToByteArray() {
        // 测试正常情况
        byte[] result = ByteArrayHelper.hexToByteArray("FF0A1B");
        assertEquals(3, result.length);
        assertEquals((byte) 0xFF, result[0]);
        assertEquals((byte) 0x0A, result[1]);
        assertEquals((byte) 0x1B, result[2]);

        // 测试带空格的情况
        byte[] spacedResult = ByteArrayHelper.hexToByteArray("FF 0A 1B");
        assertArrayEquals(result, spacedResult);

        // 测试带换行符的情况
        byte[] newlineResult = ByteArrayHelper.hexToByteArray("FF\n0A\r1B");
        assertArrayEquals(result, newlineResult);

        // 测试空字符串
        byte[] emptyResult = ByteArrayHelper.hexToByteArray("");
        assertEquals(0, emptyResult.length);

        // 测试null输入
        byte[] nullResult = ByteArrayHelper.hexToByteArray(null);
        assertEquals(0, nullResult.length);

        // 测试只有空格的字符串
        byte[] spacesResult = ByteArrayHelper.hexToByteArray("  \r\n  ");
        assertEquals(0, spacesResult.length);
    }

    @Test
    void testHexToByteArrayInvalidInput() {
        // 测试奇数长度的十六进制字符串
        assertThrows(IllegalArgumentException.class, () -> {
            ByteArrayHelper.hexToByteArray("FFF"); // 3个字符，奇数长度
        });

        // 测试单个字符
        assertThrows(IllegalArgumentException.class, () -> {
            ByteArrayHelper.hexToByteArray("F");
        });
    }

    @Test
    void testToHexString() {
        // 测试正常情况
        byte[] input = {(byte) 0xFF, (byte) 0x0A, (byte) 0x1B};
        String result = ByteArrayHelper.toHexString(input);
        assertEquals("FF0A1B", result);

        // 测试空数组
        String emptyResult = ByteArrayHelper.toHexString(new byte[0]);
        assertEquals("", emptyResult);

        // 测试null数组
        String nullResult = ByteArrayHelper.toHexString(null);
        assertEquals("", nullResult);

        // 测试单个字节
        String singleResult = ByteArrayHelper.toHexString(new byte[]{(byte) 0xAB});
        assertEquals("AB", singleResult);
    }

    @Test
    void testToHexStringWithEnd() {
        byte[] input = {(byte) 0xFF, (byte) 0x0A, (byte) 0x1B, (byte) 0xCD};

        // 测试取前两个字节
        String result = ByteArrayHelper.toHexString(input, 2);
        assertEquals("FF0A", result);

        // 测试取前零个字节
        String zeroResult = ByteArrayHelper.toHexString(input, 0);
        assertEquals("", zeroResult);

        // 测试超出数组长度的end值
        String overflowResult = ByteArrayHelper.toHexString(input, 10);
        assertEquals("FF0A1BCD", overflowResult);
    }

    @Test
    void testToHexStringWithStartAndEnd() {
        byte[] input = {(byte) 0xFF, (byte) 0x0A, (byte) 0x1B, (byte) 0xCD, (byte) 0x23};

        // 测试正常范围
        String result = ByteArrayHelper.toHexString(input, 1, 3);
        assertEquals("0A1B", result);

        // 测试从头开始
        String fromStartResult = ByteArrayHelper.toHexString(input, 0, 2);
        assertEquals("FF0A", fromStartResult);

        // 测试到末尾结束
        String toEndResult = ByteArrayHelper.toHexString(input, 3, 5);
        assertEquals("CD23", toEndResult);

        // 测试整个数组
        String fullResult = ByteArrayHelper.toHexString(input, 0, 5);
        assertEquals("FF0A1BCD23", fullResult);

        // 测试start和end相等（空结果）
        String emptyRangeResult = ByteArrayHelper.toHexString(input, 2, 2);
        assertEquals("", emptyRangeResult);

        // 测试end超过数组长度
        String overflowResult = ByteArrayHelper.toHexString(input, 2, 10);
        assertEquals("1BCD23", overflowResult);

        // 测试null数组
        String nullResult = ByteArrayHelper.toHexString(null, 1, 3);
        assertEquals("", nullResult);
    }

    @Test
    void testRoundTripConversion() {
        // 测试从字节数组转换为十六进制字符串再转换回字节数组的过程
        byte[] originalBytes = {(byte) 0xFF, (byte) 0x0A, (byte) 0x1B, (byte) 0xCD};

        // 转换为十六进制字符串
        String hexString = ByteArrayHelper.toHexString(originalBytes);

        // 再转换回字节数组
        byte[] convertedBytes = ByteArrayHelper.hexToByteArray(hexString);

        // 验证转换前后一致
        assertArrayEquals(originalBytes, convertedBytes);
    }

    @Test
    void testComplexRoundTripWithSpaces() {
        // 测试包含空格的十六进制字符串往返转换
        String originalHex = "FF 0A 1B CD";
        byte[] bytes = ByteArrayHelper.hexToByteArray(originalHex);
        String convertedHex = ByteArrayHelper.toHexString(bytes);

        assertEquals("FF0A1BCD", convertedHex);
    }
}