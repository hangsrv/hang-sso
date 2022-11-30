package com.hang.sso.management;

/***
 * 授权码管理
 */
public interface CodeManager {
    /***
     * 生成
     */
    default public String create(Integer userId) {
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
    default public boolean verification(String code) {
        return false;
    }
}
