package io.github.xiechanglei.cell.starter.web.filter;

import io.github.xiechanglei.cell.common.bean.message.GlobalParamResolver;
import io.github.xiechanglei.cell.common.bean.message.GlobalResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 类的详细说明
 *
 * @author xies
 * @date 2026/4/2
 */
@WebFilter(urlPatterns = "/*")
public class CellWebGlobalFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
        ScopedValue
                .where(GlobalResult.Result, new GlobalResult())
                .where(GlobalParamResolver.GlobalParam, req::getParameterValues)
                .run(() -> {
                    try {
                        chain.doFilter(req, res);
                    } catch (IOException | ServletException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
