package com.hang.sso.server.service.impl;

import com.hang.sso.server.model.Result;
import com.hang.sso.server.model.User;
import com.hang.sso.server.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final List<User> userList = new ArrayList<>();

    static {
        userList.add(new User(0, "超级管理员", "root", "123456"));
        userList.add(new User(1, "管理员", "hang", "123456"));
        userList.add(new User(2, "普通用户", "lp", "123456"));
    }

    @Override
    public Result<User> login(String username, String password) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return Result.error("账户名或密码错误！");
        }

        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return Result.success("登陆成功!", user);
                } else {
                    return Result.error("密码有误");
                }
            }
        }
        return Result.error("用户不存在");
    }
}
