package io.github.xiechanglei.cell.starter.rbac.core.filter;

import io.github.xiechanglei.cell.common.lang.string.StringOptional;
import io.github.xiechanglei.cell.starter.rbac.core.config.RbacBaseConfigProperties;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfo;
import io.github.xiechanglei.cell.starter.rbac.core.token.RbacTokenService;
import io.github.xiechanglei.cell.starter.web.utils.RequestHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * 从请求的参数中获取token字符串，并解析成token对象，放在request的attribute中，供后续的aop等使用
 * <p>
 * 获取token参数的优先级： 请求参数 > Cookie > Header
 *
 * @author xie
 * @date 2026/7/1
 */
@RequiredArgsConstructor
@WebFilter(value = "/*", asyncSupported = true)
public class RbacTokenFilter extends HttpFilter {
    private final RbacBaseConfigProperties rbacBaseConfigProperties;

    private final RbacTokenService tokenService;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        RbacTokenInfo tokenInfo = tokenService.decode(getTokenStrFromRequest(req));
        tokenService.setCurrentTokenInfo(tokenInfo);
        super.doFilter(req, res, chain);
    }

    /**
     * 从请求中提取Token。
     * <p>
     * 该方法首先尝试从请求参数中获取Token，如果没有则从Cookie中获取，
     * 最后从请求头中获取。使用优先级顺序来提取Token。
     * </p>
     *
     * @param request 当前的HTTP请求
     * @return 提取到的Token字符串
     */
    public String getTokenStrFromRequest(HttpServletRequest request) {
        String REQUEST_KEY = rbacBaseConfigProperties.getTokenName();//默认为auth-token
        return StringOptional.of(request.getParameter(REQUEST_KEY))
                .orWith(() -> request.getHeader(REQUEST_KEY))
                .orWith(() -> RequestHandler.getCookie(request, REQUEST_KEY)).get();
    }
}
