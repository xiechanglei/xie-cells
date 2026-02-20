# 数据组装类 (DataFit) 使用指南

数据组装类用于构建动态属性对象，通常用于构建需要序列化的数据结构。

## 功能介绍

- 动态添加键值对数据
- 支持链式调用
- 将数据转换为URL参数格式（有序）

## 使用示例

```java
import io.github.xiechanglei.cell.common.bean.message.DataFit;

public class Example {
    public void basicUsage() {
        // 使用链式调用构建DataFit对象
        DataFit dataFit = DataFit.of("name", "xiechanglei")
                                .fit("age", 18)
                                .fit("city", "Beijing");

        System.out.println(dataFit.get("name")); // 输出: xiechanglei
        System.out.println(dataFit.get("age"));  // 输出: 18
    }

    public void urlParameterConversion() {
        // 构建数据并转换为URL参数格式
        DataFit params = DataFit.of("username", "test")
                               .fit("password", "123456")
                               .fit("action", "login");

        String urlParams = params.toUrl();
        System.out.println(urlParams); // 输出: action=login&password=123456&username=test
        // 注意：由于继承了TreeMap，参数是按key排序的
    }

    public void dynamicDataBuilding() {
        DataFit data = new DataFit();

        // 根据条件动态添加数据
        boolean includeEmail = true;
        if (includeEmail) {
            data.fit("email", "test@example.com");
        }

        data.fit("username", "test_user");
        System.out.println(data); // 输出: {email=test@example.com, username=test_user}
    }
}
```

## API 参考

- `DataFit.of(String key, Object value)` - 静态方法，快速构建DataFit对象
- `fit(String key, Object value)` - 添加键值对并返回自身，支持链式调用
- `toUrl()` - 将数据转换为URL参数格式（有序）