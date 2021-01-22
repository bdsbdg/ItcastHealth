package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleService {
    PageResult<Role> findPage(QueryPageBean queryPageBean);

    void addRole(Role role) throws ServiceException;

    Role findById(Integer id);

    List<User> setRole(Role role);

    void deleteRoleById(Integer id) throws ServiceException;

    List<Role> findAll();

    boolean hasRoleIds(Set<Integer> checkGroupsId);

    Map<String, Object> findPMAll();
}
