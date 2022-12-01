package com.hang.sso.demo.controller;

import com.hang.sso.client.constant.Const;
import com.hang.sso.client.session.SessionManager;
import com.hang.sso.client.util.CookieUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/sso")
public class LoginController {

    @GetMapping("/doLogout")
    public void logout(@RequestParam(Const.SESSION_NAME) String token) {
        System.out.println("注销局部会话");
        SessionManager.removeSessionByToken(token);
    }

    @GetMapping("/logout")
    public String ssoLogout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.removeCookie(response, Const.SESSION_NAME, "/");
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute(Const.SESSION_NAME);
        session.invalidate();
        String index = "http://localhost:8081/web/index";
        return "redirect:http://localhost:8080/sso/logout" + "?token=" + token + "&redirectUri=" + index;
    }
}
