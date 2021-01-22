package com.itheima.dao;

import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

public interface Role_menuM2M {
    void addMenus4Role(@Param("menuIds")HashSet<Integer> integers, @Param("roleId")Integer id);

    void clearBindByRole(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);

    List<Integer> findMenusIdByRoleIdM2M(Integer id);
}
