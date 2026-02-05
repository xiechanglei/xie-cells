package io.github.xiechanglei.cell.common.lang.string;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest {

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("abc"));
    }


    @Test
    public void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("\t\n\r"));
        assertFalse(StringUtils.isBlank("abc"));
        assertFalse(StringUtils.isBlank(" abc "));
    }

    @Test
    public void testIsSame() {
        // 两个null应该相同
        assertTrue(StringUtils.isSame(null, null));

        // 一个null一个非null应该不同
        assertFalse(StringUtils.isSame(null, ""));
        assertFalse(StringUtils.isSame("", null));
        assertFalse(StringUtils.isSame(null, "abc"));
        assertFalse(StringUtils.isSame("abc", null));

        // 两个相同的非null字符串应该相同
        assertTrue(StringUtils.isSame("", ""));
        assertTrue(StringUtils.isSame("abc", "abc"));
        assertTrue(StringUtils.isSame("hello world", "hello world"));

        // 两个不同的字符串应该不同
        assertFalse(StringUtils.isSame("", "abc"));
        assertFalse(StringUtils.isSame("abc", ""));
        assertFalse(StringUtils.isSame("abc", "def"));
        assertFalse(StringUtils.isSame("hello", "world"));
    }
}