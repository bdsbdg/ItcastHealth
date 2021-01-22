package com.itheima.controller;

import com.itheima.entity.Result;
import com.itheima.pojo.UserWrap;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class UserController {

    @ResponseBody
    @RequestMapping("/success")
    public Result loginSuccess(HttpServletRequest request){
//        UserWrap userDetails = (UserWrap) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
//        System.out.println(userDetails.getMenu());
//        System.out.println(userDetails);
//        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Object user = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("--------------------");
//        System.out.println(user);

//        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
//        UserWrap userWrap = (UserWrap) securityContextImpl.getAuthentication().getPrincipal();
//        System.out.println(userWrap);
        return new Result(true,"登录成功");
    }

    @ResponseBody
    @RequestMapping("/fail")
    public Result loginFail(){
        return new Result(false,"登录失败,用户名或密码错误!");
    }



}
