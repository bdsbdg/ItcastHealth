package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void addSetmeal(Setmeal setmeal);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    void setSetmeal(Setmeal setmeal);

    void deleteSetmealById(Integer id);

    List<Setmeal> findAll();

    Setmeal findDetailById(Integer id);

    List<Map<String, Object>> findSetmeal4Gropu();

    Map<String, Object> findBusinessData();
}
