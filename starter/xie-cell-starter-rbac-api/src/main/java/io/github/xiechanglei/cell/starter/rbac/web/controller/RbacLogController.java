package io.github.xiechanglei.cell.starter.rbac.web.controller;

import io.github.xiechanglei.cell.starter.jpa.query.JpaQueryProducer;
import io.github.xiechanglei.cell.starter.jpa.query.TupleConvertor;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacLog;
import io.github.xiechanglei.cell.starter.rbac.core.provide.PermissionCell;
import io.github.xiechanglei.cell.starter.rbac.web.qo.RbacLogQo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 日志相关的 API 接口
 * <p>
 * </p>
 */
@RestController("toolsRbacLogController")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class RbacLogController {

    private final EntityManager entityManager;

    private final TupleConvertor<RbacLogQo> tupleConvertor = (result) -> {
        RbacLogQo rbacLogQo = new RbacLogQo();
        rbacLogQo.setLog((RbacLog) result[0]);
        rbacLogQo.setNickName((String) result[1]);
        return rbacLogQo;
    };

    private JpaQueryProducer buildQuery(String userId, String nickName, String logTitle, String logPath, String logAddress, Date startTime, Date endTime) {
        return JpaQueryProducer.createNamedQuery(entityManager, "select  l,u.nickName from RbacLog l left join RbacUser u on l.userId = u.id where 1=1")
                .filterCondition(StringUtils.hasText(userId), "and l.userId = :userId", userId)
                .filterCondition(StringUtils.hasText(nickName), "and u.nickName like :nickName", "%" + nickName + "%")
                .filterCondition(StringUtils.hasText(logTitle), "and l.logTitle like :logTitle", "%" + logTitle + "%")
                .filterCondition(StringUtils.hasText(logPath), "and l.logPath like :logPath", "%" + logPath + "%")
                .filterCondition(StringUtils.hasText(logAddress), "and l.logAddress like :logAddress", "%" + logAddress + "%")
                .filterCondition(startTime != null, "and l.createTime >= :startTime", startTime)
                .filterCondition(endTime != null, "and l.createTime <= :endTime", endTime)
                .withCondition("order by l.createTime desc");
    }


    /**
     * 分页查询日志
     */
    @RequestMapping("/rbac/log/query")
    @PermissionCell(code = "RBAC::LOG::QUERY", name = "查询日志")
    public Page<RbacLogQo> query(String userId, String nickName, String logTitle, String logPath, String logAddress, Date logTimeStart, Date logTimeEnd, PageRequest pageRequest) {
        pageRequest.withSort(Sort.unsorted());
        return buildQuery(userId, nickName, logTitle, logPath, logAddress, logTimeStart, logTimeEnd).getTuplePage(tupleConvertor, pageRequest);
    }
}
