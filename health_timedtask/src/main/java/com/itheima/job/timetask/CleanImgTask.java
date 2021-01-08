package com.itheima.job.timetask;

import com.itheima.constant.MessageConstant;
import com.itheima.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("CleanImgTask")
public class CleanImgTask {
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public CleanImgTask() {
        System.out.println("===========================");
    }

    public void cleanImg(){
//         查出7牛上的s所有图片
//        List<String> imgIn7Niu = QiNiuUtils.listFile();
//         查出数据库中的所有图片
//        List<String> imgInDb = setmealService.findImgs();
//         7牛的-数据库的 imgIn7Niu剩下的就是要删除的
//        imgIn7Niu.removeAll(imgInDb);
//         把剩下的图片名转成数组
//        String[] strings = imgIn7Niu.toArray(new String[]{});

//         删除7牛上的垃圾图片
        ShardedJedis jedisConn = shardedJedisPool.getResource();
        try {
            Map<String, String> imgKeyMap = jedisConn.hgetAll(MessageConstant.SETMEAL_IMG_KEY);
            List<String> delImgKeyList = new ArrayList<String>();
            for (String imgKey : imgKeyMap.keySet()) {
                // 判断值是否超过一定时间，超过就删除
//            System.out.println("Long.getLong(imgKeyMap.get(imgKey)):"+Long.valueOf(imgKeyMap.get(imgKey)));
//            System.out.println("System.currentTimeMillis():"+System.currentTimeMillis());
                Long offset = System.currentTimeMillis() - Long.parseLong(imgKeyMap.get(imgKey));
                if (offset>600000){
                    // 图片上传十分钟未提交
                    delImgKeyList.add(imgKey);
                }
            }
            if (delImgKeyList.size()>0){
                String[] strings = new String[delImgKeyList.size()];
                String[] delImgKeyArray = delImgKeyList.toArray(strings);
                jedisConn.hdel(MessageConstant.SETMEAL_IMG_KEY,delImgKeyArray);

//            jedisConn.close();
//            shardedJedisPool.close();
//                shardedJedisPool.returnResource(jedisConn);
                QiNiuUtils.removeFiles(delImgKeyArray);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            // 小坑
            if (jedisConn!=null) {
                jedisConn.close();
            }
        }
//        System.out.println("task start");
    }
}
