package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

public interface SetmealDao {
    void addCheckGroup(Setmeal setmeal);

    Page<Setmeal> findPage(@Param("queryParam") String queryParam);

    Setmeal findById(Integer id);

    void setCheckGroup(Setmeal setmeal);

    void deleteSetmealById(Integer id);

    String getImg(Integer id);
}