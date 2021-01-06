package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface CheckitemDao {

    List<CheckItem> findAll();

    long allCount();

    int addCheckitem(CheckItem checkItem);

    Page<CheckItem> findPage(@Param("queryParam") String queryParam);

    int deleteById(int id);

    long findCheckitemFromM2M(int CheckitemId);

    CheckItem findById(int id);

    int setCheckitem(CheckItem checkItem);

    long getIdInTableCount(Set<Integer> ids);
}
