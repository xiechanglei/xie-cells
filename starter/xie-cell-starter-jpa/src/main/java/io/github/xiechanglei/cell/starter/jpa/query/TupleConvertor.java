package io.github.xiechanglei.cell.starter.jpa.query;

import java.util.List;

/**
 * todo,就不应该有这种东西，应该根据传入的class对象自动去找属性进行值匹配
 *
 * @author xie
 * @date 2024/12/24
 */
public interface TupleConvertor<T> {
    /**
     * 将一个元组转换为一个对象
     *
     * @param tuple 元组
     * @return 对象
     */
    T convert(Object[] tuple);


    default List<T> convertList(List<Object> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream().map(o -> convert((Object[]) o)).toList();
    }
}
