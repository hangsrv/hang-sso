package com.hang.sso.server.controller;

import com.hang.sso.server.constant.Const;
import com.hang.sso.server.management.CodeManager;
import com.hang.sso.server.management.SessionManager;
import com.hang.sso.server.model.AuthorizationCode;
import com.hang.sso.server.model.Result;
import com.hang.sso.server.model.User;
import com.hang.sso.server.service.UserService;
import com.hang.sso.server.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
@RequestMapping("/sso")
public class LoginController {
    @Autowired
    private CodeManager codeManager;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserService userService;

    @GetMapping("/toLogin")
    public String doLogin(
            @RequestParam(Const.REDIRECT_URI) String redirectUri,
            @RequestParam(value = Const.ST, required = false) String st,
            HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        // 校验token
        if (checkToken(request, response)) {
            return getRedirect(redirectUri);
        }
        // 校验code
        if (StringUtils.hasLength(st) && checkCode(st, request, response)) {
            return getRedirect(redirectUri);
        }
        model.addAttribute(Const.REDIRECT_URI, redirectUri);
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(
            @RequestParam(Const.USERNAME) String username,
            @RequestParam(Const.PASSWORD) String password,
            @RequestParam(Const.REDIRECT_URI) String redirectUri,
            HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        if (!StringUtils.hasLength(redirectUri)) {
            model.addAttribute("errmsg", "页面丢失，请返回重新原页面访问！");
            return "forward:/sso/toLogin";
        }
        model.addAttribute(Const.REDIRECT_URI, redirectUri);

        Result<User> loginResult = userService.login(username, password);
        if (!loginResult.isSuccess()) {
            model.addAttribute("errmsg", loginResult.getMessage());
            return "forward:/sso/toLogin";
        }

        String code = codeManager.create(loginResult.getData());
        return getRedirect(redirectUri, code);
    }

    @GetMapping("/refresh")
    @ResponseBody
    public String refresh(
            @RequestParam(value = Const.LOGOUT_URL, required = false) String logoutUrl,
            HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return checkToken(logoutUrl, request, response) ? Const.OK : Const.ERR;
    }

    private boolean checkToken(String logoutUrl, HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookie(request, Const.TOKEN);
        if (StringUtils.hasLength(logoutUrl) && sessionManager.verification(request, response, token, logoutUrl)) {
            return true;
        } else if (sessionManager.verification(request, response, token)) {
            return true;
        }
        return false;
    }

    private boolean checkToken(HttpServletRequest request, HttpServletResponse response) {

        return checkToken(null, request, response);
    }

    @GetMapping("/logout")
    public String logout(
            @RequestParam(Const.REDIRECT_URI) String redirectUri,
            @RequestParam(Const.TOKEN) String token,
            Model model, HttpServletRequest request, HttpServletResponse response) {
        if (sessionManager.verification(request, response, token)) {
            sessionManager.remove(token);
        }
        model.addAttribute(Const.REDIRECT_URI, redirectUri);
        return "forward:/sso/toLogin";
    }

    private boolean checkCode(String st,
                              HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        AuthorizationCode verification = codeManager.verification(st);
        if (verification != null) {
            String token = sessionManager.create(verification.getUser());
            CookieUtils.addCookie(request, response, Const.TOKEN, token, "/");
            return true;
        }
        return false;
    }

    private String getRedirect(String redirectUri, String code) throws UnsupportedEncodingException {
        StringBuilder sbf = new StringBuilder("redirect:");
        sbf.append(redirectUri);
        if (StringUtils.hasLength(code)) {
            if (redirectUri.contains("?")) {
                sbf.append("&");
            } else {
                sbf.append("?");
            }
            sbf.append(Const.ST).append("=").append(code);
        }
        return URLDecoder.decode(sbf.toString(), "utf-8");
    }

    private String getRedirect(String redirectUri) throws UnsupportedEncodingException {
        return getRedirect(redirectUri, null);
    }

}
