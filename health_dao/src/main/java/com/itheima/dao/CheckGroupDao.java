package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

public interface CheckGroupDao {
    Page<CheckGroup> findPage(@Param("queryParam") String queryParam);


    void addCheckGroup(CheckGroup checkGroup);

    CheckGroup findById(Integer id);

    void setCheckGroup(CheckGroup checkGroup);

    void deleteCheckGroupById(Integer id);
}
