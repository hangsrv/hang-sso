package com.hang.sso.controller;

import com.hang.sso.constant.SsoConstant;
import com.hang.sso.management.CodeManager;
import com.hang.sso.management.SessionManager;
import com.hang.sso.model.Result;
import com.hang.sso.model.User;
import com.hang.sso.service.UserService;
import com.hang.sso.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Value("${sso.remote.port}")
    private Integer remotePort;

    @GetMapping("/toLogin")
    public String doLogin(
            @RequestParam(SsoConstant.REDIRECT_URI) String redirectUri,
            @RequestParam(value = SsoConstant.ST_REQ_PREFIX, required = false) String st,
            HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String checkResult = checkCodeAndToken(redirectUri, st, request);
        if (checkResult != null) return checkResult;
        model.addAttribute(SsoConstant.REDIRECT_URI, redirectUri);
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(
            @RequestParam(SsoConstant.USERNAME) String username,
            @RequestParam(SsoConstant.PASSWORD) String password,
            @RequestParam(SsoConstant.REDIRECT_URI) String redirectUri,
            HttpServletRequest request, HttpServletResponse response, RedirectAttributes model) throws UnsupportedEncodingException {
        if (!StringUtils.hasLength(redirectUri)) {
            model.addAttribute("errmsg", "页面丢失，请返回重新原页面访问！");
            return "login";
        }
        Result<User> loginResult = userService.login(username, password);
        if (!loginResult.isSuccess()) {
            model.addAttribute("errmsg", loginResult.getMessage());
            return "login";
        }
        String code = codeManager.create(loginResult.getData().getUserId());
        model.addFlashAttribute("userInfo", loginResult.getData());
        return getRedirect(redirectUri, code);
    }

    private String checkCodeAndToken(String redirectUri, String st, HttpServletRequest request) throws UnsupportedEncodingException {
        // 存在code
        if (StringUtils.hasLength(st) && codeManager.verification(st)) {
            String token = sessionManager.create(request.getRemoteHost(), remotePort);
            return "redirect:" + getRedirect(redirectUri, token);
        }
        // 存在token
        if (sessionManager.verification(CookieUtils.getCookie(request, SsoConstant.TOKEN_PREFIX))) {
            // 重定向相同uri，保留cookie信息
            return "redirect:" + redirectUri;
        }
        return null;
    }

    private String getRedirect(String redirectUri, String token) throws UnsupportedEncodingException {
        StringBuilder sbf = new StringBuilder("redirect:");
        sbf.append(redirectUri);
        if (redirectUri.contains("?")) {
            sbf.append("&");
        } else {
            sbf.append("?");
        }
        sbf.append(SsoConstant.ST_REQ_PREFIX).append("=").append(token);
        return URLDecoder.decode(sbf.toString(), "utf-8");
    }

}
