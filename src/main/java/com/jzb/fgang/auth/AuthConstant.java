package com.jzb.fgang.auth;


public final class AuthConstant {
	
	/**
     * 安全URL
     */
    public static final String SECURITY_URL = "/j_spring_security_check";

    /**
     * 登陆参数 用户名
     */
    public static final String LOGIN_PARAMETER_USERNAME = "j_username";

    /**
     * 登陆参数 密码
     */
    public static final String LOGIN_PARAMETER_PASSWORD = "j_password";

    /**
     * 登陆参数 验证码
     */
    public static final String SIMPLECAPTCHA = "validateCode";
    
    public static final String USER_NOT_FOUND_MESSAGE = "您输入的用户名不存在，请修改后重试!";
    
    public static final String INPUT_NOT_COMPLETE_MESSAGE = "请输入用户名和密码!";
    
    /**
     * 登陆验证消息
     */
    public static final String CAPTCHA_WRONG_MESSAGE = "您输入的验证码错误，请修改后重试!";

    public static final String SYS_EXCEPTION_MESSAGE = "系统异常，请联系管理员或稍后尝试!";
    
    public static final String NO_AUTHORITY_MESSAGE = "您无登录本系统的权限，请直接联系管理员!";
    
    public static final String USER_PASSWORD_NOT_MATCH_MESSAGE = "您输入的用户名和密码不匹配，请重新重试!";
    
    public static final String ROOT_AUTHORITY = "0";
    
    public static final String USER_ROLE_NOT = "该用户无任何授权角色，请联系系统管理员!";
    

}
