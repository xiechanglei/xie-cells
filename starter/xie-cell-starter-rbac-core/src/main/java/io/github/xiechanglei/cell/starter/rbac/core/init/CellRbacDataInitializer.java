package io.github.xiechanglei.cell.starter.rbac.core.init;

import io.github.xiechanglei.cell.starter.rbac.core.config.RbacCellConfigProperties;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacCode;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacBean;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacPermission;
import io.github.xiechanglei.cell.starter.rbac.core.repo.RbacCodeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表数据初始化,检索当前项目中的所有标住了权限注解的配置类，并将其注册到数据库中
 *
 * @author xie
 * @date 2026/6/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CellRbacDataInitializer {
    private final RbacCellConfigProperties rbacCellConfigProperties;

    private final RbacCodeRepo rbacCodeRepo;

    /**
     * 获取当前模块中的所有权限码，并将其注册到数据库中
     * 1. 扫描获取当前模块中所有的权限码
     * 2. 获取当前模块在数据库中已经存在的权限码
     * 3. 对扫描到的权限码和数据库中已经存在的权限码进行对比，找出新增的权限码，需要删除的权限码，以及需要更新的权限码
     * 4. 将新增的权限码插入到数据库中，将需要删除的权限码从数据库中删除，将需要更新的权限码更新到数据库中
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    protected void process(ApplicationContext applicationContext) {
        if ("default".equals(rbacCellConfigProperties.getModuleName())) {
            log.warn("当前模块的名称为默认值default，分体式架构下请在不同的模块的配置文件中配置各自的cell.rbac.module-name属性，建议使用当前模块的包名作为模块名称");
        }
        List<RbacCode> rbacCodes = scanPermission(applicationContext);
        log.info("scan permission size: {}", rbacCodes.size());
        List<RbacCode> all = rbacCodeRepo.findAll();
        log.info("database permission size: {}", all.size());
    }


    /**
     * 解析所有的被@RbacBean注解的类，提取其中所有被@RbacPermission注解的方法，获取@RbacPermission注解对象并且返回,注意，需要去重
     */
    private List<RbacCode> scanPermission(ApplicationContext applicationContext) {
        Map<String, RbacPermission> permissionMap = new HashMap<>();
        // 获取所有的被@RbacBean注解的类
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RbacBean.class);
        for (Object bean : beans.values()) {
            // 获取类上的所有方法
            for (var method : bean.getClass().getDeclaredMethods()) {
                // 获取方法上的@RbacPermission注解
                RbacPermission rbacPermission = method.getAnnotation(RbacPermission.class);
                if (rbacPermission != null) {
                    // 如果权限码已经存在，则不进行覆盖，避免权限码冲突
                    permissionMap.putIfAbsent(rbacPermission.code(), rbacPermission);
                }
            }
        }
        return permissionMap.values().stream().map(permission ->
                RbacCode.create(permission.code(), permission.name(), permission.description(), rbacCellConfigProperties.getModuleName(), permission.log())
        ).toList();
    }
}
