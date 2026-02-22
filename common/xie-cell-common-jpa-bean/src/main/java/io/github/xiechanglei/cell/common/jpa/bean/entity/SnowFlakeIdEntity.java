package io.github.xiechanglei.cell.common.jpa.bean.entity;

import io.github.xiechanglei.cell.common.jpa.bean.generator.SnowFlakeSequence;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * 使用雪花算法生成ID的实体基类，用在数据量比较大的表上
 */
@Getter
@Setter
@MappedSuperclass
public abstract class SnowFlakeIdEntity {
    /**
     * 雪花算法生成ID
     */
    @Id
    @SnowFlakeSequence
    @Column(length = 22)
    @Comment("ID")
    public Long id;
}