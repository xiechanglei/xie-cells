package io.github.xiechanglei.cell.starter.rbac.web.controller;

import io.github.xiechanglei.cell.common.lang.string.StringOptional;
import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRole;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRoleUser;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.provide.Permission;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacUserCustomStrategy;
import io.github.xiechanglei.cell.starter.rbac.core.repo.*;
import io.github.xiechanglei.cell.starter.rbac.core.token.RbacTokenService;
import io.github.xiechanglei.cell.starter.rbac.web.error.BusinessError;
import io.github.xiechanglei.cell.starter.rbac.web.nest.RbacPasswordService;
import io.github.xiechanglei.cell.starter.rbac.web.provide.User;
import io.github.xiechanglei.cell.starter.rbac.web.provide.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 角色相关的接口控制器
 * <p>
 * 该控制器包含了与角色相关的所有 API 接口，包括角色的查询、创建、更新、禁用、启用、删除以及权限管理等功能。
 * </p>
 */
@RestController("toolsRbacUserController")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class RbacUserController {

    private final RbacPasswordService rbacPasswordService;

    private final RbacUserRepo rbacUserRepo;

    private final RbacRoleRepo rbacRoleRepo;

    private final RbacRoleUserRepo rbacRoleUserRepo;

    private final RbacTokenService rbacTokenService;

    private final RbacLogRepo rbacLogRepo;


    private final List<RbacUserCustomStrategy> rbacUserCustomStrategies;

    @RequestMapping("/rbac/user/all")
    @Permission(code = "RBAC::USER::QUERY", name = "查询用户")
    public List<RbacUser> all() {
        return rbacUserRepo.findAll();
    }

    /**
     * 查询用户
     * <p>
     * 该方法用于根据查询关键字查找用户，并分页返回结果。
     * </p>
     *
     * @param word        查询关键字
     * @param pageRequest 分页请求信息
     * @return 用户分页结果
     */
    @RequestMapping("/rbac/user/query")
    @Permission(code = "RBAC::USER::QUERY", name = "查询用户")
    public Page<RbacUser> searchUser(String word, PageRequest pageRequest) {
        return rbacUserRepo.queryUser(word, pageRequest);
    }

    /**
     * 获取用户信息
     */
    @RequestMapping("/rbac/user/get")
    @Permission(code = "RBAC::USER::QUERY", name = "查询用户")
    public RbacUser getUserInfo(@User RbacUser user) {
        return user;
    }

    /**
     * 添加用户
     * <p>
     * 该方法用于添加新用户。用户需要提供用户名和密码，系统会进行唯一性检查，防止重复添加用户。
     * </p>
     *
     * @param user         新用户信息
     * @param userPassword 用户密码
     */
    @RequestMapping("/rbac/user/add")
    @Permission(code = "RBAC::USER::ADD", name = "添加用户")
    public void addUser(RbacUser user, String userPassword) {
        StringOptional.of(user.getUserName()).orElseThrow(BusinessError.USER.USER_NAME_IS_EMPTY);
        if (rbacUserRepo.existsByUserName(user.getUserName())) {
            throw BusinessError.USER.USER_EXISTS;
        }
        RbacUser createdUser = new RbacUser();
        createdUser.setUserName(user.getUserName());
        createdUser.setUserPassword(rbacPasswordService.encode(userPassword));
        createdUser.setNickName(user.getNickName());
        createdUser.setPhoneNumber(user.getPhoneNumber());
        createdUser.setEmail(user.getEmail());
        createdUser.setAddress(user.getAddress());
        createdUser.setUserStatus(EnableStatus.ENABLED);
        rbacUserRepo.save(createdUser);
    }

    /**
     * 更新用户信息
     * <p>
     * 该方法用于更新指定用户的详细信息。更新的内容包括除了 id、用户名、密码、状态、序列号、创建时间和更新时间之外的所有用户属性。
     * </p>
     */
    @RequestMapping("/rbac/user/update")
    @Permission(code = "RBAC::USER::EDIT", name = "编辑用户")
    public void updateUser(@User RbacUser user, RbacUser newUser) {
        user.setNickName(newUser.getNickName());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setEmail(newUser.getEmail());
        user.setAddress(newUser.getAddress());
        rbacUserRepo.save(user);
    }

    /**
     * 修改用户的密码
     * <p>
     * 该方法用于修改指定用户的密码。用户需要提供新的密码，以及当前用户的身份验证信息。
     * </p>
     */
    @RequestMapping("/rbac/user/changePass")
    @Permission(code = "RBAC::USER::EDIT", name = "编辑用户")
    public void changePass(String userPassword, String userId) {
        rbacUserRepo.updatePassword(userId, rbacPasswordService.encode(userPassword));
    }

    /**
     * 禁用用户
     * <p>
     * 该方法用于禁用指定的用户。如果用户是管理员，则无法被禁用。
     * </p>
     */
    @RequestMapping("/rbac/user/disable")
    @Permission(code = "RBAC::USER::EDIT", name = "编辑用户")
    public void disable(String userId) {
        rbacUserRepo.changeUserStatus(userId, EnableStatus.DISABLED);
    }

    /**
     * 启用用户
     * <p>
     * 该方法用于启用被禁用的用户。
     * </p>
     */
    @RequestMapping("/rbac/user/enable")
    @Permission(code = "RBAC::USER::EDIT", name = "编辑用户")
    public void enable(String userId) {
        rbacUserRepo.changeUserStatus(userId, EnableStatus.ENABLED);
    }



    /**
     * 获取用户的角色信息
     */
    @RequestMapping("/rbac/user/roles")
    @Permission(code = "RBAC::USER::QUERY", name = "查询用户")
    public List<RbacRole> getUserRoles(String userId) {
        return rbacRoleRepo.findRoleByUserId(userId);
    }




    /**
     * todo this place should be refactored, use addRoleToUser and removeRoleFromUser
     * 给用户授予角色
     * <p>
     * 该方法用于给指定用户授予一个或多个角色。
     * </p>
     *
     * @param userId  用户信息
     * @param roleIds 角色 ID 数组
     */
    @RequestMapping("/rbac/user/grantRole")
    @Permission(code = "RBAC::USER::GRANT", name = "授予用户角色")
    @Transactional
    public void grantRoleToUser(@UserId String userId, String[] roleIds) {
        if (roleIds == null) {
            roleIds = new String[0];
        }
        List<String> allByAdmin = rbacRoleRepo.findAllAdminId();
        boolean newRoleHasAdmin = false; // 新的角色是否有管理员
        for (String roleId : roleIds) {
            for (String adminId : allByAdmin) {
                if (adminId.equals(roleId)) {
                    newRoleHasAdmin = true;
                    break;
                }
            }
        }
        // 如果新的角色没有管理员，并且如果除了这个人之外没有管理员了，抛出异常
        if (!newRoleHasAdmin) {
            if (!rbacRoleUserRepo.hasAdminUserWithOutUserId(userId)) {
                throw BusinessError.USER.USER_ADMIN_ROLE;
            }
        }
        //先更新
        rbacRoleUserRepo.deleteByUserId(userId);
        for (String roleId : roleIds) {
            rbacRoleUserRepo.save(new RbacRoleUser(roleId, userId));
        }
    }


    /**
     * 删除用户,管理员角色用户不允许被删除
     */
    @RequestMapping("/rbac/user/delete")
    @Permission(code = "RBAC::USER::DELETE", name = "删除用户")
    public void deleteUser(String userId) {
        if (rbacRoleRepo.isAdminUser(userId)) {
            throw BusinessError.USER.USER_CAN_NOT_DELETE;
        }
        rbacUserRepo.deleteUser(userId);
        rbacRoleUserRepo.deleteByUserId(userId);
        rbacLogRepo.deleteByUserId(userId);
        if (rbacUserCustomStrategies != null && !rbacUserCustomStrategies.isEmpty()) {
            rbacUserCustomStrategies.forEach(s -> s.clear(userId));
        }
    }


    /**
     * 获取当前用户用户的授权码
     *
     * @param user 用户
     */
    @RequestMapping("/rbac/user/feature/get")
    @Permission(code = "RBAC::USER::QUERY", name = "查询用户")
    public String getFeature(@User RbacUser user) {
        rbacTokenService.buildFeatureTokenInfo(user);
        if (!StringUtils.hasText(user.getFeature())) {
            return resetFeature(user);
        }
        return rbacTokenService.buildFeatureTokenInfo(user);
    }


    /**
     * 重新生成用户授权码
     *
     * @param user 用户
     */
    @RequestMapping("/rbac/user/feature/reset")
    @Permission(code = "RBAC::USER::EDIT", name = "编辑用户")
    public String resetFeature(@User RbacUser user) {
        String feature = UUID.randomUUID().toString().replace("-", "");
        user.setFeature(feature);
        rbacUserRepo.save(user);
        return rbacTokenService.buildFeatureTokenInfo(user);
    }


}
