package com.itheima.permission;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class SessionManger {
    private static volatile SessionManger instance;
    private HashMap<String, HttpSession> sessionMap;

    private SessionManger() {
        sessionMap = new HashMap<String,HttpSession>();
    }

    public static SessionManger getInstance() {
        if (instance == null) {
            synchronized (SessionManger.class){
                if(instance==null){
                    instance=new SessionManger();
                }
            }
        }
        return instance;
    }

    public synchronized void addSession(String username,HttpSession session) {
        if (session != null) {
            sessionMap.put(username, session);
        }
    }

    public synchronized void delSession(String username) {
        sessionMap.remove(username);
    }

    public synchronized void invalidateSession(String username) {
        HttpSession session = getSession(username);
        if (session!=null){
            session.invalidate();
        }
    }

    public synchronized HttpSession getSession(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return sessionMap.get(username);
    }
}
