package io.github.xiechanglei.cell.starter.web.log;


import io.github.xiechanglei.cell.starter.web.utils.RequestHandler;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面类，用于处理带有自定义 @ApiLog 注解的方法的日志记录。
 * 通过面向切面编程 (AOP) 拦截带有 @ApiLog 注解的方法，
 * 并记录相关信息，如 API 名称、请求路径、IP 地址和请求参数。
 */
@Aspect
@Log4j2
@Component
@ConditionalOnProperty(prefix = "cell.web.log", name = "enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(ApiLogHandler.class)
public class CellApiLogAop implements ApplicationContextAware {
    /**
     * 初始化的时候找到日志处理器，1.找不到就不再去管理日志 2.提供一个默认的日志处理器
     */
    private Collection<ApiLogHandler> logHandlers;

    @PostConstruct
    public void init() {
        log.info("自动开始日志记录，关闭请设置:cell.web.log.enable=false");
    }

    /**
     * 设置应用程序上下文并初始化日志处理器。
     * 由 Spring 容器调用以注入 ApplicationContext。
     *
     * @param applicationContext 应用程序上下文，用于获取 bean
     * @throws BeansException 如果在获取 bean 过程中发生异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有实现了LanApiLogHandler接口的bean
        this.logHandlers = applicationContext.getBeansOfType(ApiLogHandler.class).values();
    }

    /**
     * @param joinPoint 提供对正在调用的方法的反射访问
     *                  `@ApiLog(value = "接口名称", params = {"userId", "roleId"})`
     *                  在 @ApiLog 注解的方法执行前执行此方法。
     *                  收集请求信息并将其转发给日志处理器进行处理。
     */
    @After("@annotation(io.github.xiechanglei.cell.starter.web.log.ApiLog)")
    public void apiLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ApiLog apiLog = signature.getMethod().getAnnotation(ApiLog.class);
        HttpServletRequest request = RequestHandler.getCurrentRequest();
        // 获取注解上的值
        String apiName = apiLog.value();
        // 请求路径
        String path = request.getRequestURI();
        // 请求地址
        String currentRequestIp = RequestHandler.getCurrentRequestIp();
        // 获取参数
        Map<String, Object> paramsMap = new HashMap<>();
        for (String param : apiLog.params()) {
            paramsMap.put(param, request.getParameter(param));
        }
        this.logHandlers.forEach(handler -> handler.handle(apiName, currentRequestIp, path, paramsMap));
    }
}
