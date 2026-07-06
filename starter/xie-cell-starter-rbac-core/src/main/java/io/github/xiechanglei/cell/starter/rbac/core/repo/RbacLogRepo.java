package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RbacLogRepo extends JpaRepository<RbacLog, String> {

    @Modifying
    @Transactional
    @Query("delete from RbacLog where userId = :userId")
    void deleteByUserId(String userId);
}
