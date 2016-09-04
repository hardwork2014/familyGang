package com.jzb.family.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class MyUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {
	
	private boolean useCaptcha = false;

	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        checkUserInput(request);
        checkCaptcha(request);
        return super.attemptAuthentication(request, response);
    }

    /**
     * 校验用户是否输入了用户、密码和验证码。若没有输入任一项，抛出BadCredentialsException
     */
    private void checkUserInput(HttpServletRequest request) {
        String userAccount = obtainUsername(request);
        String password = obtainPassword(request);
        if (StringUtils.isBlank(userAccount) || StringUtils.isBlank(password)) {
            throw new AuthException(AuthConstant.INPUT_NOT_COMPLETE_MESSAGE);
        }
    }

    /**
     * 校验用户验证码是否正确，否则抛出BadCredentialsException
     */
    private void checkCaptcha(HttpServletRequest request) {
        if (useCaptcha) {
            String captchaStr = request.getParameter(AuthConstant.SIMPLECAPTCHA);
            String orgCaptcha = (String)request.getSession().getAttribute(AuthConstant.SIMPLECAPTCHA);
            if (captchaStr == null || orgCaptcha == null || !orgCaptcha.equalsIgnoreCase(captchaStr)) {
                throw new AuthException(AuthConstant.CAPTCHA_WRONG_MESSAGE);
            }
        }
    }

    public void setUseCaptcha(boolean useCaptcha) {
        this.useCaptcha = useCaptcha;
    }

}
