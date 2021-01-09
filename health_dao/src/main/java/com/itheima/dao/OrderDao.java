package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    OrderSetting findByDate(OrderSetting setting);

    void addOrderSettings(List<OrderSetting> addList);

    void updateOrderSettings(List<OrderSetting> updateList);

    List<Map> findOrderByMonth(String date);

    int setOrderById(OrderSetting orderSetting);

    OrderSetting findByOrderSetting(OrderSetting orderSetting);

    void addOrderSetting(OrderSetting orderSetting);
}
