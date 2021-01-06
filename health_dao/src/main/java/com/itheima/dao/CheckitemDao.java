package com.itheima.dao;

import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckitemDao {

    List<CheckItem> findAll();

    long allCount();

    int addCheckitem(CheckItem checkItem);
}
