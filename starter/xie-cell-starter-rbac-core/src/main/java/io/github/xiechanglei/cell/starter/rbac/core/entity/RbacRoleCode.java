package io.github.xiechanglei.cell.starter.rbac.core.entity;

import io.github.xiechanglei.cell.common.jpa.bean.entity.UUIDEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 角色 与权限码的对应关系表
 *
 * @author xie
 * @date 2024/12/19
 */
@Entity
@Table(name = "rbac_role_function",
        uniqueConstraints = {@UniqueConstraint(name = "rbac_role_function_role_id_per_code", columnNames = {"role_id", "per_code"})},
        indexes = {
                @Index(name = "rbac_role_function_idx_role_id", columnList = "role_id"),
                @Index(name = "rbac_role_function_idx_per_code", columnList = "per_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("all")
public class RbacRoleCode extends UUIDEntity {

    /**
     * 角色id
     */
    @Column(name = "role_id", length = 32)
    @Comment("角色id")
    private String roleId;

    /**
     * 功能id
     */
    @Column(name = "per_code", length = 100)
    @Comment("权限编码")
    private String perCode;

    public RbacRoleCode(String roleId, String perCode) {
        this.roleId = roleId;
        this.perCode = perCode;
    }
}