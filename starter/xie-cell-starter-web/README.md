# Xie-Cells Web Starter 模块

[![Version](https://img.shields.io/badge/version-3.5.10.1-blue.svg)](https://github.com/xiechanglei/xie-cells)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen.svg)](https://spring.io/projects/spring-boot)

Web 功能 Starter，提供 Web 应用的基础配置和扩展功能，基于 Spring Boot Web 进行增强。

## 功能特性

- **全局响应统一封装** - 自动封装 Controller 返回值为统一响应格式
- **全局异常处理** - 提供统一的异常处理和错误响应格式
- **跨域配置支持** - 提供可配置的跨域访问支持
- **JSON 序列化优化** - Long 类型转字符串防止精度丢失、Date 类型转时间戳、Page 对象自定义序列化
- **自定义参数解析** - 支持 PageRequest 自动解析、Date 类型自动转换
- **资源响应工具** - 支持文件下载和媒体资源预览（图片/视频/音频）
- **范围请求支持** - 支持 HTTP 206 Partial Content（视频拖拽、断点续传）
- **缓存验证支持** - 支持 HTTP 304 Not Modified（减少不必要传输）
- **API 日志记录** - 支持自定义注解记录 API 请求日志
- **请求工具类** - 提供获取当前请求、IP 地址、Cookie 等工具方法
- **Undertow 容器** - 使用高性能 Undertow 替代默认 Tomcat

## 快速开始

在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.xiechanglei</groupId>
    <artifactId>xie-cell-starter-web</artifactId>
    <version>3.5.10.1</version>
</dependency>
```

## 配置说明

| 配置前缀 | 功能说明 |
|---------|---------|
| `cell.web.advice` | 响应封装和全局异常处理配置 |
| `cell.web.cross` | 跨域访问配置 |
| `cell.web.resolver` | 自定义参数解析器配置 |
| `cell.web.log` | API 日志记录配置 |

## 详细文档

- [全局响应封装与异常处理](docs/response-advice.md)
- [跨域配置](docs/cors.md)
- [JSON 序列化配置](docs/json-serialize.md)
- [自定义参数解析器](docs/argument-resolver.md)
- [资源响应工具](docs/resource-response.md)
- [API 日志记录](docs/api-log.md)

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。
