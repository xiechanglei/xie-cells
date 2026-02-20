# 通用异常类使用指南

通用异常类提供了一些标准的异常类型，用于处理常见的业务场景。

## 功能介绍

- ResourceNotFoundException: 资源未找到异常，用于表示请求的资源不存在
- UnauthorizedException: 未授权异常，用于表示用户未登录或没有权限访问某个资源

## 使用示例

```java
import io.github.xiechanglei.cell.common.bean.exception.ResourceNotFoundException;
import io.github.xiechanglei.cell.common.bean.exception.UnauthorizedException;

public class Example {
    
    public void handleResourceNotFound() {
        // 当找不到指定资源时抛出异常
        boolean resourceExists = checkIfResourceExists("some-resource-id");
        if (!resourceExists) {
            throw new ResourceNotFoundException();
        }
    }

    public void handleUnauthorizedAccess() {
        // 当用户未授权访问某个资源时抛出异常
        boolean userHasPermission = checkUserPermission("user-id", "resource-id");
        if (!userHasPermission) {
            throw new UnauthorizedException();
        }
    }

    // 模拟检查资源是否存在
    private boolean checkIfResourceExists(String resourceId) {
        // 实际业务逻辑...
        return false;
    }

    // 模拟检查用户权限
    private boolean checkUserPermission(String userId, String resourceId) {
        // 实际业务逻辑...
        return false;
    }
}
```

## API 参考

- `ResourceNotFoundException` - 资源未找到异常，继承自RuntimeException
- `UnauthorizedException` - 未授权异常，继承自RuntimeException，表示用户未登录或没有权限访问某个资源