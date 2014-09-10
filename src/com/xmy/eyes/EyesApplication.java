package com.xmy.eyes;

import com.baidu.location.GeofenceClient;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.tauth.Tencent;
import com.xiaomi.infra.galaxy.android.GalaxyOAuthClient;

import android.app.Application;

public class EyesApplication extends Application {

	//�ٶȵ�ͼ����Χ������
	public static GeofenceClient mGeofenceClient;
	//Tencent����Ѷ��¼����Ҫʵ����
	public static Tencent mTencent;
	//С���ƴ洢��Ҫʵ����
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
