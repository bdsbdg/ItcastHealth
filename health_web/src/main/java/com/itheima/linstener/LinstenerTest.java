package com.itheima.linstener;

import com.itheima.permission.SessionManger;
import com.itheima.pojo.UserWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class LinstenerTest implements ApplicationListener<HttpSessionCreatedEvent> {

    @Override
    public void onApplicationEvent(HttpSessionCreatedEvent event) {
        log.info("新建session:{}", event.getSession().getId());
        try {

            // 保存 session
            // 可能会为空
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            User user = null;
            if (authentication!=null){
                Object principal = authentication.getPrincipal();
                if (principal!=null){
                    user =(User)principal;
                }
            }
//            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SessionManger sessionManger = SessionManger.getInstance();
            if (user==null){
                // 未登录 存sessionid
                sessionManger.addSession(event.getSession().getId(),event.getSession());
            }
            else{
                // System.out.println(event.getSession());
                // 判断重复登录
                HttpSession hasSession = sessionManger.getSession(user.getUsername());
                if (hasSession!=null) {
                    hasSession.invalidate();
                }
                sessionManger.addSession(user.getUsername(),event.getSession());
            }
//

//            SecurityContextImpl securityContextImpl = (SecurityContextImpl)event.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
//            UserWrap userWrap = (UserWrap) securityContextImpl.getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.info(String.format("添加session:[%s]出现异常.", event.getSession().getId()), e);
        }
    }
}
