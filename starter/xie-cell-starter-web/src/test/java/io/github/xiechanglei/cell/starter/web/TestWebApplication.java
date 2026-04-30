package io.github.xiechanglei.cell.starter.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 的测试框架（@SpringBootTest）默认有一个“向上查找”的机制：它会从你的测试类所在的包开始，一层一层往上找，直到找到那个带有 @SpringBootApplication 或 @SpringBootConfiguration 注解的启动类。
 *
 * 这里创建一个测试用的 Spring Boot 应用程序类，放在 io.github.xiechanglei.cell.starter.web 包下，这样测试类就能找到它，并且这个类也能被测试类使用。
 */
@SpringBootApplication
public class TestWebApplication {
}
