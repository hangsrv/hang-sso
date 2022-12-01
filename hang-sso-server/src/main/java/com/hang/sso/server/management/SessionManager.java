package com.hang.sso.server.management;

import com.hang.sso.server.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 会话管理
 */
public interface SessionManager {
    default public String create(User user) {
        return null;
    }

    /***
     * 删除
     */
    default public void remove(String key) {
    }

    /***
     * 刷新
     */
    default public void refresh() {
    }

    /***
     * 验证
     */
    default public boolean verification(HttpServletRequest request, HttpServletResponse response, String token) {
        return false;
    }

    default public boolean verification(HttpServletRequest request, HttpServletResponse response, String token, String logoutUrl) {
        return false;
    }
}
