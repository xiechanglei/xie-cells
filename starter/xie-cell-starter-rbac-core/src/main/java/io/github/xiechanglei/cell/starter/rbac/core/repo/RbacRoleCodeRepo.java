package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacRoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RbacRoleCodeRepo extends JpaRepository<RbacRoleCode, String> {
    void deleteByPerCodeIn(List<String> perCodes);

    void deleteByRoleId(String roleId);

    @Query("select r.perCode from RbacRoleCode r where r.roleId = ?1")
    List<String> findAllPerCodeByRoleId(String roleId);
}
