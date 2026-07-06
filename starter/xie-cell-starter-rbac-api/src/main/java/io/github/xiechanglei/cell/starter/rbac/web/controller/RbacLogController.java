package io.github.xiechanglei.cell.starter.rbac.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志相关的 API 接口
 * <p>
 * </p>
 */
@RestController("toolsRbacLogController")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cell.rbac.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class RbacLogController {

//
//    @RequestMapping("/rbac/log/query")
//    @PermissionCell(code = "RBAC::LOG::QUERY", name = "查询日志")
//    public Page<RbacLogQo> query(String userId, String nickName, String logTitle, String logPath, String logAddress, Date logTimeStart, Date logTimeEnd, PageRequest pageRequest) {
//        return rbacLogService.query(userId, nickName, logTitle, logPath, logAddress, logTimeStart, logTimeEnd, pageRequest);
//    }
}
