package com.itheima.dao;

import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

public interface setmealCheckGroupM2M {
    void addChekGroup4Setmeal(@Param("groupIds")HashSet<Integer> integers, @Param("setmaelId")Integer id);

    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    void clearBindBySetmeal(Integer id);

    void addCheckGroups4Setmeal(@Param("groupIds") HashSet<Integer> integers, @Param("setmealId") Integer id);

    long findCheckGroupFromM2M(Integer id);
}
