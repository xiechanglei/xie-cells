package io.github.xiechanglei.cell.starter.rbac.web.resolver;

import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacUserRepo;
import io.github.xiechanglei.cell.starter.rbac.web.error.BusinessError;
import io.github.xiechanglei.cell.starter.rbac.web.provide.User;
import io.github.xiechanglei.cell.starter.rbac.web.provide.UserId;
import io.github.xiechanglei.cell.starter.web.resolver.WebResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * 解析器，用于根据 {@link User} 注解从 HTTP 请求中提取用户信息。
 *
 * <p>该组件实现了 {@link HandlerMethodArgumentResolver} 接口，用于解析带有 {@link User} 注解的参数，
 */
@Component
@RequiredArgsConstructor
@WebResolver
public class RbacUserResolver implements HandlerMethodArgumentResolver {

    private final RbacUserRepo rbacUserRepo;

    /**
     * 检查是否支持解析带有 {@link User} 注解的参数。
     *
     * @param parameter 方法参数信息。
     * @return 如果参数上有 {@link User} 注解，则返回 true；否则返回 false。
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(User.class)  || parameter.hasParameterAnnotation(UserId.class);
    }

    /**
     * 解析 {@link User} 注解参数，获取用户信息。
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory)  {
        if (parameter.hasParameterAnnotation(UserId.class)) {
            String id = webRequest.getParameter(Objects.requireNonNull(parameter.getParameterName()));
            if (!StringUtils.hasText(id)) {
                throw BusinessError.USER.USER_NOT_FOUND;
            }
            if (!rbacUserRepo.existsById(id)) {
                throw BusinessError.USER.USER_NOT_FOUND;
            }
            return id;
        } else if (parameter.hasParameterAnnotation(User.class)) {
            String id = webRequest.getParameter(Objects.requireNonNull(parameter.getParameterAnnotation(User.class)).value());
            if (!StringUtils.hasText(id)) {
                throw BusinessError.USER.USER_NOT_FOUND;
            }
            return rbacUserRepo.findById(id).orElseThrow(() -> BusinessError.USER.USER_NOT_FOUND);
        }
        return null;
    }
}
