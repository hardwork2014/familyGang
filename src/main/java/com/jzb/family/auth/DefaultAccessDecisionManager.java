package com.jzb.family.auth;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class DefaultAccessDecisionManager implements AccessDecisionManager {

	/**
     * @param authentication   权限
     * @param object           对象
     * @param configAttributes 配置属性
     */
	@Override
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException {
		if (configAttributes==null||configAttributes.isEmpty()) {
            return;
        }
		//TODO: IS_OUTSIDE_MENU=N 不过滤 放到application中，容器启动加载一次
		
        //所请求的资源拥有的权限(一个资源对多个权限)
        for (ConfigAttribute ca : configAttributes) {
            //访问所请求资源所需要的权限
            String needPermission = ca.getAttribute();
            //用户所拥有的权限authentication包含资源对应的权限则可以访问
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (needPermission.equals(authority.getAuthority())) {
                    return;
                }
            }
            if(AuthConstant.ROOT_AUTHORITY.equals(needPermission)) {
            	throw new AccessDeniedException("您没有权限访问此接口");
            }
        }
        //没有权限 TODO 使用StringResource
        throw new AccessDeniedException("您没有权限访问此页面");
	}

	/**
     * 判断配置属性是否支持.
     * @param attribute 属性
     * @return boolean
     */
	@Override
	public boolean supports(ConfigAttribute arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
     * 判断类别是否支持.
     * @param clazz 类泛型
     * @return boolean
     */
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
