package io.github.xiechanglei.cell.common.bean.message;

/**
 * 全局参数获取器
 *
 * @author xie
 * @date 2026/4/2
 */
public interface GlobalParamResolver {
    ScopedValue<GlobalParamResolver> GlobalParam = ScopedValue.newInstance();

    default String getParam(String name) {
        String[] paramValues = getParamValues(name);
        if (paramValues != null && paramValues.length > 0) {
            return paramValues[0];
        }
        return null;
    }

    String[] getParamValues(String name);

}
