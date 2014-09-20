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
	 * ��ȡ�洢�û���Ϣ��SP
	 * @return
	 */
	public static SharedPreferences getUserInfoSP(){
		return EyesApplication.mContext.getSharedPreferences(USER_INFO_SP_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * ��ȡUID
	 * @return
	 */
	public static String getUID(){
		return getUserInfoSP().getString(UID, null);
	}
	
	/**
	 * �洢UID
	 * @param uid
	 * @return
	 */
	public static boolean setUID(String uid){
		return getUserInfoSP().edit().putString(UID, uid).commit();
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
	
	/**
	 * �������Χ����γ�ȼ��뾶
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
	 * ��ȡ����Χ������
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
	 * ��ȡ����Χ���뾶
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
		getUserInfoSP().edit().putBoolean(IN_GEOFENCE, isIn);
	}
	
	/**
	 * �Ƿ��ڵ���Χ����
	 * @param distance
	 * @return
	 */
	public static boolean isStateChange(double distance){
		if(getPresState()){
			//����ϸ�״̬����Χ����
			if(distance <= getRadius()){
				//��ʱ��Ȼ��Χ���״̬û��
				setState(true);
				return false;
			}else{
				//��ʱ��Χ��Ϊ��״̬�ı�
				setState(false);
				return true;
			}
		}else{
			//����ϸ�״̬����Χ����
			if(distance <= getRadius()){
				//��ʱ��Χ���״̬�ı�
				setState(true);
				return true;
			}else{
				//��ʱ��Χ���⣬״̬û��
				setState(false);
				return false;
			}
		}
	}
}
