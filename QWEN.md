# Xie-Cells 项目状态文档

## 项目概述
- 项目名称: xie-cells
- 父POM名称: xie-cells-parent
- 项目类型: Java 工具框架
- 主要目标: 集成常用工具和自定义 Spring Boot Starter，加速常规项目快速开发
- 当前状态: 项目初始化阶段

## 项目结构
- 根目录: `/home/xie/code-work-space/java/xie-cells`
- 使用 Maven 进行依赖管理 (pom.xml 存在)
- 包含 .git 版本控制
- 已配置 Spring Boot 3.5.13 父项目
- 支持 Java 21
- 已配置 Lombok、单元测试等基础依赖

## 模块包结构说明
为了便于快速定位源码，以下是已创建模块的包结构映射：
- xie-cell-common-lang: io.github.xiechanglei.cell.common.lang
  - 字符串工具: io.github.xiechanglei.cell.common.lang.string
  - 字节操作工具: io.github.xiechanglei.cell.common.lang.bytes
- xie-cell-common-bean: io.github.xiechanglei.cell.common.bean
  - 业务异常类: io.github.xiechanglei.cell.common.bean.exception
  - 消息封装类: io.github.xiechanglei.cell.common.bean.message
- xie-cell-starter-spring: io.github.xiechanglei.cell.starter.spring
  - 自定义日志处理: io.github.xiechanglei.cell.starter.spring.log
新的包以及模块会根据项目需求逐步添加，保持清晰的包结构有助于代码维护和功能扩展。


## 项目架构与规范

### 模块规范
1. 每个Maven模块下都应包含一个README.md文件，描述当前模块的功能
2. 模块分为两种：
   - 父模块：包含子模块，在README中列出所有子模块及其简单描述
   - 子模块：包含具体实现代码，在README中说明该模块包含的功能
3. 详细使用手册：在模块目录下创建docs目录，用不同Markdown文件说明各功能使用方式
4. 示例子模块：xie-cell-common-lang - 提供基础语言级别工具类

### 项目模块化规范
- 采用多层级模块方式组织项目
- 根目录下包含分类模块：common(通用模块)、starter(自定义SpringBoot Starter)、plugin(Maven插件)
- 分类模块本身也是Maven模块，只用于维护其子模块
- 分类模块的pom.xml中不需要重复定义properties（已在根目录pom.xml中定义）
- 功能模块位于分类模块下，命名格式：xie-cells-分类名-模块名
- 每个功能模块都需要在根目录pom.xml的dependencyManagement中维护版本号
- 根目录pom.xml中定义的全局依赖（如lombok、spring-boot-starter-test）不需要在子模块中重复定义
- 允许其他项目按需引用，避免不必要的依赖
- 在父模块pom.xml中声明所有子模块
- 在父模块dependencyManagement中统一管理子模块间依赖版本，避免冲突

### 依赖管理规范
- 所有第三方依赖的版本号应在根目录pom.xml的properties中统一定义
- 在根目录pom.xml的dependencyManagement部分声明所有依赖的版本
- 子模块引用依赖时，不需要指定版本号，从父POM继承
- 依赖版本号命名规范：`{groupId}.{artifactId}.version`，如`com.google.auto.service.version`
- 依赖管理遵循最小权限原则，只在需要的模块中引入依赖

### 包结构规范
- 每个模块的基本包名: io.github.xiechanglei.cell.功能模块名称,比如，
  - 通用工具模块: io.github.xiechanglei.cell.common.lang ，
  - redis Starter: io.github.xiechanglei.cell.starter.redis
- 模块下的功能包结构需要创建二级包，命名格式为功能类型，如：
  - 字符串工具包: io.github.xiechanglei.cell.common.lang.string，
  - 日期时间工具: io.github.xiechanglei.cell.common.lang.date

### 类命名规范
- 类名应使用大驼峰命名法，且具有描述性，能够清晰表达类的功能和用途
- 工具类命名应以Helper结束，如StringHelper、DateHelper等.一般工具类不需要实例化，建议使用interface 的方式实现，工具类中方法过多的时候，可以将功能相关的方法分成不同的接口，然后在统一的工具类中继承这些接口实现集成
- 配置类命名应以Properties结尾，如RedisProperties、DatabaseProperties等
- 自动配置类命名应以AutoConfiguration结尾，如RedisAutoConfiguration、DatabaseAutoConfiguration等
  
### Starter开发规范
- 每个自定义Starter需包含：
  - 配置类 (Configuration Properties)
  - 自动配置类 (Auto Configuration)
  - spring.factories文件 (注册自动配置类)
- 使Starter能够被Spring Boot自动发现并加载

### 代码质量要求
- 所有公共方法必须有Javadoc注释（作用、参数、返回值说明）
- 关键逻辑处添加适当代码注释，解释复杂逻辑实现思路
- 异常处理：封装原始异常为业务异常，不直接抛出
- 工具类需提供完整单元测试，测试覆盖率不低于80%

### 版本发布规范
- 采用语义化版本控制 (MAJOR.MINOR.PATCH)
- 不兼容API修改：增加主版本号
- 向后兼容功能添加：增加次版本号
- 向后兼容问题修复：增加修订号
- 在GitHub维护发布标签，便于用户查看和使用特定版本

### 文档规范
- Markdown文档中的链接应指向相关文件，如子模块链接到对应README.md
- 项目采用Apache 2.0许可证（通过在线链接引用）
- 根目录README.md需说明两种使用方式：Parent方式和单独引用方式
- 分类模块README不包含使用方式说明
- 功能模块README只需说明单独引用的使用方式，不得包含Parent方式的说明
- 根目录README只列出一级子模块，不列出孙子模块
- README.md应包含版本徽章等常见标识
- 常见徽章包括：版本、许可证、Java版本、Spring Boot版本等
- 重要：文档中提及的功能和类必须真实存在，不得虚构或假设尚未实现的功能
- 重要：功能模块的README中不应包含业务逻辑无关的自动配置类说明

## 决策权说明

以下事项需由用户（项目负责人）决定，AI助手不应擅自决定：
1. 项目模块的具体名称和数量
2. 每个模块的具体功能和实现细节
3. 项目整体架构设计
4. 依赖库的选择和版本
5. 代码的具体实现方式
6. 模块间的依赖关系
7. 发布策略和时间安排

## 备注
此项目为长期开发项目，使用 QWEN.md 跟踪项目进度。