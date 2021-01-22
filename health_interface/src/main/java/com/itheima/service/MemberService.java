package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemberService {
    Member addMemberByTelephone(String telephone);

    Map<String,Object> findMemberByYear4Month();

    List<HashMap<String, Object>> findSexCountByGroup();

    List<Map<String, Object>> findAgeCountByGroup();

    List<Integer> getMemberReport(List<String> months);
}
