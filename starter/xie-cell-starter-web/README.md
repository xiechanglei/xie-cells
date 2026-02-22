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
- **资源响应工具** - 支持文件下载和媒体资源预览（图片/视频/音频）
- **范围请求支持** - 支持 HTTP 206 Partial Content（视频拖拽、断点续传）
- **缓存验证支持** - 支持 HTTP 304 Not Modified（减少不必要传输）
- **自动分段下载** - 文件超过阈值时自动启用范围请求支持

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

### 3. 资源响应工具

#### 文件下载模式

```java
@RestController
@RequestMapping("/api")
public class FileController {
    
    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable Long id) {
        File file = getFileById(id); // 获取文件
        ResourceInfo info = ResourceInfo.withFile(file);
        return ResourceResponseHelper.useResource(info);
    }
}
```

#### 媒体资源预览模式（图片/视频/音频）

```java
@RestController
@RequestMapping("/api")
public class MediaController {
    
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        File file = getImageById(id); // 获取图片文件
        ResourceInfo info = ResourceInfo.withFile(file);
        return ResourceResponseHelper.useMedia(info);
    }
    
    @GetMapping("/video/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id) {
        File file = getVideoById(id); // 获取视频文件
        ResourceInfo info = ResourceInfo.withFile(file);
        return ResourceResponseHelper.useMedia(info);
    }
}
```

#### 304 缓存验证

`useMedia` 方法自动支持 HTTP 304 Not Modified 响应：

**工作原理：**
1. 首次请求：服务器返回完整资源，并在响应头中包含 `Last-Modified`
2. 后续请求：浏览器发送 `If-Modified-Since` 头（值为上次收到的 Last-Modified）
3. 服务器比较：如果资源未修改，返回 304 Not Modified（无响应体）
4. 如果资源已修改，返回 200 OK 和新资源

```java
@RestController
@RequestMapping("/api")
public class MediaController {
    
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        File file = getImageById(id);
        ResourceInfo info = ResourceInfo.withFile(file);
        // 自动处理 304 缓存验证
        return ResourceResponseHelper.useMedia(info);
    }
}
```

**前端效果：**
```javascript
// 首次请求
fetch('/api/image/123').then(response => {
    console.log(response.status); // 200
    console.log(response.headers.get('Last-Modified')); // "Wed, 21 Oct 2015 07:28:00 GMT"
});

// 后续请求（浏览器自动添加 If-Modified-Since 头）
// 如果资源未修改，服务器返回 304，浏览器使用本地缓存
fetch('/api/image/123').then(response => {
    console.log(response.status); // 304
});
```

#### 范围请求支持（HTTP 206）

`useMedia` 和 `useResource` 方法自动支持 HTTP Range 请求头，实现：
- 视频拖拽播放
- 断点续传下载
- 分片加载

**分段下载阈值：**
- 默认阈值：10MB（文件超过 10MB 时自动启用范围请求支持）
- 可自定义阈值：通过方法的 `rangeThreshold` 参数设置

```java
// 使用默认阈值（10MB）
@GetMapping("/video/{id}")
public ResponseEntity<?> getVideo(@PathVariable Long id) {
    File file = getVideoById(id);
    ResourceInfo info = ResourceInfo.withFile(file);
    return ResourceResponseHelper.useMedia(info);
}

// 自定义阈值（5MB）
@GetMapping("/large-video/{id}")
public ResponseEntity<?> getLargeVideo(@PathVariable Long id) {
    File file = getVideoById(id);
    ResourceInfo info = ResourceInfo.withFile(file);
    // 文件超过 5MB 时启用范围请求支持
    return ResourceResponseHelper.useMedia(info, 5 * 1024 * 1024);
}

// 禁用分段下载（阈值设为 0）
@GetMapping("/small-video/{id}")
public ResponseEntity<?> getSmallVideo(@PathVariable Long id) {
    File file = getVideoById(id);
    ResourceInfo info = ResourceInfo.withFile(file);
    // 禁用范围请求支持
    return ResourceResponseHelper.useMedia(info, 0);
}
```

同时支持 304 缓存验证，减少不必要的传输。

前端使用示例：

```javascript
// 视频标签自动使用范围请求
<video controls>
    <source src="/api/video/123" type="video/mp4">
</video>

// 或使用 fetch API 手动请求范围
fetch('/api/video/123', {
    headers: {
        'Range': 'bytes=0-1048575' // 请求前 1MB
    }
}).then(response => {
    if (response.status === 206) {
        console.log('支持范围请求');
    }
});
```

### 5. 流式输出支持

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

### 6. WebSocket 支持

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
│   ├── ResourceInfo.java            # 资源说明类
│   ├── ResourceResponseHelper.java  # 资源响应辅助类（支持下载/预览/范围请求/304 缓存）
│   └── ResourceMediaType.java       # 媒体类型枚举（图片/视频/音频等）
└── CellWebAutoConfiguration.java
```

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。
