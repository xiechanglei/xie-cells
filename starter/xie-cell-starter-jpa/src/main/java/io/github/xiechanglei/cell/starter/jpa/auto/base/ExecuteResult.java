package io.github.xiechanglei.cell.starter.jpa.auto.base;

/**
 * 资源查找结果状态类
 * <p>
 * 用于在 {@code @FindById} 切面中传递资源查找结果，让方法自行处理资源不存在的情况
 * </p>
 * <p>
 * 使用示例：
 * <pre>{@code
 * @FindById(User.class)
 * public UserDTO getUser(Long id, ExecuteResult result) {
 *     if (!result.isFound()) {
 *         // 自定义处理逻辑
 *         return new UserDTO(); // 返回默认值
 *         // 或者抛出异常
 *         // result.throwException();
 *     }
 *     // 正常处理
 *     return convertToDTO(user);
 * }
 * }</pre>
 * </p>
 *
 * @param success 是否找到资源
 * @param get     异常消息模板
 * @author xie
 * @date 2026/3/4
 */
public record ExecuteResult<T>(Boolean success, T get) {

    public <E extends Exception> ExecuteResult<T> throwIfFailed(E e) throws E {
        if (!success) {
            throw e;
        }
        return this;
    }
}
