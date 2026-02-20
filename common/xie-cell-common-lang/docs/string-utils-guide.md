# StringHelpers 使用指南

StringHelpers 是一个字符串操作工具类，提供了常用的字符串判断和操作方法。

## 功能列表

### 1. isEmpty(String str)
检查字符串是否为空或null。

```java
StringHelpers.isEmpty(null);      // true
StringHelpers.isEmpty("");        // true
StringHelpers.isEmpty(" ");       // false
StringHelpers.isEmpty("abc");     // false
```

### 2. isBlank(String str)
检查字符串是否为空白（null、空字符串或只包含空白字符）。

```java
StringHelpers.isBlank(null);      // true
StringHelpers.isBlank("");        // true
StringHelpers.isBlank(" ");       // true
StringHelpers.isBlank("\t\n\r");  // true
StringHelpers.isBlank("abc");     // false
StringHelpers.isBlank(" abc ");    // false
```

### 3. isSame(String str1, String str2)
判断两个字符串是否相同（考虑null情况）。

```java
StringHelpers.isSame("abc", "abc");  // true
StringHelpers.isSame(null, null);    // true
StringHelpers.isSame("abc", null);   // false
StringHelpers.isSame(null, "abc");   // false
StringHelpers.isSame("abc", "def");  // false
```

## 注意事项

- 所有方法都是静态方法，可以直接通过类名调用
- 使用了Lombok的@UtilityClass注解，确保此类不可实例化
- 方法经过充分测试，具有良好的性能和准确性