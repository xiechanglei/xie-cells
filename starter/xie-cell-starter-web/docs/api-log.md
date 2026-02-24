# API 日志记录

本文档介绍 Web Starter 提供的 API 日志记录功能。

## 功能说明

通过自定义注解和 AOP 切面，实现 API 请求日志的自动记录。

## 使用方式

### 1. 实现日志处理器

首先实现 `ApiLogHandler` 接口，定义日志处理逻辑：

```java
@Component
public class CustomApiLogHandler implements ApiLogHandler {
    
    @Override
    public void handle(String name, String ip, String path, Map<String, Object> params) {
        // 自定义日志处理逻辑
        log.info("接口：{}, IP: {}, 路径：{}, 参数：{}", name, ip, path, params);
        
        // 可以将日志存储到数据库、发送到日志系统等
    }
}
```

### 2. 使用注解标记接口

在需要记录日志的接口方法上使用 `@ApiLog` 注解：

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @ApiLog(value = "创建用户", params = {"username", "email"})
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @ApiLog(value = "删除用户", params = {"id"})
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

## 配置说明

```properties
# API 日志配置前缀：cell.web.log
cell.web.log.enable=true  # 是否开启 API 日志记录，默认 true
```

## 注解说明

### @ApiLog

| 属性 | 说明 | 默认值 |
|-----|------|-------|
| `value` | 接口名称 | 空字符串 |
| `params` | 需要记录的请求参数 | 空数组 |

## 核心类

| 类名 | 说明 |
|-----|------|
| `CellApiLogAop` | API 日志切面类 |
| `ApiLogHandler` | 日志处理器接口 |
| `ApiLog` | 日志记录注解 |
| `CellApiLogConfigProperties` | 日志配置属性类 |

## 日志信息

日志处理器会收到以下信息：

| 参数 | 说明 |
|-----|------|
| `name` | 接口名称，由注解的 `value` 属性指定 |
| `ip` | 请求方的 IP 地址 |
| `path` | 请求的路径（URI） |
| `params` | 标记需要记录的请求参数 |

## 注意事项

1. **性能考虑**：日志处理应该是异步的，避免影响主业务流程
2. **敏感信息**：注意不要记录敏感信息，如密码、token 等
3. **存储策略**：根据业务需求制定合适的日志存储和清理策略
