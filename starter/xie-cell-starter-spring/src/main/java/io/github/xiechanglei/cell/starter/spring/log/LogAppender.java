package io.github.xiechanglei.cell.starter.spring.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * 通用的日志处理器
 *
 * @author xie
 * @date 2025/4/2
 */
public abstract class LogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    /**
     * 获取日志事件的类名
     *
     * @param event 日志事件
     * @return 日志事件的类名
     */
    protected String getClassName(ILoggingEvent event) {
        String className = "";
        StackTraceElement[] callerData = event.getCallerData();
        if (callerData != null && callerData.length > 0) {
            className = callerData[0].getClassName();
        }
        return className;
    }
}
