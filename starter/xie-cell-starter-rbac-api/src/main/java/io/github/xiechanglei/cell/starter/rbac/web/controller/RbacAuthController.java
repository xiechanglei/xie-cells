package io.github.xiechanglei.cell.starter.rbac.web.controller;

import io.github.xiechanglei.cell.common.bean.message.DataFit;
import io.github.xiechanglei.cell.common.lang.string.StringHelper;
import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import io.github.xiechanglei.cell.starter.rbac.core.config.RbacBaseConfigProperties;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.promotion.UserAuthedInfo;
import io.github.xiechanglei.cell.starter.rbac.core.provide.CurrentUser;
import io.github.xiechanglei.cell.starter.rbac.core.provide.CurrentUserId;
import io.github.xiechanglei.cell.starter.rbac.core.provide.PermissionCell;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacCodeRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacRoleRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacUserRepo;
import io.github.xiechanglei.cell.starter.rbac.core.token.RbacTokenService;
import io.github.xiechanglei.cell.starter.rbac.core.token.RbacUserAuthedService;
import io.github.xiechanglei.cell.starter.rbac.web.error.BusinessError;
import io.github.xiechanglei.cell.starter.rbac.web.nest.RbacPasswordService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 登陆相关的接口
 *
 * @author xie
 * @date 2026/7/6
 */
@RequiredArgsConstructor
@RestController
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class RbacAuthController {

    private final RbacPasswordService rbacPasswordService;

    private final RbacUserRepo rbacUserRepo;

    private final RbacRoleRepo rbacRoleRepo;

    private final RbacCodeRepo rbacCodeRepo;

    private final RbacTokenService rbacTokenService;

    private final RbacBaseConfigProperties rbacBaseConfigProperties;

    private final RbacUserAuthedService rbacUserAuthedService;


    /**
     * 用户登录接口
     *
     * @param userName     用户名
     * @param userPassword 用户密码
     * @return 登录成功后返回的token字符串
     */
    @RequestMapping("/rbac/auth/login")
    public String login(String userName, String userPassword) {
        // 加密密码
        userPassword = rbacPasswordService.encode(userPassword);

        // 查询用户
        RbacUser rbacUser = rbacUserRepo.findByUserNameAndUserPassword(userName, userPassword).orElseThrow(() -> BusinessError.USER.USER_LOGIN_FAILED);

        // 如果用户被禁用，则抛出异常
        if (rbacUser.getUserStatus() == EnableStatus.DISABLED) {
            throw BusinessError.USER.USER_LOGIN_FAILED;
        }

        // 生成token
        String token = rbacTokenService.buildSerialTokenInfo(rbacUser);

        // 写入cookie
        Cookie cookie = new Cookie(rbacBaseConfigProperties.getTokenName(), token);
        cookie.setPath("/"); // 设置cookie的路径
        cookie.setMaxAge(-1); // 浏览器关闭之后失效
        cookie.setHttpOnly(true); // 设置httpOnly，防止js获取cookie
//        cookie.setSecure(true); // 设置cookie只能通过https协议传输
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(Objects.requireNonNull(attributes).getResponse()).addCookie(cookie);

        return token;
    }


    /**
     * 修改当前用户的密码
     * <p>
     * 用户需要提供旧密码以进行验证，修改密码成功后会返回新的 token。
     * </p>
     *
     * @param newPass 新密码
     * @param oldPass 旧密码，若未传入，则不进行旧密码校验
     * @param user    当前登录用户的授权信息
     * @return 修改密码成功后生成的新 token
     */
    @RequestMapping("/rbac/auth/changePass")
    @PermissionCell
    public String changeMyPass(String newPass, String oldPass, @CurrentUser RbacUser user) {
        if (StringHelper.isDifferent(user.getUserPassword(), rbacPasswordService.encode(oldPass))) {
            throw BusinessError.USER.USER_OLD_PASSWORD_ERROR;
        }
        user.setUserPassword(rbacPasswordService.encode(newPass));
        user.updateSerial();  // 修改密码时更新序列号
        rbacUserRepo.save(user);
        return rbacTokenService.buildSerialTokenInfo(user);
    }


    /**
     * 获取当前用户的信息
     * <p>
     * 返回当前用户的信息，用户的角色
     * </p>
     */
    @RequestMapping("/rbac/auth/detail")
    @PermissionCell
    public DataFit getCurrentUserInfo(@CurrentUser RbacUser user) {
        return DataFit.of("user", user).fit("roles", rbacRoleRepo.findRoleByUserId(user.getId()));
    }

    /**
     * 获取当前用户的权限信息，可用以控制前端相关交互功能的显示与隐藏
     */
    @RequestMapping("/rbac/auth/permission")
    @PermissionCell
    public DataFit permission(@CurrentUserId String userId) {
        boolean admin = false;
        List<String> userPermissionCode = null;
        UserAuthedInfo userAuthedInfo = rbacUserAuthedService.loadUserAuthedInfo();
        if (userAuthedInfo != null) {
            admin = userAuthedInfo.isAdmin();
            if (!admin) { // 如果不是admin,获取用户的权限码列表
                userPermissionCode = rbacCodeRepo.findUserPermissionCode(userId);
            }
        }
        return DataFit.of("admin", admin).fit("permissionCode", userPermissionCode);
    }


    /**
     * 更新当前用户信息
     *
     * @param newUser 包含新信息的用户对象
     */
    @PermissionCell
    @RequestMapping("/rbac/auth/update")
    public void updateCurrentUserInfo(@CurrentUser RbacUser user, RbacUser newUser) {
        user.setNickName(newUser.getNickName());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setEmail(newUser.getEmail());
        user.setAddress(newUser.getAddress());
        rbacUserRepo.save(user);
    }


    /**
     * 获取当前用户用户的授权码
     *
     * @param user 用户
     */
    @RequestMapping("/rbac/auth/feature/get")
    @PermissionCell
    public String getFeature(@CurrentUser RbacUser user) {
        rbacTokenService.buildFeatureTokenInfo(user);
        if (!StringUtils.hasText(user.getFeature())) {
            return resetFeature(user);
        }
        return rbacTokenService.buildFeatureTokenInfo(user);
    }


    /**
     * 重新生成当前用户的授权码
     *
     * @param user 用户
     */
    @RequestMapping("/rbac/auth/feature/reset")
    @PermissionCell
    public String resetFeature(@CurrentUser RbacUser user) {
        String feature = UUID.randomUUID().toString().replace("-", "");
        user.setFeature(feature);
        rbacUserRepo.save(user);
        return rbacTokenService.buildFeatureTokenInfo(user);
    }
}
