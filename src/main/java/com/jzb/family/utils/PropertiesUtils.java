package com.jzb.family.utils;

import java.util.Properties;

/**
 * 系统属性文件配置
 * @author 胡启萌
 * @date 2015年9月22日
 */
public class PropertiesUtils {
	
	private static Properties properties;

	public static void setProperties(Properties properties) {
		PropertiesUtils.properties = properties;
	}
	
	public static String getProperty(String key) {
		return PropertiesUtils.properties.getProperty(key);
	}

}
