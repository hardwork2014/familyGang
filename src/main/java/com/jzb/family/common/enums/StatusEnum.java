package com.jzb.family.common.enums;

/**
 * 状态枚举
 * @author 胡启萌
 * @date 2015年8月26日 上午10:24:33
 */
public enum StatusEnum {
	
	success("000", "成功"),
	primaryKeyNull("1001","id为空"),
	notExitUser("1002", "用户不存在"),
	fail("9999", "失败");
	
	private String code;
	
	private String msg;
	
	private StatusEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
