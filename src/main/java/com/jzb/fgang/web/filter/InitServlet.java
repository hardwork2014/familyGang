package com.jzb.fgang.web.filter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jzb.fgang.utils.ApplicationUtil;

public class InitServlet extends HttpServlet{

	private static final long serialVersionUID = 4346542181633370614L;
	
	private static final Logger logger = LoggerFactory.getLogger(InitServlet.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("初始化资源...");
		ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		ApplicationUtil.setContext(context);
		ApplicationUtil.setRootPath(config.getServletContext().getRealPath("/"));
		logger.info("初始化资源 end");
	}

}
