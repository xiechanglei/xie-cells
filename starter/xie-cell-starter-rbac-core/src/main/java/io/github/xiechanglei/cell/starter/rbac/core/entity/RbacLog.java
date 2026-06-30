package io.github.xiechanglei.cell.starter.rbac.core.entity;


import io.github.xiechanglei.cell.common.jpa.bean.entity.SnowFlakeIdAndCreateTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 日志表
 */
@Getter
@Setter
@Entity
@Table(name = "rbac_log",
        indexes = {
                @Index(name = "rbac_log_idx_user_id", columnList = "user_id")
        }
)
@SuppressWarnings("all")
public class RbacLog extends SnowFlakeIdAndCreateTimeEntity {
    /**
     * 用户ID
     */
    @Column(name = "user_id", length = 40)
    @Comment("用户ID")
    private String userId;

    /**
     * 日志标题
     */
    @Column(name = "log_title", length = 100, nullable = false)
    @Comment("日志标题")
    private String logTitle;

    /**
     * 请求路径
     */
    @Column(name = "log_path", length = 100, nullable = false)
    @Comment("请求路径")
    private String logPath;

    /**
     * 请求IP地址
     */
    @Column(name = "log_address", length = 100, nullable = false)
    @Comment("请求IP地址")
    private String logAddress;
}
