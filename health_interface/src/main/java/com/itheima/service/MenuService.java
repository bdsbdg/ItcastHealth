package com.itheima.service;

import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.Menu;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface MenuService {
    List<Menu> findAll();

    void addLevel1(Menu menu);

    void addLevel2(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenu(Integer id) throws ServiceException;

    Menu findMenuById(Integer id);

    Object findPage(QueryPageBean queryPageBean);

    LinkedList<Menu> hasMenuIds(Set<Integer> ids);

    List<Menu> findAllParent();

    List<Integer> findMenusIdByRoleIdM2M(Integer id);
}
