package io.github.xiechanglei.cell.starter.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * 自动配置类，用于当前模块的自动配置，主要是一些默认的Bean的注册和一些默认的配置项的设置
 *
 * @author xie
 * @date 2026/1/13
 */
@ComponentScan
@PropertySource("classpath:cell.spring.properties")
public class CellSpringAutoConfiguration {
}
