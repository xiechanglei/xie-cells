package io.github.xiechanglei.cell.starter.rbac.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/7/1
 */

@Data
@Component
@ConfigurationProperties(prefix = "cell.rbac.gen")
public class RbacCellApiConfigProperties {

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
