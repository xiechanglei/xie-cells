package io.github.xiechanglei.cell.starter.rbac.core.provide;

/**
 * 用户策略自定义接口，需要添加为spring bean
 */
public interface RbacUserCustomStrategy {
    /**
     * 扩展内置删除用户的接口功能
     * 内置接口会删除用户表，用户角色表，用户日志表
     *
     * @param userId 用户id
     */
    void clear(String userId);
}
