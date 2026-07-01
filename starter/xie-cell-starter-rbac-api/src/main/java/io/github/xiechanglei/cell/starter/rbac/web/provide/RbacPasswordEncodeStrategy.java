package io.github.xiechanglei.cell.starter.rbac.web.provide;

/**
 * 可以自己重写密码加密解密规则，默认为MD5加密，定义的类需要配置到spring容器中
 */
public interface RbacPasswordEncodeStrategy {
    /**
     * 实现密码明文到密文的转换过程
     *
     * @param value 密码明文
     * @return 密码密文
     */
    String encode(String value);

    /**
     * 检查密码明文和密文是否匹配，一般情况下不需要重写该方法，除非你有特殊的密码校验规则
     */
    default boolean check(String value, String encodedPassword) {
        return value != null && encode(value).equals(encodedPassword);
    }
}
