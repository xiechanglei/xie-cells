package io.github.xiechanglei.cell.starter.rbac.core.init;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/6/29
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cell.rbac", name = "enable", havingValue = "true", matchIfMissing = true)
public class CellRbacInitializer implements ApplicationContextAware {
    private final CellRbacDataInitializer cellRbacDataInitializer;
    private final CellRbacRoleInitializer cellRbacRoleInitializer;
    private final CellRbacUserInitializer cellRbacUserInitializer;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        cellRbacDataInitializer.process(applicationContext);
//        cellRbacRoleInitializer.process();
//        cellRbacUserInitializer.process();
    }
}
