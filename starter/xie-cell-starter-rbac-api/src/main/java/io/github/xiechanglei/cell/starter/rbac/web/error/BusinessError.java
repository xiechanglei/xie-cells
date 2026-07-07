package io.github.xiechanglei.cell.starter.rbac.web.error;

import io.github.xiechanglei.cell.common.bean.exception.BusinessException;

/**
 * 业务异常处理
 */
public interface BusinessError {
    /**
     * 10开头：用户相关
     */
    interface USER {
        BusinessException USER_LOGIN_FAILED = BusinessException.of(1000001, "用户名或密码错误");
        BusinessException USER_NOT_FOUND = BusinessException.of(1000002, "用户不存在");
        BusinessException USER_DISABLED = BusinessException.of(1000003, "用户已禁用");
        BusinessException USER_LOGIN_EXPIRED = BusinessException.of(1000004, "登陆过期");
        BusinessException USER_NO_PERMISSION = BusinessException.of(1000005, "用户无权限");
        BusinessException USER_OLD_PASSWORD_ERROR = BusinessException.of(1000006, "旧密码错误,无法修改");
        BusinessException USER_EXISTS = BusinessException.of(1000007, "用户已存在");
        BusinessException USER_NOT_LOGIN = BusinessException.of(1000008, "用户未登陆");
        BusinessException USER_CAN_NOT_OPERATE = BusinessException.of(1000009, "超级管理员无法禁用");
        BusinessException USER_NAME_IS_EMPTY = BusinessException.of(1000011, "用户名不能为空");
        BusinessException USER_UPDATE_FAILED = BusinessException.of(1000013, "用户更新失败");
        BusinessException USER_CAN_NOT_DELETE = BusinessException.of(1000014, "用户无法删除");
    }

    /**
     * 11开头：角色相关
     */
     interface ROLE {
        BusinessException ROLE_EXISTS = BusinessException.of(1200001, "角色名称已存在");
        BusinessException ROLE_CAN_NOT_DELETE = BusinessException.of(1200002, "角色下有用户，不能删除");
        BusinessException ROLE_NOT_EXISTS = BusinessException.of(1200003, "角色不存在");
        BusinessException ROLE_CAN_NOT_OPERATE = BusinessException.of(1200004, "系统角色，无法操作");
        BusinessException ROLE_HAS_BEEN_GRANTED = BusinessException.of(1200005, "角色已分配，请勿重复分配");
        BusinessException ROLE_NOT_GRANTED = BusinessException.of(1200006, "用户未分配该角色");
        BusinessException USER_ADMIN_ROLE = BusinessException.of(1200007, "系统至少存在一个用户拥有管理员角色");
    }

    /**
     * 12开头：权限相关
     */
    interface PERMISSION {
        BusinessException MODULE_CAN_NOTE_DISABLE = BusinessException.of(1300001, "模块不能禁用");
        BusinessException FUNCTION_CAN_NOTE_DISABLE = BusinessException.of(1300002, "功能不能禁用");
        BusinessException PERMISSION_NOT_EXISTS = BusinessException.of(1300003, "权限不存在");
    }
}
