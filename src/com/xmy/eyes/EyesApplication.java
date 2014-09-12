package com.xmy.eyes;

import android.app.Application;
import android.content.Context;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;

import com.baidu.api.Baidu;
import com.baidu.location.GeofenceClient;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.tauth.Tencent;
import com.xiaomi.infra.galaxy.android.GalaxyOAuthClient;
/**
 * 为使用百度推送，需将父类修改为com.baidu.frontia.FrontiaApplication。
 */
public class EyesApplication extends Application {
	
	
	public static Context mContext;
	
	//百度地图地理围栏服务
	public static GeofenceClient mGeofenceClient;
	//Tencent是腾讯登录的主要实现类
	public static Tencent mTencent;
	//小米云存储主要实现类
	public static GalaxyOAuthClient mMiClient;
	//百度
	public static Baidu mBaidu;
	//Bmob推送
	public static BmobPushManager mBmobPushManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		SDKInitializer.initialize(getApplicationContext());
		mTencent = Tencent.createInstance("1102488799", getApplicationContext());
		mMiClient = GalaxyOAuthClient.createInstance(Contants.MI_APP_ID);
		//初始化Bmob
		Bmob.initialize(getApplicationContext(), Contants.BMOB_APP_ID);
		mBaidu = new Baidu(Contants.BAIDU_APPID, getApplicationContext());
		BmobInstallation.getCurrentInstallation(getApplicationContext()).save();
		BmobPush.startWork(getApplicationContext(), Contants.BMOB_APP_ID);
		mBmobPushManager = new BmobPushManager(getApplicationContext());
	}
}
