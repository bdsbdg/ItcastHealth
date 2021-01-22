package com.itheima.dao;

import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.Set;

public interface UserPermissionDao {
    User findUserByName(String username);
}
