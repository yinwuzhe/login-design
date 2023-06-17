package com.example.demo.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * my RedisSessionManager
 */
//确保redis server能连接的时候再打开这个注解
@RestController
public class UnifiedLoginController {
    RedisSessionManager sessionManager=new RedisSessionManager();
    @Value("${auth.domain:/}")
    String domain;

    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password,@RequestParam(value = "from",required = false) String url,
            HttpServletResponse response) throws IOException {
        if (Utils.authenticate(username,password)) {
            RedisSessionManager.Session session = sessionManager.createSession(username, 3600);
            Cookie cookie = new Cookie("unified_session_id", session.getSessionId());
            cookie.setMaxAge(3600); // 1 hour
            cookie.setDomain(domain);
            response.addCookie(cookie);
            response.sendRedirect(url);
        } else {
            response.getWriter().println("Invalid username or password");
        }
    }





}