package io.github.xiechanglei.cell.starter.web.advice.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 用于标记不需要返回值处理的注解,
 * 当我们使用了全局返回值处理的时候，全局所有的接口都进行了统一的返回值处理，
 * 但是有时候个别接口上我们不需要进行这种处理，
 * 比如，给第三方提供的接口，由于为了兼容第三方，我们希望返回一个其他的格式的结构，
 * 那么可以在对应接口方法上标注这个注解，全局统一返回值处理那边会忽略这个接口的返回值处理，
 * 当然也可以在类上标注这个注解，那么这个类下的所有接口都不会进行返回值处理
 * </pre>
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableResponseAdvice {
}
