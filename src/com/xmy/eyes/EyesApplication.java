package com.xmy.eyes;

import com.baidu.location.GeofenceClient;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.tauth.Tencent;
import com.xiaomi.infra.galaxy.android.GalaxyOAuthClient;

import android.app.Application;

public class EyesApplication extends Application {

	//百度地图地理围栏服务
	public static GeofenceClient mGeofenceClient;
	//Tencent是腾讯登录的主要实现类
	public static Tencent mTencent;
	//小米云存储主要实现类
	public static GalaxyOAuthClient mMiClient;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		SDKInitializer.initialize(getApplicationContext());
		mTencent = Tencent.createInstance("", getApplicationContext());
		mMiClient = GalaxyOAuthClient.createInstance(Contants.MI_APP_ID);
	}
}
