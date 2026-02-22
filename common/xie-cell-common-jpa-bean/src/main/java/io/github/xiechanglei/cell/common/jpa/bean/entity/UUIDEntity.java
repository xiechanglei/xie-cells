package io.github.xiechanglei.cell.common.jpa.bean.entity;

import io.github.xiechanglei.cell.common.jpa.bean.generator.UUIDSequence;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.UUID;


/**
 * 使用UUID做为主键，uuid采用v7版本生成
 */
@Getter
@Setter
@MappedSuperclass
public class UUIDEntity {
    /*
     * UUID生成ID
     */
    @Id
    @UUIDSequence
    @Comment("ID")
    private UUID id;
}
