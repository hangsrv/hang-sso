package com.hang.sso.management.local;

import com.hang.sso.constant.SsoConstant;
import com.hang.sso.management.CodeManager;
import com.hang.sso.model.AuthorizationCode;
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
    public String create(Integer userId) {
        CODE_MAP.forEach((code, authorizationCode) -> {
            if (authorizationCode.getUserId().equals(userId)) {
                CODE_MAP.remove(code);
            }
        });
        String newCode = UUID.randomUUID().toString();
        AuthorizationCode authorizationCode = new AuthorizationCode(userId, newCode, System.currentTimeMillis() + SsoConstant.CODE_EXPIRE);
        CODE_MAP.put(newCode, authorizationCode);
        log.info("调用授权码生成成功, accessToken:{}", newCode);
        return newCode;
    }

    @Override
    @Scheduled(cron = SsoConstant.CODE_SCHEDULED_CRON)
    public void refresh() {
        CODE_MAP.forEach((userId, code) -> {
            if (System.currentTimeMillis() > code.getExpired()) {
                CODE_MAP.remove(userId);
                log.info("授权码已失效, code:{}", code);
            }
        });
    }

    @Override
    public boolean verification(String code) {
        AuthorizationCode authorizationCode = CODE_MAP.remove(code);
        if (authorizationCode == null) {
            return false;
        }
        log.info("授权码已删除, code:{}", code);
        return true;
    }
}
