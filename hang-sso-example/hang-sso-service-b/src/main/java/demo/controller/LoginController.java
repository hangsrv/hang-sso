package demo.controller;

import com.hang.sso.client.config.LogoutConfig;
import com.hang.sso.client.constant.Const;
import com.hang.sso.client.session.SessionManager;
import com.hang.sso.client.util.ReturnUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/sso")
public class LoginController {

    @GetMapping("/doLogout")
    public void logout(@RequestParam(Const.TOKEN) String token) {
        SessionManager.removeSessionByToken(token);
    }

    @GetMapping("/logout")
    public String ssoLogout(HttpServletRequest request, HttpServletResponse response) {
        return ReturnUtils.getLogoutUrl(request, response,
                new LogoutConfig("http://localhost:8080", "http://localhost:8082/web/index"));
    }

}
