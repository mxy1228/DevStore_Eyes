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
	//消息类型：请求绑定结果
	public static final int MSG_TYPE_REQUEST_BIND_RESULT = 6;
	//消息类型：请求定位
	public static final int MSG_TYPE_REQUEST_LOCATE = 7;
	//消息类型：请求定位结果
	public static final int MSG_TYPE_REQUEST_LOCATE_RESULT = 8;
	//消息类型：围栏状态改变
	public static final int MSG_GEOFENCE_STATE_CHANGE = 9;
	
	public static final String TYPE = "type";
	public static final String CONTENT = "content";
	public static final String LON = "lon";
	public static final String LAT = "lat";
	public static final String RADIUS = "radius";
	public static final String RESULT = "result";
	public static final String MSG = "msg";
}
