package com.example.demo.login;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
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
 * HttpSession用户登录演示
 */
@RestController
public class LoginController {

    @RequestMapping("/")
    public String home(HttpSession session, HttpServletResponse response) throws IOException {
        // Check if the user is logged in
        Object username = session.getAttribute("username");
        System.out.println("username = " + username);

        if (username == null) {
            // Redirect to the login page
            response.sendRedirect("/login.html");
        } else {
            return "Hello " + username;
        }
        return "";
    }


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password,HttpSession session) {
        // Authenticate user credentials here
        boolean authenticated = Utils.authenticate(username, password);
        if (authenticated) {
            System.out.println("authenticated = " + authenticated);
            session.setAttribute("username",username);
//            session.setMaxInactiveInterval(3600);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }



}