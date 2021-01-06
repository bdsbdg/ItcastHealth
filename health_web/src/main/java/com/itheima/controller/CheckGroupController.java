package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @ResponseBody
    @PostMapping("/find/page")
    public Result findPage(@Validated @RequestBody QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkGroupService.findPage(queryPageBean));
    }

    /**
     * 添加CheckGroup 添加前check Checkitem对应id是否存在
     * @param checkGroup
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public Result addCheckGroup(@Validated @RequestBody CheckGroup checkGroup){
        System.out.println(checkGroup);
        checkGroupService.addCheckGroup(checkGroup);
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @ResponseBody
    @GetMapping("/find/{id}")
    public Result findCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1") Integer id){
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkGroupService.findById(id));
    }

}
