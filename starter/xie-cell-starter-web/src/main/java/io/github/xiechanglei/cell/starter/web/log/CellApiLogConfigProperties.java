package io.github.xiechanglei.cell.starter.web.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * API 日志配置属性类。
 * <p>
 * 该类用于配置 API 请求日志的开关状态，可通过配置文件进行自定义设置。
 * 默认情况下，API 日志功能是开启的。
 * </p>
 * <p>
 * 配置示例：
 * <pre>
 * cell.web.log.enable=false  # 关闭 API 日志记录
 * </pre>
 * </p>
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
