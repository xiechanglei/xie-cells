package io.github.xiechanglei.cell.common.jpa.bean.entity;

import io.github.xiechanglei.cell.common.jpa.bean.generator.UUIDStringSequence;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 使用UUID生成String类型ID的实体基类，用在数据量比较小的表上，
 * 比直接用UUID类型的ID浪费存储空间，优点是代码复杂度不高，使用String可以操作
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class UUIDAndTimeEntity {
    /*
     * UUID生成ID
     */
    @Id
    @UUIDStringSequence
    @Column(length = 32)
    @Comment("ID")
    private String id;


    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    @Comment("更新时间")
    private Date updateTime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    @Comment("创建时间")
    private Date createTime;
}
