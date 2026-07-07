package io.github.xiechanglei.cell.starter.rbac.web.controller;

import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import io.github.xiechanglei.cell.starter.rbac.core.entity.*;
import io.github.xiechanglei.cell.starter.rbac.core.provide.Permission;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacCodeRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacRoleCodeRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacRoleRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacRoleUserRepo;
import io.github.xiechanglei.cell.starter.rbac.web.error.BusinessError;
import io.github.xiechanglei.cell.starter.rbac.web.provide.UserId;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色相关的接口控制器
 * <p>
 * 该控制器包含了与角色相关的所有 API 接口，包括角色的查询、创建、更新、禁用、启用、删除以及权限管理等功能。
 * </p>
 */
@Validated
@RestController("toolsRbacRoleController")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class RbacRoleController {

    private final RbacRoleRepo rbacRoleRepo;
    private final RbacRoleUserRepo rbacRoleUserRepo;
    private final RbacRoleCodeRepo rbacRoleCodeRepo;
    private final RbacCodeRepo rbacCodeRepo;

    /**
     * 查询所有角色
     * <p>
     * 该方法用于查询所有角色信息，可以通过角色名称进行过滤，并分页返回结果。
     * </p>
     *
     * @param pageRequest 分页请求信息
     * @param roleName    搜索关键字
     * @return 角色信息的分页结果
     */
    @Permission(code = "RBAC::ROLE::QUERY", name = "查询角色")
    @RequestMapping("/rbac/role/query")
    public Page<RbacRole> searchRole(PageRequest pageRequest, @RequestParam(required = false, defaultValue = "") String roleName) {
        return rbacRoleRepo.searchRole(pageRequest, roleName);
    }

    /**
     * 获取所有的角色，用于列表等性质的场合
     */
    @Permission(code = "RBAC::ROLE::QUERY", name = "查询角色")
    @RequestMapping("/rbac/role/all")
    public List<RbacRole> all() {
        return rbacRoleRepo.findAll();
    }


    /**
     * 创建角色
     * <p>
     * 该方法用于创建新角色，角色名称必须符合长度要求（1-20个字符）。
     * </p>
     *
     * @param roleName 角色名称
     */
    @Permission(code = "RBAC::ROLE::ADD", name = "创建角色")
    @RequestMapping("/rbac/role/add")
    @Transactional
    public void createRole(@Size(min = 1, max = 20, message = "角色名称长度必须在1-20个字符") String roleName, String roleRemark) {
        if (rbacRoleRepo.existsByRoleName(roleName)) {
            throw BusinessError.ROLE.ROLE_EXISTS;
        }
        RbacRole role = new RbacRole();
        role.setRoleName(roleName);
        role.setRoleStatus(EnableStatus.ENABLED);
        role.setRoleRemark(roleRemark);
        rbacRoleRepo.save(role);
    }


    /**
     * 根据角色 ID 查询角色详情
     * <p>
     * 该方法用于根据角色 ID 获取角色的详细信息。
     * </p>
     *
     * @param roleId 角色 ID
     * @return 角色详细信息
     */
    @Permission(code = "RBAC::ROLE::QUERY", name = "查询角色")
    @RequestMapping("/rbac/role/get")
    public RbacRole searchRoleById(String roleId) {
        return rbacRoleRepo.findById(roleId).orElseThrow(() -> BusinessError.ROLE.ROLE_NOT_EXISTS);
    }


    /**
     * 更新角色信息
     * <p>
     * 该方法用于更新现有角色的信息，包括角色名称和角色备注。
     * </p>
     *
     * @param roleName   角色名称
     * @param roleId     角色 ID
     * @param roleRemark 角色备注
     */
    @Permission(code = "RBAC::ROLE::EDIT", name = "更新角色")
    @RequestMapping("/rbac/role/update")
    @Transactional
    public void editRole(String roleName, String roleId, String roleRemark) {
        // 管理员角色不可操作
        if (rbacRoleRepo.isAdminRole(roleId)) {
            throw BusinessError.ROLE.ROLE_CAN_NOT_OPERATE;
        }

        if (rbacRoleRepo.existsByRoleNameAndIdNot(roleName, roleId)) {
            throw BusinessError.ROLE.ROLE_EXISTS;
        }
        rbacRoleRepo.updateById(roleId, roleName, roleRemark);
    }

    /**
     * 禁用角色
     * <p>
     * 该方法用于禁用指定的角色。禁用的角色将无法被分配给用户或使用。
     * </p>
     *
     * @param roleId 角色 ID
     */
    @Permission(code = "RBAC::ROLE::EDIT", name = "更新角色")
    @RequestMapping("/rbac/role/disable")
    public void disableRole(String roleId) {
        // 管理员角色不可操作
        if (rbacRoleRepo.isAdminRole(roleId)) {
            throw BusinessError.ROLE.ROLE_CAN_NOT_OPERATE;
        }
        rbacRoleRepo.updateRoleStatusById(roleId, EnableStatus.DISABLED);
    }

    /**
     * 启用角色
     * <p>
     * 该方法用于启用指定的角色，启用后的角色可以重新被分配给用户。
     * </p>
     *
     * @param roleId 角色 ID
     */
    @Permission(code = "RBAC::ROLE::EDIT", name = "更新角色")
    @RequestMapping("/rbac/role/enable")
    public void enableRole(String roleId) {
        // 管理员角色不可操作
        if (rbacRoleRepo.isAdminRole(roleId)) {
            throw BusinessError.ROLE.ROLE_CAN_NOT_OPERATE;
        }
        rbacRoleRepo.updateRoleStatusById(roleId, EnableStatus.ENABLED);
    }

    /**
     * 删除角色
     * <p>
     * 该方法用于删除指定的角色。删除的角色将无法恢复，且所有与该角色相关的用户也会受到影响。
     * </p>
     *
     * @param roleId 角色 ID
     */
    @Permission(code = "RBAC::ROLE::DELETE", name = "删除角色")
    @RequestMapping("/rbac/role/delete")
    @Transactional
    public void deleteRole(String roleId) {
        // 角色下不能有用户
        if (rbacRoleUserRepo.existsByRoleId(roleId)) {
            throw BusinessError.ROLE.ROLE_CAN_NOT_DELETE;
        }
        // 管理员角色不可操作
        if (rbacRoleRepo.isAdminRole(roleId)) {
            throw BusinessError.ROLE.ROLE_CAN_NOT_OPERATE;
        }
        // 删除角色关联的功能信息
        rbacRoleCodeRepo.deleteByRoleId(roleId);
        // 删除角色
        rbacRoleRepo.deleteById(roleId);
    }

    /**
     * 查询角色权限
     * <p>
     * 该方法用于查询指定角色的所有权限资源。
     * </p>
     *
     * @param roleId 角色 ID
     * @return 角色拥有的权限资源列表
     */
    @Permission(code = "RBAC::ROLE::QUERY", name = "查询角色")
    @RequestMapping("/rbac/role/permission/get")
    public List<String> loadRoleResource(String roleId) {
        return rbacRoleCodeRepo.findAllPerCodeByRoleId(roleId);
    }

    @Permission(code = "RBAC::ROLE::QUERY", name = "查询角色")
    @RequestMapping("/rbac/role/permission/all")
    public List<RbacCode> loadAllPermission() {
        return rbacCodeRepo.findAll();
    }


    /**
     * 设置角色权限
     * <p>
     * 该方法用于为角色分配权限资源。可以将多个权限资源分配给一个角色。
     * </p>
     *
     * @param roleId        角色 ID
     * @param permissionIds 权限资源 ID 数组
     */
    @Permission(code = "RBAC::ROLE::EDIT::PERMISSION", name = "更新角色权限")
    @RequestMapping("/rbac/role/permission/update")
    public void grantResource(String roleId, String[] permissionIds) {
        if (permissionIds == null) {
            permissionIds = new String[0];
        }
        // 判断角色是否存在
        RbacRole rbacRole = rbacRoleRepo.findById(roleId).orElseThrow(() -> BusinessError.ROLE.ROLE_NOT_EXISTS);

        // 管理员角色的权限由系统自动管理，不允许手动修改
        if (rbacRole.isAdmin()) {
            throw BusinessError.ROLE.ROLE_CAN_NOT_OPERATE;
        }

        // 对权限进行存在性以及去重处理，保证数据的合法性
        List<String> allCode = rbacCodeRepo.findAllCode();
        Set<String> allCodeSet = new HashSet<>(allCode);
        Set<String> permissionCodeSet = new HashSet<>();
        for (String permissionId : permissionIds) {
            if (allCodeSet.contains(permissionId)) {
                permissionCodeSet.add(permissionId);
            }
        }

        // 入库
        List<RbacRoleCode> list = permissionCodeSet.stream().map(perCode -> new RbacRoleCode(roleId, perCode)).toList();
        rbacRoleCodeRepo.saveAll(list);
    }


    /**
     * 根据角色 ID 查询关联的所有用户
     * <p>
     * 该方法用于查询与指定角色关联的所有用户，并分页返回结果。
     * </p>
     *
     * @param pageRequest 分页请求信息
     * @param roleId      角色 ID
     * @return 角色关联的用户分页结果
     */
    @Permission(code = "RBAC::ROLE::QUERY", name = "查询角色")
    @RequestMapping("/rbac/role/users")
    public Page<RbacUser> getUserByRoleId(PageRequest pageRequest, String roleId) {
        return rbacRoleUserRepo.findUserByRoleId(roleId, pageRequest);
    }


    /**
     *
     * 给用户授予角色
     * <p>
     * 该方法用于给指定用户授予一个或多个角色。
     * </p>
     *
     * @param userId 用户信息
     * @param roleId 角色 ID
     */
    @RequestMapping("/rbac/role/grant")
    @Permission(code = "RBAC::ROLE::GRANT", name = "用户分配角色")
    @Transactional
    public void grantRoleToUser(@UserId String userId, String roleId) {
        // 判断角色是否存在
        rbacRoleRepo.findById(roleId).orElseThrow(() -> BusinessError.ROLE.ROLE_NOT_EXISTS);

        // 判断用户是否已经拥有该角色
        if (rbacRoleUserRepo.existsByUserIdAndRoleId(userId, roleId)) {
            throw BusinessError.ROLE.ROLE_HAS_BEEN_GRANTED;
        }

        rbacRoleUserRepo.save(new RbacRoleUser(roleId, userId));
    }


    /**
     * 撤销用户角色
     * <p>
     * 该方法用于撤销指定用户的一个或多个角色，系统至少保留一个管理员角色用户。
     * </p>
     * @param userId 用户信息
     * @param roleId 角色 ID
     */
    @RequestMapping("/rbac/role/revoke")
    @Permission(code = "RBAC::ROLE::GRANT", name = "用户分配角色")
    @Transactional
    public void revokeRoleFromUser(@UserId String userId, String roleId) {
        // 判断用户是否已经拥有该角色
        if (!rbacRoleUserRepo.existsByUserIdAndRoleId(userId, roleId)) {
            throw BusinessError.ROLE.ROLE_NOT_GRANTED;
        }

        //如果是管理员角色，需要检查是否还有其他管理员
        if (rbacRoleRepo.isAdminRole(roleId)) {
            if (!rbacRoleUserRepo.hasAdminUserWithOutUserId(userId)) {
                throw BusinessError.ROLE.USER_ADMIN_ROLE;
            }
        }

        // 删除用户和角色的关联
        rbacRoleUserRepo.deleteByUserIdAndRoleId(userId, roleId);
    }
}
