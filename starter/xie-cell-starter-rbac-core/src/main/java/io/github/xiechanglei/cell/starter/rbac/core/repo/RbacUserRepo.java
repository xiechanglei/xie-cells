package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.promotion.UserAuthedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RbacUserRepo extends JpaRepository<RbacUser, String> {
    boolean existsByUserName(String username);

    // 查询某个用户的认证字段信息，包含是否拥有管理员角色等信息
    @Query("select u.id as id, u.userSerial as userSerial, u.feature as feature, u.enableStatus as enableStatus, exists(select 1 from RbacRoleUser ru join RbacRole r on ru.roleId = r.id where ru.userId = u.id and r.admin = true) as admin from RbacUser u where u.id = :userId")
    UserAuthedInfo findUserAuthedInfoById(String userId);
}
