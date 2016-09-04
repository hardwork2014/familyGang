package com.jzb.family.auth;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * CCC 验证异常类
 */
public class AuthException extends BadCredentialsException {
	private static final long serialVersionUID = -3320932367865450050L;

	/**
     * 构造函数
     * @param msg 内容信息
     */
    /**
     * 构造器
     * @param msg 异常信息
     */
    public AuthException(String msg) {
        super(msg);
    }

}
