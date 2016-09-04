package com.jzb.family.mapper;

import com.jzb.family.domain.SysRole;

public interface SysRoleMapper {

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer idRole);

    int updateByPrimaryKeySelective(SysRole record);

}