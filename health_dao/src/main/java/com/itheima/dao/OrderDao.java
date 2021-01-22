package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
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

    int addReservationsByOrderDate(Date orderDate);

    /**
     * 根据日期删除预约设置历史数据
     * @param lastMothStr
     * @return
     */
    int deleteHistoricalDataByDate(String lastMothStr);
}
