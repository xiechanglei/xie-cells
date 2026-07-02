package io.github.xiechanglei.cell.starter.rbac.core.token;

import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 默认的Token信息类，实现了TokenInfo接口。
 * token 在权限拦截的时候会执行两种模式，
 * 一种是根据用户id和序列号进行拦截，另一种是根据用户id和特征值进行拦截
 * <p>
 * 该类用于存储与用户相关的Token信息，包括用户ID和序列号。序列号主要用于在用户密码更改后使之前的登录会话失效，从而实现踢出用户的功能。
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DefaultTokenInfo implements RbacTokenInfo {
    /**
     * 用户ID，用于唯一标识一个用户。
     */
    private String userId;

    /**
     * 序列号，用于用户会话的管理。每次用户密码修改时都会更新此序列号，
     * 从而使之前的会话失效，实现用户的踢出操作。
     */
    private Short serialNumber;


    /**
     * 用户特征值，有的时候跟第三方对接的时候，需要提供一个特殊的token,用以提供对应的接口访问，或者内嵌当前系统的页面
     * 该值不会遭到修改密码，单端登陆等影响，只要与用户id能对应的上，会一直保持有效，直到特征值被修改。
     * 特征值的生成规则应该由管理员角色在页面交互生成，然后提供给第三方使用
     */
    private String feature;

}

