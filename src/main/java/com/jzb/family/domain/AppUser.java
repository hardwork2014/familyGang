package com.jzb.family.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUser extends SysUser implements UserDetails {

	private static final long serialVersionUID = -1733128662322907925L;

	private Collection<GrantedAuthority> authorities;
	
	private List<SysMenu> sysMenuList;
	
	private Map<String, Collection<ConfigAttribute>> resourceMap;
	
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	
	public List<SysMenu> getSysMenuList() {
		return sysMenuList;
	}

	public void setSysMenuList(List<SysMenu> sysMenuList) {
		this.sysMenuList = sysMenuList;
	}

	public Map<String, Collection<ConfigAttribute>> getResourceMap() {
		return resourceMap;
	}

	public void setResourceMap(Map<String, Collection<ConfigAttribute>> resourceMap) {
		this.resourceMap = resourceMap;
	}

	public String getUsername() {
		return super.getUserAccount();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

}
