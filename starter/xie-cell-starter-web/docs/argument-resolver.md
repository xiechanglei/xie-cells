# 自定义参数解析器

本文档介绍 Web Starter 提供的自定义参数解析器功能。

## 功能说明

提供自定义的 HandlerMethodArgumentResolver 实现，简化 Controller 方法参数处理。

## 支持的解析类型

### 1. PageRequest 自动解析

自动解析分页参数，无需在 Controller 中手动处理。

**使用示例：**
```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    public Page<User> getUsers(PageRequest pageRequest) {
        // pageRequest 已自动解析，包含页码、每页大小和排序信息
        return userService.findAll(pageRequest);
    }
}
```

**请求参数：**
```
GET /api/users?pageNo=1&pageSize=20&sort=id:asc,name:desc
```

**参数说明：**
- `pageNo` - 页码，从 1 开始，默认 1
- `pageSize` - 每页大小，默认 20
- `sort` - 排序字段，格式：字段名：排序方式，多个字段用逗号分隔

### 2. Date 类型自动转换

支持多种日期格式的自动转换。

**支持的格式：**
- `yyyy-MM-dd HH:mm:ss` - 完整日期时间格式
- `yyyy-MM-dd` - 仅日期格式
- 时间戳 - 毫秒时间戳

**使用示例：**
```java
@RestController
@RequestMapping("/api")
public class OrderController {

    @GetMapping("/orders")
    public List<Order> getOrders(Date startTime, Date endTime) {
        // startTime 和 endTime 自动转换
        return orderService.findByTimeRange(startTime, endTime);
    }
}
```

**请求参数：**
```
GET /api/orders?startTime=2024-01-01&endTime=2024-01-31 23:59:59
或
GET /api/orders?startTime=1704067200000&endTime=1706745599000
```

## 配置说明

```properties
# 参数解析器配置前缀：cell.web.resolver
cell.web.resolver.page-no-name=pageNo    # 分页页码参数名，默认 pageNo
cell.web.resolver.page-size-name=pageSize # 每页大小参数名，默认 pageSize
```

## 核心类

| 类名 | 说明 |
|-----|------|
| `CellWebResolverConfiguration` | 参数解析器配置类 |
| `CellWebPageResolver` | 分页参数解析器 |
| `CellWebDateResolver` | 日期参数解析器 |
| `WebPageParam` | 分页参数封装类 |
| `WebResolver` | 参数解析器注解 |
| `WebConverter` | 转换器注解 |

## 自定义参数解析器

实现 `WebResolver` 或 `WebConverter` 注解的自定义解析器：

```java
@WebResolver
@Component
public class CustomParameterResolver implements HandlerMethodArgumentResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CustomType.class);
    }

    @Override
    public Object resolveArgument(...) {
        // 自定义解析逻辑
    }
}
```
