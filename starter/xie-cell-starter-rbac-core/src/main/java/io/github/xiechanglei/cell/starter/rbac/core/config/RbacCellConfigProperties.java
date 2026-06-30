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
@ConfigurationProperties(prefix = "cell.rbac")
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
     * 超级管理员的角色名，默认为 超级管理员
     */
    private String adminRoleName = "超级管理员";

    /**
     * 超级管理员的用户名，默认为 admin
     */
    private String adminUsername = "admin";

    /**
     * 超级管理员的密码，默认为 123456
     */
    private String adminPassword = "123456";

    /**
     * 密码规则，默认为 6-20位数字和字母的组合
     */
    private String passwordRule = "\\w{6,20}";
}
