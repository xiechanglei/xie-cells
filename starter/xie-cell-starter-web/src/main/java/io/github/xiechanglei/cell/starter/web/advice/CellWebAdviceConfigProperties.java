package io.github.xiechanglei.cell.starter.web.advice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 定义spring boot中advice的一些常规：
 * 1. 全局一场处理，因为在不处理的情况下，spring boot会默认返回一个html页面，这样不利于前后端分离
 * 2. 全局json结构返回，这样可以统一返回的结构，方便前端处理，也可以减少相关的代码量
 * </pre>
 * <p>
 * 全局异常处理的前提是配置了全局json结构返回，否则没有意义，因为不知道异常处理是否需要返回哪一种结构
 * </p>
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cell.web.advice")
public class CellWebAdviceConfigProperties {
    /**
     * 是否开启全局异常处理
     */
    private boolean exception = true;

    /**
     * 是否开启全局返回处理
     */
    private boolean response = true;
}
