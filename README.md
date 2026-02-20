# Xie-Cells - Java 工具框架

![Version](https://img.shields.io/badge/version-3.5.10.1-blue.svg)
![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)
![Java Version](https://img.shields.io/badge/java-21-red.svg)
![Spring Boot](https://img.shields.io/badge/spring--boot-3.5.10-brightgreen.svg)

Xie-Cells 是一个集成常用工具和自定义 Spring Boot Starter 的 Java 框架，旨在加速常规项目的快速开发。

## 项目概述

本项目采用多模块方式组织，每个功能相对独立的工具集或 Starter 作为独立的 Maven 子模块，允许其他项目按需引用，避免不必要的依赖。

## 模块列表

项目采用多层级模块架构，分为三大类：

### 通用工具模块 (common)
- [`xie-cells-common`](./common/README.md) - 通用工具模块聚合

### Spring Boot Starter 模块 (starter)
- [`xie-cells-starter`](./starter/README.md) - Spring Boot Starter 模块聚合

### Maven 插件模块 (plugin)
- [`xie-cells-plugin`](./plugin/README.md) - Maven 插件模块聚合

更多模块正在规划和开发中...

## 使用方式

您可以选择以下任一种方式来使用本项目：

### 方式一：Parent方式

将本项目作为父项目引入，这样就不需要在每个模块中重复定义版本号：

```xml
<parent>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cells-parent</artifactId>
    <version>3.5.10.1</version>
    <relativePath/>
</parent>
```

然后在需要的模块中直接引入依赖，无需指定版本号：

```xml
<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cells-common-lang</artifactId>
</dependency>
```

### 方式二：单独引用

如果您不想使用Parent方式，也可以单独引用某个模块，这时需要显式指定版本号：

```xml
<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cells-common-lang</artifactId>
    <version>3.5.10.1</version>
</dependency>
```

## 版本发布

我们采用语义化版本控制 (MAJOR.MINOR.PATCH)：
- 不兼容API修改：增加主版本号
- 向后兼容功能添加：增加次版本号
- 向后兼容问题修复：增加修订号

## 贡献指南

请参考各子模块的 README.md 文件了解具体贡献指南。

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。