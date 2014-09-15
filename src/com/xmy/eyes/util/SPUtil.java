package com.xmy.eyes.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.baidu.mapapi.model.LatLng;
import com.xmy.eyes.EyesApplication;

public class SPUtil {
	
	private static final String USER_INFO_SP_NAME = "userinfo";
	private static final String USERNAME = "username";
	private static final String LAT = "lat";
	private static final String LON = "lon";
	private static final String RADIUS = "radius";
	private static final String UID = "uid";

	
	/**
	 * 获取存储用户信息的SP
	 * @return
	 */
	public static SharedPreferences getUserInfoSP(){
		return EyesApplication.mContext.getSharedPreferences(USER_INFO_SP_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * 获取UID
	 * @return
	 */
	public static String getUID(){
		return getUserInfoSP().getString(UID, null);
	}
	
	/**
	 * 存储UID
	 * @param uid
	 * @return
	 */
	public static boolean setUID(String uid){
		return getUserInfoSP().edit().putString(UID, uid).commit();
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
	
	/**
	 * 保存电子围栏经纬度及半径
	 * @param lon
	 * @param lat
	 * @param radius
	 * @return
	 */
	public static boolean saveGeofence(double lon,double lat,int radius){
		Editor e = getUserInfoSP().edit();
		e.putString(LAT, lat+"");
		e.putString(LON, lon+"");
		e.putInt(RADIUS, radius);
		return e.commit();
	}
	
	/**
	 * 获取电子围栏坐标
	 * @return
	 */
	public static LatLng getGeofence(){
		double lat = Double.valueOf(getUserInfoSP().getString(LAT, null));
		double lng = Double.valueOf(getUserInfoSP().getString(LON, null));
		LatLng p = new LatLng(lat, lng);
		return p;
	}
	
	/**
	 * 获取电子围栏半径
	 * @return
	 */
	public static int getRadius(){
		int radius = getUserInfoSP().getInt(RADIUS, 0);
		return radius;
	}
}
