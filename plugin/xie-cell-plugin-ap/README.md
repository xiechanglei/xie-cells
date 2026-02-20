# Xie-Cells Annotation Processor Tools

此模块包含Xie-Cells框架使用的各种注解处理器工具。

## 环境要求

- Java 21+
- Maven 3.6+

## 功能特性

## 使用方式

## 扩展性

该架构支持创建多种不同的注解处理器，每种处理器只需继承BaseProcessor并实现processTree方法即可。
公共的处理逻辑（遍历被注解的元素）已在BaseProcessor中实现，子类只需关注具体的语法树修改逻辑。

## 依赖管理

此模块遵循项目的依赖管理规范，所有第三方依赖的版本号均在根目录pom.xml中统一管理。