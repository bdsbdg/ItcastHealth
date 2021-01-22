package com.itheima.dao;

import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

public interface Role_permissionM2M {
    void addpermissions4Role(@Param("permissionIds")HashSet<Integer> integers, @Param("roleId")Integer id);

    void clearBindByRole(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

}
