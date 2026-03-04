package io.github.xiechanglei.cell.starter.jpa.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sql日志输出配置类，可以打印sql日志和慢sql日志，慢sql的阈值可以配置，默认200毫秒，超过这个时间的sql会被认为是慢sql，并且会被打印出来
 *
 * @author xie
 * @date 2026/1/27
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cell.jpa.log")
public class CellSqlLogProperties {
    /**
     * 是否开启sql日志,默认为true
     */
    private boolean enable = true;

    /**
     * 慢sql阈值，单位毫秒，默认200毫秒
     */
    private int lowSqlLimit = 200;
}
