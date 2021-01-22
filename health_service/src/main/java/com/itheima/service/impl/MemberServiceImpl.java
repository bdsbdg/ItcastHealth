package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member addMemberByTelephone(String telephone) {
        Member member = new Member();
        member.setPhoneNumber(telephone);
        member.setRemark("快速注册用户");
        member.setPassword(telephone);
        memberDao.add(member);
        return member;
    }

    @Override
    public Map<String,Object> findMemberByYear4Month() {
        // 组装过去12个月的数据, 前端是一个数组
        LinkedList<String> months = new LinkedList<>();
        // 使用java中的calendar来操作日期, 日历对象
        Calendar calendar = Calendar.getInstance();
        // 设置过去一年的时间  年-月-日 时:分:秒, 减去12个月
        calendar.add(Calendar.MONTH, -12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 构建12个月的数据
        for (int i = 0; i < 12; i++) {
            // 每次增加一个月就
            calendar.add(Calendar.MONTH, 1);
            // 过去的日期, 设置好的日期
            Date date = calendar.getTime();
            // 2020-06 前端只展示年-月
            months.add(sdf.format(date));
        }
        Map<String,Integer> data = memberDao.findByYear(months);

        HashMap<String, Object> res = new HashMap<>();
        res.put("months",months);

        LinkedList<Integer> counts = new LinkedList<>();
        months.forEach(k ->counts.add(data.get(k)));
        res.put("count",data.values());
        return res;
    }

    @Override
    public List<HashMap<String, Object>> findSexCountByGroup() {
        List<Map<String, Object>> data = memberDao.findSexCountByGroup();
        List<HashMap<String, Object>> result = new LinkedList<>();
        for (Map<String, Object> s : data) {
            HashMap<String, Object> group = new HashMap<>();
            if ("1".equals(s.get("name"))){
                group.put("name", "男");
            }else if("2".equals(s.get("name"))){
                group.put("name", "女");
            }else {
                group.put("name", "未知");
            }
            group.put("value", s.get("value"));
            result.add(group);
        }
        return result;
    }

    @Override
    public List<Map<String,Object>> findAgeCountByGroup() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> data = memberDao.findAgeCountByGroup();
        LinkedList<Map<String,Object>> result = new LinkedList<>();
        HashMap<String, Object> group1 = new HashMap<>();
        HashMap<String, Object> group2 = new HashMap<>();
        HashMap<String, Object> group3 = new HashMap<>();
        HashMap<String, Object> group4 = new HashMap<>();
        HashMap<String, Object> group5 = new HashMap<>();
        group1.put("name","0-18");
        group2.put("name","18-30");
        group3.put("name","30-45");
        group4.put("name","age>45");
        group5.put("name","未填写");
        group1.put("value",0);
        group2.put("value",0);
        group3.put("value",0);
        group4.put("value",0);
        group5.put("value",0);

        // 创建当前时间
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.YEAR,-18);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.add(Calendar.YEAR,-30);
//        Calendar calendar3 = Calendar.getInstance();
//        calendar3.add(Calendar.YEAR,-45);

//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.add(Calendar.YEAR,-30);
//        Calendar calendar3 = Calendar.getInstance();
//        calendar3.add(Calendar.YEAR,-45);
//        Date case1 = calendar1.getTime();

//        System.out.println(calendar1.getTime());
//        System.out.println(calendar2.getTime());
//        System.out.println(calendar3.getTime());
//        for (String s : data) {
//            try {
//                Date bar = sdf.parse(s);
//                if (calendar1.before(bar)){
//                    int count = (int) group1.get("value");
//                    count++;
//                    group1.put("value",count);
//                }else if (calendar2.before(bar)){
//                    int count = (int) group2.get("value");
//                    count++;
//                    group2.put("value",count);
//                }else if (calendar3.before(bar)){
//                    int count = (int) group3.get("value");
//                    count++;
//                    group3.put("value",count);
//                }else {
//                    int count = (int) group4.get("value");
//                    count++;
//                    group4.put("value",count);
//                }
//            } catch (ParseException e) {}
//        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-18);
        Date case1 = calendar.getTime();
        calendar.add(Calendar.YEAR,-12);
        Date case2 = calendar.getTime();
        calendar.add(Calendar.YEAR,-15);
        Date case3 = calendar.getTime();
        for (String s : data) {
            if (s==null){
                int count = (int) group5.get("value");
                count++;
                group5.put("value",count);
                continue;
            }
            try {
                Date bar = sdf.parse(s);
                if (case1.before(bar)){
                    int count = (int) group1.get("value");
                    count++;
                    group1.put("value",count);
                }else if (case2.before(bar)){
                    int count = (int) group2.get("value");
                    count++;
                    group2.put("value",count);
                }else if (case3.before(bar)){
                    int count = (int) group3.get("value");
                    count++;
                    group3.put("value",count);
                }else {
                    int count = (int) group4.get("value");
                    count++;
                    group4.put("value",count);
                }
            } catch (ParseException e) {}
        }
        result.add(group1);
        result.add(group2);
        result.add(group3);
        result.add(group4);
        result.add(group5);
        return result;
    }


    /**
     * 统计每个月的会员总数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        List<Integer> list = new ArrayList<Integer>();
        if(null != months){
            // 2020-02
            for (String month : months) {
                month+="-31";
                list.add(memberDao.findMemberCountBeforeDate(month));
            }
        }
        return list;
    }
}
