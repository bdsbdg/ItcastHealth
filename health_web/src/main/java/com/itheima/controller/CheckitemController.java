package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.itheima.constant.MessageConstant;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/checkitem")
public class CheckitemController {


    @Reference
    private CheckitemService checkitemService;

    /**
     * 查询所有checkitem
     * @return
     */
    @ResponseBody
    @GetMapping("/find/all")
    public Result findAll(){
            List<CheckItem> checkItemList = checkitemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
    }

    /**
     * 添加checkitem
     * @param checkItem
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @PostMapping("/add")
    public Result addCheckitem(@Validated @RequestBody CheckItem checkItem){
            checkitemService.addCheckitem(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询checkitem
     * @param queryPageBean
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @PostMapping("/find/page")
    public Result findPage(@Validated @RequestBody QueryPageBean queryPageBean){
        System.out.println(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkitemService.findPage(queryPageBean));
    }

    /**
     * 删除checkitem 存在关联的检查组时抛出ServiceException
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public Result deleteCheckitem(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        System.out.println("delete:"+id);
        int count = checkitemService.deleteById(id);
        if (count!=0){
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }
        return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
    }

    /**
     * 查询一个 By id
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/find/{id}")
    public Result findCheckitem(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkitemService.findById(id));
    }

    /**
     * 修改一个 By id  修改失败抛出ServiceException
     * @param checkItem
     * @return
     */
    @ResponseBody
    @PutMapping("/set")
    public Result setCheckitem(@Validated @RequestBody CheckItem checkItem){
//        System.out.println(checkItem);
        checkitemService.setCheckitem(checkItem);
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

}
