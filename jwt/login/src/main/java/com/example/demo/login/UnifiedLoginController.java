package com.example.demo.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
            HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        if (Utils.authenticate(username,password)) {
            String secretKey = Utils.getSecretKey(url);
            if (secretKey==null){
                response.getWriter().println("未注册的域名！");
            }
            Cookie cookie = new Cookie("token", createJwtToken(username,secretKey));
            cookie.setMaxAge(3600); // 1 hour
            cookie.setDomain(domain);
            response.addCookie(cookie);

            response.sendRedirect(url);
        } else {
            response.getWriter().println("Invalid username or password");
        }
    }



    public String createJwtToken(String username,String key) throws NoSuchAlgorithmException, IOException {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date exp = new Date(currentTimeMillis + 3600 * 1000); // 过期时间设为1小时

        PrivateKey privateKey = getPrivateKey();

        String token = Jwts.builder()
                .setSubject("yuangui.info")
                .setIssuedAt(now)
                .claim("userName", username)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.ES256, privateKey)
//                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        System.out.println("token = " + token);

        return token;
    }

    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("privateKey = " + privateKey);
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("publicKey = " + publicKey);
        byte[] publicKeyBytes = publicKey.getEncoded();
        FileOutputStream fos = new FileOutputStream("/Users/ywz/login-design/jwt/publicKeyFile");
        fos.write(publicKeyBytes);
        fos.close();
        return privateKey;
    }


}