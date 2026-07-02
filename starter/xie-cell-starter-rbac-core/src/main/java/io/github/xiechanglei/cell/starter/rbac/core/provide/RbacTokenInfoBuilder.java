package io.github.xiechanglei.cell.starter.rbac.core.provide;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;

/**
 * 自定义创建token，需要添加为spring bean
 */
public interface RbacTokenInfoBuilder {

    /**
     * 获取token类
     */
    Class<? extends RbacTokenInfo> getTokenClass();

    /**
     * 使用序列号产生 Token 信息，主要用于登陆产生的token
     * <p>
     * 使用 {@link RbacUser} 对象来初始化 Token 信息。可以在此方法中根据用户的认证信息设置 Token 的各项数据。
     * </p>
     *
     * @param user 包含用户认证信息的 {@link RbacUser} 对象
     */
    RbacTokenInfo createWithSerial(RbacUser user);

    /**
     * 使用特征值产生 Token 信息，主要用于非登陆场景，比如交给第三方调用接口，或者内嵌当前系统的页面
     * <p>
     * 使用 {@link RbacUser} 对象来初始化 Token 信息。可以在此方法中根据用户的认证信息设置 Token 的各项数据。
     * </p>
     *
     * @param user 包含用户认证信息的 {@link RbacUser} 对象
     */
    RbacTokenInfo createWithFeature(RbacUser user);


}
