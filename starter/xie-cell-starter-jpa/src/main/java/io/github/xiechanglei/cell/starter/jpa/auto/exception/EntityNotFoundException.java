package io.github.xiechanglei.cell.starter.jpa.auto.exception;

import io.github.xiechanglei.cell.common.bean.exception.BusinessException;

/**
 * 资源不存在异常
 *
 * @author xie
 * @date 2026/3/4
 */
public class EntityNotFoundException extends BusinessException {
    private EntityNotFoundException(String message) {
        super(-1, message);
    }

    public static EntityNotFoundException of(String message) {
        return new EntityNotFoundException(message);
    }
}
