package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.List;

public interface OrderDao {
    OrderSetting findByDate(OrderSetting setting);

    void addOrderSettings(List<OrderSetting> addList);

    void updateOrderSettings(List<OrderSetting> updateList);
}
