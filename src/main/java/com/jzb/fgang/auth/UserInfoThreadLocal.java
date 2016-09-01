package com.jzb.fgang.auth;

import com.jzb.fgang.model.AppUser;

/**
 * 用户信息本地线程
 */
public final class UserInfoThreadLocal {
    private static ThreadLocal<AppUser> userInfoThreadLocal = new ThreadLocal<AppUser>();

    /**
     * constructor
     */
    private UserInfoThreadLocal() {
    }

    /**
     * 获取当前用户
     * @return AppUser
     */
    public static AppUser getCurrentUser() {
        return userInfoThreadLocal.get();
    }

    /**
     * 设置本地用户线程
     * @param appUser 用户信息
     */
    public static void setUserToThreadLocal(AppUser sysUser) {
        userInfoThreadLocal.set(sysUser);
    }

    /**
     * clearThreadLocal
     */
    public static void clearThreadLocal() {
        userInfoThreadLocal.remove();
    }
}
