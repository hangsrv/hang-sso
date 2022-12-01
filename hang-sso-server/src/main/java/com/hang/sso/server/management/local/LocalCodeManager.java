package com.hang.sso.server.management.local;

import com.hang.sso.server.constant.Const;
import com.hang.sso.server.management.CodeManager;
import com.hang.sso.server.model.AuthorizationCode;
import com.hang.sso.server.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/***
 * 本地授权码管理
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "sso.server", value = "type", havingValue = "local")
public class LocalCodeManager implements CodeManager {

    private final Map<String, AuthorizationCode> CODE_MAP = new ConcurrentHashMap<>();

    @Override
    public String create(User user) {
        String newCode = UUID.randomUUID().toString();
        AuthorizationCode authorizationCode = new AuthorizationCode(user, newCode, System.currentTimeMillis() + Const.CODE_EXPIRE);
        CODE_MAP.put(newCode, authorizationCode);
        log.info("调用授权码生成成功, accessToken:{}", newCode);
        return newCode;
    }

    @Override
    @Scheduled(cron = Const.CODE_SCHEDULED_CRON)
    public void refresh() {
        CODE_MAP.forEach((userId, code) -> {
            if (System.currentTimeMillis() > code.getExpired()) {
                CODE_MAP.remove(userId);
                log.info("授权码已失效, code:{}", code);
            }
        });
    }

    @Override
    public AuthorizationCode verification(String code) {
        AuthorizationCode authorizationCode = CODE_MAP.remove(code);
        if (authorizationCode == null) {
            return null;
        }
        log.info("授权码已删除, code:{}", code);
        return authorizationCode;
    }
}
