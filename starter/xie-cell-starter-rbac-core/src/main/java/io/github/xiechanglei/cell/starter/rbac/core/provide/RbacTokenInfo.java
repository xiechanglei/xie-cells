package io.github.xiechanglei.cell.starter.rbac.core.provide;

/**
 * 自定义token信息
 */
public interface RbacTokenInfo {

    /**
     * 获取用户 ID
     * <p>
     * 返回用户的唯一标识符。该方法用于获取与当前 Token 关联的用户的 ID。
     * </p>
     *
     * @return 用户的唯一标识符
     */
    String getUserId();

    /**
     * 获取序列号
     * <p>
     * 序列号用于标识 Token 的版本，主要用于用户登出或密码修改时的用户踢出机制。每次用户修改密码时，序列号会更新，确保旧 Token 无效。
     * </p>
     *
     * @return 当前 Token 的序列号
     */
    Short getSerialNumber();

    /**
     * 用户特征值，有的时候跟第三方对接的时候，需要提供一个特殊的token,用以提供对应的接口访问，或者内嵌当前系统的页面
     * 该值不会遭到修改密码，单端登陆等影响，只要与用户id能对应的上，会一直保持有效，直到特征值被修改。
     * 特征值的生成规则应该由管理员角色在页面交互生成，然后提供给第三方使用
     */
    String getFeature();
}
