package com.example.demo.login;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Utils {

    public static boolean authenticate(String username, String password) {
        System.out.println("username  = " + username +",password = " + password);
        HashMap<Object, Object> users = new HashMap<>();
        users.put("user1","pass1");
        users.put("user2","pass2");
        users.put("manager1","manager1");
        users.put("manager2","manager2");
        users.put("圆规","hello");
        if (users.get(username)!=null &&users.get(username).equals(password)){
            return true;
        }
        return false;
    }

    //Urls
    //账号密码表里存储
    static String getSecretKey(String urlString) throws MalformedURLException {
        HashMap<String, String> keys = new HashMap<>();

        URL url = new URL(urlString);
        String s = url.getPort() == -1 ? "" : (":" + url.getPort());
        String domain = url.getHost()+ s;


        keys.put("localhost:7080","key1");
        keys.put("app1.yuangui.info","key1");
        keys.put("jwt-app1.yuangui.info","key1");

        keys.put("localhost:7070","key1");
        keys.put("app2.yuangui.info","key1");
        keys.put("jwt-app2.yuangui.info","key1");

        return keys.get(domain);


    }

}
