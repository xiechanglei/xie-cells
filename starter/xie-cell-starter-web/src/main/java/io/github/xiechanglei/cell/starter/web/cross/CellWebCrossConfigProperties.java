package io.github.xiechanglei.cell.starter.web.cross;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 该类定义了跨域配置的属性,
 * 支持用于在application.properties或application.yml中进行自定义配置,
 * 如:
 * cell.web.cross.enable=false #表示是否开启跨域配置
 * cell.web.cross.origins=* #表示跨域的域名
 * </pre>
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cell.web.cross")
public class CellWebCrossConfigProperties {
    /**
     * 是否开启跨域配置,默认为true
     */
    private boolean enable = true;

    /**
     * 允许跨域的路径,默认为"/**"
     */
    private String mapping = "/**";

    /**
     * 允许跨域的域名,默认为"*"
     */
    private String[] origins = new String[]{"*"};

    /**
     * 允许跨域的方法,默认为"*"
     */
    private String[] methods = new String[]{"*"};

    /**
     * 允许跨域的请求头,默认为"*"
     */
    private String[] headers = new String[]{"*"};

    /**
     * 是否允许发送Cookie,默认为false
     */
    private boolean allowCredentials = false;

    /**
     * 预检请求的有效期,单位为秒,默认为1800秒
     * 预检请求主要发生在以下情况:
     * 1. 请求方法为PUT,DELETE,OPTIONS,PATCH
     * 2. 请求头中包含自定义头信息
     * 3. 请求中的Content-Type为application/json
     * 4. 请求中的Content-Type为application/xml
     */
    private long maxAge = 1800;
}
