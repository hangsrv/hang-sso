package com.hang.sso.client.session;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public final class SessionManager {

    private static final Map<String, HttpSession> tokenSessionMap = new HashMap<>();

    public static synchronized void addSession(final String accessToken, final HttpSession session) {
        tokenSessionMap.put(accessToken, session);
    }

    public static synchronized void removeSessionByToken(final String accessToken) {
        HttpSession httpSession = tokenSessionMap.remove(accessToken);
        httpSession.invalidate();
    }
}
