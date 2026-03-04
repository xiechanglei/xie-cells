package io.github.xiechanglei.cell.common.bean.exception;

import io.github.xiechanglei.cell.common.bean.message.CodeMessage;
import lombok.Getter;

/**
 * <pre>
 * 业务异常封装，提供了code和message两个属性:
 * 1. code 代表异常码
 * 2. message 代表异常信息
 * </pre>
 * 主要配合WebResult以及全局异常处理使用，方便统一处理异常，业务系统代码中建议使用一个全局字典类来管理：如：
 * <pre>
 *     // 全局异常码字典类
 *     public class BusinessError {
 *         // 用户相关异常
 *         public static class USER {
 *             public static final BusinessException USER_NOT_FOUND = BusinessException.of(1000001, "用户不存在");
 *             public static final BusinessException USER_PASSWORD_ERROR = BusinessException.of(1000002, "用户密码错误");
 *             public static final BusinessException USER_LOCKED = BusinessException.of(1000003, "用户被锁定");
 *             public static final BusinessException USER_EXPIRED = BusinessException.of(1000004, "用户已过期");
 *         }
 *
 *          // 订单相关异常
 *         public static class ORDER {
 *              public static final BusinessException ORDER_NOT_FOUND = BusinessException.of(2000001, "订单不存在");
 *              public static final BusinessException ORDER_STATUS_ERROR = BusinessException.of(2000002, "订单状态错误");
 *              public static final BusinessException ORDER_AMOUNT_ERROR = BusinessException.of(2000003, "订单金额错误");
 *              public static final BusinessException ORDER_PAY_ERROR = BusinessException.of(2000004, "订单支付错误");
 *         }
 *     }
 * </pre>
 * 使用方式：
 * <pre>
 *     public void test(){
 *          throw BusinessError.USER.USER_NOT_FOUND;
 *     }
 * </pre>
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 静态工厂方法，方便创建BusinessException对象，避免new
     *
     * @param code    异常码
     * @param message 异常信息
     * @return BusinessException对象
     */
    public static BusinessException of(int code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 静态工厂方法，方便创建BusinessException对象，避免new
     *
     * @param message 异常信息
     * @return BusinessException对象
     */
    public static BusinessException of(String message) {
        return new BusinessException(-1, message);
    }

    /**
     * 静态工厂方法，通过CodeMessage对象创建BusinessException
     *
     * @param codeMessage CodeMessage对象，包含异常码和消息
     * @return BusinessException对象
     */
    public static BusinessException of(CodeMessage codeMessage) {
        return new BusinessException(codeMessage.code(), codeMessage.message());
    }
}
