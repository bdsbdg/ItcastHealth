package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface UserAdminDao {
    Page<User> findPage(@Param("queryParam") String queryParam);

    void addUser(User user);

    User findById(Integer id);

    String findNameById(Integer id);

    void setUser(User user);

    void deleteUserById(Integer id);

    List<User> findAll();

    long getIdInTableCount(Set<Integer> idSet);

    Long findByName(String username);

    User getPasswordByUsername(String username);

    User findUserByName(String username);

    void setPasswordById(User user);
}
