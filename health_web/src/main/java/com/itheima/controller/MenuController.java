package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.pojo.UserWrap;
import com.itheima.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    @GetMapping("/user/all")
    public Result getMainMenu(HttpServletRequest request){
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        UserWrap userWrap = (UserWrap) securityContextImpl.getAuthentication().getPrincipal();
        HashMap<Integer, Menu> menuResult = new HashMap<>();
        // 获取菜单
        Set<Role> roles = userWrap.getLoginUser().getRoles();
        for (Role role : roles) {
            LinkedHashSet<Menu> menus = role.getMenus();
            for (Menu menu : menus) {
                if (menuResult.get(menu.getId())!=null){
                    continue;
                }
                menuResult.put(menu.getId(),menu);
            }
        }
        HashMap<String, Object> res = new HashMap<>();
        res.put("menuList",menuResult.values());
        res.put("username",userWrap.getUsername());
        return new Result(true, MessageConstant.GET_MENU_SUCCESS, res);
    }

    @GetMapping("/find/all")
    public Result getMenuALl(){

        return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, menuService.findAll());
    }

    /**
     * 添加一个根菜单
     * @param menu
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_ADD')")
    @PostMapping("/level1/add")
    public Result addMenuOfLevel1(@RequestBody Menu menu){
        menuService.addLevel1(menu);
        return new Result(true, MessageConstant.ADD_MENU_1_SUCCESS);
    }

    /**
     * 添加一个子菜单
     * @param menu
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_ADD')")
    @PostMapping("/level2/add")
    public Result addMenuOfLevel2(@RequestBody Menu menu){
        menuService.addLevel2(menu);
        return new Result(true, MessageConstant.ADD_MENU_2_SUCCESS);
    }

    /**
     * 更新一个
     * @param menu
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_EDIT')")
    @PostMapping("/update")
    public Result updateMenu(@RequestBody Menu menu){
        menuService.updateMenu(menu);
        return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
    }

    /**
     * 按ID删除一个
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_DELETE')")
    @DeleteMapping("/delete/{id}")
    public Result deleteMenu(@PathVariable Integer id){
        menuService.deleteMenu(id);
        return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
    }

    /**
     * 按菜单ID查询一个
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @GetMapping("/find/{id}")
    public Result findMenu(@PathVariable Integer id){
        return new Result(true, MessageConstant.QUERY_MENU_BYID_SUCCESS, menuService.findMenuById(id));
    }

    /**
     * 分页查询菜单
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @PostMapping("/find/page")
    public Result findPage(@Validated @RequestBody QueryPageBean queryPageBean){
//        System.out.println(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, menuService.findPage(queryPageBean));
    }

}
