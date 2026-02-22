package io.github.xiechanglei.cell.starter.web.log;

import java.util.Map;

/**
 * API 日志处理器接口。
 * <p>
 * 该接口定义了 API 请求日志的处理规范，用于统一处理带有 {@link ApiLog} 注解的接口方法的日志记录。
 * 实现该接口的类需要定义具体的日志处理逻辑，如将日志信息输出到控制台、写入文件或存储到数据库。
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * {@code @Component}
 * public class CustomApiLogHandler implements ApiLogHandler {
 *     {@code @Override}
 *     public void handle(String name, String ip, String path, Map<String, Object> params) {
 *         // 自定义日志处理逻辑
 *         log.info("接口：{}, IP: {}, 路径：{}, 参数：{}", name, ip, path, params);
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author xie
 * @date 2026/2/12
 */
public interface ApiLogHandler {
    /**
     * 处理 API 请求日志。
     * <p>
     * 该方法在带有 {@link ApiLog} 注解的接口方法执行后被调用，用于记录请求的详细信息。
     * </p>
     *
     * @param name   接口名称，由 {@link ApiLog#value()} 指定
     * @param ip     请求方的 IP 地址
     * @param path   请求的路径（URI）
     * @param params 标记需要记录的请求参数
     */
    void handle(String name, String ip, String path, Map<String, Object> params);
}
