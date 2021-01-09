package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    /**
     * 上传图片
     * @param imgFile
     */
    @PostMapping("/upload")
    public Result uploadSetmealImg(MultipartFile imgFile){
        // 获取文件后缀
        String fileName = imgFile.getOriginalFilename();
        String typeName = fileName.substring(fileName.lastIndexOf("."));
        //- 生成唯一文件名，拼接后缀名
        String filename = UUID.randomUUID() + typeName;

        //- 调用七牛上传文件
        try {
            //- 返回数据给页面
            //{
            //    flag:
            //    message:
            //    data:图片url

            Map<String,String> map = new HashMap<String,String>();
            String imgId = QiNiuUtils.uploadViaByte(imgFile.getBytes(), filename);
//             imgKey缓存至redis  前端提交时删除redis的imgKey
            ShardedJedis jedisConn = shardedJedisPool.getResource();
            try {
                jedisConn.hset(MessageConstant.SETMEAL_IMG_KEY,imgId, String.valueOf(System.currentTimeMillis()));
            }catch (Exception e){
                throw e;
            }finally {
                jedisConn.close();
            }

            map.put("path", QiNiuUtils.DOMAIN);
            map.put("imgId", imgId);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 添加Setmeal 添加前check Checkitem对应id是否存在
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public Result addSetmeal(@Validated @RequestBody Setmeal setmeal){
//        System.out.println(setmeal);
        setmealService.addSetmeal(setmeal);
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @ResponseBody
    @PostMapping("/find/page")
    public Result findPage(@Validated @RequestBody QueryPageBean queryPageBean){
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmealService.findPage(queryPageBean));
    }

    /**
     * 查出套餐及套餐下group的id
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/find/{id}")
    public Result findCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1") Integer id){
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, setmealService.findById(id));
    }

    @ResponseBody
    @PutMapping("/set")
    public Result setCheckGroup(@Validated @RequestBody Setmeal setmeal){
//        System.out.println(setmeal);

        setmealService.setSetmeal(setmeal);
        return new Result(true, "编辑套餐成功");
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public Result deleteCheckGroup(@PathVariable("id") @Min(value = 1,message = "id不能小于1")Integer id){
        setmealService.deleteSetmealById(id);
        return new Result(true, "删除套餐成功");
    }

}
