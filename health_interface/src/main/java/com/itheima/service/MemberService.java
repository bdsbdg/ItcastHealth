package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member addMemberByTelephone(String telephone);

    Map<String,Object> findMemberByYear4Month();

}
