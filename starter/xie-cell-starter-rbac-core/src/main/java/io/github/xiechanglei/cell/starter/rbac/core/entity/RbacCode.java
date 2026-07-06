package io.github.xiechanglei.cell.starter.rbac.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 功能与权限码的对应关系表
 */
@Entity
@Table(name = "rbac_code",
        indexes = {
                @Index(name = "rbac_code_idx_ref_module", columnList = "ref_module")
        }
)
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings("all")
public class RbacCode {

    /**
     * 功能编码
     */
    @Id
    @Column(name = "per_code", length = 100)
    @Comment("功能编码")
    private String code;

    /**
     * 权限名称
     */
    @Column(name = "per_name", length = 100)
    @Comment("权限名称")
    private String name;


    /**
     * 权限描述
     */
    @Column(name = "per_desc", length = 200)
    @Comment("权限描述")
    private String description;

    /**
     * 所属模块，主要用于权限码的diff更新
     */
    @Column(name = "ref_module", length = 100)
    @Comment("所属模块")
    private String refModule;


    public static RbacCode create(String code, String name, String description, String refModule) {
        RbacCode rbacCode = new RbacCode();
        rbacCode.setCode(code);
        rbacCode.setName(name);
        rbacCode.setDescription(description);
        rbacCode.setRefModule(refModule);
        return rbacCode;
    }

}
