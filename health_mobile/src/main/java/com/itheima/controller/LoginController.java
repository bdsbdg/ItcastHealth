package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.CommonUtils;
import com.itheima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result loginVerifyCode(@RequestBody Map<String,String> loginInfo, HttpServletResponse res){
//        telephone: ""
//        validateCode: ""
        ShardedJedis jedisConn = shardedJedisPool.getResource();

        try {
            String _validateCode = jedisConn.get(MessageConstant.SET_VERIFY_CODE_LOGIN_HEDER + loginInfo.get("telephone"));
            if (!(_validateCode!=null && _validateCode.equals(loginInfo.get("validateCode")))){
                // 验证码错误
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            // 删除验证码
            jedisConn.del(MessageConstant.SET_VERIFY_CODE_LOGIN_HEDER + loginInfo.get("telephone"));
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }finally {
            jedisConn.close();
        }
        // 注册会员
        Member member = memberService.addMemberByTelephone(loginInfo.get("telephone"));
        Cookie cookie = new Cookie("login_member_telephone",loginInfo.get("telephone"));
        cookie.setMaxAge(30*24*60*60); // 存1个月
        cookie.setPath("/"); // 访问的路径 根路径下时 网站的所有路径 都会发送这个cookie
        res.addCookie(cookie);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @ResponseBody
    @GetMapping("/verifycode")
    public Result getVerifyCode(String telephone){
        // 验证手机号码合法性
        CommonUtils.verifyTelephone(telephone);
        // 生成验证码
        ShardedJedis jedisConn = shardedJedisPool.getResource();
        // 存在不验证
        String telephoneKey = MessageConstant.SET_VERIFY_CODE_LOGIN_HEDER+telephone;
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
