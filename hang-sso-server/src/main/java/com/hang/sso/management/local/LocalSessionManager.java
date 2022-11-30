package com.hang.sso.management.local;

import com.hang.sso.constant.SsoConstant;
import com.hang.sso.management.SessionManager;
import com.hang.sso.model.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 本地会话管理
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "sso.server", value = "type", havingValue = "local")
public class LocalSessionManager implements SessionManager {
    private final Map<String, AccessToken> TOKEN_MAP = new ConcurrentHashMap<>();
    private final Map<String, Set<InetSocketAddress>> LOGOUT_NOTICE_MAP = new ConcurrentHashMap<>();

    @Override
    public String create(String address, Integer port) {
        String token = UUID.randomUUID().toString();
        AccessToken accessToken = new AccessToken(token, address, port, System.currentTimeMillis() + SsoConstant.TOKEN_EXPIRE);
        TOKEN_MAP.put(token, accessToken);
        Set<InetSocketAddress> sources = LOGOUT_NOTICE_MAP.getOrDefault(token, new HashSet<>());
        sources.add(new InetSocketAddress(address, port));
        log.info("调用凭证生成成功, accessToken:{}", token);
        return token;
    }

    @Override
    public void remove(String token) {
        TOKEN_MAP.remove(token);
        logoutNotice(token);
    }

    @Override
    @Scheduled(cron = SsoConstant.TOKEN_SCHEDULED_CRON)
    public void refresh() {
        TOKEN_MAP.forEach((token, accessToken) -> {
            if (System.currentTimeMillis() > accessToken.getExpireTime()) {
                remove(token);
                log.info("凭证已失效, token:{}", token);
            }
        });

    }

    private void logoutNotice(String token) {
        Set<InetSocketAddress> sources = LOGOUT_NOTICE_MAP.remove(token);
        log.info("凭证已删除, token:{}，sources{}", token, sources);
        // todo 全局会话销毁，回调所有局部会话，注销会话
    }

    @Override
    public boolean verification(String token) {
        if (!StringUtils.hasLength(token)) {
            return false;
        }
        AccessToken accessToken = TOKEN_MAP.computeIfPresent(token, (k, v) -> {
            v.setExpireTime(System.currentTimeMillis() + SsoConstant.TOKEN_EXPIRE);
            return v;
        });
        return accessToken != null && System.currentTimeMillis() <= accessToken.getExpireTime();
    }
}
