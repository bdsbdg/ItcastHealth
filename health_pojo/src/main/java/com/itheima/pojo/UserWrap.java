package com.itheima.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserWrap extends User {

    private com.itheima.pojo.User  loginUser;

    public UserWrap(String username, String password, Collection<? extends GrantedAuthority> authorities, com.itheima.pojo.User loginUser) {
        super(username, password, authorities);
        this.loginUser = loginUser;
    }

    public UserWrap(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public com.itheima.pojo.User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(com.itheima.pojo.User loginUser) {
        this.loginUser = loginUser;
    }
}
