package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderDao;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    /**
     * 批量导入
     * @param settingList
     */
    @Override
    public void addSettings(List<OrderSetting> settingList) {
        // 多个设置
        List<OrderSetting> addList = new ArrayList<>();
        List<OrderSetting> updateList = new ArrayList<>();
        // 查出该list中有的date
            settingList.forEach(setting->{
                // TODO 做date in(date1,date2...)查询[]
                OrderSetting inDbSetting = orderDao.findByDate(setting);

                if (inDbSetting==null){
                    // 未查出  添加
                    addList.add(setting);
                }else {
                    // 查出 判断查出来的ordersetting中的已预约人数是否超过要添加的最大预约人数
                    if (inDbSetting.getReservations() > setting.getNumber()){
                        // 是 抛出
                        throw new ServiceException("要添加的最大预约人数『小于』已预约人数!");
                    }else {
                        if (inDbSetting.getNumber()!=setting.getNumber()){
                            // 否 且与数据库中保存的不一样时 更新最大预约人数
                            setting.setId(inDbSetting.getId());
                            updateList.add(setting);
                        }
                    }
                }
            });

        if (addList.size()>0){
            orderDao.addOrderSettings(addList);
        }
        if (updateList.size()>0){
            orderDao.updateOrderSettings(updateList);
        }

    }

    /**
     * 查询一个月的
     * @param date
     * @return
     */
    @Override
    public List<Map> findOrderByMonth(String date) {
        return orderDao.findOrderByMonth(date);
    }

    /**
     * 设置单个
     * @param orderSetting
     */
    @Override
    public void setOrderByDate(OrderSetting orderSetting) {
        OrderSetting inDbOrdersetting = orderDao.findByOrderSetting(orderSetting);
        if (inDbOrdersetting==null){
            orderDao.addOrderSetting(orderSetting);
        }else {
            if (inDbOrdersetting.getReservations()>orderSetting.getNumber()){
                throw new ServiceException("修改失败,最大预约人数『小于』已预约人数!");
            }
            orderSetting.setId(inDbOrdersetting.getId());
            orderDao.setOrderById(orderSetting);
        }
    }


}
