package io.github.xiechanglei.cell.common.lang.array;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/3/4
 */
public class ArrayHelper {

    /**
     * 合并数组,
     */
    @SafeVarargs
    public static <T> T[] concat(T[]... arrays) {
        List<T> list = new ArrayList<>();
        for (T[] array : arrays) {
            if (array != null) {
                Collections.addAll(list, array);
            }
        }
        return list.toArray(arrays[0]);
    }

    // 去重数组
    @SuppressWarnings("unchecked")
    public static <T> T[] distinct(T[] arr) {
        TreeSet<T> set = new TreeSet<>(Arrays.asList(arr));
        T[] res = (T[]) Array.newInstance(arr.getClass().getComponentType(), 0);
        return set.toArray(res);
    }

    /**
     * 判断两个数组内的元素是否相同,A中的每一个元素在B中都存在，B中的每一个元素在A中都存在
     */
    public static <T> boolean equals(T[] arr1, T[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null || arr2 == null) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        Set<T> set1 = new HashSet<>(Arrays.asList(arr1));
        Set<T> set2 = new HashSet<>(Arrays.asList(arr2));
        return set1.equals(set2);
    }
}
