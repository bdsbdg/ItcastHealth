package com.itheima.linstener;

import com.itheima.permission.SessionManger;
import com.itheima.pojo.UserWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class SessionDestroyedEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {
 
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		log.info("失效session:{}", event.getSession().getId());
		try {
			// 移除session
//			System.out.println(event);
			Object obj = event.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
			Object user = null;
			if (obj!=null){
				SecurityContextImpl securityContextImpl = (SecurityContextImpl) obj;
				Authentication authentication = securityContextImpl.getAuthentication();
				if (authentication!=null){
					user = authentication.getPrincipal();
				}
			}
			if (user!=null){
				SessionManger.getInstance().delSession(((User)user).getUsername());
			}else {
				SessionManger.getInstance().delSession(event.getSession().getId());
			}
		} catch (Exception e) {
			log.error(String.format("失效session:[%s]发生异常.", event.getId()), e);
		}
	}
}