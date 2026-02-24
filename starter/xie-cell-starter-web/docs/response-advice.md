# 全局响应封装与异常处理

本文档介绍 Web Starter 提供的全局响应封装和异常处理功能。

## 功能说明

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

## 配置说明

```properties
# 响应封装配置前缀：cell.web.advice
cell.web.advice.exception.enable=true   # 是否开启全局异常处理，默认 true
cell.web.advice.response.enable=true    # 是否开启全局返回结果封装，默认 true
```

## 核心类

| 类名 | 说明 |
|-----|------|
| `CellWebExceptionAdvice` | 全局异常处理器 |
| `CellWebGlobalResponseBodyAdvice` | 全局响应体处理器 |
| `DisableResponseAdvice` | 禁用响应封装注解 |
| `CellWebAdviceConfigProperties` | 配置属性类 |

## 异常处理类型

| 异常类型 | 处理说明 |
|---------|---------|
| `BusinessException` | 业务异常，返回错误码和错误信息 |
| `UnauthorizedException` | 未授权异常，返回 401 状态码 |
| `ConstraintViolationException` | 参数校验失败，返回参数错误提示 |
| `NoResourceFoundException` | 资源未找到，返回 404 状态码 |
| `Throwable` | 其他未分类异常，返回错误信息 |
