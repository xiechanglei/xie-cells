package io.github.xiechanglei.cell.starter.rbac.web.nest;

import io.github.xiechanglei.cell.common.lang.secret.Md5Helper;
import io.github.xiechanglei.cell.starter.rbac.web.provide.RbacPasswordEncodeStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rbac的密码加密策略,默认使用md5进行加密，如果需要自定义加密策略，请实现RbacPasswordEncodeStrategy接口，并将其配置到spring容器中
 *
 * @author xie
 * @date 2024/12/20
 */
@Component
public class RbacPasswordService {
    private RbacPasswordEncodeStrategy rbacPasswordEncodeStrategy;

    @Autowired(required = false)
    public void setRbacPasswordEncodeStrategy(RbacPasswordEncodeStrategy rbacPasswordEncodeStrategy) {
        this.rbacPasswordEncodeStrategy = rbacPasswordEncodeStrategy;
    }

    @PostConstruct
    public void init() {
        if (rbacPasswordEncodeStrategy == null) {
            rbacPasswordEncodeStrategy = Md5Helper::encode;
        }
    }

    public String encode(String password) {
        return rbacPasswordEncodeStrategy.encode(password);
    }
}
