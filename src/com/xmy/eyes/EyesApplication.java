package com.xmy.eyes;

import android.app.Application;
import android.content.Context;
import cn.bmob.v3.Bmob;

import com.baidu.api.Baidu;
import com.baidu.location.GeofenceClient;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.tauth.Tencent;
import com.xiaomi.infra.galaxy.android.GalaxyOAuthClient;

public class EyesApplication extends Application {

	public static Context mContext;
	
	//�ٶȵ�ͼ����Χ������
	public static GeofenceClient mGeofenceClient;
	//Tencent����Ѷ��¼����Ҫʵ����
	public static Tencent mTencent;
	//С���ƴ洢��Ҫʵ����
	public static GalaxyOAuthClient mMiClient;
	//�ٶ�
	public static Baidu mBaidu;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		SDKInitializer.initialize(getApplicationContext());
		mTencent = Tencent.createInstance("1102488799", getApplicationContext());
		mMiClient = GalaxyOAuthClient.createInstance(Contants.MI_APP_ID);
		//��ʼ��Bmob
		Bmob.initialize(getApplicationContext(), Contants.BMOB_APP_ID);
		mBaidu = new Baidu(Contants.BAIDU_APPID, getApplicationContext());
	}
}
