package io.github.xiechanglei.cell.starter.web.resolver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 参数解析器配置属性类。
 * <p>
 * 该类用于配置自定义参数解析器的相关属性，如分页参数名称等。
 * 可通过配置文件进行自定义设置，以适应不同的前端参数命名规范。
 * </p>
 * <p>
 * 配置示例：
 * <pre>
 * cell.web.resolver.page-no-name=pageNum    # 自定义页码参数名
 * cell.web.resolver.page-size-name=pageSize  # 自定义每页大小参数名
 * </pre>
 * </p>
 *
 * @author xie
 * @date 2024/12/26
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cell.web.resolver")
public class CellWebResolverConfigProperties implements WebMvcConfigurer {
    /**
     * 分页页码参数名称，默认为 "pageNo"
     */
    private String pageNoName = "pageNo";

    /**
     * 每页显示的记录数参数名称，默认为 "pageSize"
     */
    private String pageSizeName = "pageSize";
}
