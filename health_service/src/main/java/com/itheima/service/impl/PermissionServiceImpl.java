package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    @Override
    public long allCount() {
        return permissionDao.allCount();
    }

    @Override
    public int addPermission(Permission permission) {
        return permissionDao.addPermission(permission);
    }

    @Override
    public PageResult<Permission> findPage(QueryPageBean queryPageBean) {
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
        Page<Permission> page = permissionDao.findPage(queryParam);

        return new PageResult<Permission>(page.getTotal(), page.getResult());
    }

    @Override
    public int deleteById(int id) throws ServiceException {
        // 查看是否有关联的checkgroup
        if (permissionDao.findPermissionFromM2M(id)>0) {
            // 抛出异常
            throw new ServiceException("该权限存在关联角色 不能删除!");
        }
        return permissionDao.deleteById(id);
    }

    @Override
    public Permission findById(int id) throws ServiceException {
        Permission permission = permissionDao.findById(id);
        if (permission!=null){
            return permission;
        }
        throw new ServiceException("未找到该权限!");
    }

    @Override
    public void setPermission(Permission permission) throws ServiceException {
        if (permissionDao.setPermission(permission)<1) {
            // 没修改到
            throw new ServiceException("修改权限失败,该权限已不存在!");
        }
    }

    @Override
    public boolean hasPermissionIds(List<Integer> permissionIds) {
        if (permissionIds.size()==0){
            // 没有选择checkitem
            return true;
        }
        HashSet<Integer> idSet = new HashSet<>(permissionIds);
        long count = permissionDao.getIdInTableCount(idSet);
        if (count!=idSet.size()){
            // 添加时有人删除了某个检查项
            return false;
        }
        return true;
    }
}
