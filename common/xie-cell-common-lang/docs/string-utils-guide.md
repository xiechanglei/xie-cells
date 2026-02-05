# StringUtils 使用指南

StringUtils 是一个字符串操作工具类，提供了常用的字符串判断和操作方法。

## 功能列表

### 1. isEmpty(String str)
检查字符串是否为空或null。

```java
StringUtils.isEmpty(null);      // true
StringUtils.isEmpty("");        // true
StringUtils.isEmpty(" ");       // false
StringUtils.isEmpty("abc");     // false
```

### 2. isNotEmpty(String str)
检查字符串是否非空。

```java
StringUtils.isNotEmpty(null);   // false
StringUtils.isNotEmpty("");     // false
StringUtils.isNotEmpty(" ");    // true
StringUtils.isNotEmpty("abc");  // true
```

### 3. isBlank(String str)
检查字符串是否为空白（null、空字符串或只包含空白字符）。

```java
StringUtils.isBlank(null);      // true
StringUtils.isBlank("");        // true
StringUtils.isBlank(" ");       // true
StringUtils.isBlank("\t\n\r");  // true
StringUtils.isBlank("abc");     // false
StringUtils.isBlank(" abc ");    // false
```

### 4. isNotBlank(String str)
检查字符串是否非空白。

```java
StringUtils.isNotBlank(null);   // false
StringUtils.isNotBlank("");     // false
StringUtils.isNotBlank(" ");    // false
StringUtils.isNotBlank("\t\n\r");// false
StringUtils.isNotBlank("abc");  // true
StringUtils.isNotBlank(" abc "); // true
```

## 注意事项

- 所有方法都是静态方法，可以直接通过类名调用
- 使用了Lombok的@UtilityClass注解，确保此类不可实例化
- 方法经过充分测试，具有良好的性能和准确性