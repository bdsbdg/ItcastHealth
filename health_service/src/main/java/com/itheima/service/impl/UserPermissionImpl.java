package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.UserPermissionDao;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.User;
import com.itheima.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = UserPermissionService.class)
public class UserPermissionImpl implements UserPermissionService{
    @Autowired
    private UserPermissionDao userPermissionDao;

    @Override
    public User findUserByName(String username) throws ServiceException {
        User user = userPermissionDao.findUserByName(username);
        System.out.println(user);
        return user;
    }
}
