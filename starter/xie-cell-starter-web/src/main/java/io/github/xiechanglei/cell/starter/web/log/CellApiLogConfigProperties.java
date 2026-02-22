package io.github.xiechanglei.cell.starter.web.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2024/12/19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cell.web.log")
public class CellApiLogConfigProperties {
    /**
     * 是否开启接口日志,默认为true
     */
    private boolean enable = true;
}
