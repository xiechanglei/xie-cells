package io.github.xiechanglei.cell.plugin.ap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Hello注解
 * 用于标记需要在方法中插入"hello world!"输出语句的类
 * 该注解将在编译时被HelloProcessor处理，向类中的所有方法添加System.out.println("hello world!")语句
 *
 * @author xie
 * @date 2026/2/11
 */
@Target({ElementType.TYPE})  // 该注解只能应用于类、接口或枚举类型
@Retention(RetentionPolicy.SOURCE)  // 该注解仅在源代码级别保留，编译时会被处理，不会存在于class文件中
public @interface Hello {
}
