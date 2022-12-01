package com.hang.sso.client.interceptors;

import com.hang.sso.client.config.SsoConfig;
import com.hang.sso.client.constant.Const;
import com.hang.sso.client.session.SessionManager;
import com.hang.sso.client.util.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringJoiner;

public abstract class ClientInterceptors implements HandlerInterceptor {
    protected SsoConfig ssoConfig;

    protected RestTemplate restTemplate;

    protected boolean checkLocalSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        String tokenSession = SessionManager.getTokenBySessionId(session.getId());
        String tokenCookie = CookieUtils.getCookie(request, Const.TOKEN);
        if (StringUtils.isEmpty(tokenSession) && StringUtils.isNotEmpty(tokenCookie)) {
            if (SessionManager.removeSessionByToken(tokenCookie)) {
                SessionManager.addSession(tokenCookie, session);
                tokenSession = tokenCookie;
            }
        }

        if (StringUtils.isNotEmpty(tokenSession) && tokenSession.equals(tokenCookie)) {
            // 刷新本地会话
            request.getSession().setMaxInactiveInterval(Const.TOKEN_EXPIRE);
            return true;
        }
        return false;
    }

    protected boolean checkToken(HttpServletRequest request, String ssoServer, String logoutUrl) {
        String tokenCookie = CookieUtils.getCookie(request, Const.TOKEN);
        if (StringUtils.isNotEmpty(tokenCookie)) {
            // cookie校验
            String resp = getRefreshRes(ssoServer, tokenCookie, logoutUrl);
            if (Const.OK.equals(resp)) {
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(Const.TOKEN_EXPIRE);
                session.setAttribute(Const.TOKEN, tokenCookie);
                SessionManager.addSession(tokenCookie, session);
                return true;
            }
        }
        return false;
    }

    private String getRefreshRes(String ssoServer, String tokenCookie, String logoutUrl) {
        String url = ssoServer + Const.TOKEN_REFRESH;
        if (logoutUrl != null) {
            url += "?logoutUrl=" + logoutUrl;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("cookie", Const.TOKEN + "=" + tokenCookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class).getBody();
    }

    private String getRefreshRes(String ssoServer, String tokenCookie) {
        return getRefreshRes(ssoServer, tokenCookie, null);
    }

    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String server) throws IOException {
        StringBuilder ssoServer = new StringBuilder(server);
        ssoServer.append(Const.LOGIN_URL);
        Enumeration<String> parameterNames = request.getParameterNames();
        StringJoiner joiner = new StringJoiner("&");
        while (parameterNames.hasMoreElements()) {
            String nextElement = parameterNames.nextElement();
            String requestParameter = request.getParameter(nextElement);
            StringJoiner stringJoiner = new StringJoiner("=");
            stringJoiner.add(nextElement)
                    .add(requestParameter);
            joiner.add(stringJoiner.toString());
        }
        if (!request.getParameterMap().containsKey(Const.REDIRECT_URI)) {
            joiner.add(Const.REDIRECT_URI + "=" + request.getRequestURL());
        }

        if (StringUtils.isNotEmpty(joiner.toString())) {
            ssoServer.append("?").append(joiner.toString());
        }
        response.sendRedirect(ssoServer.toString());
    }
}
