package com.itheima.service;

import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserAdminService {
    List<User> findAll();

    Object findPage(QueryPageBean queryPageBean);

    void addUser(User user)throws ServiceException;

    Map<String,Object> setUser(User user) throws ServiceException;

    String deleteUserById(Integer id);

    User findById(Integer id);

    void setPassword(String username,String oldPassword, String newPassword) throws ServiceException;
}
