package com.itheima.service;

import com.itheima.exception.ServiceException;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {
    void addSettings(List<OrderSetting> settingList) throws ServiceException;

    List<Map> findOrderByMonth(String date);

    void setOrderByDate(OrderSetting orderSetting) throws ServiceException;

    int deleteHistoricalDataByDate(String lastMothStr);
}
