package com.example.demo.login;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.UUID;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisSessionManager {
//    private static HashMap<String, Session> sessions = new HashMap<>();
    JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "::1", 6379);
    Jedis jedis = jedisPool.getResource();

    public  Session createSession(String username, int expirationTime) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, username, expirationTime);

        jedis.set(sessionId,JSON.toJSONString(session));
        jedis.pexpire(sessionId,expirationTime*1000);
        return session;
    }
    public  Session getSession(String sessionId) {
        String s = jedis.get(sessionId);


        if (s != null) {

            return JSON.parseObject(s, Session.class);
        } else {
            return null;
        }
    }
    public static class Session {
        private String sessionId;
        private String username;
        private long expirationTime;
        private long expiration;
        public Session(String sessionId, String username, int expirationTime) {
            this.sessionId = sessionId;
            this.username = username;
            this.expirationTime = expirationTime ;
//            this.expiration = System.currentTimeMillis() + expirationTime * 1000;
        }
        public String getSessionId() {
            return sessionId;
        }
        public String getUsername() {
            return username;
        }
//        public boolean isValid() {
//            return System.currentTimeMillis() < expiration;
//        }
    }
}

