package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckitemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

@Service
public class CheckitemServiceImpl implements CheckitemService {

    @Autowired
    private CheckitemDao checkitemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkitemDao.findAll();
    }

    @Override
    public long allCount() {
        return checkitemDao.allCount();
    }

    @Override
    public int addCheckitem(CheckItem checkItem) {
        return checkitemDao.addCheckitem(checkItem);
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
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
        Page<CheckItem> page = checkitemDao.findPage(queryParam);

        return new PageResult<CheckItem>(page.getTotal(), page.getResult());
    }

    @Override
    public int deleteById(int id) {
        // 查看是否有关联的checkgroup
        if (checkitemDao.findCheckitemFromM2M(id)>0) {
            // 抛出异常
            throw new ServiceException("该检查项已关联检查组 不能删除!");
        }
        return checkitemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(int id){
        CheckItem checkItem = checkitemDao.findById(id);
        if (checkItem!=null){
            return checkItem;
        }
        throw new ServiceException("未找到该检查项!");
    }

    @Override
    public void setCheckitem(CheckItem checkItem){
        if (checkitemDao.setCheckitem(checkItem)<1) {
            // 没修改到
            throw new ServiceException("修改检查项失败,该检查项已不存在!");
        }
    }

    @Override
    public boolean hasCheckitemIds(List<Integer> checkitemIds) {
        if (checkitemIds.size()==0){
            // 没有选择checkitem
            return true;
        }
        HashSet<Integer> idSet = new HashSet<>(checkitemIds);
        long count = checkitemDao.getIdInTableCount(idSet);
        if (count!=idSet.size()){
            // 添加时有人删除了某个检查项
            return false;
        }
        return true;
    }
}
