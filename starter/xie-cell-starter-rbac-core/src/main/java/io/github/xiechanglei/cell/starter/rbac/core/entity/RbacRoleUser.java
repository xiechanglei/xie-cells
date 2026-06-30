package io.github.xiechanglei.cell.starter.rbac.core.entity;

import io.github.xiechanglei.cell.common.jpa.bean.entity.UUIDEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 角色与用户的对应关系表
 */
@Entity
@Table(name = "rbac_role_user",
        uniqueConstraints = {@UniqueConstraint(name = "rbac_role_user_idx_role_id_user_id", columnNames = {"role_id", "user_id"})},
        indexes = {
                @Index(name = "rbac_role_user_idx_role_id", columnList = "role_id"),
                @Index(name = "rbac_role_user_idx_user_id", columnList = "user_id")
        }
)
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings("all")
public class RbacRoleUser extends UUIDEntity {

    @Column(name = "role_id", length = 100)
    @Comment("角色id")
    private String roleId;

    @Column(name = "user_id", length = 100)
    @Comment("用户id")
    private String userId;

    public RbacRoleUser(String roleId, String userId) {
        this.roleId = roleId;
        this.userId = userId;
    }

}
