package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderRecordDao;
import com.itheima.entity.Result;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderRecordService.class)
public class OrderRecordImpl implements OrderRecordService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderRecordDao orderRecordDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Order addOrder(Map<String, String> orderInfo) {
        // 处理时间
        String orderDateStr = orderInfo.get("orderDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = null;
        try {
            orderDate = sdf.parse(orderDateStr);
        } catch (ParseException e) {
            throw new ServiceException("预约日期格式不正确");
        }
        // 创建订单
        Order order = new Order();
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        order.setOrderDate(orderDate);
        order.setOrderStatus(Order.ORDERSTATUS_NO);

        // 根据手机号获取会员id
        Member member = memberDao.findByTelephone(orderInfo.get("telephone"));
        if (member==null){
            // 无 创建一个
            member = new Member();
            member.setPhoneNumber(orderInfo.get("telephone"));
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            member.setIdCard(orderInfo.get("idCard"));
            memberDao.add(member);
            order.setMemberId(member.getId());
        }else {
            // 有 查看order中是否已存在setmealid 存在为重复预约
            order.setMemberId(member.getId());
            List<Order> orderList = orderRecordDao.findByCondition(order);
            if (orderList!=null && orderList.size()>0){
                throw new ServiceException("已于"+ orderDateStr +"预约体检");
            }
        }

        // 判断预约日期是否超出可预约人数
        int count = orderDao.addReservationsByOrderDate(orderDate);
        if(count == 0){
            throw new ServiceException("所选日期预约已满，请选其它日期");
        }

//        OrderSetting orderSetting = new OrderSetting();
//        orderSetting.setOrderDate(orderDate);
//        OrderSetting orderSettingByDate = orderDao.findByDate(orderSetting);
//        if (orderSettingByDate.getReservations()>=orderSettingByDate.getNumber()){
//            throw new ServiceException(orderDateStr+"预约人数已满!");
//        }
        // 添加预约记录
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        orderRecordDao.add(order);
        return order;
    }

    @Override
    public Map findOrderRecordById(Integer id) {
        return orderRecordDao.findById4Detail(id);
    }
}
