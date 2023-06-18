package com.example.demo.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Date;
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
    @Value("${app.home:/}")
    String home;

    @Value("${auth.loginUrl:/}")
    String loginUrl;

    private static final String SECRET_KEY = "key1";



    @RequestMapping("/")
    public String home(HttpServletRequest request,HttpServletResponse response) throws IOException {

        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        String token = cookieMap.get("token");
        if (token==null){
            response.sendRedirect(loginUrl+"?from="+home);
            return "";

        }
        String userName=null;
        System.out.println("token = " + token);

        userName = decodeToken(token, userName);

        if ( userName== null) {
            response.sendRedirect(loginUrl+"?from="+home);
        } else {
            System.out.println("username = " + userName);
            return String.format(Utils.welcome,"App1 欢迎你， " + userName);
        }
        return "";
    }

    private String decodeToken(String token, String userName) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())){
                System.out.println("expiration已过期：  " + expiration);
                return null;
            }
            String customKey = claims.get("userName", String.class);

            System.out.println("Subject: " + subject);
            System.out.println("Custom key-value: " + customKey);
            userName =customKey;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while decoding JWT token");
        }
        return userName;
    }


}