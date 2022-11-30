package com.hang.sso.management;

/***
 * 会话管理
 */
public interface SessionManager {
    default public String create(String host, Integer port) {
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
    default public boolean verification(String key) {
        return false;
    }
}
