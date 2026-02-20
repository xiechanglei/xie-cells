package io.github.xiechanglei.cell.starter.web.advice.exception;

import io.github.xiechanglei.cell.common.bean.exception.BusinessException;
import io.github.xiechanglei.cell.common.bean.exception.ResourceNotFoundException;
import io.github.xiechanglei.cell.common.bean.exception.UnauthorizedException;
import io.github.xiechanglei.cell.common.bean.message.WebResult;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * <pre>
 * 全局异常处理器，用于捕获和处理系统中的异常，将其转换为统一的返回结果 {@link WebResult}。
 * 该功能默认启用，如果需要禁用，可以在配置文件中设置相应的属性。
 * </pre>
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(prefix = "cell.web.advice.exception", name = "enable", havingValue = "true", matchIfMissing = true)
public class CellWebExceptionAdvice {
    /**
     * 初始化方法，在类加载完成后执行，记录日志以表示全局异常处理器已经启用。
     */
    @PostConstruct
    public void init() {
        log.info("自动开启全局异常处理，关闭请设置: cell.web.advice.exception.enable=false");
    }

    /**
     * 处理业务异常。
     * 当系统抛出 {@link BusinessException} 类型的异常时，该方法将捕获并处理该异常，
     * 将异常的错误码和错误信息封装成统一的 {@link WebResult} 结构化返回结果。
     *
     * @param e 业务异常对象，包含了错误码和错误信息
     * @return 封装了业务异常信息的 {@link WebResult} 对象
     */
    @ExceptionHandler(value = BusinessException.class)
    public WebResult<?> handleException(BusinessException e) {
        return WebResult.failed(e.getMessage(), e.getCode());
    }


    /**
     * 处理方法参数校验失败的异常。
     * 当方法参数验证失败（例如 @NotBlank、@Size、@Max 等注解导致的验证失败）时，该方法将捕获并处理该异常，
     * 将字段错误信息封装成统一的 {@link WebResult} 结构化返回结果。
     *
     * @param e 参数校验异常对象，包含了验证失败的信息
     * @return 封装了字段错误信息的 {@link WebResult} 对象
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public WebResult<?> handleValidationExceptions(ConstraintViolationException e) {
        log.error("请求参数非法异常", e);
        return WebResult.failed("非法的请求参数");

    }

    /**
     * 处理未授权异常。
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(401).body(WebResult.failed(e.getMessage()));
    }

    /**
     * 处理静态资源未找到的异常。
     */
    @ExceptionHandler(value = {NoResourceFoundException.class, ResourceNotFoundException.class})
    public void handleNoResourceFoundException(HttpServletResponse response) {
        response.setStatus(404);
    }

    /**
     * 处理所有未分类的异常。
     * 该方法作为兜底处理器，处理所有未被专门处理的异常，确保系统在出现未知异常时不会崩溃。
     * 实际开发中，应尽量减少这种异常的出现，以确保系统的稳定性和可维护性。
     *
     * @param e 捕获的所有其他异常
     * @return 封装了异常信息的 {@link WebResult} 对象
     */
    @ExceptionHandler(value = Throwable.class)
    public WebResult<?> handleException(Exception e) {
        log.error("系统异常", e);
        return WebResult.failed(e.getMessage());
    }

}
