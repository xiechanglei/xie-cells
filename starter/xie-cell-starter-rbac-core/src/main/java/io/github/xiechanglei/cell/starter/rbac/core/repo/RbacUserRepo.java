package io.github.xiechanglei.cell.starter.rbac.core.repo;

import io.github.xiechanglei.cell.starter.jpa.entity.EnableStatus;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.promotion.UserAuthedInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RbacUserRepo extends JpaRepository<RbacUser, String> {
    boolean existsByUserName(String username);

    // 查询某个用户的认证字段信息，包含是否拥有管理员角色等信息
    @Query("select u.id as id, u.userSerial as userSerial, u.feature as feature, u.enableStatus as enableStatus, exists(select 1 from RbacRoleUser ru join RbacRole r on ru.roleId = r.id where ru.userId = u.id and r.admin = true) as admin from RbacUser u where u.id = :userId")
    UserAuthedInfo findUserAuthedInfoById(String userId);

    Optional<RbacUser> findByUserNameAndUserPassword(String userName, String userPassword);

    /**
     * 查询用户，支持模糊查询昵称、手机号、邮箱，地址等字段
     * @param word 查询关键字
     * @param pageable 分页参数
     * @return 用户列表
     */
    @Query("select u from RbacUser u where u.userName like %:word% or u.nickName like %:word%  or u.phoneNumber like %:word% or u.email like %:word% or u.address like %:word%")
    Page<RbacUser> queryUser(String word, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from RbacUser u where u.id = :userId")
    void deleteUser(String userId);

    @Modifying
    @Transactional
    @Query("update RbacUser u set u.enableStatus = :enableStatus where u.id = :userId")
    void changeUserStatus(String userId, EnableStatus enableStatus);

    @Modifying
    @Transactional
    @Query("update RbacUser u set u.userPassword = :encode where u.id = :userId")
    void updatePassword(String userId, String encode);
}
