package com.hang.sso.client.util;

import com.hang.sso.client.config.LogoutConfig;
import com.hang.sso.client.constant.Const;
import com.hang.sso.client.session.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReturnUtils {
    public static String getLogoutUrl(HttpServletRequest request, HttpServletResponse response, LogoutConfig logoutConfig) {
        CookieUtils.removeCookie(response, Const.TOKEN, "/");
        String token = (String) request.getSession().getAttribute(Const.TOKEN);
        SessionManager.removeSessionByToken(token);
        return "redirect:" + logoutConfig.getSsoServer() + Const.LOGOUT_URL + "?token=" + token + "&redirectUri=" + logoutConfig.getIndexUrl();
    }
}
