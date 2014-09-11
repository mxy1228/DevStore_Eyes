package com.xmy.eyes;

public class Contants {

	public static final long GEOFENCE_DURATION = 1000 * 60 * 60 * 24 * 30;//百度电子围栏的有效期，设置成最长1个月
	
	public static final String GEOFENCE_ID = "geofence";//百度电子围栏ID
	
	//小米结构化存储APPID
	public static final String MI_APP_ID = "2882303761517253721";
	
	//Bmob：列名：uid
	public static final String BMOB_UID = "uid";
	//Bmob：列名：bind
	public static final String BMOB_BIND = "bind";
	//Bmob：列名：username
	public static final String BMOB_USER_NAME = "username";
	
	//QQ APPID
	public static final String QQ_APPID = "1102488799";
	//Bmob ApplicationID
	public static final String BMOB_APP_ID = "82e45767262e1ec0c071397e49f30154";
	//Bmob Table Name
	public static final String BMOB_TABLE_NAME = "MyUser";
	//百度APPID
	public static final String BAIDU_APPID = "Al8y2B0qikgt6viHQXAYbgZp";
	//连接请求超时时间
	public static int TIME_OUT = 20 * 1000;
}
