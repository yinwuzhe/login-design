package com.example.demo.login;

import com.example.demo.login.SessionManager.Session;
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
 * my SessionManager
 */
@RestController
public class Login3Controller {

    @RequestMapping("/mysession")
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
            response.sendRedirect("/login3.html");

        } else {
            String username = SessionManager.getSession(mysession).getUsername();
            System.out.println("username = " + username);
            return "Hello, " + username;
        }
        return "redirect:/login3";
    }

    @PostMapping("/login3")
    public void login(@RequestParam String username, @RequestParam String password,
            HttpServletResponse response) throws IOException {
        if (Utils.authenticate(username,password)) {
            Session session = SessionManager.createSession(username, 3600);
            Cookie cookie = new Cookie("session_id", session.getSessionId());
            cookie.setMaxAge(3600); // 1 hour
            cookie.setPath("/mysession");
            response.addCookie(cookie);
            response.sendRedirect("/mysession");
        } else {
            response.getWriter().println("Invalid username or password");
        }
    }

}