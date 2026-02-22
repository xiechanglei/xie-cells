package io.github.xiechanglei.cell.common.jpa.bean.entity;

import io.github.xiechanglei.cell.common.jpa.bean.generator.UUIDStringSequence;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;


/**
 * 使用UUID生成String类型ID的实体基类，用在数据量比较小的表上，
 * 比直接用UUID类型的ID浪费存储空间，优点是代码复杂度不高，使用String可以操作
 */
@Getter
@Setter
@MappedSuperclass
public class UUIDStringEntity {
    /*
     * UUID生成ID
     */
    @Id
    @UUIDStringSequence
    @Column(length = 32)
    @Comment("ID")
    private String id;
}
