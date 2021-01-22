package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Menu;
import org.apache.ibatis.annotations.Param;
import sun.rmi.runtime.Log;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface MenuDao {
    List<Menu> findAll();

    Page<Menu> findPage(@Param("queryParam") String queryParam);

    void addLevel1(Menu menu);

    void addLevel2(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenu(Integer id);

    Menu findMenuById(Integer id);

    Long haveSon(Integer id);

    LinkedList<Menu> getIdInTableCount(Set<Integer> idSet);

    List<Menu> findAllParent();

}
