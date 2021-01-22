package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.UserAdminDao;
import com.itheima.dao.UserPermissionDao;
import com.itheima.dao.User_RoleM2M;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.RoleService;
import com.itheima.service.UserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = UserAdminService.class)
public class UserAdminServiceImpl implements UserAdminService {

    @Autowired
    private UserAdminDao userAdminDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private User_RoleM2M user_roleM2M;
    @Autowired
    private UserPermissionDao userPermissionDao;
    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Object findPage(QueryPageBean queryPageBean) {
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
        Page<User> page = userAdminDao.findPage(queryParam);
//        System.out.println(page.getResult());
        return new PageResult<User>(page.getTotal(), page.getResult());
    }

    @Override
    public void addUser(User user) throws ServiceException {
        // 查询角色id是否全部都有
        HashSet<Integer> idSet = new HashSet<>(user.getRoleIds());
        if (idSet.size()>0){
            if (!roleService.hasRoleIds(idSet)) {
                throw new ServiceException("添加了已被删除的角色!");
            }
        }
        // 查询是否存在同名账号
        if (userAdminDao.findByName(user.getUsername())>0) {
            throw new ServiceException("已存在名为"+ user.getUsername() +"的账号");
        }
        // 添加user
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userAdminDao.addUser(user);
        if (idSet.size()>0){
            // 添加角色
            user_roleM2M.addRoles4User(idSet,user.getId());
        }
    }

    @Override
    public Map<String,Object> setUser(User user) throws ServiceException{
        String rawName = userAdminDao.findNameById(user.getId());
        if (rawName==null){
            throw new ServiceException("未找到该用户!");
        }
        userAdminDao.setUser(user);
        // 查询是否有已删除的角色
        if (!roleService.hasRoleIds(user.getRoleIds())) {
            throw new ServiceException("添加了已被删除的角色!");
        }
//        // 查询是否存在同名账号
//        if (userAdminDao.findByName(user.getUsername())>0) {
//            throw new ServiceException("已存在名为"+ user.getUsername() +"的账号");
//        }
        // 解绑
        user_roleM2M.clearBindByUser(user.getId());
        if (user.getRoleIds().size()>0){
            // 添加角色
            user_roleM2M.addRoles4User(user.getRoleIds(),user.getId());
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("user",userPermissionDao.findUserByName(user.getUsername()));
        data.put("rawName",rawName);
        return data;
    }

    @Override
    public String deleteUserById(Integer id) {
        // 删除角色关系
        String username = userAdminDao.findNameById(id);
        user_roleM2M.clearBindByUser(id);
        userAdminDao.deleteUserById(id);
        return username;
    }

    @Override
    public User findById(Integer id) {
        User user = userAdminDao.findById(id);
        if (user==null){
            throw new ServiceException("未找到该用户!");
        }
        // 查所有的角色
        user.setRoles(new HashSet<>(roleService.findAll()));
        // 选中的角色
        user.setRoleIds(new HashSet<>(user_roleM2M.findRoleIdsByUserId(user.getId())));
        return user;
    }

    @Override
    public void setPassword(String username,String oldPassword, String newPassword) throws ServiceException {
        // 获取对应用户名的密码
        User user = userAdminDao.getPasswordByUsername(username);
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        if (bpe.matches(oldPassword,user.getPassword())) {
            // 修改密码
            user.setPassword(bpe.encode(newPassword));
            userAdminDao.setPasswordById(user);
        }else {
            throw new ServiceException("原密码错误!");
        }
    }
}
