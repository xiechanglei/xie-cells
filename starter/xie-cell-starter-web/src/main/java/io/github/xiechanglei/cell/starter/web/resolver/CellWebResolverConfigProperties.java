package io.github.xiechanglei.cell.starter.web.resolver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring boot配置参数类， 可自定义相关配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cell.web.resolver")
public class CellWebResolverConfigProperties implements WebMvcConfigurer {
    /**
     * 分页参数名称
     */
    private String pageNoName = "pageNo";

    /**
     * 每页显示的记录数参数名称
     */
    private String pageSizeName = "pageSize";
}
