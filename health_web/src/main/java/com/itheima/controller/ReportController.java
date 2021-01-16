package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
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

    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res){
        // 获取excel表路径
        String excel_template_path = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        System.out.println(excel_template_path);
        try(XSSFWorkbook wk = new XSSFWorkbook(excel_template_path);
            OutputStream out = res.getOutputStream()) {
            // 获取excel表对象

            // 获取sheet
            XSSFSheet sht = wk.getSheetAt(0);
            // 获取数据
            Map<String,Object> data = setmealService.findBusinessData();
            // 插入数据
            // 日期2，5
            sht.getRow(2).getCell(5).setCellValue(data.get("reportDate").toString());
            // 新增会员数 4,5
            sht.getRow(4).getCell(5).setCellValue(Math.toIntExact((Long) data.get("todayNewMember")));
            // 总会员数 4,7
            sht.getRow(4).getCell(7).setCellValue(Math.toIntExact((Long) data.get("totalMember")));
            // 本周新增会员数5,5
            sht.getRow(5).getCell(5).setCellValue(Math.toIntExact((Long) data.get("thisWeekNewMember")));
            // 本月新增会员数 5,7
            sht.getRow(5).getCell(7).setCellValue(Math.toIntExact((Long) data.get("thisMonthNewMember")));

            //=================== 预约 ============================
            sht.getRow(7).getCell(5).setCellValue(Math.toIntExact((Long) data.get("todayOrderNumber")));
            sht.getRow(7).getCell(7).setCellValue(Math.toIntExact((Long) data.get("todayVisitsNumber")));
            sht.getRow(8).getCell(5).setCellValue(Math.toIntExact((Long) data.get("thisWeekOrderNumber")));
            sht.getRow(8).getCell(7).setCellValue(Math.toIntExact((Long) data.get("thisWeekVisitsNumber")));
            sht.getRow(9).getCell(5).setCellValue(Math.toIntExact((Long) data.get("thisMonthOrderNumber")));
            sht.getRow(9).getCell(7).setCellValue(Math.toIntExact((Long) data.get("thisMonthVisitsNumber")));

            // 热门套餐
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>> )data.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> setmealMap : hotSetmeal) {
                sht.getRow(row).getCell(4).setCellValue((String)setmealMap.get("name"));
                sht.getRow(row).getCell(5).setCellValue((Long)setmealMap.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) setmealMap.get("proportion");
                sht.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                sht.getRow(row).getCell(7).setCellValue((String)setmealMap.get("remark"));
                row++;
            }
            // 输出excel
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            // 解决下载的文件名 中文乱码
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            res.setHeader("Content-Disposition","attachement;filename=" + filename);
            wk.write(out);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
