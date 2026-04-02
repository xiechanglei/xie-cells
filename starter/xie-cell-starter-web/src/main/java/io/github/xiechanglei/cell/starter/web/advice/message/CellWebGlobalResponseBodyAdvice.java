package io.github.xiechanglei.cell.starter.web.advice.message;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xiechanglei.cell.common.bean.message.GlobalResult;
import io.github.xiechanglei.cell.common.bean.message.WebResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Objects;

/**
 * <p>
 * 全局响应体处理器，用于统一封装接口的返回结果。
 * 在前后端分离的开发模式中，后端返回的结果一般为JSON格式，
 * 为了简化前端处理逻辑，本类提供了全局的返回结果封装机制。
 * 通过该机制，所有接口的返回结果都会被封装成 {@link WebResult} 格式，
 * 使得前端可以统一处理成功和失败的提示。
 * </p>
 * <p>
 * 如果接口方法中标注了 {@link DisableResponseAdvice} 注解，该接口的返回结果将不会进行封装。
 * 本类还需要与全局异常处理器联合使用，以处理异常情况下的响应。
 * </p>
 */
@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cell.web.advice.response", name = "enable", havingValue = "true", matchIfMissing = true)
public class CellWebGlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 在类加载完成后，初始化日志记录，标记全局返回结果封装功能已启用。
     */
    @PostConstruct
    public void init() {
        log.info("自动开启全局返回结果封装，关闭请设置: cell.web.advice.response.enable=false");
    }

    private final ObjectMapper objectMapper;

    /**
     * 判断是否支持封装返回结果。
     *
     * @param returnType 方法的返回类型
     * @param aClass     当前使用的消息转换器类型
     * @return 是否支持封装返回结果
     */
    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        Class<?> rt = Objects.requireNonNull(returnType.getMethod()).getReturnType();
        // 返回值是SseEmitter的不进行封装，SseEmitter是Spring框架中用于处理服务器发送事件（Server-Sent Events）的一个类，它允许服务器向客户端推送实时更新的数据流，因此不适合进行统一的返回结果
        if (SseEmitter.class.isAssignableFrom(rt)) {
            return false;
        }

        // 返回值是Flux的不进行封装，Flux是响应式编程中的一个核心类型，表示一个异步的数据流，
        if (Flux.class.isAssignableFrom(rt)) {
            return false;
        }
        // 返回值是ResponseEntity的不进行封装
        if (ResponseEntity.class.isAssignableFrom(rt)) {
            return false;
        }

        // 返回值是WebResult的不进行封装
        if (WebResult.class.isAssignableFrom(rt)) {
            return false;
        }

        // 类上有DisableResponseAdvice的不进行封装
        if (returnType.getDeclaringClass().getAnnotation(DisableResponseAdvice.class) != null) {
            return false;
        }

        // 方法上有DisableResponseAdvice的不进行封装
        if (Objects.requireNonNull(returnType.getMethod()).getAnnotation(DisableResponseAdvice.class) != null) {
            return false;
        }
        return true;
    }

    /**
     * 在响应体写入之前，对返回结果进行封装。
     * 如果返回结果已经是 {@link WebResult} 类型，则不做额外处理。
     * 否则，封装为 {@link WebResult} 的成功模式。
     *
     * @param body                  方法返回的原始结果
     * @param returnType            方法的返回类型
     * @param selectedContentType   选择的媒体类型
     * @param selectedConverterType 选择的消息转换器类型
     * @param request               当前的请求
     * @param response              当前的响应
     * @return 封装后的返回结果
     */
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        return processWebResult(body, response);
    }

    public Object processWebResult(Object body, ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        WebResult<?> result;
        if (body instanceof WebResult) {
            result = (WebResult<?>) body;
        } else {
            GlobalResult globalResult = GlobalResult.Result.get();
            if (globalResult.isBound()) {
                result = WebResult.success(globalResult.getResult());
            } else {
                result = WebResult.success(body);
            }
        }

        //  String 类型的body不进行封装,spring boot 框架的问题，
        if (body instanceof String) {
            try {
                response.getBody().write(objectMapper.writeValueAsString(result).getBytes());
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}