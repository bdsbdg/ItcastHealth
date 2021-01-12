package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
