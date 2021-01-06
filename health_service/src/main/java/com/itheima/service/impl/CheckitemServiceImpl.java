package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.CheckitemDao;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CheckitemServiceImpl implements CheckitemService {

    @Autowired
    private CheckitemDao checkitemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkitemDao.findAll();
    }

    @Override
    public long allCount() {
        return checkitemDao.allCount();
    }

    @Override
    public int addCheckitem(CheckItem checkItem) {
        return checkitemDao.addCheckitem(checkItem);
    }
}
