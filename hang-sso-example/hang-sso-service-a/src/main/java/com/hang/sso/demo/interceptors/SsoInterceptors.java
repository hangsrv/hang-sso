package com.hang.sso.demo.interceptors;

import com.hang.sso.client.config.SsoConfig;
import com.hang.sso.client.interceptors.LoginInterceptors;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class SsoInterceptors implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        SsoConfig ssoConfig = new SsoConfig("http://localhost:8080", "http://localhost:8081/web/doLogout");
        registry.addInterceptor(new LoginInterceptors(ssoConfig))
                .excludePathPatterns("/sso/**", ssoConfig.getLogoutUrl());
    }

}
