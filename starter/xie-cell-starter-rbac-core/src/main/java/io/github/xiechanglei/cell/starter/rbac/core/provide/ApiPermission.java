package io.github.xiechanglei.cell.starter.rbac.core.provide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限单元，标记在对应的方法上（一般标记在RequestMapping 方法上）
 * 标注了该注解的方法会被aop进行拦截执行
 * <p>
 * 前端对应的菜单以及功能控制，需要自行对对应的权限码进行组合
 * <p>
 * 系统启动的时候回自动扫描进行入库，请在对应的类上添加@Rbac 注解，方便系统扫描
 *
 * @author xie
 * @date 2026/6/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPermission {
    // 权限码，全局唯一，建议使用模块编码:功能编码:操作类型的方式进行命名，方便权限码的管理
    // 如果检索的时候发现权限码已经在数据库中其他的模块下存在，该模块码不会被入库，最终导致的结果是该接口无法访问
    // 如果code的值为空，表示仅仅只拦截登陆，不进行权限码的校验
    String code() default "";

    // 权限名称
    String name() default "";

    // 描述
    String description() default "";

    // 是否记录日志，默认不记录
    boolean log() default false;

    // 模块名称，默认取当前的spring.application.name，如果没有配置，则为空.用于全量diff更新的最终判断依据，建议在微服务中，不同的微服务使用不同的模块名称，避免权限码冲突
    // String module() default "";
}
