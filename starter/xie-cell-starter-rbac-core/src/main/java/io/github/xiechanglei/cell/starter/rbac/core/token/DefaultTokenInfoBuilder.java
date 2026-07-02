package io.github.xiechanglei.cell.starter.rbac.core.token;


import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfo;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfoBuilder;

/**
 * 默认的Token信息类，实现了TokenInfo接口。
 * token 在权限拦截的时候会执行两种模式，
 * 一种是根据用户id和序列号进行拦截，另一种是根据用户id和特征值进行拦截
 * <p>
 * 该类用于存储与用户相关的Token信息，包括用户ID和序列号。序列号主要用于在用户密码更改后使之前的登录会话失效，从而实现踢出用户的功能。
 * </p>
 */
public class DefaultTokenInfoBuilder implements RbacTokenInfoBuilder {

    @Override
    public Class<DefaultTokenInfo> getTokenClass() {
        return DefaultTokenInfo.class;
    }

    public RbacTokenInfo createWithSerial(RbacUser user) {
        return new DefaultTokenInfo(user.getId(), user.getUserSerial(), null);
    }

    @Override
    public RbacTokenInfo createWithFeature(RbacUser user) {
        return new DefaultTokenInfo(user.getId(), null, user.getFeature());
    }
}

