package com.jzb.family.auth;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class DefaultAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {
	
	private Logger LOG = Logger.getLogger(DefaultAuthenticationSuccessHandler.class);

	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
        //抽取session中 的参数
        Map<String, Object> attributesToMigrate = extractAttributes(session);
        //放弃原来的session
        session.invalidate();
        //创建新的session
        session = request.getSession(true);
        //将原session 的参数转移到新的session
        transferAttributes(attributesToMigrate, session);
        //记录日志
        LOG.info("Authentication success: account:" + request.getParameter("j_username")
                + ", ipadress:" + request.getRemoteAddr());
        String successMessage = "success";
        response.getOutputStream().write(successMessage.getBytes("UTF-8"));
	}
	
	private Map<String, Object> extractAttributes(HttpSession session) {
        Map<String, Object> attributesToMigrate = new HashMap<String, Object>();

        @SuppressWarnings("rawtypes")
		Enumeration enumer = session.getAttributeNames();

        while (enumer.hasMoreElements()) {
            String key = (String) enumer.nextElement();

            attributesToMigrate.put(key, session.getAttribute(key));
        }
        return attributesToMigrate;
    }

    private void transferAttributes(Map<String, Object> attributes, HttpSession newSession) {
        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                newSession.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

}
