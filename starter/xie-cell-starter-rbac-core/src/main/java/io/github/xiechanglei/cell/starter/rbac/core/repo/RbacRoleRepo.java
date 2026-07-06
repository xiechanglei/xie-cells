package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RbacRoleRepo extends JpaRepository<RbacRole, String> {
    // 是否存在一个管理员角色
//    boolean existsByAdminTrue();

    // 最多获取一个管理员角色
    RbacRole findFirstByAdminTrue();

    // 是否存在一个指定名称的角色
    boolean existsByRoleName(String name);

    // 根据用户id查询角色
    @Query("select b from RbacRoleUser a,RbacRole b where a.roleId = b.id and a.userId = ?1")
    List<RbacRole> findRoleByUserId(String userId);
}
