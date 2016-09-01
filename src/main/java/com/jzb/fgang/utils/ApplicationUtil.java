package com.jzb.fgang.utils;

import org.springframework.context.ApplicationContext;

public class ApplicationUtil {
	
	private static ApplicationContext context;
	
	private static String rootPath;

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		ApplicationUtil.context = context;
	}

	public static String getRootPath() {
		return rootPath;
	}

	public static void setRootPath(String rootPath) {
		ApplicationUtil.rootPath = rootPath;
	}
	
	
	

}
