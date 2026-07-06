package io.github.xiechanglei.cell.starter.rbac.core.entity;

import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
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


    /**
     * 是否记录日志的用户自定义级别，用户可以指定该字段的值用以覆盖logStatus的值，日志是否记录的策略取决于该字段的值，若该字段为null，则取logStatus的值
     */
    @Column(name = "log_status_set", length = 10)
    @Enumerated(EnumType.STRING)
    @Comment("是否日志记录的用户自定义级别，0:禁用，1:启用，null:使用log_status的值")
    private EnableStatus logStatusUserDefined;


    public static RbacCode create(String code, String name, String description, String refModule) {
        RbacCode rbacCode = new RbacCode();
        rbacCode.setCode(code);
        rbacCode.setName(name);
        rbacCode.setDescription(description);
        rbacCode.setRefModule(refModule);
        return rbacCode;
    }

}
