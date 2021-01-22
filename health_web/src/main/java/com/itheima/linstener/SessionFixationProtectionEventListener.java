package com.itheima.linstener;

import com.itheima.permission.SessionManger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

// Spring security 登录成功后，防止session的固化攻击，会将旧的sessionId销毁，重新生成一个新的sessionId,

@Component
class SessionFixationProtectionEventListener implements ApplicationListener<SessionFixationProtectionEvent> {
	@Override
	public void onApplicationEvent(SessionFixationProtectionEvent event) {

//		String oldSessionId = event.getOldSessionId();
		Object source = event.getSource();
		User user = null;
		if (source!=null){
			Authentication authentication = (Authentication) source;
			Object principal = authentication.getPrincipal();
			if (principal!=null){
				user = (User) principal;
			}
		}
		SessionManger sessionManger = SessionManger.getInstance();
		if (user == null){
			// 换userid
			sessionManger.addSession(event.getNewSessionId(),sessionManger.getSession(event.getOldSessionId()));
		}else {
			// 切name
			HttpSession hasSession = sessionManger.getSession(user.getUsername());
			if (hasSession!=null) {
				hasSession.invalidate();
			}
			sessionManger.addSession(user.getUsername(),sessionManger.getSession(event.getOldSessionId()));
			sessionManger.delSession(event.getOldSessionId());
		}


	}
}