package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    List<Permission> findAll();

    long allCount();

    int addPermission(Permission permission);

    Page<Permission> findPage(@Param("queryParam") String queryParam);

    int deleteById(int id);

    long findPermissionFromM2M(int PermissionId);

    Permission findById(int id);

    int setPermission(Permission permission);

    long getIdInTableCount(Set<Integer> ids);
}
