package io.github.xiechanglei.cell.starter.rbac.web.init;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRole;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRoleUser;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacRoleRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacRoleUserRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacUserRepo;
import io.github.xiechanglei.cell.starter.rbac.web.config.RbacCellApiConfigProperties;
import io.github.xiechanglei.cell.starter.rbac.web.nest.RbacPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 角色数据初始化，应该至少有一个角色，超级管理员角色
 *
 * @author xie
 * @date 2026/6/29
 */
@Component
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class CellRbacRoleAndUserInitializer implements ApplicationContextAware {

    public final RbacRoleRepo rbacRoleRepo;

    public final RbacCellApiConfigProperties rbacCellApiConfigProperties;

    private final RbacRoleUserRepo rbacRoleUserRepo;

    private final RbacPasswordService rbacPasswordService;

    private final RbacUserRepo rbacUserRepo;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        // 创建超级管理员角色
        RbacRole admin = rbacRoleRepo.findFirstByAdminTrue();
        if (admin == null) {
            if (!rbacRoleRepo.existsByRoleName(rbacCellApiConfigProperties.getAdminRoleName())) {
                admin = rbacRoleRepo.save(RbacRole.createAdmin(rbacCellApiConfigProperties.getAdminRoleName()));
            } else {
                log.warn("创建超级管理员角色失败，当前模块中已经存在一个名称为{}的非管理员角色，请检查配置文件中的cell.rbac.gen.admin-role-name属性是否正确", rbacCellApiConfigProperties.getAdminRoleName());
            }
        }

        // 创建管理员用户
        if (admin == null) {
            log.warn("创建超级管理员用户失败，当前系统中没有超级管理员角色");
        } else {
            if (!rbacRoleUserRepo.existsAdminUser()) {
                log.info("当前系统中没有超级管理员用户，正在创建超级管理员用户，用户名为：{}，密码为：{}", rbacCellApiConfigProperties.getAdminUsername(), rbacCellApiConfigProperties.getAdminPassword());
                if (!rbacUserRepo.existsByUserName(rbacCellApiConfigProperties.getAdminUsername())) {
                    RbacUser rbacUser = RbacUser.buildAdmin(rbacCellApiConfigProperties.getAdminUsername());
                    rbacUser.setUserPassword(rbacPasswordService.encode(rbacCellApiConfigProperties.getAdminPassword()));
                    rbacUserRepo.save(rbacUser);
                    RbacRoleUser roleUser = new RbacRoleUser();
                    roleUser.setRoleId(admin.getId());
                    roleUser.setUserId(rbacUser.getId());
                    rbacRoleUserRepo.save(roleUser);
                } else {
                    log.warn("创建超级管理员用户失败，当前系统中已经存在一个用户名为{}的非管理员用户，请检查配置文件中的cell.rbac.gen.admin-username属性是否正确", rbacCellApiConfigProperties.getAdminUsername());
                }
            }
        }
    }
}
