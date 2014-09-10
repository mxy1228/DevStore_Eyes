package com.xmy.eyes;

public class Contants {

	public static final long GEOFENCE_DURATION = 1000 * 60 * 60 * 24 * 30;//百度电子围栏的有效期，设置成最长1个月
	
	public static final String GEOFENCE_ID = "geofence";//百度电子围栏ID
	
	//小米结构化存储APPID
	public static final String MI_APP_ID = "2882303761517253721";
	
	//小米结构化存储：列名：uid
	public static final String UID = "uid";
	//小米结构化存储：列名：token
	public static final String TOKEN = "token";
	//小米结构化存储：列名：bind
	public static final String BIND = "bind";
	//小米结构化存储：表名：EyesUser
	public static final String TABLE_NAME = "EyesUser";
}
