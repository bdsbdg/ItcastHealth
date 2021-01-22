package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.exception.ServiceException;
import com.itheima.permission.SessionManger;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.pojo.UserWrap;
import com.itheima.service.RoleService;
import com.itheima.service.UserAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/user")
public class UserAmdinController {
    @Reference
    private UserAdminService userAdminService;


    /**
     * 查询所有checkgroup
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('USER_QUERY')")
    @GetMapping("/find/all")
    public Result findAll(){
        List<User> checkGroupList = userAdminService.findAll();
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,checkGroupList);
    }


    @ResponseBody
    @PreAuthorize("hasAuthority('USER_QUERY')")
    @PostMapping("/find/page")
    public Result findPage(@Validated @RequestBody QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, userAdminService.findPage(queryPageBean));
    }

    /**
     * 添加CheckGroup 添加前check Checkitem对应id是否存在
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('USER_ADD')")
    @PostMapping("/add")
    public Result addCheckGroup(@Validated @RequestBody User user){
//        System.out.println(role);
        if (StringUtils.isEmpty(user.getPassword())){
            return new Result(false, "密码不能为空!");
        }
        if (StringUtils.isEmpty(user.getUsername())){
            return new Result(false, "账号名不能为空!");
        }
        userAdminService.addUser(user);
        return new Result(true,MessageConstant.ADD_USER_SUCCESS);
    }

    /**
     * 查出组及组下item的id
     * @param id
     * @return
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('USER_QUERY')")
    @GetMapping("/find/{id}")
    public Result findCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1") Integer id){
        User user = userAdminService.findById(id);
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority('USER_EDIT')")
    @PutMapping("/set")
    public Result setCheckGroup(@Validated @RequestBody User user,HttpServletRequest request){
//        System.out.println(role);
        if (StringUtils.isEmpty(user.getUsername())){
            return new Result(false, "账号名不能为空!");
        }
        Map<String,Object> data = userAdminService.setUser(user);
        User newUser = (User) data.get("user");
        String rawName = (String) data.get("rawName");
        // 刷新权限
        SessionManger sessionManger = SessionManger.getInstance();
        if (newUser.getRoles()==null){
            sessionManger.invalidateSession(rawName);
            return new Result(true, MessageConstant.UPDATE_USER_SUCCESS);
        }
        Collection<Role> roleSet = newUser.getRoles();
        LinkedList<GrantedAuthority> permissionList = new LinkedList<>();
        for (Role role : roleSet) {
            // 添加角色key
            permissionList.add(new SimpleGrantedAuthority(role.getKeyword()));
            if (role.getPermissions()==null){
                continue;
            }
            // 添加权限key
            Collection<Permission> permissionSet = role.getPermissions();
            for (Permission permission : permissionSet) {
                if (!StringUtils.isEmpty(permission.getKeyword())) {
                    permissionList.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }
        }
        // 获取修改的权限对象
        HttpSession session = sessionManger.getSession(rawName);
        if (session!=null) {
            SecurityContextImpl security_context = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
            // 需要修改权限的用户
            Authentication auth = security_context.getAuthentication();
            UserWrap userWrap = (UserWrap)auth.getPrincipal();
            userWrap.setLoginUser(newUser);

            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(userWrap, auth.getCredentials(), permissionList);

            sessionManger.delSession(rawName);
            sessionManger.addSession(newUser.getUsername(),session);
            security_context.setAuthentication(newAuth);
//            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        return new Result(true, MessageConstant.UPDATE_USER_SUCCESS);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/delete/{id}")
    public Result deleteCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        String username = userAdminService.deleteUserById(id);
        SessionManger.getInstance().invalidateSession(username);
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }

    @ResponseBody
    @PostMapping("/set/password")
    public Result setPassword(@RequestBody Map<String,String> map,HttpServletRequest request){
        if (map.get("oldPassword")==null || map.get("newPassword")==null || map.get("checkPassword")==null){
            return new Result(false, "不能有空密码!");
        }
        if (!map.get("newPassword").equals(map.get("checkPassword"))){
            return new Result(false, "两次输入的密码不一致!");
        }
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        UserWrap userWrap = (UserWrap) securityContextImpl.getAuthentication().getPrincipal();

       userAdminService.setPassword(userWrap.getUsername(),map.get("oldPassword"),map.get("newPassword"));
       SessionManger.getInstance().invalidateSession(userWrap.getUsername());
       return new Result(true, "修改密码成功");
    }


//    /**
//     * 添加角色时查询所有权限和父菜单
//     * 添加菜单应该为添加子菜单自动加入其对应父菜单
//     * 单独添加父菜单并没有什么用
//     * @return
//     */
//    @GetMapping("/pm/all")
//    public Result findPermissionMenu(){
//        return new Result(true, MessageConstant.QUERY_PM_SUCCESS,userAdminService.findPMAll());
//    }
}
