package io.github.xiechanglei.cell.common.bean.message;

/**
 * code 与消息的定义，提供了code和message两个属性:
 * public class Errors{
 * public static class User{
 * public static final CodeMessage USER_NOT_FOUND = ErrorDefinition.of(1000001, "用户不存在");
 * }
 * }
 * <p>
 * 使用方式：
 * Errors.User.USER_NOT_FOUND;
 */
public record CodeMessage(int code, String message) {
    public static CodeMessage of(int code, String message) {
        return new CodeMessage(code, message);
    }
}
