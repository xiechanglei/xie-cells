package io.github.xiechanglei.cell.starter.web;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * 自动配置类，用于当前模块的自动配置，主要是一些默认的Bean的注册和一些默认的配置项的设置
 *
 * @author xie
 * @date 2026/2/12
 */
@ComponentScan("io.github.xiechanglei.cell.starter.web")
@PropertySource("classpath:cell.web.properties")
@ServletComponentScan("io.github.xiechanglei.cell.starter.web")
public class CellWebAutoConfiguration {
}
