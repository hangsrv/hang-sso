package com.hang.sso.client.session;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public final class SessionManager {

    private static final Map<String, HttpSession> TOKEN_TO_SESSION = new HashMap<>();
    private static final Map<String, String> SESSION_ID_TO_TOKEN = new HashMap<>();

    public static synchronized void addSession(final String accessToken, final HttpSession session) {
        TOKEN_TO_SESSION.put(accessToken, session);
        SESSION_ID_TO_TOKEN.put(session.getId(), accessToken);
    }

    public static synchronized String getTokenBySessionId(final String sessionId) {
        return SESSION_ID_TO_TOKEN.get(sessionId);
    }

    public static synchronized boolean removeSessionByToken(final String accessToken) {
        HttpSession session = TOKEN_TO_SESSION.remove(accessToken);
        if (session != null) {
            SESSION_ID_TO_TOKEN.remove(session.getId());
            session.invalidate();
            return true;
        }
        return false;
    }
}
