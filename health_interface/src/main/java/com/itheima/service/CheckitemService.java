package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckitemService {

    List<CheckItem> findAll();

    long allCount();

    int addCheckitem(CheckItem checkItem);

    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    int deleteById (int id) throws ServiceException;

    CheckItem findById(int id)  throws ServiceException;

    void setCheckitem(CheckItem checkItem) throws ServiceException;

    boolean hasCheckitemIds(List<Integer> checkitemIds);
}
