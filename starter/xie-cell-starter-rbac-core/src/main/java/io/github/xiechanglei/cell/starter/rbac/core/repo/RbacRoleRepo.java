package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RbacRoleRepo extends JpaRepository<RbacRole, String> {
    // 是否存在一个管理员角色
//    boolean existsByAdminTrue();

    // 最多获取一个管理员角色
    RbacRole findFirstByAdminTrue();

    // 是否存在一个指定名称的角色
    boolean existsByRoleName(String name);

}
