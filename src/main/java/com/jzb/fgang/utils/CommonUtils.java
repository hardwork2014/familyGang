package com.jzb.fgang.utils;

import java.util.Collection;

public class CommonUtils {
	
	/**
     * 判断字符串为空，或者空白字符或者Null
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isBlank(Integer id) {
        return id == null || id.intValue() == 0;
    }
    
    public static boolean isNullOrEmpty(Collection c){
        return c==null||c.isEmpty();
    }
    
}
