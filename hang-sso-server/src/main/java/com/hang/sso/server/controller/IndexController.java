package com.hang.sso.server.controller;

import com.hang.sso.client.constant.Const;
import com.hang.sso.client.session.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
public class IndexController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 注销局部会话
     **/
    @GetMapping("/logout")
    public void logout(@RequestParam(Const.SESSION_NAME) String token) {
        System.out.println("注销局部会话");
        SessionManager.removeSessionByToken(token);
    }
}
