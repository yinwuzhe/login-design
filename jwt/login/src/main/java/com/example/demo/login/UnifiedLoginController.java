package com.example.demo.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
public class UnifiedLoginController {
    @Value("${auth.domain:/}")
    String domain;

    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password,@RequestParam(value = "from",required = false) String url,
            HttpServletResponse response) throws IOException {
        if (Utils.authenticate(username,password)) {
            String key = Utils.getSecretKey(url);
            if (key==null){
                response.getWriter().println("未注册的域名！");
            }
//            RedisSessionManager.Session session = sessionManager.createSession(username, 3600);
            Cookie cookie = new Cookie("token", createJwtToken(username,key));
            cookie.setMaxAge(3600); // 1 hour
            cookie.setDomain(domain);
            response.addCookie(cookie);
            //给URL带上token试下
            //是否给每一个登录的domain配置一个token呢？
//            response.setHeader("token",createJwtToken());
            response.sendRedirect(url);
        } else {
            response.getWriter().println("Invalid username or password");
        }
    }



    public String createJwtToken(String username,String key) {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date exp = new Date(currentTimeMillis + 3600 * 1000); // 过期时间设为1小时

        String token = Jwts.builder()
                .setSubject("yuangui.info")
                .setIssuedAt(now)
                .claim("userName", username) // 可以添加其他自定义 key-value 对
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        System.out.println("token = " + token);

        return token;
    }





}