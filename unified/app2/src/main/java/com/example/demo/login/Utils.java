package com.example.demo.login;

import java.util.HashMap;

public class Utils {
    public static String welcome="<!DOCTYPE html>\n"
            + "<html>\n"
            +"<style>\n"
            + "</style>\n"
            + "<body>\n"
            + "    <p>%s</p>\n"
            + "    <iframe src=\"https://juejin.cn/book/7209116225988165667/section/7220039574544711691\" frameborder=\"0\""
            + " height=\"1000\" width=\"1000\"></iframe>\n"
            + "</body>\n"
            + "</html>";
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

}
