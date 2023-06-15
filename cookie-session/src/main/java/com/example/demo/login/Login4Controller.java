package com.example.demo.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * my RedisSessionManager
 */
//确保redis server能连接的时候再打开这个注解
@RestController
public class Login4Controller {
    RedisSessionManager sessionManager=new RedisSessionManager();

    @PostMapping("/login4")
    public void login(@RequestParam String username, @RequestParam String password,
            HttpServletResponse response) throws IOException {
        if (Utils.authenticate(username,password)) {
            RedisSessionManager.Session session = sessionManager.createSession(username, 3600);
            Cookie cookie = new Cookie("session_id", session.getSessionId());
            cookie.setMaxAge(3600); // 1 hour
            response.addCookie(cookie);
            response.sendRedirect("/redisSession");
        } else {
            response.getWriter().println("Invalid username or password");
        }
    }

    @RequestMapping("/redisSession")
    public String home(HttpServletRequest request,HttpServletResponse response) throws IOException {

        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        String mysession = cookieMap.get("session_id");


        if ( mysession== null) {
            response.sendRedirect("/login4.html");
        } else {
            String username = sessionManager.getSession(mysession).getUsername();
            System.out.println("username = " + username);
            return "Hello " + username;
        }
        return "";
    }



}