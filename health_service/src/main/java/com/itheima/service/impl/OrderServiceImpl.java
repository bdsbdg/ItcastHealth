package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderDao;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

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
}
