package com.hang.sso.server.interceptors;

import com.hang.sso.client.config.SsoConfig;
import com.hang.sso.client.interceptors.LoginInterceptors;
import com.hang.sso.client.interceptors.LogoutInterceptors;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class SsoInterceptors implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        SsoConfig ssoConfig = new SsoConfig("http://localhost:8080", "http://localhost:8080/web/logout");
        registry.addInterceptor(new LoginInterceptors(ssoConfig))
                .excludePathPatterns("/sso/**", ssoConfig.getLogoutUrl());
        registry.addInterceptor(new LogoutInterceptors(ssoConfig))
                .addPathPatterns("/sso/logout");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
