package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

public interface SetmealService {
    void addSetmeal(Setmeal setmeal);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    void setSetmeal(Setmeal setmeal);

    void deleteSetmealById(Integer id);

}
