package com.hang.sso.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationCode implements Serializable {
    private Integer userId;
    private String code;
    private Long expired;
}
