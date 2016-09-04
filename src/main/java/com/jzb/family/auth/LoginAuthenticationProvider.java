package com.jzb.family.auth;

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

import com.jzb.family.domain.AppUser;
import com.jzb.family.domain.SysUser;
import com.jzb.family.service.DefaultUserDetailsService;
import com.jzb.family.utils.BeanUtils;
import com.jzb.family.utils.MD5;

public class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	private Md5PasswordEncoder passwordEncoder;
	
	private DefaultUserDetailsService userDetailService;
	
	/**
	 * 检索用户
	 */
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
	private UserDetails userLoginAuth(UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		UserDetails appUser = null;
		String userAccount = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		try {
			appUser = userDetailService.setUserDetails(userAccount, MD5.ecodeByMD5(password));
		} catch (AuthException e1){
        	throw new AuthException(e1.getMessage());
        } catch (Exception e) {
			logger.error("--userLoginAuth error={}",e);
			throw new AuthException(AuthConstant.SYS_EXCEPTION_MESSAGE);
		}
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
