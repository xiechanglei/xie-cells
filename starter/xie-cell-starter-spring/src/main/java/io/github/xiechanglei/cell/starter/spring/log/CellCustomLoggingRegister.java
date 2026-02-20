package io.github.xiechanglei.cell.starter.spring.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义日志处理器的注册
 *
 * 提供了两种日志扩展模式：
 * 1. 可以自己编写一个日志处理器，继承 LogAppender 类即可，然后自定义日志处理逻辑，比如输出到数据库或者网络流等其他地方
 * 2. 根据log4j的mark机制，可以将输出到文件的日志进行指定名称的切割，比如： log.info(FileMarker.of("test"), "tt");  这条日志会单独的输出到 test.log 文件中，全量的日志也会存留
 *
 * @author xie
 * @date 2025/4/2
 */
@Component
public class CellCustomLoggingRegister implements ApplicationContextAware {
    public static final String ROOT_LOGGER_NAME = "ROOT";
    public static final String FILE_APPENDER_NAME = "FILE";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // register custom appender
        Logger rootLogger = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
        Map<String, Appender> beansOfType = applicationContext.getBeansOfType(Appender.class);
        beansOfType.values().forEach(v -> {
            v.setContext(loggerContext);
            rootLogger.addAppender(v);
            v.start();
        });
        // file logger split
        Appender<ILoggingEvent> appender = rootLogger.getAppender(FILE_APPENDER_NAME);
        if (appender != null) {
            FileMarkerLogAppender fileMarkerLogAppender = new FileMarkerLogAppender(appender);
            fileMarkerLogAppender.setContext(loggerContext);
            rootLogger.addAppender(fileMarkerLogAppender);
            fileMarkerLogAppender.start();
        }
    }
}
