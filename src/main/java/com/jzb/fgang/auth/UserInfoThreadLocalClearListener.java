package com.jzb.fgang.auth;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.jzb.fgang.model.AppUser;
import com.jzb.fgang.model.SysUser;

/**
 * 当请求结束时，清除Thread Local的值，否则线程重用时会有问题。
 */
public class UserInfoThreadLocalClearListener implements ServletRequestListener {
    private static final Logger LOG = LoggerFactory.getLogger(UserInfoThreadLocalClearListener.class);

    public void requestDestroyed(ServletRequestEvent sre) {
        UserInfoThreadLocal.clearThreadLocal();
    }

    public void requestInitialized(ServletRequestEvent sre) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("request initialized");
        }
        SysUser currentUser = UserInfoThreadLocal.getCurrentUser();
        if (currentUser == null) {
            HttpServletRequest req = (HttpServletRequest)sre.getServletRequest();
            HttpSession session = req.getSession();
            SecurityContext context = (SecurityContext)session
                    .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (context != null) {
                Authentication authentication = context.getAuthentication();
                if (authentication != null) {
                	AppUser appUser = (AppUser)authentication.getPrincipal();
                    UserInfoThreadLocal.setUserToThreadLocal(appUser);
                }
            }
        }
    }
}
