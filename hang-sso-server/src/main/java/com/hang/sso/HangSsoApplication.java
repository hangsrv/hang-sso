package com.hang.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HangSsoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HangSsoApplication.class, args);
    }
}
