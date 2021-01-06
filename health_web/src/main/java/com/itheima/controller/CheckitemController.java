package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.itheima.constant.MessageConstant;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/checkitem")
public class CheckitemController {


    @Reference
    private CheckitemService checkitemService;

    @ResponseBody
    @GetMapping("/find/all")
    public Result findAll(){
            List<CheckItem> checkItemList = checkitemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
    }

    @ResponseBody
    @PostMapping("/add")
    public Result addCheckitem(@Validated @RequestBody CheckItem checkItem){
            checkitemService.addCheckitem(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

}
