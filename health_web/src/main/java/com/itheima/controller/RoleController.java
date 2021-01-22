package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.permission.SessionManger;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.CheckGroupService;
import com.itheima.service.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    /**
     * 查询所有checkgroup
     * @return
     */
    @ResponseBody
    @GetMapping("/find/all")
    public Result findAll(){
        List<Role> checkGroupList = roleService.findAll();
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,checkGroupList);
    }


    @ResponseBody
    @PostMapping("/find/page")
    public Result findPage(@Validated @RequestBody QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleService.findPage(queryPageBean));
    }

    /**
     * 添加CheckGroup 添加前check Checkitem对应id是否存在
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public Result addCheckGroup(@Validated @RequestBody Role role){
//        System.out.println(role);
        roleService.addRole(role);
        return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
    }

    /**
     * 查出组及组下item的id
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/find/{id}")
    public Result findCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1") Integer id){
        Role role = roleService.findById(id);

        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, role);
    }

    @ResponseBody
    @PutMapping("/set")
    public Result setCheckGroup(@Validated @RequestBody Role role){
//        System.out.println(role);
        List<User> userList = roleService.setRole(role);
        SessionManger sessionManger = SessionManger.getInstance();
        for (User user : userList) {
            sessionManger.invalidateSession(user.getUsername());
        }
        return new Result(true, MessageConstant.UPDATE_ROLE_SUCCESS);
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public Result deleteCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        roleService.deleteRoleById(id);
        return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
    }

    /**
     * 添加角色时查询所有权限和父菜单
     * 添加菜单应该为添加子菜单自动加入其对应父菜单
     * 单独添加父菜单并没有什么用
     * @return
     */
    @GetMapping("/pm/all")
    public Result findPermissionMenu(){
        return new Result(true, MessageConstant.QUERY_PM_SUCCESS,roleService.findPMAll());
    }

}
