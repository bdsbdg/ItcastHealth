package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckGroup;

public interface CheckGroupService {
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    void addCheckGroup(CheckGroup checkGroup) throws ServiceException;

    CheckGroup findById(Integer id);
}
