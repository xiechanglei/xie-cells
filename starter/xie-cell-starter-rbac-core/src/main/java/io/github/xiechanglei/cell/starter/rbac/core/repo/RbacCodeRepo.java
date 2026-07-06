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
    @Query("select c from RbacCode c join RbacRoleCode rc on c.code = rc.perCode join RbacRoleUser ru on rc.roleId = ru.roleId where ru.userId = :userId and c.code = :code")
    Optional<RbacCode> findByUserIdAndCode(String userId, String code);
}
