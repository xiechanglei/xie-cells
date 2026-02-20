package io.github.xiechanglei.cell.common.bean.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 资源未找到异常
 *
 * @author xie
 * @date 2025/7/10
 */
@Setter
@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final String path;
}
