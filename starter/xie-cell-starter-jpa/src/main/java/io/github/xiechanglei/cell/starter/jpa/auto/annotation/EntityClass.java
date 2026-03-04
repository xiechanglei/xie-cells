package io.github.xiechanglei.cell.starter.jpa.auto.annotation;

import java.lang.annotation.*;

/**
 * 定义在类上，用于指定方法内部自动化所使用的实体类，如果方法上没有指定实体类，则在类上寻找注解 @EntityClass 来获取实体类
 *
 * @author xie
 * @date 2026/3/4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityClass {
    Class<?> value();
}
