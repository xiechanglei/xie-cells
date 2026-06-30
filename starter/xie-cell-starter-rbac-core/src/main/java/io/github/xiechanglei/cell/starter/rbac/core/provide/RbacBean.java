package io.github.xiechanglei.cell.starter.rbac.core.provide;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用以标注在spring boot的bean上，系统会扫描标注了该注解的类，扫描类中的方法，入库权限数据
 *
 * @author xie
 * @date 2026/6/29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RbacBean {
}
