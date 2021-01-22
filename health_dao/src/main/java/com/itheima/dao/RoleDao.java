package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface RoleDao {
    Page<Role> findPage(@Param("queryParam") String queryParam);

    void addRole(Role role);

    Role findById(Integer id);

    Collection<Role> findByUserId(Integer id);

    void setRole(Role role);

    void deleteRoleById(Integer id);

    List<Role> findAll();

    long getIdInTableCount(Set<Integer> idSet);
}
