package io.github.xiechanglei.cell.starter.rbac.core.init;

import io.github.xiechanglei.cell.common.lang.string.StringHelper;
import io.github.xiechanglei.cell.starter.rbac.core.config.RbacBaseConfigProperties;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacCode;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacBean;
import io.github.xiechanglei.cell.starter.rbac.core.provide.Permission;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacCodeRepo;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacRoleCodeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/6/29
 */
@Component
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class CellRbacInitializer implements ApplicationContextAware {

    private final RbacBaseConfigProperties rbacBaseConfigProperties;

    private final RbacCodeRepo rbacCodeRepo;

    private final RbacRoleCodeRepo rbacRoleCodeRepo;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if ("default".equals(rbacBaseConfigProperties.getModuleName())) {
            log.warn("当前模块的名称为默认值default，分体式架构下请在不同的模块的配置文件中配置各自的cell.rbac.base.module-name属性，建议使用当前模块的包名作为模块名称");
        }
        Map<String, RbacCode> rbacCodes = scanPermission(applicationContext);
        List<RbacCode> all = rbacCodeRepo.findByRefModule(rbacBaseConfigProperties.getModuleName());
        Map<String, RbacCode> allMap = all.stream().collect(Collectors.toMap(RbacCode::getCode, Function.identity()));

        List<RbacCode> toSave = new ArrayList<>();
        // 循环rbacCodes，如果allMap中不存在，则新增，如果存在，判断几个字段是否有变化，如果有变化，则更新，如果没有变化，则不做任何操作
        for (RbacCode code : rbacCodes.values()) {
            RbacCode existing = allMap.remove(code.getCode());
            if (existing == null) { // 如果不存在，则新增
                toSave.add(code);
            } else {
                if (StringHelper.isDifferent(existing.getName(), code.getName())
                        || StringHelper.isDifferent(existing.getDescription(), code.getDescription())) {
                    existing.setName(code.getName());
                    existing.setDescription(code.getDescription());
                    toSave.add(existing); // 需要更新
                }
            }
        }

        // 需要删除的
        List<String> toDelete = allMap.values().stream().map(RbacCode::getCode).toList();
        if (!toSave.isEmpty()) {
            rbacCodeRepo.saveAll(toSave);
        }
        if (!toDelete.isEmpty()) {
            rbacCodeRepo.deleteAllById(toDelete);
            // 删除权限码后，需要删除相关的角色权限关联数据
            rbacRoleCodeRepo.deleteByPerCodeIn(toDelete);
        }
    }


    /**
     * 解析所有的被@RbacBean注解的类，提取其中所有被@RbacPermission注解的方法，获取@RbacPermission注解对象并且返回,注意，需要去重
     */
    private Map<String, RbacCode> scanPermission(ApplicationContext applicationContext) {
        Map<String, Permission> permissionMap = new HashMap<>();
        // 获取所有的被@RbacBean注解的类
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RbacBean.class);
        for (Object bean : beans.values()) {
            // 获取类上的所有方法
            for (var method : AopUtils.getTargetClass(bean).getDeclaredMethods()) {
                // 获取方法上的@RbacPermission注解
                Permission rbacPermission = method.getAnnotation(Permission.class);
                if (rbacPermission != null && StringHelper.isNotBlank(rbacPermission.code())) {
                    // 如果权限码已经存在，则不进行覆盖，避免权限码冲突
                    permissionMap.putIfAbsent(rbacPermission.code(), rbacPermission);
                }
            }
        }
        // 将权限码转换为RbacCode对象
        Map<String, RbacCode> rbacCodeMap = new HashMap<>();
        permissionMap.forEach((code, permission) -> rbacCodeMap.put(code, RbacCode.create(permission.code(), permission.name(), permission.description(), rbacBaseConfigProperties.getModuleName())));
        return rbacCodeMap;
    }
}
