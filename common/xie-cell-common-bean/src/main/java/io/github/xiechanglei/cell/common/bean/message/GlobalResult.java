package io.github.xiechanglei.cell.common.bean.message;

import lombok.Getter;

/**
 * 一个全局的结果包装类,使用ScopedValue实现
 *
 * @author xie
 * @date 2026/4/2
 */
@Getter
public class GlobalResult {
    public static final ScopedValue<GlobalResult> Result = ScopedValue.newInstance();

    private Object result;
    private boolean bound;

    public void setResult(Object result) {
        this.result = result;
        this.bound = true;
    }
}
