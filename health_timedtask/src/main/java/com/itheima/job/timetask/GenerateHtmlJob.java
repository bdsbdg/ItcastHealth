package com.itheima.job.timetask;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiNiuUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: No Description
 * User: Eric
 */
//@Component("generateHtmlJob")
public class GenerateHtmlJob {

    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);

    /**
     * spring创建对象后，调用的初始化方法
     */
    @PostConstruct
    private void init(){
        // 设置模板所在
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(),"/ftl"));
        // 指定默认编码
        configuration.setDefaultEncoding("utf-8");
    }

    /** jedis连接池 */
    @Autowired
    private JedisPool jedisPool;

    /** 订阅套餐服务 */
    @Reference
    private SetmealService setmealService;

    /** 注入freemarker主配置类 */
    @Autowired
    private Configuration configuration;

    /** 生成静态页面存放的目录 */
    @Value("${out_put_path}")
    private String out_put_path;

    /**
     * 任务执行的方法
     */
    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void doGenerateHtml(){
        // 获取redis连接对象
        Jedis jedis = jedisPool.getResource();
        // redis中set集合的key
        String key = "static:setmeal:html";
        // 获取集合中所有的套餐id数据
        Set<String> setmealIds = jedis.smembers(key);
        if(null != setmealIds && setmealIds.size() > 0){
            // 有数据则需要处理
            for (String setmealId : setmealIds) {
                String[] ss = setmealId.split("\\|");
                // 获取套餐的id
                int id = Integer.valueOf(ss[0]);
                // 获取操作类型
                String operation = ss[1];
                // 需要生成套餐详情页面的操作
                if("1".equals(operation)) {
                    // 查询套餐详情
                    Setmeal setmealDetail = setmealService.findDetailById(id);
                    // 设置图片的全路径
                    setmealDetail.setImg(QiNiuUtils.DOMAIN + setmealDetail.getImg());
                    // 生成套餐详情静态页面
                    generateSetmealDetailHtml(setmealDetail);
                }else{
                    // 删除套餐静态页面
                    removeSetmealDetailFile(id);
                }
                // 每处理完一个，删除set集合中对应的数据
                jedis.srem(key, setmealId);
            }
            // 套餐列表的数据也发生了变化，要重新生成静态页面
            generateSetmealList();
        }
    }

    /**
     * 删除（被删除的套餐）静态页面
     * @param id 套餐id
     */
    private void removeSetmealDetailFile(int id){
        File file = new File(out_put_path,String.format("setmeal_%d.html",id));
        if(file.exists()){
            // 如果文件存在，则删除
            file.delete();
        }
    }

    /**
     * 生成套餐详情页面
     * @param setmeal
     */
    private void generateSetmealDetailHtml(Setmeal setmeal){
        // 构建数据模型
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmeal", setmeal);
        // 模板名
        String templateName = "mobile_setmeal_detail.ftl";
        // 生成文件的全路径
        String filename = String.format("%s/setmeal_%d.html",out_put_path,setmeal.getId());
        // 生成文件
        generateFile(templateName,dataMap,filename);
    }

    /**
     * 生成套餐列表页面
     */
    private void generateSetmealList(){
        // 获取所有套餐信息
        List<Setmeal> setmealList = setmealService.findAll();
        setmealList.forEach(setmeal -> {
            // 设置每个套餐图片的全路径
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        // 构建数据模型
        Map<String,Object> dataMap = new HashMap<String,Object>();
        // key setmealList 与模板中的变量要一致
        dataMap.put("setmealList",setmealList);
        // 生成的文件全路径
        String setmealListFile = out_put_path + "/mobile_setmeal.html";
        // 生成文件
        generateFile("mobile_setmeal.ftl", dataMap, setmealListFile);
    }

    /**
     * 生成文件
     * @param templateName 模板名
     * @param dataMap 要填充的数据
     * @param filename 生成的文件名 全路径
     */
    private void generateFile(String templateName, Map<String,Object> dataMap, String filename){
        try {
            // 获取模板
            Template template = configuration.getTemplate(templateName);
            // utf-8 不能少了。少了就中文乱码
            BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
            // 填充数据到模板
            template.process(dataMap,writer);
            // 关闭流
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("生成静态页面失败",e);
        }
    }
}
