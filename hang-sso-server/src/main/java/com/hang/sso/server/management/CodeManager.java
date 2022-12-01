package com.hang.sso.server.management;

import com.hang.sso.server.model.AuthorizationCode;
import com.hang.sso.server.model.User;

/***
 * 授权码管理
 */
public interface CodeManager {
    /***
     * 生成
     */
    default public String create(User user) {
        return null;
    }

    /***
     * 刷新
     */
    default public void refresh() {
    }

    /***
     * 验证
     */
    default public AuthorizationCode verification(String code) {
        return null;
    }
}
