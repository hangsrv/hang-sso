package com.hang.sso.client.interceptors;


import com.hang.sso.client.config.SsoConfig;
import com.hang.sso.client.constant.Const;
import com.hang.sso.client.session.SessionManager;
import com.hang.sso.client.util.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutInterceptors extends ClientInterceptors {
    public LogoutInterceptors(String ssoServer, String logoutUrl) {
        super.ssoConfig = new SsoConfig(ssoServer, logoutUrl);
        super.restTemplate = new RestTemplate();
    }

    public LogoutInterceptors(SsoConfig ssoConfig) {
        super.ssoConfig = ssoConfig;
        super.restTemplate = new RestTemplate();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = (String) request.getSession().getAttribute(Const.SESSION_NAME);
        if (StringUtils.isNotEmpty(token)) {
            request.setAttribute(Const.SESSION_NAME, token);
            request.setAttribute(Const.REDIRECT_URI, request.getRequestURL().toString().replaceAll(Const.REGEXP_URL, Const.REGEXP_REPLACE_URL) + Const.INDEX_URL);
            CookieUtils.removeCookie(response, Const.SESSION_NAME, "/");
            request.getSession().invalidate();
            SessionManager.removeSessionByToken(token);
            return true;
        }
        return false;
    }
}
