package io.github.xiechanglei.cell.starter.rbac.core.aop;

import io.github.xiechanglei.cell.common.bean.exception.NoPermissionException;
import io.github.xiechanglei.cell.common.bean.exception.UnauthorizedException;
import io.github.xiechanglei.cell.common.lang.string.StringHelper;
import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacCode;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacLog;
import io.github.xiechanglei.cell.starter.rbac.core.promotion.UserAuthedInfo;
import io.github.xiechanglei.cell.starter.rbac.core.provide.ApiPermission;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacCodeRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacLogRepo;
import io.github.xiechanglei.cell.starter.rbac.core.token.CellRbacUserAuthedService;
import io.github.xiechanglei.cell.starter.rbac.core.token.RbacTokenService;
import io.github.xiechanglei.cell.starter.web.utils.RequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * 拦截器，拦截带有@RbacPermission 注解的方法，对当前登陆的用户进行权限校验，
 * 如果用户没有登陆，则返回未401状态码
 * 如果用户已登陆但没有权限，则返回403状态码
 *
 * @author xie
 * @date 2026/7/1
 */
@Order(0)
@Aspect
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "filter-auth", havingValue = "true", matchIfMissing = true)
@Log4j2
public class CellRbacPermissionAop {

    private final RbacTokenService rbacTokenService;

    private final RbacCodeRepo rbacCodeRepo;

    private final RbacLogRepo rbacLogRepo;

    private final CellRbacUserAuthedService cellRbacUserAuthedService;

    /**
     * 首先在request中获取当前用户，然后查询用户是否拥有当前请求所需要的权限码
     */
    @Before("@annotation(io.github.xiechanglei.cell.starter.rbac.core.provide.ApiPermission)")
    public void before(JoinPoint joinPoint) {
        // 获取当前登陆的用户信息，如果没有登陆，则抛出未登陆
        RbacTokenInfo tokenInfo = rbacTokenService.getCurrentTokenInfo().orElseThrow(() -> UnauthorizedException.INSTANCE);

        // 获取当前登陆用户的认证信息并进行信息校验 do sql query
        UserAuthedInfo userAuthedInfo = checkUserAuthedInfo(cellRbacUserAuthedService.loadUserAuthedInfo(), tokenInfo);

        // 获取当前请求所需要的权限码
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ApiPermission apiPermission = signature.getMethod().getAnnotation(ApiPermission.class);

        if (StringHelper.isNotBlank(apiPermission.code()) && !userAuthedInfo.isAdmin()) {
            //查询当前用户是否拥有当前请求所需要的权限码，如果没有，则抛出未授权异常 do sql query
            Optional<RbacCode> rbacCodeOp = rbacCodeRepo.findByUserIdAndCode(tokenInfo.getUserId(), apiPermission.code());
            rbacCodeOp.orElseThrow(() -> NoPermissionException.INSTANCE);
        }

        // 判断是否需要记录日志
        if (apiPermission.log()) {
            // 记录日志
            doLog(apiPermission, tokenInfo);
        }

    }


    /**
     * 校验用户认证信息
     */
    private UserAuthedInfo checkUserAuthedInfo(UserAuthedInfo userAuthedInfo, RbacTokenInfo tokenInfo) {
        if (userAuthedInfo == null) {
            // 用户不存在
            throw UnauthorizedException.INSTANCE;
        }
        // 校验用户状态
        if (userAuthedInfo.getEnableStatus() != EnableStatus.ENABLED) {
            throw UnauthorizedException.INSTANCE;
        }

        // 特征值模式，不会检验serialNumber，直接成功
        if (tokenInfo.getFeature() != null && tokenInfo.getFeature().equals(userAuthedInfo.getFeature())) {
            return userAuthedInfo;
        }

        // 校验serial
        if (!Objects.equals(tokenInfo.getSerialNumber(), userAuthedInfo.getUserSerial())) {
            throw UnauthorizedException.INSTANCE;
        }


        return userAuthedInfo;
    }

    /**
     * 记录日志
     */
    private void doLog(ApiPermission apiPermission, RbacTokenInfo tokenInfo) {
        HttpServletRequest request = RequestHandler.getCurrentRequest();
        String currentRequestIp = RequestHandler.getCurrentRequestIp();
        RbacLog rbacLog = new RbacLog();
        rbacLog.setUserId(tokenInfo.getUserId());
        rbacLog.setLogTitle(apiPermission.name());
        rbacLog.setLogPath(request.getRequestURI());
        rbacLog.setLogAddress(currentRequestIp);
        rbacLogRepo.save(rbacLog);
    }
}
