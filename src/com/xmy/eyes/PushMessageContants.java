package com.xmy.eyes;

public class PushMessageContants {

	//消息类型：设置电子围栏
	public static final int MSG_TYPE_SET_GEOFENCE = 1;
	//消息类型：电子围栏
	public static final int MSG_TYPE_GEOFENCE = 2;
	//消息类型：信息
	public static final int MSG_TYPE_NORMAL = 3;
	//消息类型：设置电子围栏结果
	public static final int MSG_TYPE_GEOFENCE_RESULT = 4;
	//消息类型：请求绑定
	public static final int MSG_TYPE_REQUEST_BIND = 5;
	//
	public static final int MSG_TYPE_REQUEST_BIND_RESULT = 6;
	
	public static final String TYPE = "type";
	public static final String CONTENT = "content";
	public static final String LON = "lon";
	public static final String LAT = "lat";
	public static final String RADIUS = "radius";
	public static final String RESULT = "result";
	public static final String MSG = "msg";
}
