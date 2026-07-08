package io.github.xiechanglei.cell.common.bean.message;

/**
 * <pre>
 * web常用返回的结构封装，包含返回码，返回信息，返回数据等，
 * 大部分的项目中都会有这样的一个结构，所以这里定义一个基础的结构，包含了：
 * 1.返回码
 * 2.返回信息
 * 3.返回数据
 * </pre>
 */
public record WebResult<T>(int code, boolean success, String msg, T data) {

    // 成功的消息编码
    public static final int DEFAULT_SUCCESS_CODE = 0;
    // 失败的消息编码
    public static final int DEFAULT_FAILED_CODE = -1;

    /**
     * 一个快速的构建成功消息的方法，当我们请求成功的时候，我们可以调用这个方法来快速的构建一个成功的返回消息
     * 默认成功的code为0,如果需要自定义code,请使用{@link #success(Object, int)}
     *
     * @param data 返回的数据
     * @param <T>  返回的数据类型
     * @return 返回一个成功的消息
     */
    public static <T> WebResult<T> success(T data) {
        return success(data, DEFAULT_SUCCESS_CODE);
    }

    /**
     * 一个快速的构建成功消息的方法，当我们请求成功的时候，我们可以调用这个方法来快速的构建一个成功的返回消息
     *
     * @param data 返回的数据
     * @param code 成功的code
     * @param <T>  返回的数据类型
     * @return 返回一个成功的消息
     */
    public static <T> WebResult<T> success(T data, int code) {
        return new WebResult<>(code, true, null, data);
    }


    /**
     * 一个快速的构建失败消息的方法，当我们请求失败的时候，我们可以调用这个方法来快速的构建一个失败的返回消息
     *
     * @param code    失败的错误码
     * @param message 失败的消息提示
     * @return 返回一个失败的消息
     */
    public static WebResult<?> failed(String message, int code) {
        return new WebResult<>(code, false, message, null);
    }

    /**
     * 快速构建失败消息，code 默认为-1，表示未知错误，通常由于业务代码设计缺陷导致，应该尽量避免。
     *
     * @param message 失败的消息提示
     * @return 返回一个失败的消息
     */
    public static WebResult<?> failed(String message) {
        return failed(message, DEFAULT_FAILED_CODE);
    }

    /**
     * 通过CodeMessage对象来构建失败消息
     */
    public static WebResult<?> failed(CodeMessage codeMessage) {
        return failed(codeMessage.message(), codeMessage.code());
    }

}
