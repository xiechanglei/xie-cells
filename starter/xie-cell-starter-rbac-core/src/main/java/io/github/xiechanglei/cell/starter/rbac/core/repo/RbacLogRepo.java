package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RbacLogRepo extends JpaRepository<RbacLog, String> {
}
