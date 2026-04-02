package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.UseTypeDefinedClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据 Id 获取实体对象的方法的注解，标注在方法上，表示自动实现根据 Id 获取实体对象的方法，
 * 方法的参数必须是一个 id类型的值
 *
 * @author xie
 * @date 2026/3/4
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindById {
    /**
     * 指定要查询的实体类，如果为 TypeUsedClass.class 则在类上寻找注解 @EntityClass 来获取实体类
     */
    Class<?> value() default UseTypeDefinedClass.class;

    /**
     * 指定方法参数中 ID 参数的名称
     * <p>
     * 默认为 "id"，如果方法参数名称不是 id，可以通过此属性指定
     * </p>
     */
    String idParamName() default "id";
}
