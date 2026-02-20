# 错误码消息类 (CodeMessage) 使用指南

错误码消息类用于定义错误码和消息的配对，通常用于统一管理系统的错误码。

## 功能介绍

- 定义错误码和消息的配对
- 提供静态工厂方法创建对象
- 便于统一管理错误码
- 使用Java Record类实现，自动提供getter方法

## 使用示例

```java
import io.github.xiechanglei.cell.common.bean.message.CodeMessage;

public class Example {
    // 定义全局错误码字典类
    public class Errors {
        public static class User {
            public static final CodeMessage USER_NOT_FOUND = CodeMessage.of(1000001, "用户不存在");
            public static final CodeMessage USER_PASSWORD_ERROR = CodeMessage.of(1000002, "用户密码错误");
        }

        public static class Order {
            public static final CodeMessage ORDER_NOT_FOUND = CodeMessage.of(2000001, "订单不存在");
            public static final CodeMessage ORDER_STATUS_ERROR = CodeMessage.of(2000002, "订单状态错误");
        }
    }

    public void test() {
        // 使用预定义的错误码
        CodeMessage error = Errors.User.USER_NOT_FOUND;
        System.out.println("错误码: " + error.code());      // 输出: 错误码: 1000001
        System.out.println("错误信息: " + error.message()); // 输出: 错误信息: 用户不存在
    }

    public void directUsage() {
        // 直接创建错误码消息对象
        CodeMessage customError = CodeMessage.of(9999999, "自定义错误");
        System.out.println(customError.code() + ": " + customError.message());
    }
}
```

## API 参考

- `CodeMessage.of(int code, String message)` - 创建错误码消息对象
- `code()` - 获取错误码（Record自动生成的getter）
- `message()` - 获取错误消息（Record自动生成的getter）