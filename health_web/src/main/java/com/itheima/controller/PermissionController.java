package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.CheckitemService;
import com.itheima.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /**
     * 查询所有checkitem
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('PERMISSION_QUERY')")
    @GetMapping("/find/all")
    public Result findAll(){
        List<Permission> checkItemList = permissionService.findAll();
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,checkItemList);
    }

    /**
     * 添加checkitem
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('PERMISSION_ADD')")
    @PostMapping("/add")
    public Result addPermission(@Validated @RequestBody Permission permission){
        permissionService.addPermission(permission);
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    /**
     * 分页查询checkitem
     * @param queryPageBean
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('PERMISSION_QUERY')")
    @PostMapping("/find/page")
    public Result findPage(@Validated @RequestBody QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permissionService.findPage(queryPageBean));
    }

    /**
     * 删除checkitem 存在关联的检查组时抛出ServiceException
     * @param id
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    @DeleteMapping("/delete/{id}")
    public Result deletePermission(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        System.out.println("delete:"+id);
        int count = permissionService.deleteById(id);
        if (count!=0){
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        }
        return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL+"该权限已存在关联角色!");
    }

    /**
     * 查询一个 By id
     * @param id
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('PERMISSION_QUERY')")
    @GetMapping("/find/{id}")
    public Result findPermission(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permissionService.findById(id));
    }

    /**
     * 修改一个 By id  修改失败抛出ServiceException
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('PERMISSION_EDIT')")
    @PostMapping("/update")
    public Result setPermission(@Validated @RequestBody Permission permission){
//        System.out.println(checkItem);
        permissionService.setPermission(permission);
        return new Result(true,MessageConstant.UPDATE_PERMISSION_SUCCESS);
    }
}
