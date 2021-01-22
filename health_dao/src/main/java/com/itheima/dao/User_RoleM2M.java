package com.itheima.dao;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface User_RoleM2M {
    long findCountByRoleId(Integer id);

    void addRoles4User(@Param("roleIds") Set<Integer> itemIds, @Param("userId") int uid);

    List<Integer> findRoleIdsByUserId(Integer id);

    void clearBindByUser(Integer id);


    List<User> findUsersByRoleId(Integer id);
}
