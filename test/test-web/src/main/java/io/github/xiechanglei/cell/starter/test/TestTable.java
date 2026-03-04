package io.github.xiechanglei.cell.starter.test;

import io.github.xiechanglei.cell.common.jpa.bean.entity.UUIDStringEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/3/4
 */
@Entity
@Getter
@Setter
public class TestTable extends UUIDStringEntity {
    private String userName;
    private String password;
}
