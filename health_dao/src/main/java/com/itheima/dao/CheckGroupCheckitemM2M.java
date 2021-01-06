package com.itheima.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface CheckGroupCheckitemM2M {
    void addChekitems4Group(@Param("itemIds")Set<Integer> itemIds, @Param("groupId") int gid);

    List<Integer> findCheckitemIdsByCheckGroupId(Integer id);
}
