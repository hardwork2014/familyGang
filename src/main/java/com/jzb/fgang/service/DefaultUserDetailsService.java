package com.jzb.fgang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jzb.fgang.model.SysMenu;
import com.jzb.fgang.model.SysUser;

public class DefaultUserDetailsService {
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private ISysResourceService sysResourceService;
	
	public SysUser selectUser(String userAccount, String password)
			throws UsernameNotFoundException {
		SysUser user = new SysUser();
		user.setUserAccount(userAccount);
		user.setPassword(password);
		return sysUserMapper.selectUser(user);
	}
	
	public List<SysMenu> selectMunuList() {
		return sysResourceService.fillMenuList();
	}
	
}
