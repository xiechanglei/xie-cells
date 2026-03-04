package io.github.xiechanglei.cell.starter.test;

import io.github.xiechanglei.cell.starter.jpa.auto.annotation.FindById;
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
public class TestJpaController {

    @RequestMapping("/test/jpa/findById")
    @FindById
    public void findById() {
    }
}
