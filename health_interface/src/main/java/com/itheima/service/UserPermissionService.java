package com.itheima.service;

import com.itheima.exception.ServiceException;
import com.itheima.pojo.User;

public interface UserPermissionService {

    User findUserByName(String username) throws ServiceException;
}
