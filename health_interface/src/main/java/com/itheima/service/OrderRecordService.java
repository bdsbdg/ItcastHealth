package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.Order;

import java.util.Map;

public interface OrderRecordService {
    Order addOrder(Map<String, String> orderInfo) throws ServiceException;

    Map findOrderRecordById(Integer id);
}
