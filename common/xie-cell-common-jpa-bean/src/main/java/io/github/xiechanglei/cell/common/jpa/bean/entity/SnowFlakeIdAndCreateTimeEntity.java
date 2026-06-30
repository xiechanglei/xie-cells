package io.github.xiechanglei.cell.common.jpa.bean.entity;

import io.github.xiechanglei.cell.common.jpa.bean.generator.SnowFlakeSequence;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 使用雪花算法生成ID的实体基类，用在数据量比较大的表上
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class SnowFlakeIdAndCreateTimeEntity {
    /**
     * 雪花算法生成ID
     */
    @Id
    @SnowFlakeSequence
    @Column(length = 22)
    @Comment("ID")
    public Long id;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    @Comment("创建时间")
    private Date createTime;
}