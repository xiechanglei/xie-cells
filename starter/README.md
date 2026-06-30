# Xie-Cells Starter 模块

Spring Boot Starter 模块集合，提供各种自定义的 Spring Boot Starter。这个模块可以说是整个starter分类的核心，其他的很多的功能性的starter模块都依赖于这个模块提供的基础功能。

## 子模块

- [xie-cell-starter-spring](./xie-cell-starter-spring/README.md) - Spring Boot核心功能Starter
- [xie-cell-starter-web](./xie-cell-starter-web/README.md) - Web功能Starter

## todo

- 设计角色权限体系
    - xie-cell-starter-rbac-core  表结构初始化，权限入库，角色初始化，entity, repository, initializer
    - xie-cell-starter-rbac-web  权限拦截器，权限注解，权限验证（包含xie-cell-starter-core）
    - xie-cell-starter-rbac-api   用户角色权限相关接口，包含 xie-cell-starter-core，xie-cell-starter-web
- 小程序接入权限体系
  - xie-cell-starter-wechat-mini  小程序接入权限体系，如何接入小程序以及接口权限规划
- 简单的认证体系  只对接口进行简单的是否登陆认证
  - xie-cell-starter-simple-auth  简单的认证体系，用户名和密码配置在配置文件之中，或者自定义用户民和密码校验机制，所有的接口只判断用户是否登陆，登陆之后可以访问所有的接口，不会有详细的角色以及权限划分

## 许可证

本项目采用 [Apache 2.0 许可证](https://www.apache.org/licenses/LICENSE-2.0)。