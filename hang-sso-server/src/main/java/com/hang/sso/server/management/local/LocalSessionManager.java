package com.hang.sso.server.management.local;

import com.hang.sso.server.constant.Const;
import com.hang.sso.server.management.SessionManager;
import com.hang.sso.server.model.AccessToken;
import com.hang.sso.server.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 本地会话管理
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "sso.server", value = "type", havingValue = "local")
public class LocalSessionManager implements SessionManager {
    @Autowired
    private RestTemplate restTemplate;

    private final Map<String, AccessToken> TOKEN_MAP = new ConcurrentHashMap<>();

    @Override
    public synchronized String create(User user) {
        TOKEN_MAP.forEach((token, accessToken) -> {
            if (accessToken.getUser().getUserId().equals(user.getUserId())) {
                TOKEN_MAP.remove(token);
            }
        });
        String token = UUID.randomUUID().toString();
        AccessToken accessToken = new AccessToken(user, token, new HashSet<>(), System.currentTimeMillis() + Const.TOKEN_EXPIRE);
        TOKEN_MAP.put(token, accessToken);
        log.info("调用凭证生成成功, accessToken:{}", token);
        return token;
    }

    @Override
    public void remove(String token) {
        AccessToken remove = TOKEN_MAP.remove(token);
        log.info("凭证已删除, token:{}", token);
        for (String logoutUrl : remove.getLogoutUrl()) {
            restTemplate.getForObject(logoutUrl, String.class);
            log.info("局部会话已删除, logoutUrl:{}", logoutUrl);
        }

    }

    @Override
    @Scheduled(cron = Const.TOKEN_SCHEDULED_CRON)
    public void refresh() {
        TOKEN_MAP.forEach((token, accessToken) -> {
            if (System.currentTimeMillis() > accessToken.getExpireTime()) {
                log.info("凭证已失效, token:{}", token);
                remove(token);
            }
        });

    }

    @Override
    public boolean verification(String token) {
        return verification(token, null);
    }

    @Override
    public boolean verification(String token, String logoutUrl) {
        if (!StringUtils.hasLength(token)) {
            return false;
        }
        AccessToken accessToken = TOKEN_MAP.computeIfPresent(token, (k, v) -> {
            if (StringUtils.hasLength(logoutUrl)) {
                StringBuilder url = new StringBuilder(logoutUrl);
                url.append("?")
                        .append(Const.TOKEN)
                        .append("=")
                        .append(token);
                v.getLogoutUrl().add(url.toString());
            }
            v.setExpireTime(System.currentTimeMillis() + Const.TOKEN_EXPIRE);
            log.info("凭证已刷新, token:{}", token);
            return v;
        });
        return accessToken != null && System.currentTimeMillis() < accessToken.getExpireTime();
    }
}
