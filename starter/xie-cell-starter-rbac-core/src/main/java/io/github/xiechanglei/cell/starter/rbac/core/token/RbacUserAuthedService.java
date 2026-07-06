package io.github.xiechanglei.cell.starter.rbac.core.token;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.promotion.UserAuthedInfo;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacUserRepo;
import io.github.xiechanglei.cell.starter.web.utils.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/7/6
 */
@Service
@RequiredArgsConstructor
public class RbacUserAuthedService {
    private static final String REQUEST_CURRENT_AUTHED_ATTR_NAME = "cellRbacCurrentAuthedUser";
    private static final String REQUEST_CURRENT_USER_ATTR_NAME = "cellRbacCurrentUser";


    private final RbacTokenService rbacTokenService;
    private final RbacUserRepo rbacUserRepo;


    /**
     * 获取当前用户关于认证体系的信息
     */
    public UserAuthedInfo loadUserAuthedInfo() {
        UserAuthedInfo authedInfo = (UserAuthedInfo) RequestHandler.getCurrentRequest().getAttribute(REQUEST_CURRENT_AUTHED_ATTR_NAME);
        if (authedInfo == null) {
            RbacTokenInfo currentTokenInfo = rbacTokenService.getCurrentTokenInfo().orElse(null);
            if (currentTokenInfo != null) {
                authedInfo = rbacUserRepo.findUserAuthedInfoById(currentTokenInfo.getUserId());
                RequestHandler.getCurrentRequest().setAttribute(REQUEST_CURRENT_AUTHED_ATTR_NAME, authedInfo);
            }
        }
        return authedInfo;
    }

    /**
     * 获取在request上下文中的当前用户，如果没有，则从数据库中进行查询,如果没有认证用户，则返回null
     */
    public RbacUser loadCurrentUser() {
        RbacUser user = (RbacUser) RequestHandler.getCurrentRequest().getAttribute(REQUEST_CURRENT_USER_ATTR_NAME);
        if (user == null) {
            Optional<RbacTokenInfo> currentTokenInfo = rbacTokenService.getCurrentTokenInfo();
            if (currentTokenInfo.isPresent()) {
                user = rbacUserRepo.findById(currentTokenInfo.get().getUserId()).orElse(null);
                RequestHandler.getCurrentRequest().setAttribute(REQUEST_CURRENT_USER_ATTR_NAME, user);
            }
        }
        return user;
    }
}
