package com.jzb.fgang.model;

import java.io.Serializable;
import java.util.Date;

public class SysUser implements Serializable {
    private String userId;

    private String userAccount;

    private String userName;

    private String password;
    
    private Integer validFlag;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

	public Integer getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Integer validFlag) {
        this.validFlag = validFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}