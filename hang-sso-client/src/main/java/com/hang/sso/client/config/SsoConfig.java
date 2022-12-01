package com.hang.sso.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SsoConfig {
    private String ssoServer;
    private String logoutUrl;
}
