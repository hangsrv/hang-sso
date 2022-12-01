package com.hang.sso.server.util;

import com.hang.sso.server.constant.Const;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    /**
     * 获取cookie
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || !StringUtils.hasLength(name)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /***
     * 添加cookie
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(Const.COOKIE_EXPIRE);
        response.addCookie(cookie);
    }

    /***
     * 删除cookie
     */
    public static void removeCookie(HttpServletResponse response, String name, String path) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(path);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    public static void refreshCookie(HttpServletRequest request, HttpServletResponse response, String name, String path) {
        String cookie = getCookie(request, name);
        if (StringUtils.hasLength(cookie)) {
            addCookie(request, response, name, cookie, path);
        }
    }
}
