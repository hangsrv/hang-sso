package com.hang.sso.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SsoConfig {
    private String ssoServer;
    private String logoutUrl;
}
