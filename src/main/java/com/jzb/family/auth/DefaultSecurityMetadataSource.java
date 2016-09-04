package com.jzb.family.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.StringUtils;

import com.jzb.family.domain.AppUser;

public class DefaultSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
	
	@SuppressWarnings("unchecked")
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		String url = ((FilterInvocation)object).getHttpRequest().getServletPath();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !authenticationTrustResolver.isAnonymous(authentication)) {
        	AppUser appUser = (AppUser)authentication.getPrincipal();
        	Map<String, Collection<ConfigAttribute>> resourceMap = appUser.getResourceMap();
        	for(String rsUrl : resourceMap.keySet()) {
        		if(url.equals(rsUrl)) {
        			return resourceMap.get(rsUrl);
        		}
        	}
        	if(!StringUtils.isEmpty(url)) {
        		if(url.contains("/json/")) {
        			Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        			attributes.add(new SecurityConfig(AuthConstant.ROOT_AUTHORITY));
        			return attributes;
        		}
        	}
        }
        return (Collection<ConfigAttribute>)Collections.EMPTY_LIST;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return new ArrayList<ConfigAttribute>();
	}

}
