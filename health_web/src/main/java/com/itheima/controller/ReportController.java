package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private SetmealService setmealService;

    @Reference
    private MemberService memberService;

    /**
     * 套餐百分比饼图 {total:100,data:[{"套餐名":预约记录数}]}
     * @return Result
     */
    @GetMapping("/setmeal/proportion")
    public Result setmealProportionReport(){
        List<Map<String, Object>> data = setmealService.findSetmeal4Gropu();

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
    }

    @GetMapping("/member/year")
    public Result memberCountOfYear(){
        return new Result(true, "获取会员数据成功",memberService.findMemberByYear4Month());
    }

    @GetMapping("/member/table")
    public Result memberTableReport(){
        /*
        * 本日新增
        * 本周新增
        * 本月新增
         * 总会员
         *
        * 今日预约
        * 本周预约
        * 本月预约
        *
        * 今日到诊
        * 本周到诊
        * 本月到诊
        *
        * 热门套餐 前四个
        * */
        Map<String,Object> data = setmealService.findBusinessData();
        return new Result(true, "获取报表数据成功",data);
    }
}
