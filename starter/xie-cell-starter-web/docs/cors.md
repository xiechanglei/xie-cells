# 跨域配置

本文档介绍 Web Starter 提供的跨域访问配置功能。

## 功能说明

提供基于 Spring MVC 的跨域访问支持，解决前后端分离项目中的跨域问题。

## 配置说明

```properties
# 跨域配置前缀：cell.web.cross
cell.web.cross.enable=true              # 是否开启跨域配置，默认 true
cell.web.cross.mapping=/**              # 允许跨域的路径，默认 /**
cell.web.cross.allowed-origins=*        # 允许的访问源，默认 *
cell.web.cross.allowed-methods=*        # 允许的请求方式，默认 *
cell.web.cross.allowed-headers=*        # 允许的请求头，默认 *
cell.web.cross.allow-credentials=false  # 是否允许发送 Cookie，默认 false
cell.web.cross.max-age=1800             # 预检请求的有效期（秒），默认 1800
```

## 使用示例

### 开发环境配置

```properties
# 允许特定域名访问
cell.web.cross.allowed-origins=http://localhost:3000,http://127.0.0.1:3000
cell.web.cross.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cell.web.cross.allow-credentials=true
```

### 生产环境配置

```properties
# 生产环境建议指定具体域名
cell.web.cross.allowed-origins=https://www.example.com
cell.web.cross.allow-credentials=true
```

## 核心类

| 类名 | 说明 |
|-----|------|
| `CellWebCrossOriginConfiguration` | 跨域配置类 |
| `CellWebCrossConfigProperties` | 跨域配置属性类 |

## 注意事项

1. **生产环境安全**：生产环境建议指定具体域名，避免使用 `*`
2. **Cookie 跨域**：如需发送 Cookie，需同时设置 `allow-credentials=true` 和具体的 `allowed-origins`
3. **预检请求**：使用 `max-age` 可以减少预检请求次数，提高性能
