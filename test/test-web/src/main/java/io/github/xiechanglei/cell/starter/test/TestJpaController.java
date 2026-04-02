package io.github.xiechanglei.cell.starter.test;

import io.github.xiechanglei.cell.starter.jpa.auto.base.EntityClass;
import io.github.xiechanglei.cell.starter.jpa.auto.task.DeleteById;
import io.github.xiechanglei.cell.starter.jpa.auto.task.FindAll;
import io.github.xiechanglei.cell.starter.jpa.auto.task.FindById;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试资源相关的功能，主要是测试starter/xie-cell-starter-web模块中的资源相关的功能
 *
 * @author xie
 * @date 2026/2/22
 */
@RestController
@Slf4j
@EntityClass(value = TestTable.class, name = "用户")
public class TestJpaController {

    @FindById(ignoreFields = "password")
    @RequestMapping("/test/jpa/findById")
    public void findById() {
    }

    @DeleteById
    @RequestMapping("/test/jpa/deleteById")
    public void deleteById() {
    }

    @FindAll(ignoreFields = "password")
    @RequestMapping("/test/jpa/findAll")
    public void findAll() {
    }
}
