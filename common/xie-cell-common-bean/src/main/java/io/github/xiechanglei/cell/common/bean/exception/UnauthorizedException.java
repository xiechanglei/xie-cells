package io.github.xiechanglei.cell.common.bean.exception;

/**
 * 未登陆异常，表示用户未登录
 *
 * @author xie
 * @date 2025/7/10
 */
public class UnauthorizedException extends RuntimeException {
    public static final UnauthorizedException INSTANCE = new UnauthorizedException();
}
