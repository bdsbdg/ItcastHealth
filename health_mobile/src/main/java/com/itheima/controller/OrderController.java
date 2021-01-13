package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderRecordService orderRecordService;

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @ResponseBody
    @PostMapping("/submit")
    public Result submitOrder(@RequestBody Map<String,String> orderInfo){
        //        idCard: "身份证"
        //        name: "体检人名"
        //        orderDate: "2021-01-20"
        //        setmealId: "18"
        //        sex: "1"
        //        telephone: "手机号"
        //        validateCode: "671728"

        // 验证验证码
        // 是否是会员 不是就注册 获取会员id
        // 返回预约信息
        ShardedJedis jedisConn = shardedJedisPool.getResource();
        try {
            String _validateCode = jedisConn.get(MessageConstant.SET_VERIFY_CODE_HEDER + orderInfo.get("telephone"));
            if (!(_validateCode!=null && _validateCode.equals(orderInfo.get("validateCode")))){
                // 验证码错误
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            // 删除验证码
            jedisConn.del(MessageConstant.SET_VERIFY_CODE_HEDER + orderInfo.get("telephone"));
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }finally {
            jedisConn.close();
            System.out.println();
        }
        Order order = orderRecordService.addOrder(orderInfo);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @GetMapping("/findById")
    public Result findOrderRecordById(Integer id){
        return new Result(true,MessageConstant.ORDER_SUCCESS,orderRecordService.findOrderRecordById(id));
    }
}
