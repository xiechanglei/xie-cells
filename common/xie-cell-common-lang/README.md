# Xie-Cells Common Lang 模块

这是一个通用语言级别工具模块，提供各种常用的Java语言级别工具类。

## 功能列表

- 字符串工具类 (StringUtils) - 提供字符串判空、截取、格式化等功能
- 字节操作工具类 (ByteHelper) - 提供字节数组与十六进制字符串相互转换功能

## 使用方式

在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cell-common-lang</artifactId>
    <version>3.5.9.1</version>
</dependency>
```

或者如果您使用父POM的方式：

```xml
<parent>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cells-parent</artifactId>
    <version>3.5.9.1</version>
</parent>

<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cell-common-lang</artifactId>
</dependency>
```

## 依赖管理

此模块遵循项目的依赖管理规范，所有第三方依赖的版本号均在根目录pom.xml中统一管理。

## 详细文档

请参阅 `docs` 目录下的详细使用手册。

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。