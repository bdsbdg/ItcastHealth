package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
