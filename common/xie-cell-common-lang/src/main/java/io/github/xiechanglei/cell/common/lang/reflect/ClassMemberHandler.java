package io.github.xiechanglei.cell.common.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * field handler
 *
 * @author xie
 * @date 2024/12/20
 */
public class ClassMemberHandler {
    private final List<Field> allFields = new ArrayList<>();
    private final List<Method> allMethods = new ArrayList<>();

    public List<Field> getFields() {
        return allFields;
    }

    public String[] getFieldNameArray() {
        return allFields.stream().map(Field::getName).toArray(String[]::new);
    }


    public Field getField(String name) {
        for (Field field : allFields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public List<Method> getMethods() {
        return allMethods;
    }

    public List<AccessibleObject> getMembers() {
        List<AccessibleObject> members = new ArrayList<>();
        members.addAll(allFields);
        members.addAll(allMethods);
        return members;
    }


    /**
     * 打开访问权限
     */
    public ClassMemberHandler openAccess() {
        allFields.forEach(field -> field.setAccessible(true));
        allMethods.forEach(method -> method.setAccessible(true));
        return this;
    }

    public ClassMemberHandler notStatic() {
        allFields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        allMethods.removeIf(method -> Modifier.isStatic(method.getModifiers()));
        return this;
    }

    public ClassMemberHandler isStatic() {
        allFields.removeIf(field -> !Modifier.isStatic(field.getModifiers()));
        allMethods.removeIf(method -> !Modifier.isStatic(method.getModifiers()));
        return this;
    }


    public ClassMemberHandler notFinal() {
        allFields.removeIf(field -> Modifier.isFinal(field.getModifiers()));
        allMethods.removeIf(method -> Modifier.isFinal(method.getModifiers()));
        return this;
    }

    public ClassMemberHandler isFinal() {
        allFields.removeIf(field -> !Modifier.isFinal(field.getModifiers()));
        allMethods.removeIf(method -> !Modifier.isFinal(method.getModifiers()));
        return this;
    }

    public ClassMemberHandler notTransient() {
        allFields.removeIf(field -> Modifier.isTransient(field.getModifiers()));
        allMethods.removeIf(method -> Modifier.isTransient(method.getModifiers()));
        return this;
    }

    public ClassMemberHandler isTransient() {
        allFields.removeIf(field -> !Modifier.isTransient(field.getModifiers()));
        allMethods.removeIf(method -> !Modifier.isTransient(method.getModifiers()));
        return this;
    }

    @SafeVarargs
    public final ClassMemberHandler withAnyAnnotation(Class<? extends Annotation>... annotations) {
        allFields.removeIf(field -> {
            for (Class<? extends Annotation> annotation : annotations) {
                if (field.isAnnotationPresent(annotation)) {
                    return false;
                }
            }
            return true;
        });
        allMethods.removeIf(method -> {
            for (Class<? extends Annotation> annotation : annotations) {
                if (method.isAnnotationPresent(annotation)) {
                    return false;
                }
            }
            return true;
        });
        return this;
    }

    @SafeVarargs
    public final ClassMemberHandler withAnnotation(Class<? extends Annotation>... annotations) {
        allFields.removeIf(field -> {
            for (Class<? extends Annotation> annotation : annotations) {
                if (!field.isAnnotationPresent(annotation)) {
                    return true;
                }
            }
            return false;
        });
        allMethods.removeIf(method -> {
            for (Class<? extends Annotation> annotation : annotations) {
                if (!method.isAnnotationPresent(annotation)) {
                    return true;
                }
            }
            return false;
        });
        return this;
    }

    /**
     * 不包含指定注解的字段
     *
     * @param annotation 注解
     */
    @SafeVarargs
    public final ClassMemberHandler withOutAnnotation(Class<? extends Annotation>... annotation) {
        allFields.removeIf(field -> {
            for (Class<? extends Annotation> a : annotation) {
                if (field.isAnnotationPresent(a)) {
                    return true;
                }
            }
            return false;
        });
        allMethods.removeIf(method -> {
            for (Class<? extends Annotation> a : annotation) {
                if (method.isAnnotationPresent(a)) {
                    return true;
                }
            }
            return false;
        });
        return this;
    }

    public static ClassMemberHandler of(Class<?> clazz) {
        ClassMemberHandler fieldHandler = new ClassMemberHandler();
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            Collections.addAll(fieldHandler.allFields, currentClass.getDeclaredFields());
            Collections.addAll(fieldHandler.allMethods, currentClass.getDeclaredMethods());
            currentClass = currentClass.getSuperclass();
        }
        return fieldHandler;
    }

    /**
     * 直接通过反射设置实体对象的字段值。
     * <p>
     * 支持从父类中查找字段，处理实体继承关系。
     * </p>
     *
     * @param entity    实体对象
     * @param fieldName 字段名
     * @param value     字段值
     */
    public static void setFieldValue(Object entity, String fieldName, Object value) throws IllegalAccessException {
        // 从实体类及其父类中查找字段
        Field field = findField(entity.getClass(), fieldName);
        if (field != null) {
            field.setAccessible(true);
            field.set(entity, value);
        }
    }

    /**
     * 从类及其父类中查找字段。
     *
     * @param clazz     起始类
     * @param fieldName 字段名
     * @return 找到的字段，未找到返回 null
     */
    public static Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // 在当前类未找到，继续向父类查找
                current = current.getSuperclass();
            }
        }
        return null;
    }
}
