package com.itheima.controller;

import com.itheima.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class UserController {

    @ResponseBody
    @RequestMapping("/success")
    public Result loginSuccess(){
        return new Result(true,"登录成功");
    }

    @ResponseBody
    @RequestMapping("/fail")
    public Result loginFail(){
        return new Result(false,"登录失败,用户名或密码错误!");
    }
}
