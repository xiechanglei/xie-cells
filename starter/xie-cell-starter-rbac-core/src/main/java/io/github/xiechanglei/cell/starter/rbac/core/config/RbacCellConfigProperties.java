package io.github.xiechanglei.cell.starter.rbac.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于权限的配置属性
 *
 * @author xie
 * @date 2024/10/10
 */
@Data
@Component
@ConfigurationProperties(prefix = "cell.rbac.base")
public class RbacCellConfigProperties {
    /**
     * 当前模块的名称，书要用于模块权限的全量diff，如果是分体式架构模式下，不同的模块应该使用不同的moduleName,否则会出现权限入库的时候缺失
     */
    private String moduleName = "default";

    /**
     * 是否开启角色权限配置,默认为true
     */
    private boolean enable = true;

    /**
     * 是否拦截权限,默认为true, 某些情况下,开启权限配置,但是不拦截
     */
    private boolean filterAuth = true;

    /**
     * 从header，param，cookie中存放token的key， 默认为auth-token
     */
    private String tokenName = "auth-token";

    /**
     * token 密钥
     */
    private String tokenSecret = "cell-rbac";
}
