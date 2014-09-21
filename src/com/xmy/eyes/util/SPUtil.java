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
	private static final String IN_GEOFENCE = "in";

	
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
		String slat = getUserInfoSP().getString(LAT, null);
		String slon = getUserInfoSP().getString(LON, null);
		if(slat != null && slon != null){
			double lat = Double.valueOf(slat);
			double lng = Double.valueOf(slon);
			LatLng p = new LatLng(lat, lng);
			return p;
		}
		return null;
	}
	
	/**
	 * 获取电子围栏半径
	 * @return
	 */
	public static int getRadius(){
		int radius = getUserInfoSP().getInt(RADIUS, 0);
		return radius;
	}
	
	private static boolean getPresState(){
		return getUserInfoSP().getBoolean(IN_GEOFENCE, false);
	}
	
	private static void setState(boolean isIn){
		getUserInfoSP().edit().putBoolean(IN_GEOFENCE, isIn).commit();
	}
	
	/**
	 * 是否在电子围栏中
	 * @param distance
	 * @return
	 */
	public static boolean isStateChange(double distance){
		if(getPresState()){
			//如果上个状态是在围栏里
			if(distance <= getRadius()){
				//此时依然在围栏里，状态没变
				setState(true);
				return false;
			}else{
				//此时在围栏为，状态改变
				setState(false);
				return true;
			}
		}else{
			//如果上个状态是在围栏外
			if(distance <= getRadius()){
				//此时在围栏里，状态改变
				setState(true);
				return true;
			}else{
				//此时在围栏外，状态没变
				setState(false);
				return false;
			}
		}
	}
}
