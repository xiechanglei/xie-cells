package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RbacCodeRepo extends JpaRepository<RbacCode, String> {

    List<RbacCode> findByRefModule(String module);

    /**
     * 根据用户id查询用户是否拥有某个权限码
     */
    @Query("select distinct c from RbacCode c join RbacRoleCode rc on c.code = rc.perCode join RbacRoleUser ru on rc.roleId = ru.roleId join RbacRole r on ru.roleId = r.id where r.roleStatus = io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus.ENABLED and " +
            "ru.userId = :userId and c.code = :code")
    Optional<RbacCode> findByUserIdAndCode(String userId, String code);

    /**
     * 查询某个用户所有的权限码
     */
    @Query("select distinct c.code from RbacCode c join RbacRoleCode rc on c.code = rc.perCode join RbacRoleUser ru on rc.roleId = ru.roleId join RbacRole r on ru.roleId = r.id where r.roleStatus = io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus.ENABLED and " +
            "ru.userId = :userId")
    List<String> findUserPermissionCode(String userId);

    @Query("select distinct c.code from RbacCode c")
    List<String> findAllCode();
}
