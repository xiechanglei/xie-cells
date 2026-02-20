# Xie-Cells Common Bean 模块

提供业务开发相关的基础类和功能。

## 功能列表

- [业务异常类 (BusinessException)](./docs/business-exception-guide.md) - 业务异常封装，提供code和message属性
- [通用异常类 (ResourceNotFoundException & UnauthorizedException)](./docs/general-exceptions-guide.md) - 标准异常类型，用于处理常见业务场景
- [错误码消息类 (CodeMessage)](./docs/code-message-guide.md) - 错误码与消息的定义类
- [数据组装类 (DataFit)](./docs/data-fit-guide.md) - 用于构建动态属性对象
- [Web结果封装类 (WebResult)](./docs/web-result-guide.md) - 统一Web接口返回格式

## 使用方式

在项目中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cell-common-bean</artifactId>
    <version>${xie.cells.version}</version>
</dependency>
```

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。