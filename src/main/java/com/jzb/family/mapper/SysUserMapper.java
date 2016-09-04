package com.jzb.family.mapper;

import com.jzb.family.domain.SysUser;

public interface SysUserMapper {

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String idUser);

    int updateByPrimaryKeySelective(SysUser record);

    SysUser selectUser(String userAccount, String password);
}