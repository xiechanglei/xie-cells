package io.github.xiechanglei.cell.common.bean.message;

import java.util.TreeMap;

/**
 * 数据组装类，通常用于构建一些动态的属性对象，如：
 * <pre>
 *     DataFit dataFit = DataFit.of("name", "xiechanglei").fit("age", 18);
 * </pre>
 */
public class DataFit extends TreeMap<String, Object> {
    /**
     * 给DataFit对象添加属性，并且返回DataFit对象，方便链式调用
     */
    public DataFit fit(String key, Object value) {
        if (value != null) {
            this.put(key, value);
        }
        return this;
    }

    /**
     * 静态方法，用于快速构建DataFit对象
     */
    public static DataFit of(String key, Object value) {
        return new DataFit().fit(key, value);
    }

    /**
     * 将数据转换成url参数,因为继承了TreeMap，所以参数是有序的，用于一些需要排序后做签名的场景
     */
    public String toUrl() {
        StringBuilder sb = new StringBuilder();
        this.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }
}
