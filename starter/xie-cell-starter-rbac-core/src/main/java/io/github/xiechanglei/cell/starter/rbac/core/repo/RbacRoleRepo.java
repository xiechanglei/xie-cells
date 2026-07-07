package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRole;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    // 指定userId是否拥有管理员角色
    @Query("select case when count(r) > 0 then true else false end from RbacRoleUser ru join RbacRole r on ru.roleId = r.id where ru.userId = ?1 and r.admin = true")
    boolean isAdminUser(String userId);

    // 获取所有的管理员角色Id
    @Query("select r.id from RbacRole r where r.admin = true ")
    List<String> findAllAdminId();

    @Query("select r from RbacRole r where r.roleName like %:roleName%")
    Page<RbacRole> searchRole(PageRequest pageRequest, String roleName);

    /**
     * 检查角色名称在除了指定的id之外是否存在
     *
     * @param roleName 角色名称
     * @param roleId   角色ID
     */
    boolean existsByRoleNameAndIdNot(String roleName, String roleId);

    @Modifying
    @Transactional
    @Query("update RbacRole set roleName = ?2, roleRemark = ?3 where id = ?1 and admin = false")
    void updateById(String roleId, String roleName, String roleRemark);


    @Modifying
    @Transactional
    @Query("update RbacRole set roleStatus = ?2 where id = ?1 and admin = false")
    void updateRoleStatusById(String roleId, EnableStatus roleStatus);

    List<RbacRole> findTop2ByAdmin(boolean admin);



    @Modifying
    @Transactional
    @Query(value = "delete from RbacRole where id = ?1")
    void deleteById(@NonNull String id);

    @Query("select case when count(r) > 0 then true else false end from RbacRole r where r.id = ?1 and r.admin = true")
    boolean isAdminRole(String roleId);
}
