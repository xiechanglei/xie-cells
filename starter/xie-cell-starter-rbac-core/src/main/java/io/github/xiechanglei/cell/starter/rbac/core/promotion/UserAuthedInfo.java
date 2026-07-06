package io.github.xiechanglei.cell.starter.rbac.core.promotion;

import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;

/**
 * 用户认证信息
 *
 * @author xie
 * @date 2026/7/6
 */
public interface UserAuthedInfo {

    String getId();

    Short getUserSerial();

    String getFeature();

    EnableStatus getEnableStatus();

    boolean isAdmin();
}
