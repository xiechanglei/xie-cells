package io.github.xiechanglei.cell.starter.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 自定义时间参数解析器。
 * <p>
 * 该解析器用于将请求中的时间参数转换为 {@link Date} 类型，以简化控制器中的参数处理逻辑。
 * 支持的时间格式包括：
 * 1. 完整日期时间格式: yyyy-MM-dd HH:mm:ss
 * 2. 仅日期格式: yyyy-MM-dd
 * 3. 时间戳
 * </p>
 */
@WebResolver
@Component
public class CellWebDateResolver implements HandlerMethodArgumentResolver {
    /**
     * 日期时间格式化器，用于解析 "yyyy-MM-dd HH:mm:ss" 格式的时间字符串
     */
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 日期格式化器，用于解析 "yyyy-MM-dd" 格式的日期字符串
     */
    private static final DateTimeFormatter shortDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * 时区设置为 GMT+8
     */
    private static final ZoneId zoneId = ZoneId.of("GMT+8");

    /**
     * 判断当前解析器是否支持处理指定的参数类型。
     * <p>
     * 该方法检查参数类型是否为 {@link Date}，如果是，则返回 true，表示该解析器支持处理该类型的参数。
     * </p>
     *
     * @param parameter 要检查的参数
     * @return 如果支持处理 {@link Date} 类型的参数，则返回 true；否则返回 false
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Date.class);
    }

    /**
     * 解析方法参数并从请求中获取对应的参数值。
     * <p>
     * 从请求中获取时间参数字符串，根据其格式（日期时间格式、日期格式或时间戳）将其解析为 {@link Date} 对象。
     * 如果参数字符串为空，则返回 null。
     * </p>
     *
     * @param parameter     当前方法的参数
     * @param mavContainer  当前的模型和视图容器
     * @param webRequest    当前的原生 web 请求
     * @param binderFactory 数据绑定工厂
     * @return 解析后的 {@link Date} 对象，如果时间字符串为空则返回 null
     * @throws NumberFormatException 如果时间戳格式不正确
     */
    @Override
    public Date resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String paramName = parameter.getParameterName();
        assert paramName != null;
        String dateStr = webRequest.getParameter(paramName);
        // If the dateStr is empty, return null
        return convert(dateStr);
    }

    public Date convert(String dateStr) {
        if (!StringUtils.hasText(dateStr)) {
            return null;
        }
        if (dateStr.contains("-")) { // If the dateStr contains "-", it is a date format string

            if (dateStr.length() > 10) { // use LocalDateTime to parse the dateStr
                return Date.from(LocalDateTime.parse(dateStr, dateFormat).atZone(zoneId).toInstant());
            } else { // use LocalDate to parse the dateStr
                return Date.from(LocalDate.parse(dateStr, shortDateFormat).atStartOfDay(zoneId).toInstant());
            }

        } else { // If the dateStr is a timestamp
            return new Date(Long.parseLong(dateStr));
        }
    }
}

