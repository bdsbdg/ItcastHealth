package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll();

    long allCount();

    int addPermission(Permission permission);

    PageResult<Permission> findPage(QueryPageBean queryPageBean);

    int deleteById (int id) throws ServiceException;

    Permission findById(int id)  throws ServiceException;

    void setPermission(Permission permission) throws ServiceException;

    boolean hasPermissionIds(List<Integer> permissionIds);
}
