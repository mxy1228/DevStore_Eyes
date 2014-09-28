package com.xmy.eyes;

import android.app.Application;
import android.content.Context;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobPushManager;
import cn.sharesdk.framework.ShareSDK;

import com.amap.api.location.LocationManagerProxy;
import com.baidu.api.Baidu;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.location.GeofenceClient;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.tauth.Tencent;
import com.xiaomi.infra.galaxy.android.GalaxyOAuthClient;
import com.xmy.eyes.bean.MyUser;
public class EyesApplication extends FrontiaApplication {
	
	
	public static Context mContext;
	public static MyUser mMyUser;
	
	//�ٶȵ�ͼ����Χ������
	public static GeofenceClient mGeofenceClient;
	//Tencent����Ѷ��¼����Ҫʵ����
//	public static Tencent mTencent;
	//С���ƴ洢��Ҫʵ����
//	public static GalaxyOAuthClient mMiClient;
	//�ٶ�
	public static Baidu mBaidu;
	//Bmob����
	public static BmobPushManager mBmobPushManager;
	//�ߵµ�ͼ
	public static LocationManagerProxy mGaodeManager;
	//ShareSDK
	public static ShareSDK mShareSDK;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mGeofenceClient.setInterval(Contants.GEOFENCE_INTERVAL);//���õ���Χ�����ѵ�ʱ����
		SDKInitializer.initialize(getApplicationContext());
//		mTencent = Tencent.createInstance("1102488799", getApplicationContext());
//		mMiClient = GalaxyOAuthClient.createInstance(Contants.MI_APP_ID);
		//��ʼ��Bmob
		Bmob.initialize(getApplicationContext(), Contants.BMOB_APP_ID);
		mBaidu = new Baidu(Contants.BAIDU_APPID, getApplicationContext());
		mBmobPushManager = new BmobPushManager(getApplicationContext());
		BmobPush.startWork(getApplicationContext(), Contants.BMOB_APP_ID);
		mGaodeManager = LocationManagerProxy.getInstance(this);
		ShareSDK.initSDK(this, Contants.SHARE_APP_KRY);
		FrontiaApplication.initFrontiaApplication(this);
	}
}
