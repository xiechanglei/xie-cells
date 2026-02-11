package io.github.xiechanglei.cell.common.lang.string;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StringHelpersTest {

    @Test
    public void testIsEmpty() {
        assertTrue(StringHelpers.isEmpty(null));
        assertTrue(StringHelpers.isEmpty(""));
        assertFalse(StringHelpers.isEmpty(" "));
        assertFalse(StringHelpers.isEmpty("abc"));
    }


    @Test
    public void testIsBlank() {
        assertTrue(StringHelpers.isBlank(null));
        assertTrue(StringHelpers.isBlank(""));
        assertTrue(StringHelpers.isBlank(" "));
        assertTrue(StringHelpers.isBlank("\t\n\r"));
        assertFalse(StringHelpers.isBlank("abc"));
        assertFalse(StringHelpers.isBlank(" abc "));
    }

    @Test
    public void testIsSame() {
        // 两个null应该相同
        assertTrue(StringHelpers.isSame(null, null));

        // 一个null一个非null应该不同
        assertFalse(StringHelpers.isSame(null, ""));
        assertFalse(StringHelpers.isSame("", null));
        assertFalse(StringHelpers.isSame(null, "abc"));
        assertFalse(StringHelpers.isSame("abc", null));

        // 两个相同的非null字符串应该相同
        assertTrue(StringHelpers.isSame("", ""));
        assertTrue(StringHelpers.isSame("abc", "abc"));
        assertTrue(StringHelpers.isSame("hello world", "hello world"));

        // 两个不同的字符串应该不同
        assertFalse(StringHelpers.isSame("", "abc"));
        assertFalse(StringHelpers.isSame("abc", ""));
        assertFalse(StringHelpers.isSame("abc", "def"));
        assertFalse(StringHelpers.isSame("hello", "world"));
    }
}