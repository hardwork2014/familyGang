package com.jzb.fgang.auth;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * CCC 验证异常类
 */
public class AuthenticationException extends BadCredentialsException {
    /**
     * 构造函数
     * @param msg 内容信息
     */
    /**
     * 构造器
     * @param msg 异常信息
     */
    public AuthenticationException(String msg) {
        super(msg);
    }

    private static final long serialVersionUID = 207670382548006158L;

}
