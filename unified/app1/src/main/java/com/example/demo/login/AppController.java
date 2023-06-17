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
public class AppController {
    RedisSessionManager sessionManager=new RedisSessionManager();
    @Value("${app.home:/}")
    String home;

    @Value("${auth.loginUrl:/}")
    String loginUrl;


    @RequestMapping("/")
    public String home(HttpServletRequest request,HttpServletResponse response) throws IOException {

        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        String mysession = cookieMap.get("unified_session_id");


        if ( mysession== null) {
            response.sendRedirect(loginUrl+"?from="+home);
        } else {
            String username = sessionManager.getSession(mysession).getUsername();
            System.out.println("username = " + username);
            return String.format(Utils.welcome,"App1 欢迎你， " + username);
        }
        return "";
    }



}