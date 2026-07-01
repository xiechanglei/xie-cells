package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RbacRoleUserRepo extends JpaRepository<RbacRoleUser, String> {

    // 查询是否存在一个管理员的角色,关联RbacRole表
    @Query("select exists(select 1 from RbacRoleUser u join RbacRole r on u.roleId = r.id where r.admin = true)")
    boolean existsAdminUser();
}
