package com.hang.sso.service;

import com.hang.sso.model.Result;
import com.hang.sso.model.User;

public interface UserService {
    public Result<User> login(String username, String password);
}
