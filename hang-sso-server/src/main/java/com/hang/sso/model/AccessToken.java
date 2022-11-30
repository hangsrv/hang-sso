package com.hang.sso.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private String token;
    private String address;
    private Integer port;
    private Long expireTime;
}
