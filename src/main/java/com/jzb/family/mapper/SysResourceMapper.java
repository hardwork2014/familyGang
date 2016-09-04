package com.jzb.family.mapper;

import java.util.List;

import com.jzb.family.domain.SysResource;

public interface SysResourceMapper {

    int insertSelective(SysResource record);

    SysResource selectByPrimaryKey(Integer idResource);

    int updateByPrimaryKeySelective(SysResource record);

    List<SysResource> selectByUserId(String userId);
    
    List<SysResource> selectAll();
}