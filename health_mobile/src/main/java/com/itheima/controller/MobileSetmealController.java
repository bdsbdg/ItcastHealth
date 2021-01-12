package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.CommonUtils;
import com.itheima.utils.QiNiuUtils;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/setmeal")
public class MobileSetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @GetMapping("/all")
    public Result setmealAll(){
        List<Setmeal> setmealList = setmealService.findAll();
        setmealList.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealList);
    }


    @GetMapping("/findDetailById")
    public Result setmealDetail(@Min(value = 1,message = "id不能小于1")Integer id){
        Setmeal setmeal = setmealService.findDetailById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    @GetMapping("/findById")
    public Result setmealInfo(@Min(value = 1,message = "id不能小于1")Integer id){
        Setmeal setmeal = setmealService.findById(id);
//        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    @GetMapping("/verifycode")
    public Result getVerifyCode(String telephone){
//        log.debug("telephone:");
        // 验证手机号码合法性
        CommonUtils.verifyTelephone(telephone);
        // 生成验证码
        ShardedJedis jedisConn = shardedJedisPool.getResource();
        // 存在不验证
        String telephoneKey = MessageConstant.SET_VERIFY_CODE_HEDER+telephone;
        if (jedisConn.exists(telephoneKey)){
            return new Result(false,"已发送验证码，请120秒后重试!");
        }
        String verifyCode = ValidateCodeUtils.generateValidateCode(6).toString();
        System.out.println(verifyCode);
        // 发送短信不写 TODO
//         SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,verifyCode);
        try {
            jedisConn.setex(telephoneKey,120,verifyCode);
        }catch (Exception e){
            log.error(e.getMessage());
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }finally {
            jedisConn.close();
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }
}
