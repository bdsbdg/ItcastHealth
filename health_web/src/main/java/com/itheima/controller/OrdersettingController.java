package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.POIUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Validated
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    private OrderService orderService;

    /**
     * 上传指定格式的excel 解析为预约设置
     * @param excelFile excel文件
     */
    @PostMapping("/upload")
    public Result uploadSetting(MultipartFile excelFile){
        // 解析excel
        System.out.println(excelFile);
        try {
            // 校验文件合法性
            POIUtils.checkFile(excelFile);
            List<String[]> OrderSettings = POIUtils.readExcel(excelFile);
//            for (String[] string : OrderSettings) {
//                System.out.println(Arrays.toString(string));
//            }
            ArrayList<OrderSetting> orderSettingList = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            OrderSettings.forEach(setting->{
                try {
                    orderSettingList.add(new OrderSetting(dateFormat.parse(setting[0]),Integer.parseInt(setting[1])));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            System.out.println(orderSettingList);
            orderService.addSettings(orderSettingList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "文件有问题!");
        }
        // 获取数据
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }
}
