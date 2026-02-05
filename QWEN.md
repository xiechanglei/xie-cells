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
- 已配置 Spring Boot 3.5.9 父项目
- 支持 Java 21
- 已配置 Lombok、单元测试等基础依赖

## 当前进度
- [x] 项目初始化完成
- [x] 基础 POM 配置完成 (Spring Boot 3.5.9, Java 21)
- [x] 项目规范制定完成
- [x] 根目录文档编写完成（含徽章）
- [x] 模块命名规范化
- [x] 分类模块结构建立
- [x] 分类模块文档编写完成
- [x] 第一个功能模块创建 (xie-cells-common-lang)
- [x] 功能模块依赖优化
- [x] 功能模块包结构建立
- [x] 创建第一个工具类及测试 (StringUtils)
- [x] 创建工具类使用文档
- [x] 模块名称规范化 (xie-cells-common-lang -> xie-cell-common-lang)
- [ ] 自定义 Starter 设计
- [ ] 更多功能模块开发

## 下一步计划
1. 继续开发更多工具类 (DateUtils, CollectionUtils, NumberUtils等) 在 xie-cell-common-lang 模块中
2. 创建相应的测试类和文档
3. 开发第一个自定义Spring Boot Starter

## 项目架构与规范

### 模块规范
1. 每个Maven模块下都应包含一个README.md文件，描述当前模块的功能
2. 模块分为两种：
   - 父模块：包含子模块，在README中列出所有子模块及其简单描述
   - 子模块：包含具体实现代码，在README中说明该模块包含的功能
3. 详细使用手册：在模块目录下创建docs目录，用不同Markdown文件说明各功能使用方式
4. 示例子模块：xie-cell-common-lang - 提供基础语言级别工具类

### 包结构规范
- 每个模块的基本包名: io.github.xiechanglei.cell.功能模块名称
- 通用工具类: io.github.xiechanglei.cell.common.lang
  - 字符串工具: io.github.xiechanglei.cell.common.lang.string
  - 日期时间工具: io.github.xiechanglei.cell.common.lang.date
  - 集合工具: io.github.xiechanglei.cell.common.lang.collection
  - 数字工具: io.github.xiechanglei.cell.common.lang.number
  - 其他功能按需创建二级包
- 自定义Starter: io.github.xiechanglei.cell.starter
  - Redis相关: io.github.xiechanglei.cell.starter.redis
  - Elasticsearch相关: io.github.xiechanglei.cell.starter.elasticsearch
  - 其他功能按需创建二级包
- 测试代码: io.github.xiechanglei.cell (对应源码结构)

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
- README.md需说明两种使用方式：Parent方式和单独引用方式（仅在根目录README中）
- 分类模块README不包含使用方式说明
- 功能模块README只需说明单独引用的使用方式
- 根目录README只列出一级子模块，不列出孙子模块
- README.md应包含版本徽章等常见标识
- 常见徽章包括：版本、许可证、Java版本、Spring Boot版本等

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