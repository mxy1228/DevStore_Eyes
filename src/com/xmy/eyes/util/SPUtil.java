package com.xmy.eyes.util;

import com.xmy.eyes.EyesApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	
	private static final String USER_INFO_SP_NAME = "userinfo";
	private static final String USERNAME = "username";

	/**
	 * 获取存储用户信息的SP
	 * @return
	 */
	public static SharedPreferences getUserInfoSP(){
		return EyesApplication.mContext.getSharedPreferences(USER_INFO_SP_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * 获取用户昵称
	 * @return
	 */
	public static String getUserName(){
		return getUserInfoSP().getString(USERNAME, null);
	}
	
	/**
	 * 存储用户昵称
	 * @param username
	 * @return
	 */
	public static boolean setUserName(String username){
		return getUserInfoSP().edit().putString(USERNAME, username).commit();
	}
	
	/**
	 * 用户是否已经登录
	 * @return
	 */
	public static boolean isLogin(){
		return getUserName() != null;
	}
}
