package com.itheima.job.timetask;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 定时清理预约设置历史数据
 * @Author Sean
 * @Date 2021/1/18 11:35
 */
@Component
public class CleanTableJob {

    /**
     * 日志信息
     */
    private static final Logger log = LoggerFactory.getLogger(CleanTableJob.class);

    @Reference
    private OrderService orderSettingService;

    /**
     * 根据日期删除预约设置历史数据
     * 每个月的最后一天执行
     */
    //@Scheduled(initialDelay = 3000, fixedDelay = 1800000)
    @Scheduled(cron = "0 0 2 L * ?")
    public void cleanTable(){
        log.info("开始执行删除预约设置历史数据");
        // 当前日期
        Date today = new Date();
        // 获得上个月的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH,-1);
        Date lastMoth = calendar.getTime();
        /* 日期格式转换 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String lastMothStr = sdf.format(lastMoth)+"-31";
        log.debug("准备删除{}前的预约设置历史数据",lastMothStr);
        // 调用orderSettingService根据上个月日期删除预约设置历史数据
        int count = orderSettingService.deleteHistoricalDataByDate(lastMothStr);
        log.debug("清理了{}条数据",count);
    }
}
