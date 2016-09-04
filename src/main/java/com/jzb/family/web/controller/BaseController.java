package com.jzb.family.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.jzb.family.common.Constants;
import com.jzb.family.domain.AppUser;

/**
 * @author 000000022
 *
 */
public class BaseController {
	@Autowired
	protected HttpSession session;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	/**
	 * 视图数据绑定模型前将字符串类型时间转型为日期格式
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	public AppUser getAppUser() {
		return (AppUser) request.getAttribute(Constants.APP_USER);
	}

}
