package com.itheima.service;

import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckitemService {

    List<CheckItem> findAll();

    long allCount();

    int addCheckitem(CheckItem checkItem);

}
