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
}
