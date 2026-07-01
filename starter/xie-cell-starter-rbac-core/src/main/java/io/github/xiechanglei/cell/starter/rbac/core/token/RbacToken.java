package io.github.xiechanglei.cell.starter.rbac.core.token;

import io.github.xiechanglei.cell.common.lang.secret.AESHelper;

import java.util.Date;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/7/1
 */
public class RbacToken {
    static void main() throws Exception {
        String encode = AESHelper.encode("" + new Date().getTime(), "cell-rbac", "cell-rbac");
        System.out.println(encode.length());
    }
}
