# 自定义日志处理器使用指南

自定义日志处理器提供了扩展的日志处理功能，包括自定义日志输出和基于标记的日志文件分离。

## 功能介绍

- 自定义日志处理器：继承LogAppender类可实现自定义日志处理逻辑
- 文件标记日志：通过FileMarker标记将日志输出到指定文件
- 日志扩展：支持输出到数据库、网络流等其他目的地

## 使用示例

### 1. 使用文件标记功能

```java
import io.github.xiechanglei.cell.starter.spring.log.FileMarker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Example {

    public void logWithFileMarker() {
        // 使用FileMarker将日志输出到指定文件
        log.info(FileMarker.of("test"), "这条日志会输出到test.log文件中");
        log.error(FileMarker.of("error"), "这条错误日志会输出到error.log文件中");
        
        // 可以和其他日志信息一起使用
        log.warn(FileMarker.of("warning"), "警告信息: {}", "这是一个警告");
    }
}
```

### 2. 创建自定义日志处理器

```java
import io.github.xiechanglei.cell.starter.spring.log.LogAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomLogAppender extends LogAppender {

    @Override
    protected void append(ILoggingEvent event) {
        // 获取日志事件的类名
        String className = getClassName(event);

        // 自定义日志处理逻辑
        // 例如：将日志发送到数据库、网络服务或其他存储
        System.out.printf("[%s] [%s] [%s] %s%n",
                event.getLevel().levelStr,
                event.getTimeStamp(),
                className,
                event.getMessage());

        // 实际应用中，您可以在这里实现：
        // - 将日志保存到数据库
        // - 将日志发送到远程服务器
        // - 对日志进行特殊格式化处理
        // - 其他自定义处理逻辑
        // 如果你需要有针对性的处理指定的日志，可以使用slf4j的Marker功能来标记日志事件，并在这里进行判断和处理。模块中已经提供了一个NamedMarker的实现，你可以直接使用
    }
}
```
## 实现原理

自定义日志处理器通过以下机制工作：

1. CellCustomLoggingRegister实现了ApplicationContextAware接口，在Spring容器初始化完成后自动注册自定义的日志处理器
2. FileMarkerLogAppender监听日志事件，检测是否存在FileMarker标记
3. 如果检测到FileMarker标记，则将日志输出到对应的标记命名的文件中
4. 同时保留原有的日志输出（如控制台、默认日志文件等）

## API 参考

- `FileMarker.of(String name)` - 创建文件标记，指定日志输出的目标文件名
- `LogAppender` - 抽象类，用于创建自定义日志处理器
- `LogAppender.getClassName(ILoggingEvent event)` - 获取日志事件的类名
- `CellCustomLoggingRegister` - 自动注册自定义日志处理器和文件标记功能
- `FileMarkerLogAppender` - 根据标记将日志输出到指定文件的处理器