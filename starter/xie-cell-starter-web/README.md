# Xie-Cells Web Starter 模块

Web 功能 Starter，提供 Web 应用的基础配置和扩展功能，基于 Spring Boot Web 进行增强。

## 功能列表

- **全局响应统一封装** - 自动封装 Controller 返回值为统一响应格式
- **全局异常处理** - 提供统一的异常处理和错误响应格式
- **跨域配置支持** - 提供可配置的跨域访问支持
- **响应式流式输出支持** - 支持 SSE、WebFlux 等流式输出方式
- **WebSocket 支持** - 集成 WebSocket 双向通信能力
- **参数验证支持** - 提供 Bean Validation 验证支持
- **Undertow 容器** - 使用高性能 Undertow 替代默认 Tomcat

## 使用方式

在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cell-starter-web</artifactId>
    <version>3.5.10.1</version>
</dependency>
```

## 配置说明

### 基础配置

模块已提供默认配置（`cell.web.properties`）：

```properties
# Jackson 时间戳序列化
spring.jackson.serialization.write-dates-as-timestamps=true

# 字符编码配置
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.enabled=true
server.servlet.encoding.force=true
```

### 跨域配置

可通过以下配置项自定义跨域设置：

```properties
# 跨域配置前缀：cell.web.cross
cell.web.cross.allowed-origins=http://localhost:3000
cell.web.cross.allowed-methods=GET,POST,PUT,DELETE
cell.web.cross.allowed-headers=*
cell.web.cross.allow-credentials=true
cell.web.cross.max-age=3600
```

### 响应封装配置

```properties
# 响应封装配置前缀：cell.web.advice
cell.web.advice.enabled=true
```

## 核心功能

### 1. 全局响应统一封装

所有 Controller 返回值会自动封装为统一格式：

```java
@RestController
@RequestMapping("/api")
public class UserController {
    
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        // 返回值会自动封装为：{ "code": 200, "message": "success", "data": {...} }
        return new User(id, "张三");
    }
}
```

如需禁用某个接口的响应封装，使用 `@DisableResponseAdvice` 注解：

```java
@DisableResponseAdvice
@GetMapping("/download")
public ResponseEntity<byte[]> download() {
    // 直接返回原始响应
}
```

### 2. 全局异常处理

自动捕获并处理 Controller 中的异常，返回统一错误格式：

```java
@RestController
@RequestMapping("/api")
public class OrderController {
    
    @GetMapping("/order/{id}")
    public Order getOrder(@PathVariable Long id) {
        // 抛出业务异常时，会自动捕获并返回统一错误格式
        throw new BusinessException("订单不存在");
    }
}
```

### 3. 流式输出支持

#### SSE (Server-Sent Events)

```java
@RestController
@RequestMapping("/api")
public class StreamController {
    
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter();
        
        CompletableFuture.runAsync(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    emitter.send(SseEmitter.event()
                        .name("message")
                        .data("数据：" + i));
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        
        return emitter;
    }
}
```

#### WebFlux 响应式流

```java
@RestController
@RequestMapping("/api")
public class ReactiveController {
    
    @GetMapping(value = "/flux", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> flux() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(i -> "数据：" + i)
            .take(10);
    }
}
```

#### StreamingResponseBody

```java
@GetMapping("/stream-body")
public ResponseEntity<StreamingResponseBody> streamBody() {
    StreamingResponseBody stream = outputStream -> {
        for (int i = 0; i < 10; i++) {
            outputStream.write(("数据：" + i + "\n").getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            Thread.sleep(1000);
        }
    };
    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .body(stream);
}
```

### 4. WebSocket 支持

```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }
}
```

## 依赖说明

本模块包含以下核心依赖：

- `spring-boot-starter-websocket` - WebSocket 支持
- `spring-boot-starter-undertow` - Undertow 容器（替代 Tomcat）
- `spring-boot-starter-validation` - 参数验证
- `spring-boot-starter-aop` - AOP 支持
- `reactor-core` - 响应式流支持
- `xie-cell-common-bean` - 通用 Bean 工具

## 包结构

```
io.github.xiechanglei.cell.starter.web
├── advice/                      # 响应封装和异常处理
│   ├── CellWebAdviceConfigProperties.java
│   ├── CellWebExceptionAdvice.java
│   ├── message/
│   │   ├── CellWebGlobalResponseBodyAdvice.java
│   │   └── DisableResponseAdvice.java
├── cross/                       # 跨域配置
│   ├── CellWebCrossConfigProperties.java
│   └── CellWebCrossOriginConfiguration.java
├── resoure/                     # 资源响应工具
│   ├── ResourceInfo.java
│   └── ResourceResponseHelper.java
└── CellWebAutoConfiguration.java
```

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。
