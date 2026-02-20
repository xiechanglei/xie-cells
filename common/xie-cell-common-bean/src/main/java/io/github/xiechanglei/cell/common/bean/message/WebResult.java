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
     * <pre>
     * 返回码,区别与http请求中的状态码，通常情况下，0表示成功，其他表示失败，具体的返回码定义在具体的项目中
     * 所以我们可以根据code来判断请求是否成功，并且当请求失败的时候，根据code来判断失败的原因，
     * 提交给第三方对接的时候，可以同时提供一份错误码的文档，方便对接方查阅并且调试
     * 为方便定义，这里建议使用以下规则：
     * 1.请求码0 表示成功
     * 2.错误码可以使用xxxyyyy的形式，其中xxx表示模块，yyyy表示错误码，例如：1000001 表示用户模块100的错误码1
     * 3.错误码应该尽量的保持唯一且详细，方便定位问题
     * 4.错误码的长度应该尽量的保持一致，方便查看
     * 5.错误码-1表示未知错误，通常由于业务代码设计缺陷导致，应该尽量避免
     * </pre>
     */
//    private int code;

    /**
     * <pre>
     * success 表示请求是否成功，true表示成功，false表示失败
     * 一般情况下，按照通俗的设计，上述的code已经可以表示请求的结果的状态，
     * 但是可能在一些情况下，我们需要一个更加直观的状态，所以这里增加了一个success字段，
     * 比如在一些简单的项目中, 我们不愿意去定义复杂的错误码，只是简单的成功和失败，这个时候success就可以派上用场
     * </pre>
     */
//    private boolean success;

    /**
     * <pre>
     * 返回信息，通常情况下，当请求失败的时候，我们会返回一些错误信息，方便开发人员查看错误信息，
     * 同样与success的设计类似，如果我们设计了完备的code体系，那么是可以不需要msg的，
     * 具体如何使用，可以根据具体的项目需求来定义
     * </pre>
     */
//    private String msg;

    /**
     * 返回数据，通常情况下，我们的请求都会返回一些数据，这个数据可以是一个对象，也可以是一个集合，
     * 甚至是一个map，具体的数据类型可以根据具体的项目需求来定义
     */
//    private T data;

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
