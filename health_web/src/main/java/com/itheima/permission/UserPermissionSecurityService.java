package com.itheima.permission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserPermissionService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import sun.plugin.javascript.navig.LinkArray;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

@Validated
@Component("UserPermissionSecurityService")
public class UserPermissionSecurityService implements UserDetailsService {
    @Reference
    private UserPermissionService userPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("UserPermissionSecurityService:" + username);
        com.itheima.pojo.User loginUser = userPermissionService.findUserByName(username);
        System.out.println(loginUser);
        if (loginUser==null){
            throw new ServiceException("用户名或密码错误!");
        }
        // 创建权限用户对象
        LinkedList<GrantedAuthority> permissionList = new LinkedList<>();
        if (loginUser.getRoles()==null){
            return null;
        }
        Collection<Role> roleSet = loginUser.getRoles();
        for (Role role : roleSet) {
            // 添加角色key
            permissionList.add(new SimpleGrantedAuthority(role.getKeyword()));
            if (role.getPermissions()==null){
                continue;
            }
            // 添加权限key
            Collection<Permission> permissionSet = role.getPermissions();
            for (Permission permission : permissionSet) {
                permissionList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        return new User(username, loginUser.getPassword(),permissionList);
    }
}
