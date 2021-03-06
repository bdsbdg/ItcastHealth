package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    void addCheckGroup(CheckGroup checkGroup) throws ServiceException;

    CheckGroup findById(Integer id);

    void setCheckGroup(CheckGroup checkGroup);

    void deleteCheckGroupById(Integer id);

    List<CheckGroup> findAll();

    boolean hasCheckGroupIds(List<Integer> checkGroupsId);
}
