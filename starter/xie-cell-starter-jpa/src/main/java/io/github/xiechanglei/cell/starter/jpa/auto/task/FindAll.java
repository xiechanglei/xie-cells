package io.github.xiechanglei.cell.starter.jpa.auto.task;

import io.github.xiechanglei.cell.starter.jpa.auto.base.UseTypeDefinedClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解标注在方法上，表示自动实现获取所有实体对象的方法，
 * <p>
 * 请注意因为需要返回数据,方法的返回值不能是void,我尝试着使用scopedValue在全局返回封装那里去解决了这个问题,所以你需要使用我的web starter那个模块
 * 但是这并不是最好的方案,你应该需要给方案添加一个合理的返回值类型
 * <p>
 *
 * @author xie
 * @date 2026/3/4
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindAll {
    /**
     * 指定要查询的实体类，如果为 TypeUsedClass.class 则在类上寻找注解 @EntityClass 来获取实体类
     */
    Class<?> value() default UseTypeDefinedClass.class;
}
