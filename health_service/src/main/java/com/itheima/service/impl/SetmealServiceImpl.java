package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.dao.setmealCheckGroupM2M;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.exception.ServiceException;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.HashSet;
import java.util.List;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private CheckGroupService checkGroupService;
    @Autowired
    private setmealCheckGroupM2M setmealCheckGroupM2M;
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Override
    public void addSetmeal(Setmeal setmeal) {
        if (!checkGroupService.hasCheckGroupIds(setmeal.getCheckGroupsId())) {
            throw new ServiceException("添加了已被删除的检查项!");
        }

        // 添加套餐
        setmealDao.addCheckGroup(setmeal);

        // 绑定多对多关系
        if (setmeal.getCheckGroupsId().size()!=0){
            // 添加时可以不点 group
            setmealCheckGroupM2M.addChekGroup4Setmeal(new HashSet<>(setmeal.getCheckGroupsId()),setmeal.getId());
        }

        delImgKey(setmeal.getImg());

    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
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
        Page<Setmeal> page = setmealDao.findPage(queryParam);

        return new PageResult<Setmeal>(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id);
        if (setmeal!=null){
//            System.out.println(checkGroup);
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
            return setmeal;
        }
        throw new ServiceException("未找到该套餐!");
    }

    @Override
    public void setSetmeal(Setmeal setmeal) {
        // 开启事务
        // 改变group属性
        setmealDao.setCheckGroup(setmeal);
        // 删除旧多对多关系
        setmealCheckGroupM2M.clearBindBySetmeal(setmeal.getId());
        // 添加新多对多关系
        setmealCheckGroupM2M.addCheckGroups4Setmeal(new HashSet<Integer>(setmeal.getCheckGroupsId()),setmeal.getId());
        // 删除图片key
        delImgKey(setmeal.getImg());
    }

    @Override
    public void deleteSetmealById(Integer id) {
        // 解除多对多绑定关系
        setmealCheckGroupM2M.clearBindBySetmeal(id);
        // 获取图片key 删除图片
        QiNiuUtils.removeFiles(setmealDao.getImg(id));
        // 删除套餐
        setmealDao.deleteSetmealById(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 套餐详详详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(Integer id) {
        return setmealDao.findDetailById(id);
    }

    public void delImgKey(String imgKey){
        //         删除redis上的imgKey
        ShardedJedis jedisConn = shardedJedisPool.getResource();
        try {
            jedisConn.hdel(MessageConstant.SETMEAL_IMG_KEY, imgKey);
        }catch (Exception e){
        }finally {
            jedisConn.close();
        }
    }

}
