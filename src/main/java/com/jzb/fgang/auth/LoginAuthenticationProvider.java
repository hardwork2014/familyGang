package com.jzb.fgang.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.jzb.fgang.service.DefaultUserDetailsService;
import com.jzb.fgang.utils.BeanUtils;

public class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	private Md5PasswordEncoder passwordEncoder;
	
	private DefaultUserDetailsService userDetailService;
	
	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		
		UserDetails loadedUser;
        try {
            loadedUser = userLoginAuth(authentication);
        } catch (AuthenticationException e) {
        	throw e;
        }
        return loadedUser;
	}
	
	/**
	 * 用户验证接口
	 * author zhouhuiqun470
	 * @param url
	 * @param userVO
	 * @return
	 */
	private AppUser userLoginAuth(UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		
		String userAccount = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		SysUser user = userDetailService.selectUser(userAccount, PasswordEncryption.encrypt(password));
		if(user == null) {
			throw new com.paic.clearing.authentication.AuthenticationException(AuthConstant.USER_PASSWORD_NOT_MATCH_MESSAGE);
		}
		AppUser appUser = BeanUtils.copyAs(user, AppUser.class);
		appUser.setAuthorities(getAuthorities());
		appUser.setSysMenuList(userDetailService.selectMunuList());
		appUser.setSysChannelList(userDetailService.selectChannelList());
		return appUser;
	}
	
	/**
	 * 获得访问角色权限
	 * 
	 * @param access
	 * @return
	 */
	public Collection<GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new GrantedAuthorityImpl("ROLE_USER"));
		return authList;
	}

	public Md5PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(Md5PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public DefaultUserDetailsService getUserDetailService() {
		return userDetailService;
	}

	public void setUserDetailService(DefaultUserDetailsService userDetailService) {
		this.userDetailService = userDetailService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		
	}
	
}
