# 字节操作工具类 (ByteArrayHelper & ByteHelper) 使用指南

字节操作工具类提供了便捷的字节操作功能，主要用于字节数组与十六进制字符串之间的相互转换。

## 功能介绍

- 将字节数组转换为十六进制字符串
- 将十六进制字符串转换为字节数组
- 构建字节数组
- 字节操作相关的其他辅助功能

## 使用示例

```java
import io.github.xiechanglei.cell.common.lang.bytes.ByteArrayHelper;
import io.github.xiechanglei.cell.common.lang.bytes.ByteHelper;

public class Example {
    public static void main(String[] args) {
        // 将字节数组转换为十六进制字符串
        byte[] bytes = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        String hexString = ByteArrayHelper.toHexString(bytes);
        System.out.println(hexString); // 输出: 48656C6C6F
        
        // 将十六进制字符串转换为字节数组
        byte[] result = ByteArrayHelper.hexToByteArray(hexString);
        System.out.println(new String(result)); // 输出: Hello
        
        // 构建字节数组
        byte[] byteArray = ByteArrayHelper.build(0x48, 0x65, 0x6C, 0x6C, 0x6F);
        System.out.println(new String(byteArray)); // 输出: Hello
        
        // 指定范围转换为十六进制字符串
        String partialHexString = ByteArrayHelper.toHexString(bytes, 0, 2);
        System.out.println(partialHexString); // 输出: 4865
    }
}
```

## API 参考

- `ByteArrayHelper.toHexString(byte[] bytes)` - 将字节数组转换为十六进制字符串
- `ByteArrayHelper.toHexString(byte[] bytes, int end)` - 将字节数组前end个元素转换为十六进制字符串
- `ByteArrayHelper.toHexString(byte[] bytes, int start, int end)` - 将字节数组指定范围转换为十六进制字符串
- `ByteArrayHelper.hexToByteArray(String hexString)` - 将十六进制字符串转换为字节数组
- `ByteArrayHelper.build(int... bytes)` - 从整数数组构建字节数组