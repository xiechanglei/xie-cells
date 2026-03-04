package io.github.xiechanglei.cell.starter.jpa.auto.annotation;

import java.lang.annotation.*;

/**
 * 根据Id获取实体对象的方法的注解，标注在方法上，表示自动实现根据Id获取实体对象的方法，
 * 方法的参数必须是一个id
 *
 * @author xie
 * @date 2026/3/4
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindById {
    // 指定要查询的实体类，如果为TypeUsedClass.class 则在类上寻找注解 @EntityClass 来获取实体类
    Class<?> value() default  TypeUsedClass.class;
}
