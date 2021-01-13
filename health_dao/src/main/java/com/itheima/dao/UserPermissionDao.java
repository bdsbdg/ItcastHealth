package com.itheima.dao;

import com.itheima.pojo.User;

public interface UserPermissionDao {
    User findUserByName(String username);
}
