package com.example.demo.login;

import java.util.HashMap;

public class Utils {
    public static boolean authenticate(String username, String password) {
        System.out.println("username+  = " + username +",password = " + password);
        HashMap<Object, Object> users = new HashMap<>();
        users.put("user1","pass1");
        users.put("user2","pass2");
        users.put("manager1","manager1");
        users.put("manager2","manager2");
        if (users.get(username)!=null &&users.get(username).equals(password)){
            return true;
        }
        return false;
    }

}
