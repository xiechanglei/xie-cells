package io.github.xiechanglei.cell.starter.spring.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 指定文件输出日志
 *
 * @author xie
 * @date 2025/4/2
 */
public class FileMarkerLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private final Appender<ILoggingEvent> template;
    private final HashMap<String, Appender<ILoggingEvent>> appenderMap;

    private static final Field totalSizeCapField;
    private static final Field maxFileSizeField;

    static {
        try {
            maxFileSizeField = SizeAndTimeBasedRollingPolicy.class.getDeclaredField("maxFileSize");
            maxFileSizeField.setAccessible(true);
            totalSizeCapField = TimeBasedRollingPolicy.class.getDeclaredField("totalSizeCap");
            totalSizeCapField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public FileMarkerLogAppender(Appender<ILoggingEvent> template) {
        this.template = template;
        this.appenderMap = new HashMap<>();
//        this.appenderMap = new LRULinkedHashMap<>(lruSize);
    }

    @Override
    protected void append(ILoggingEvent event) {
        List<Marker> markerList = event.getMarkerList();
        markerList.stream().map(FileMarker::convert).filter(Objects::nonNull).forEach(s -> this.appendToFile(s, event));
    }

    /**
     * 输出到指定文件
     *
     * @param fileName 文件名
     * @param event    日志事件
     */
    private void appendToFile(String fileName, ILoggingEvent event) {
        Appender<ILoggingEvent> fileAppender = getFileAppender(fileName);
        if (fileAppender == null) {
            return;
        }
        fileAppender.doAppend(event);
    }

    /**
     * 获取指定文件的Appender
     *
     * @param fileName 文件名
     */
    private Appender<ILoggingEvent> getFileAppender(String fileName) {
        if (template == null) {
            return null;
        }
        return appenderMap.computeIfAbsent(fileName, this::createAppender);
    }

    /**
     * 参照模板进行Appender创建
     *
     * @param fileName 文件名
     */
    private Appender<ILoggingEvent> createAppender(String fileName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (template instanceof RollingFileAppender<ILoggingEvent> roll) {
            RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
            String file = roll.getFile();
            File oldFile = new File(file);
            String newFileName = oldFile.getParent() + File.separator + fileName + File.separator + "spring.log";
            appender.setFile(newFileName);
            appender.setContext(loggerContext);
            appender.setEncoder(roll.getEncoder());
            SizeAndTimeBasedRollingPolicy<ILoggingEvent> policy = new SizeAndTimeBasedRollingPolicy<>();
            RollingPolicy rollingPolicy = roll.getRollingPolicy();
            if (rollingPolicy instanceof SizeAndTimeBasedRollingPolicy<?> basedRollingPolicy) {
                policy.setContext(roll.getContext());
                try {
                    policy.setMaxFileSize((FileSize) maxFileSizeField.get(basedRollingPolicy));
                    policy.setTotalSizeCap((FileSize) totalSizeCapField.get(basedRollingPolicy));
                } catch (IllegalAccessException e) {
                    policy.setMaxFileSize(FileSize.valueOf("10MB"));
                }
                policy.setFileNamePattern(basedRollingPolicy.getFileNamePattern().replace(file, newFileName));
                policy.setMaxHistory(basedRollingPolicy.getMaxHistory());
                policy.setParent(appender);
            }
            appender.setRollingPolicy(policy);
            policy.start();
            appender.start();
            return appender;
        } else if (template instanceof FileAppender<ILoggingEvent> fileAppender) {
            String file = fileAppender.getFile();
            File oldFile = new File(file);
            String newFileName = oldFile.getParent() + File.separator + fileName + ".log";
            FileAppender<ILoggingEvent> appender = new FileAppender<>();
            appender.setFile(newFileName);
            appender.setContext(loggerContext);
            appender.setEncoder(fileAppender.getEncoder());
            appender.start();
            return appender;
        }
        return null;
    }
}
