package com.jzb.family.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	private Logger LOG = Logger.getLogger(DefaultAuthenticationFailureHandler.class);

	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if (exception instanceof AuthenticationException) {
            response.getOutputStream().write(exception.getMessage().getBytes("UTF-8"));
            //记录日志
            LOG.info("Authentication failed: account:" + request.getParameter("j_username")
                    + ", ipadress:" + request.getRemoteAddr() + ",reason:" + exception.getMessage(), exception);
        } else {
            response.getOutputStream().write(AuthConstant.SYS_EXCEPTION_MESSAGE.getBytes("UTF-8"));
            //记录日志
            LOG.info("Authentication failed: account:" + request.getParameter("j_username")
                    + ", ipadress:" + request.getRemoteAddr() + ",reason:" + AuthConstant.SYS_EXCEPTION_MESSAGE, exception);
        }
	}

}
