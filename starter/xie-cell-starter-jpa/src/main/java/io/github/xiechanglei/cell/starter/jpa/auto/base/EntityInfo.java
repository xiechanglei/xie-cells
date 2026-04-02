package io.github.xiechanglei.cell.starter.jpa.auto.base;

import io.github.xiechanglei.cell.common.lang.reflect.ClassMemberHandler;
import jakarta.persistence.Id;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体类描述
 *
 * @author xie
 * @date 2026/3/4
 */
public record EntityInfo(Class<?> entityClass, String entityName) {
    private static final Map<Class<?>, String> entityIdPropertyCaChe = new ConcurrentHashMap<>();

    public String idPropertyName() {
        return entityIdPropertyCaChe.computeIfAbsent(entityClass, cls -> {
            // 通过反射获取实体类的 id 属性名称
            // 这里假设实体类的 id 属性使用 @Id 注解标注
            List<Field> fields = ClassMemberHandler.of(cls).withAnnotation(Id.class).getFields();
            if (fields.isEmpty()) {
                throw new IllegalStateException("实体类 " + cls.getName() + " 没有标注 @Id 的属性");
            }
            if (fields.size() > 1) {
                throw new IllegalStateException("实体类 " + cls.getName() + " 有多个标注 @Id 的属性");
            }
            return fields.getFirst().getName();
        });
    }
}
