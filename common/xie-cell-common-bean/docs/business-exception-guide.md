# 业务异常类 (BusinessException) 使用指南

业务异常类提供了封装异常码和异常信息的功能，主要用于业务系统的异常处理。

## 功能介绍

- 封装异常码和异常信息
- 提供静态工厂方法创建异常对象
- 与WebResult及全局异常处理器配合使用
- 支持通过CodeMessage对象创建异常

## 使用示例

```java
import io.github.xiechanglei.cell.common.bean.exception.BusinessException;
import io.github.xiechanglei.cell.common.bean.message.CodeMessage;

public class Example {
    // 定义全局异常码字典类
    public class BusinessError {
        // 用户相关异常
        public static class USER {
            public static final BusinessException USER_NOT_FOUND = BusinessException.of(1000001, "用户不存在");
            public static final BusinessException USER_PASSWORD_ERROR = BusinessException.of(1000002, "用户密码错误");
            public static final BusinessException USER_LOCKED = BusinessException.of(1000003, "用户被锁定");
            public static final BusinessException USER_EXPIRED = BusinessException.of(1000004, "用户已过期");
        }

        // 订单相关异常
        public static class ORDER {
            public static final BusinessException ORDER_NOT_FOUND = BusinessException.of(2000001, "订单不存在");
            public static final BusinessException ORDER_STATUS_ERROR = BusinessException.of(2000002, "订单状态错误");
            public static final BusinessException ORDER_AMOUNT_ERROR = BusinessException.of(2000003, "订单金额错误");
            public static final BusinessException ORDER_PAY_ERROR = BusinessException.of(2000004, "订单支付错误");
        }
    }

    // 定义错误码字典类（使用CodeMessage）
    public class ErrorCodes {
        public static class USER {
            public static final CodeMessage USER_NOT_FOUND = CodeMessage.of(1000001, "用户不存在");
            public static final CodeMessage USER_PASSWORD_ERROR = CodeMessage.of(1000002, "用户密码错误");
        }
    }

    public void test() {
        // 抛出业务异常
        throw BusinessError.USER.USER_NOT_FOUND;
    }

    public void anotherExample() {
        // 直接创建业务异常
        throw BusinessException.of(9999999, "自定义业务异常");
    }

    public void codeMessageExample() {
        // 通过CodeMessage创建业务异常
        CodeMessage error = ErrorCodes.USER.USER_NOT_FOUND;
        throw BusinessException.of(error);
    }
}
```

## API 参考

- `BusinessException.of(int code, String message)` - 创建带有异常码和消息的业务异常
- `BusinessException.of(String message)` - 创建只带消息的业务异常（异常码默认为-1）
- `BusinessException.of(CodeMessage codeMessage)` - 通过CodeMessage对象创建业务异常
- `getCode()` - 获取异常码
- `getMessage()` - 获取异常信息