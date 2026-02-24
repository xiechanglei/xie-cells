# JSON 序列化配置

本文档介绍 Web Starter 提供的 JSON 序列化优化功能。

## 功能说明

自动配置 Jackson 序列化规则，优化 API 返回结果格式。

## 序列化规则

### 1. Long 类型序列化为字符串

防止前端 JavaScript 精度丢失（JavaScript 只能安全表示 -2^53 到 2^53 之间的整数）。

**序列化前：**
```json
{
  "id": 1234567890123456789
}
```

**序列化后：**
```json
{
  "id": "1234567890123456789"
}
```

### 2. Date 类型序列化为时间戳字符串

便于前端 JavaScript 直接使用 `new Date(timestamp)` 还原。

**序列化前：**
```json
{
  "createTime": "2024-01-01 12:00:00"
}
```

**序列化后：**
```json
{
  "createTime": "1704081600000"
}
```

### 3. Page 对象自定义序列化

简化 Spring Data JPA 的 Page 对象返回结果。

**序列化后格式：**
```json
{
  "totalElements": 100,
  "totalPages": 10,
  "pageNo": 1,
  "pageSize": 20,
  "first": true,
  "last": false,
  "content": [...]
}
```

## 配置说明

```properties
# 参数解析器配置前缀：cell.web.resolver
cell.web.resolver.page-no-name=pageNo    # Page 对象页码参数名，默认 pageNo
cell.web.resolver.page-size-name=pageSize # Page 对象每页大小参数名，默认 pageSize
```

## 核心类

| 类名 | 说明 |
|-----|------|
| `CellWebJsonSerializeConfig` | JSON 序列化配置类 |
| `WebLongSerializer` | Long 类型序列化器 |
| `WebDateSerializer` | Date 类型序列化器 |
| `CellJpaPageSerializer` | Page 对象序列化器 |

## 使用示例

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        // Long 类型的 id 会自动序列化为字符串
        return new User(id, "张三", new Date());
    }

    @GetMapping("/users")
    public Page<User> getUsers(PageRequest pageRequest) {
        // Page 对象会自动序列化为简化格式
        return userService.findAll(pageRequest);
    }
}
```
