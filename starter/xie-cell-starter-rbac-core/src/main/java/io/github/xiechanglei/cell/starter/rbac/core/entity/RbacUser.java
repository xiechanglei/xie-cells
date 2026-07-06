package io.github.xiechanglei.cell.starter.rbac.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.xiechanglei.cell.common.jpa.bean.entity.UUIDAndTimeEntity;
import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 用户基础信息表，如果需要扩展用户信息，可以选择增加扩展表，增加相关的管理接口
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rbac_user",
        uniqueConstraints = {@UniqueConstraint(name = "rbac_user_user_name_unique", columnNames = "user_name")}
)
public class RbacUser extends UUIDAndTimeEntity {

    // 默认用户序列号
    public static Short DEFAULT_USER_SERIAL = 1;
    /**
     * 用户名称
     */
    @Column(name = "user_name", length = 100, nullable = false, unique = true)
    @Comment("用户登陆账号")
    private String userName;

    /**
     * 用户密码
     */
    @JsonIgnore
    @Column(name = "user_password", length = 100, nullable = false)
    @Comment("用户密码")
    private String userPassword;

    /**
     * 用户状态
     */
    @Column(name = "user_status", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("用户状态，disable:禁用，enable:启用")
    private EnableStatus userStatus;

    /**
     * 昵称
     */
    @Column(name = "nick_name", length = 100)
    @Comment("用户昵称")
    private String nickName;

    /**
     * 手机号码
     */
    @Column(name = "phone_number", length = 20)
    @Comment("用户手机号码")
    private String phoneNumber;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    @Comment("用户邮箱")
    private String email;

    /**
     * 地址
     */
    @Column(name = "address", length = 100)
    @Comment("用户地址")
    private String address;


    /**
     * 用户序列号，每次修改密码+1
     */
    @JsonIgnore
    @Column(name = "user_serial", length = 5)
    @Comment("用户序列号,每次修改密码+1，前面的登陆token失效")
    private Short userSerial;

    /**
     * 用户特征值，有的时候跟第三方对接的时候，需要提供一个特殊的token,用以提供对应的接口访问，或者内嵌当前系统的页面
     * 该值不会遭到修改密码，单端登陆等影响，只要与用户id能对应的上，会一直保持有效，直到特征值被修改。
     * 特征值的生成规则应该由管理员角色在页面交互生成，然后提供给第三方使用
     */
    @JsonIgnore
    @Column(name = "user_feature", length = 32)
    @Comment("用户授权值，基于授权值产生的token不会过期，除非修改了授权值")
    private String feature;

    public RbacUser(String id, Short userSerial, String feature, EnableStatus userStatus) {
        this.setId(id);
        this.userSerial = userSerial;
        this.feature = feature;
        this.userStatus = userStatus;
    }

    /**
     * 更新用户版本号
     */
    public void updateSerial() {
        if (userSerial == null) {
            userSerial = DEFAULT_USER_SERIAL;
        }
        userSerial++;
    }

    /**
     * 一共一个构建管理员的方法，用于系统初始化的时候，判断是否有管理员，如果没有就创建一个
     */
    public static RbacUser buildAdmin(String adminName) {
        RbacUser admin = new RbacUser();
        admin.setId(null);
        admin.userStatus = EnableStatus.ENABLED;
        admin.userSerial = DEFAULT_USER_SERIAL;
        admin.nickName = adminName;
        admin.userName = adminName;
        return admin;
    }
}
