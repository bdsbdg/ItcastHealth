package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.Count;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void addCheckGroup(Setmeal setmeal);

    Page<Setmeal> findPage(@Param("queryParam") String queryParam);

    Setmeal findById(Integer id);

    void setCheckGroup(Setmeal setmeal);

    void deleteSetmealById(Integer id);

    String getImg(Integer id);

    List<Setmeal> findAll();


    List<Map<String, Object>> findSetmeal4Gropu();


    Setmeal findDetailById(Integer id);

    Map<String, Object> findBusinessData(Map<String,Object> queryParam);

    List<Map<String,Object>> findSetmealHot();
}
