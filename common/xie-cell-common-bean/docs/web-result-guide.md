# Web结果封装类 (WebResult) 使用指南

Web结果封装类用于统一Web接口的返回格式，包含返回码、成功标志、消息和数据。

## 功能介绍

- 统一Web接口返回格式
- 包含返回码、成功标志、消息和数据
- 提供便捷的成功和失败结果构建方法
- 支持使用CodeMessage对象构建失败结果

## 使用示例

```java
import io.github.xiechanglei.cell.common.bean.message.WebResult;
import io.github.xiechanglei.cell.common.bean.message.CodeMessage;

public class Example {
    public void successResult() {
        // 构建成功结果
        WebResult<String> successResult = WebResult.success("操作成功");
        System.out.println("code: " + successResult.code());      // 输出: code: 0
        System.out.println("success: " + successResult.success()); // 输出: success: true
        System.out.println("msg: " + successResult.msg());        // 输出: msg: null
        System.out.println("data: " + successResult.data());      // 输出: data: 操作成功

        // 构建带自定义码的成功结果
        WebResult<Integer> customSuccess = WebResult.success(100, 200); // 数据为100，码为200
        System.out.println("code: " + customSuccess.code());      // 输出: code: 200
        System.out.println("data: " + customSuccess.data());      // 输出: data: 100
    }

    public void failedResult() {
        // 构建失败结果
        WebResult<?> failedResult = WebResult.failed("操作失败", 400);
        System.out.println("code: " + failedResult.code());      // 输出: code: 400
        System.out.println("success: " + failedResult.success()); // 输出: success: false
        System.out.println("msg: " + failedResult.msg());        // 输出: msg: 操作失败
        System.out.println("data: " + failedResult.data());      // 输出: data: null

        // 构建默认失败结果（码为-1）
        WebResult<?> defaultFailed = WebResult.failed("未知错误");
        System.out.println("code: " + defaultFailed.code());     // 输出: code: -1
    }

    public void failedWithCodeMessage() {
        // 使用CodeMessage构建失败结果
        CodeMessage error = CodeMessage.of(1001, "用户不存在");
        WebResult<?> codeMessageFailed = WebResult.failed(error);
        System.out.println("code: " + codeMessageFailed.code());      // 输出: code: 1001
        System.out.println("msg: " + codeMessageFailed.msg());        // 输出: msg: 用户不存在
        System.out.println("success: " + codeMessageFailed.success()); // 输出: success: false
    }

    public void businessUsage() {
        // 在业务方法中使用
        WebResult<User> userResult = getUserById(123);
        if (userResult.success()) {
            User user = userResult.data();
            System.out.println("获取用户成功: " + user);
        } else {
            System.out.println("获取用户失败: " + userResult.msg());
        }
    }

    // 模拟获取用户的方法
    public WebResult<User> getUserById(long id) {
        if (id <= 0) {
            CodeMessage invalidIdError = CodeMessage.of(1001, "用户ID无效");
            return WebResult.failed(invalidIdError);
        }
        // 实际业务逻辑...
        User user = new User(id, "张三");
        return WebResult.success(user);
    }

    // 用户类示例
    static class User {
        private long id;
        private String name;

        public User(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }
}
```

## API 参考

- `WebResult.success(T data)` - 构建成功结果（默认码为0）
- `WebResult.success(T data, int code)` - 构建带自定义码的成功结果
- `WebResult.failed(String message, int code)` - 构建带码的失败结果
- `WebResult.failed(String message)` - 构建默认码的失败结果（码为-1）
- `WebResult.failed(CodeMessage codeMessage)` - 通过CodeMessage对象构建失败结果
- `code()` - 获取返回码
- `success()` - 获取成功标志
- `msg()` - 获取返回消息
- `data()` - 获取返回数据