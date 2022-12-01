package com.hang.sso.client.interceptors;


import com.hang.sso.client.config.SsoConfig;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptors extends ClientInterceptors {
    public LoginInterceptors(String ssoServer, String logoutUrl) {
        super.ssoConfig = new SsoConfig(ssoServer, logoutUrl);
        super.restTemplate = new RestTemplate();
    }

    public LoginInterceptors(SsoConfig ssoConfig) {
        super.ssoConfig = ssoConfig;
        super.restTemplate = new RestTemplate();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查本地 session
        if (checkLocalSession(request, ssoConfig.getSsoServer())) {
            return true;
        }

        // 检查 tokenCookie
        if (checkToken(request, ssoConfig.getSsoServer(), ssoConfig.getLogoutUrl())) {
            return true;
        }

        // 检查st 或 未登陆
        sendRedirect(request, response, ssoConfig.getSsoServer());
        return false;
    }

}
