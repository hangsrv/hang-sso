package com.hang.sso.server.service;

import com.hang.sso.server.model.Result;
import com.hang.sso.server.model.User;

public interface UserService {
    public Result<User> login(String username, String password);
}
