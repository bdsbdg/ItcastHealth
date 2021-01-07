package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 查询所有checkgroup
     * @return
     */
    @ResponseBody
    @GetMapping("/find/all")
    public Result findAll(){
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
    }


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

    /**
     * 查出组及组下item的id
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/find/{id}")
    public Result findCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1") Integer id){
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkGroupService.findById(id));
    }

    @ResponseBody
    @PutMapping("/set")
    public Result setCheckGroup(@Validated @RequestBody CheckGroup checkGroup){
        System.out.println(checkGroup);
        checkGroupService.setCheckGroup(checkGroup);
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public Result deleteCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        checkGroupService.deleteCheckGroupById(id);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

}
