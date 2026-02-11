# Xie-Cells Annotation Processor Tools

此模块包含Xie-Cells框架使用的各种注解处理器工具。

## 环境要求

- Java 21+
- Maven 3.6+

## 功能特性

- `@Hello` - 用于向类中的方法添加System.out.println("hello world!")语句
- `@Constructor` - 用于为类生成构造函数
- 其他注解处理器...

## 使用方式

此模块演示了如何使用JDK内部API来实现编译时代码增强功能。

## 当前实现

### @Hello 注解

这是一个演示注解，用于展示如何在编译时向方法中添加代码。

使用方式：
```java
@Hello
public class MyClass {
    public void myMethod() {
        // 编译时，此方法会自动添加System.out.println("hello world!");语句
    }
}
```

### @Constructor 注解

这是一个演示注解，用于展示如何在编译时为类生成构造函数。

使用方式：
```java
@Constructor
public class MyClass {
    private String name;
    private int age;
    // 编译时，会自动生成包含所有字段的构造函数
}
```

### 实现原理

该模块使用了JDK编译器的内部API（com.sun.tools.javac），通过以下方式实现：

1. `BaseProcessor` - 提供了访问Javac内部API的基类，封装了公共逻辑
2. `HelloProcessor` - 注解处理器，遍历语法树并向方法中添加代码
3. `ConstructorProcessor` - 注解处理器，为类生成构造函数
4. 编译时通过`--add-exports`参数访问内部API

注意：需要在编译时添加适当的模块导出参数才能使用这些内部API。

## 扩展性

该架构支持创建多种不同的注解处理器，每种处理器只需继承BaseProcessor并实现processTree方法即可。
公共的处理逻辑（遍历被注解的元素）已在BaseProcessor中实现，子类只需关注具体的语法树修改逻辑。

## 依赖管理

此模块遵循项目的依赖管理规范，所有第三方依赖的版本号均在根目录pom.xml中统一管理。