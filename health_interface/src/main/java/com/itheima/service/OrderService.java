package com.itheima.service;

import com.itheima.exception.ServiceException;
import com.itheima.pojo.OrderSetting;

import java.util.List;

public interface OrderService {
    void addSettings(List<OrderSetting> settingList) throws ServiceException;
}
