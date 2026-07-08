package io.github.xiechanglei.cell.starter.rbac.core.resolver;

import io.github.xiechanglei.cell.common.bean.exception.UnauthorizedException;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.provide.CurrentUser;
import io.github.xiechanglei.cell.starter.rbac.core.provide.CurrentUserId;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfo;
import io.github.xiechanglei.cell.starter.rbac.core.token.RbacUserAuthedService;
import io.github.xiechanglei.cell.starter.rbac.core.token.RbacTokenService;
import io.github.xiechanglei.cell.starter.web.resolver.WebResolver;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/7/6
 */
@WebResolver
@RequiredArgsConstructor
public class RbacCurrentUserResolver implements HandlerMethodArgumentResolver {

    private final RbacTokenService rbacTokenService;
    private final RbacUserAuthedService rbacUserAuthedService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) || parameter.hasParameterAnnotation(CurrentUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @Nonnull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        CurrentUser currentUser = parameter.getParameterAnnotation(CurrentUser.class);
        if (currentUser != null) {
            RbacUser user = rbacUserAuthedService.loadCurrentUser();
            if (currentUser.required() && user == null) {
                throw UnauthorizedException.INSTANCE;
            }
            return user;
        }
        CurrentUserId currentUserId = parameter.getParameterAnnotation(CurrentUserId.class);
        if (currentUserId != null) {
            Optional<RbacTokenInfo> currentTokenInfo = rbacTokenService.getCurrentTokenInfo();
            if (currentUserId.required() && currentTokenInfo.isEmpty()) {
                throw UnauthorizedException.INSTANCE;
            }
            if (currentTokenInfo.isPresent()) {
                return currentTokenInfo.get().getUserId();
            }
        }
        return null;
    }
}
