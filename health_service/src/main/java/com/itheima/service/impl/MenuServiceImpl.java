package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.Role_menuM2M;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private Role_menuM2M role_menuM2M;

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll();
    }

    @Override
    public void addLevel1(Menu menu) {
        menuDao.addLevel1(menu);
    }

    @Override
    public void addLevel2(Menu menu) {
        menuDao.addLevel2(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        menuDao.updateMenu(menu);
    }

    @Override
    public void deleteMenu(Integer id)throws ServiceException {
        // 查询菜单是否存在，并确认是几级菜单
        Menu menu = menuDao.findMenuById(id);
        if (menu==null){
            throw new ServiceException("该菜单不存在!");
        }
        List<String> roleNames = role_menuM2M.findRoleByMenuId(id);
        if (roleNames.size()>0) {
            StringBuilder msg = new StringBuilder();
            roleNames.forEach(name -> {
                if (name!=null){
                    msg.append(name);
                    msg.append(",");
                }
            });
            throw new ServiceException("该菜单已被角色:"+ msg.toString() +"使用 不能删除!");
        }

        if (menu.getLevel()==1){
            // 父菜单 确认子菜单是否删干净了
            if (menuDao.haveSon(id)>0) {
                throw new ServiceException("该菜单存在关联子菜单 不能删除!");
            }
        }
        menuDao.deleteMenu(id);
    }

    @Override
    public Menu findMenuById(Integer id) {
        return menuDao.findMenuById(id);
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
        Page<Menu> page = menuDao.findPage(queryParam);

        return new PageResult<Menu>(page.getTotal(), page.getResult());
    }

    @Override
    public LinkedList<Menu> hasMenuIds(Set<Integer> menuIds) {
        if (menuIds.size()==0){
            // 没有选择menu
            return new LinkedList<Menu>();
        }
        LinkedList<Menu> menuList = menuDao.getIdInTableCount(menuIds);
        if (menuList.size()!=menuIds.size()){
            // 添加时有人删除了某个检查项
            return null;
        }
        return menuList;
    }

    @Override
    public List<Menu> findAllParent() {
        return menuDao.findAllParent();
    }

    @Override
    public List<Integer> findMenusIdByRoleIdM2M(Integer id) {
        return role_menuM2M.findMenusIdByRoleIdM2M(id);
    }
}
