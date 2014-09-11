package com.xmy.eyes.util;

import com.xmy.eyes.EyesApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	
	private static final String USER_INFO_SP_NAME = "userinfo";
	private static final String USERNAME = "username";

	/**
	 * ��ȡ�洢�û���Ϣ��SP
	 * @return
	 */
	public static SharedPreferences getUserInfoSP(){
		return EyesApplication.mContext.getSharedPreferences(USER_INFO_SP_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * ��ȡ�û��ǳ�
	 * @return
	 */
	public static String getUserName(){
		return getUserInfoSP().getString(USERNAME, null);
	}
	
	/**
	 * �洢�û��ǳ�
	 * @param username
	 * @return
	 */
	public static boolean setUserName(String username){
		return getUserInfoSP().edit().putString(USERNAME, username).commit();
	}
	
	/**
	 * �û��Ƿ��Ѿ���¼
	 * @return
	 */
	public static boolean isLogin(){
		return getUserName() != null;
	}
}
