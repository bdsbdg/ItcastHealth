package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupCheckitemM2M;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sound.midi.Soundbank;
import java.util.HashSet;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckGroupCheckitemM2M checkGroupCheckitemM2M;
    @Autowired
    private CheckitemService checkitemService;

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
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
        Page<CheckGroup> page = checkGroupDao.findPage(queryParam);

        return new PageResult<CheckGroup>(page.getTotal(), page.getResult());
    }

    /**
     * 添加检查组
     * @param checkGroup
     */
    @Override
    public void addCheckGroup(CheckGroup checkGroup) {
        if (!checkitemService.hasCheckitemIds(checkGroup.getCheckItemsId())) {
            throw new ServiceException("添加了已被删除的检查项!");
        }

        // TODO  事务
        // 添加组
        checkGroupDao.addCheckGroup(checkGroup);
        System.out.println("添加的组id："+checkGroup.getId());

        // 添加多对多关系
        checkGroupCheckitemM2M.addChekitems4Group(new HashSet<>(checkGroup.getCheckItemsId()),checkGroup.getId());
    }

    /**
     * 获取检查组By id
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        if (checkGroup!=null){
//            System.out.println(checkGroup);
            return checkGroup;
        }
        throw new ServiceException("未找到该检查项!");
    }
}
