# Xie-Cells Common Lang 模块

这是一个通用语言级别工具模块，提供各种常用的Java语言级别工具类。

## 功能列表

- [字符串工具类 (StringHelpers)](./docs/string-utils-guide.md) - 提供字符串判空、判空格、比较等功能
- [字节操作工具类 (ByteArrayHelper & ByteHelper)](./docs/byte-helper-guide.md) - 提供字节数组与十六进制字符串相互转换功能

## 使用方式

在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cell-common-lang</artifactId>
    <version>${xie.cells.version}</version>
</dependency>
```

## 依赖管理

此模块遵循项目的依赖管理规范，所有第三方依赖的版本号均在根目录pom.xml中统一管理。

## 详细文档

请参阅 `docs` 目录下的详细使用手册。

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。