package io.github.xiechanglei.cell.starter.test;

import io.github.xiechanglei.cell.starter.web.resoure.CellResourceResponseHandler;
import io.github.xiechanglei.cell.starter.web.resoure.ResourceInfo;
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
public class TestResourceController {

    @RequestMapping("/test/resource")
    public ResponseEntity<?> testResource() {
        return CellResourceResponseHandler.useResource(ResourceInfo.withFilePath("/home/xie/movie/06. 框架-vue从入门到手撕.mp4"));
    }
}
