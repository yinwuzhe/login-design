package com.example.demo.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class SessionManager {
    private static HashMap<String, Session> sessions = new HashMap<>();
    public static Session createSession(String username, int expirationTime) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, username, expirationTime);
        sessions.put(sessionId, session);
        return session;
    }
    public static Session getSession(String sessionId) {
        Session session = sessions.get(sessionId);
        if (session != null && session.isValid()) {
            return session;
        } else {
            return null;
        }
    }
    public static class Session {
        private String sessionId;
        private String username;
        private long expirationTime;
        public Session(String sessionId, String username, int expirationTime) {
            this.sessionId = sessionId;
            this.username = username;
            this.expirationTime = System.currentTimeMillis() + expirationTime * 1000;
        }
        public String getSessionId() {
            return sessionId;
        }
        public String getUsername() {
            return username;
        }
        public boolean isValid() {
            return System.currentTimeMillis() < expirationTime;
        }
    }
}

