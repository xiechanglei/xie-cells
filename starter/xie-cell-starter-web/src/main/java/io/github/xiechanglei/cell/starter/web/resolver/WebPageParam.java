package io.github.xiechanglei.cell.starter.web.resolver;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Web 分页参数封装类。
 * <p>
 * 该类用于封装前端传递的分页查询参数，包括页码、每页大小和排序信息。
 * 与 {@link CellWebPageResolver} 配合使用，可自动解析请求参数并构建 {@link PageRequest} 对象，
 * 简化 Controller 中的分页参数处理逻辑。
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * {@code @GetMapping}("/list")
 * public Result list(PageRequest pageRequest) {
 *     // pageRequest 已自动解析，包含页码、每页大小和排序信息
 *     return Result.success(service.findAll(pageRequest));
 * }
 * </pre>
 * </p>
 *
 * @author xie
 * @date 2024/12/26
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class WebPageParam {
    /**
     * 默认页码，从 1 开始
     */
    public static final int defaultPage = 1;

    /**
     * 默认每页显示的记录数
     */
    public static final int defaultSize = 20;

    /**
     * 当前页码。
     * <p>
     * 请求不传递该参数时，默认使用 1（表示第一页）。
     * 如果小于 1 会抛出 {@link IllegalArgumentException} 异常。
     * </p>
     */
    private Integer pageNo;

    /**
     * 每页显示的记录数。
     * <p>
     * 请求不传递该参数时，默认使用 20（表示每页显示 20 条记录）。
     * 如果小于 1 会抛出 {@link IllegalArgumentException} 异常。
     * </p>
     */
    private Integer pageSize;

    /**
     * 排序信息。
     * <p>
     * 用于指定查询结果的排序规则，通常与 Spring Data JPA 配合使用。
     * 参数格式：字段名：排序方式，多个字段用逗号分隔。
     * 例如：sort=id:+,name:- 或 sort=id:asc,name:desc
     * </p>
     * <p>
     * 如果不使用 Spring Data JPA，可添加以下依赖：
     * <pre>
     * &lt;dependency&gt;
     *     &lt;groupId&gt;org.springframework.data&lt;/groupId&gt;
     *     &lt;artifactId&gt;spring-data-commons&lt;/artifactId&gt;
     * &lt;/dependency&gt;
     * </pre>
     * </p>
     */
    private Sort sort = null;

    /**
     * 构建 {@link PageRequest} 对象。
     * <p>
     * 将当前分页参数转换为 Spring Data 的 {@link PageRequest} 对象，
     * 用于 Repository 层的分页查询。
     * </p>
     *
     * @return {@link PageRequest} 对象，包含页码、每页大小和排序信息
     */
    public PageRequest toPageRequest() {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        if (sort != null) {
            pageRequest = pageRequest.withSort(sort);
        }
        return pageRequest;
    }
}
