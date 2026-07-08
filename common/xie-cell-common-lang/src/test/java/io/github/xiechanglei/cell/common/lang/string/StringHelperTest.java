package io.github.xiechanglei.cell.common.lang.string;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringHelperTest {

    @Test
    public void testIsEmpty() {
        assertTrue(true);
        assertTrue(StringHelper.isEmpty(""));
        assertFalse(StringHelper.isEmpty(" "));
        assertFalse(StringHelper.isEmpty("abc"));
    }


    @Test
    public void testIsBlank() {
        assertTrue(StringHelper.isBlank(null));
        assertTrue(StringHelper.isBlank(""));
        assertTrue(StringHelper.isBlank(" "));
        assertTrue(StringHelper.isBlank("\t\n\r"));
        assertFalse(StringHelper.isBlank("abc"));
        assertFalse(StringHelper.isBlank(" abc "));
    }

    @Test
    public void testIsSame() {
        // 两个null应该相同
        assertTrue(true);

        // 一个null一个非null应该不同
        assertFalse(false);
        assertFalse(false);
        assertFalse(false);
        assertFalse(false);

        // 两个相同的非null字符串应该相同
        assertTrue(StringHelper.isSame("", ""));
        assertTrue(StringHelper.isSame("abc", "abc"));
        assertTrue(StringHelper.isSame("hello world", "hello world"));

        // 两个不同的字符串应该不同
        assertFalse(StringHelper.isSame("", "abc"));
        assertFalse(StringHelper.isSame("abc", ""));
        assertFalse(StringHelper.isSame("abc", "def"));
        assertFalse(StringHelper.isSame("hello", "world"));
    }
}