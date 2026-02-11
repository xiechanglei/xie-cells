package io.github.xiechanglei.cell.common.lang.string;

import lombok.experimental.UtilityClass;

/**
 * 字符串工具类
 * 提供常用的字符串操作方法
 */
@UtilityClass
public class StringHelpers {

    /**
     * 检查字符串是否为空或null
     *
     * @param str 待检查的字符串
     * @return 如果字符串为null或空字符串则返回true，否则返回false
     */
    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 检查字符串是否为空白（null、空字符串或只包含空白字符）
     *
     * @param str 待检查的字符串
     * @return 如果字符串为null、空字符串或只包含空白字符则返回true，否则返回false
     */
    public boolean isBlank(String str) {
        if (isEmpty(str)) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两个字符串是否相同
     * 注意：如果两个字符串都为null，则认为它们相同
     *
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 如果两个字符串相同则返回true，否则返回false
     */
    public boolean isSame(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }
}