package io.github.xiechanglei.cell.starter.rbac.core.entity;

import io.github.xiechanglei.cell.common.jpa.bean.entity.UUIDAndTimeEntity;
import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 角色
 *
 * @author xie
 * @date 2024/12/19
 */
@Entity
@Table(name = "rbac_role",
        uniqueConstraints = {@UniqueConstraint(name = "rbac_role_role_name_unique", columnNames = "role_name")}
)
@Setter
@Getter
@SuppressWarnings("all")
public class RbacRole extends UUIDAndTimeEntity {

    /**
     * 角色名称
     */
    @Column(name = "role_name", length = 100, nullable = false)
    @Comment("角色名称")
    private String roleName;

    /**
     * 角色状态
     */
    @Column(name = "role_status", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("角色状态,disable:禁用，enable:启用")
    private EnableStatus roleStatus;

    /**
     * 是否是超级管理员
     */
    @Column(name = "is_admin", length = 1, nullable = false)
    @Comment("是否是超级管理员，0:否，1:是")
    private boolean admin;

    /**
     * 角色备注
     */
    @Column(name = "role_remark", length = 255)
    @Comment("角色备注")
    private String roleRemark;

    /**
     * 创建管理员角色
     */
    public static RbacRole createAdmin(String roleName) {
        RbacRole rbacRole = new RbacRole();
        rbacRole.setRoleName(roleName);
        rbacRole.setRoleStatus(EnableStatus.ENABLED);
        rbacRole.setAdmin(true);
        rbacRole.setRoleRemark("系统自动创建的超级管理员角色,具有所有权限，不可编辑，不可删除");
        return rbacRole;
    }
}