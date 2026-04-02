package io.github.xiechanglei.cell.starter.jpa.auto.task;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 级联删除的注解，标注在方法上，表示在执行删除操作时，除了删除当前实体对象之外，还需要级联删除与之相关的其他实体对象
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CascadeDelete {
    /**
     * 级联删除的对象的属性名称
     */
    Class<?> entity();

    /**
     * 级联删除的对象的关联属性名称
     */
    String property();

}