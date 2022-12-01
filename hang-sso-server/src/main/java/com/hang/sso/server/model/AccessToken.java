package com.hang.sso.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private User user;
    private String token;
    private HashSet<String> logoutUrl;
    private Long expireTime;
}
