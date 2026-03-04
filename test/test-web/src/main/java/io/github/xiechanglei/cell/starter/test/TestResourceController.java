package io.github.xiechanglei.cell.starter.test;

import io.github.xiechanglei.cell.starter.web.resoure.CellResourceResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
public class TestResourceController {

    @RequestMapping("/test/resource")
    public ResponseEntity<?> testResource() {
        return CellResourceResponseHandler.useFile("/home/xie/图片/v2-d7df064e60173084053c37b30cd8f21d_r.jpg");
    }
}
