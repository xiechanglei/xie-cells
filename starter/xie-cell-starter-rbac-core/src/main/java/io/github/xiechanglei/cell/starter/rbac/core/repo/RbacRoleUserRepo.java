package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRoleUser;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RbacRoleUserRepo extends JpaRepository<RbacRoleUser, String> {

    // 查询是否存在一个管理员的角色,关联RbacRole表
    @Query("select exists(select 1 from RbacRoleUser u join RbacRole r on u.roleId = r.id where r.admin = true)")
    boolean existsAdminUser();

    @Query("delete from RbacRoleUser u where u.userId = ?1")
    @Modifying
    @Transactional
    void deleteByUserId(String userId);


    @Query("select count(1) > 0 from RbacRoleUser a,RbacRole  b where a.roleId = b.id and b.admin = true and a.userId != ?1")
    boolean hasAdminUserWithOutUserId(String userId);

    boolean existsByRoleId(String roleId);

    // 查询某个角色下的所有用户信息
    @Query("select u from RbacRoleUser ru,RbacUser u where ru.userId = u.id and ru.roleId = ?1")
    Page<RbacUser> findUserByRoleId(String roleId, Pageable pageable);

    boolean existsByUserIdAndRoleId(String userId, String roleId);

    @Modifying
    @Transactional
    @Query("delete from RbacRoleUser u where u.userId = ?1 and u.roleId = ?2")
    void deleteByUserIdAndRoleId(String userId, String roleId);
}
