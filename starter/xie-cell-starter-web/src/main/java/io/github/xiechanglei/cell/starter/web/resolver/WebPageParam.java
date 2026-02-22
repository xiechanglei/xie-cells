package io.github.xiechanglei.cell.starter.web.resolver;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * web分页参数封装类，用于接收前端传递的分页参数
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public class WebPageParam {
    // 默认的分页参数 ,从1 开始
    public static final int defaultPage = 1;
    // 默认的每页显示的记录数
    public static final int defaultSize = 20;

    /**
     * 当前页码,请求不传递该参数的时候，默认使用1,表示第一页，如果小于1应该抛出一个异常
     */
    private Integer pageNo;

    /**
     * 每页显示的记录数，请求不传递该参数的时候，默认使用20,表示每页显示20条记录，如果小于0应该抛出一个异常
     */
    private Integer pageSize;

    /**
     * <pre>
     * 排序字段,当你需要使用这个属性的时候，默认你是需要spring-data-jpa的支持的，
     * 如果你只是单纯的使用这个属性,而并不是使用spring-data-jpa的时候，如只是测试等等，那么可以增加以下依赖，消除报错：
     * <dependency>
     *     <groupId>org.springframework.data</groupId>
     *     <artifactId>spring-data-commons</artifactId>
     * </dependency>
     * </pre>
     */
    private Sort sort = null;

    /**
     * 构建成PageRequest对象
     */
    public PageRequest toPageRequest() {
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        if (sort != null) {
            pageRequest = pageRequest.withSort(sort);
        }
        return pageRequest;
    }
}
