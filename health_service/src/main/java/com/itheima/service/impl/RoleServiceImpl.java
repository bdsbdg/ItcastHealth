package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.*;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.*;
import com.itheima.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private Role_menuM2M role_menuM2M;
    @Autowired
    private Role_permissionM2M role_permissionM2M;
    @Autowired
    private User_RoleM2M user_roleM2M;

    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
        // 分页拦截器装配limit
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        String queryParam = queryPageBean.getQueryString();
        if(queryParam != null){
            queryParam = queryParam.trim();
            if(!queryParam.equals("")){
                // 非空字符串拼接 %
                queryParam = "%" + queryParam + "%";
            }else {
                queryParam = null;
            }
        }
        // 分页器封装的当前页数据
        Page<Role> page = roleDao.findPage(queryParam);
        System.out.println(page.getResult());
        return new PageResult<Role>(page.getTotal(), page.getResult());
    }

    @Override
    public void addRole(Role role) throws ServiceException {
        HashSet<Integer> idSet = new HashSet<>(role.getMenuIds());
        // 要添加的菜单集合
        LinkedList<Menu> menuList = menuService.hasMenuIds(idSet);
        if (menuList==null) {
            throw new ServiceException("添加了已被删除的菜单!");
        }
        if (!permissionService.hasPermissionIds(role.getPermissionIds())) {
            throw new ServiceException("添加了已被删除的权限!");
        }

        // TODO  事务
        // 添加组
        String roleKeyword = role.getKeyword();
        String[] roleKeywordArr = roleKeyword.split("_");
        if (roleKeywordArr.length<=1){
            // 没有ROLE_
            role.setKeyword("ROLE_"+roleKeyword);
        }else {
            // 取出第一个比较ROLE_
            if (!"ROLE".equalsIgnoreCase(roleKeywordArr[0])){
                role.setKeyword("ROLE_"+roleKeyword);
            }else {
                char[] keywordChars = roleKeyword.toCharArray();
                keywordChars[0] = 'R';
                keywordChars[1] = 'O';
                keywordChars[2] = 'L';
                keywordChars[3] = 'E';
                role.setKeyword(new String(keywordChars));
            }
        }
        roleDao.addRole(role);
//        System.out.println("添加的组id："+checkGroup.getId());

//        int a = 1/0;

        // 添加多对多关系
        if (menuList.size()!=0){
            // 添加时可以不点 item
            // 没有父菜单时添加父菜单关系

            // 已添加过 的菜单id 集合
//            List<Integer> hasMneuIds = menuService.findMenusIdByRoleIdM2M(role.getId());
            HashSet<Integer> add2RM_Ids = new HashSet<>();
            for (Menu menu : menuList) {
                if (menu.getParentMenuId()!=null){
                    // 子菜单 获取父菜单id添加进去 添加父id
                    add2RM_Ids.add(menu.getParentMenuId());
                }
                // 添加自己
                add2RM_Ids.add(menu.getId());
            }
            // 要添加的id中的 已在多对多表中的id
//            if (hasMneuIds!=null){
//                add2RM_Ids.removeAll(hasMneuIds);
//            }
            role_menuM2M.addMenus4Role(add2RM_Ids,role.getId());
        }
        if (role.getPermissionIds().size()!=0){
            // 添加时可以不点 item
            role_permissionM2M.addpermissions4Role(new HashSet<>(role.getPermissionIds()),role.getId());
        }
    }

    @Override
    public Role findById(Integer id) {
        Role role = roleDao.findById(id);
        if (role==null){
//            System.out.println(checkGroup);
            throw new ServiceException("未找到该角色!");
        }
        List<Permission> permissionList = permissionService.findAll();
        if (permissionList!=null){
            role.setPermissions(new HashSet<>(permissionList));
        }
        List<Menu> menuList = menuService.findAll();
        if (menuList!=null){
            role.setMenus(new LinkedHashSet<Menu>(menuList));
        }
        // 选中数据
        role.setPermissionIds(role_permissionM2M.findPermissionIdsByRoleId(role.getId()));
        role.setMenuIds(role_menuM2M.findMenuIdsByRoleId(role.getId()));

        return role;
    }

    @Override
    public List<User> setRole(Role role) {
        // 开启事务
        // 改变group属性
        String roleKeyword = role.getKeyword();
        String[] roleKeywordArr = roleKeyword.split("_");
        if (roleKeywordArr.length<=1){
            // 没有ROLE_
            role.setKeyword("ROLE_"+roleKeyword);
        }else {
            // 取出第一个比较ROLE_
            if (!"ROLE".equalsIgnoreCase(roleKeywordArr[0])){
                role.setKeyword("ROLE_"+roleKeyword);
            }else {
                char[] keywordChars = roleKeyword.toCharArray();
                keywordChars[0] = 'R';
                keywordChars[1] = 'O';
                keywordChars[2] = 'L';
                keywordChars[3] = 'E';
                role.setKeyword(new String(keywordChars));
            }
        }
        roleDao.setRole(role);
        // 查询是否存在已删除的数据
        HashSet<Integer> idSet = new HashSet<>(role.getMenuIds());
        // 要添加的菜单集合
        LinkedList<Menu> menuList = menuService.hasMenuIds(idSet);
        if (menuList==null) {
            throw new ServiceException("修改了已被删除的菜单!");
        }
        if (!permissionService.hasPermissionIds(role.getPermissionIds())) {
            throw new ServiceException("修改了已被删除的权限!");
        }

        // 删除旧多对多关系
        role_menuM2M.clearBindByRole(role.getId());
        role_permissionM2M.clearBindByRole(role.getId());

//        // 添加新多对多关系
//        HashSet<Integer> mIds = new HashSet<>(role.getMenuIds());
//        if (mIds.size()>0){
//            role_menuM2M.addMenus4Role(mIds,role.getId());
//        }
//        HashSet<Integer> pIds = new HashSet<>(role.getPermissionIds());
//        if (pIds.size()>0){
//            role_permissionM2M.addpermissions4Role(pIds,role.getId());
//        }

        if (menuList.size()!=0){
            // 修改时可以不点 item
            // 没有父菜单时添加父菜单关系

            // 已添加过 的菜单id 集合
//            List<Integer> hasMneuIds = menuService.findMenusIdByRoleIdM2M(role.getId());
            HashSet<Integer> add2RM_Ids = new HashSet<>();
            for (Menu menu : menuList) {
                if (menu.getParentMenuId()!=null){
                    // 子菜单 获取父菜单id添加进去 添加父id
                    add2RM_Ids.add(menu.getParentMenuId());
                }
                // 添加自己
                add2RM_Ids.add(menu.getId());
            }
            // 要添加的id中的 已在多对多表中的id
//            if (hasMneuIds!=null){
//                add2RM_Ids.removeAll(hasMneuIds);
//            }
            role_menuM2M.addMenus4Role(add2RM_Ids,role.getId());
        }
        if (role.getPermissionIds().size()!=0){
            // 添加时可以不点 item
            role_permissionM2M.addpermissions4Role(new HashSet<>(role.getPermissionIds()),role.getId());
        }
        // 查询关联了该角色的user 注销
        List<User> userList = user_roleM2M.findUsersByRoleId(role.getId());
        return userList;
    }

    @Override
    public void deleteRoleById(Integer id) throws ServiceException{
        // 查询是否有用户在使用该角色
        if (user_roleM2M.findCountByRoleId(id)>0) {
            throw new ServiceException("该角色正被使用中不能删除!");
        }
        // 删除
        role_permissionM2M.clearBindByRole(id);
        role_menuM2M.clearBindByRole(id);
        roleDao.deleteRoleById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public boolean hasRoleIds(Set<Integer> roleIds) {
        if (roleIds.size()==0){
            // 全删
            return true;
        }
        long count = roleDao.getIdInTableCount(roleIds);
        if (roleIds.size()==count){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> findPMAll() {
        List<Permission> permissions = permissionService.findAll();
        List<Menu> menus = menuService.findAll();
        HashMap<String , Object> data = new HashMap<>();
        data.put("permissions",permissions);
        data.put("menus",menus);
        return data;
    }
}
