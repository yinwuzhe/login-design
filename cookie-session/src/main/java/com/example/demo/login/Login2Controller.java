package com.example.demo.login;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.jws.WebParam.Mode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 明文的user,cookie设置
 */
@RestController
public class Login2Controller {

    @RequestMapping("/admin")
    public String admin(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        String username = cookieMap.get("user");
        if ( username== null) {
            response.sendRedirect("/login2.html");
        } else {
            return "Welcome to Admin, " + username;
        }
        return "";
    }

    @PostMapping("/login2")
    public void login(@RequestParam String username, @RequestParam String password,
            HttpServletResponse response) throws IOException {

        if(Utils.authenticate(username,password)) {
            // Login successful, set cookie
            Cookie cookie = new Cookie("user", username);
            cookie.setMaxAge(36000); // 过期时间1小时
            cookie.setPath("/admin");
            response.addCookie(cookie);
            // Redirect to original program with 302 status code
            response.sendRedirect("/admin");
        } else {
            // Login failed, show error message
            response.getWriter().println("Invalid username or password");
        }
    }

}