package com.xmy.eyes.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationProviderProxy;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.PushMessageContants;
import com.xmy.eyes.bean.GeofenceBean;
import com.xmy.eyes.presenter.BmobPushMsgPresenter;
import com.xmy.eyes.util.SPUtil;

import de.greenrobot.event.EventBus;

public class GeoFenceService extends Service implements AMapLocationListener{

	private static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
	
	private PendingIntent mPendingIntent;
	private double mLat;
	private double mLng;
	private int mRadius;
	
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(GEOFENCE_BROADCAST_ACTION);
		registerReceiver(mGeofenceReceiver, filter);
		Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
		mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.mLat = intent.getDoubleExtra("lat", 0);
		this.mLng = intent.getDoubleExtra("lng", 0);
		this.mRadius = intent.getIntExtra("radius", 0);
		EyesApplication.mGaodeManager.addGeoFenceAlert(mLat, mLng, mRadius, Contants.GEOFENCE_DURATION, mPendingIntent);
		EyesApplication.mGaodeManager.requestLocationData(LocationProviderProxy.AMapNetwork, Contants.GEOFENCE_INTERVAL, 10, this);
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private BroadcastReceiver mGeofenceReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)){
				GeofenceBean bean = new GeofenceBean();
				bean.setType(PushMessageContants.MSG_TYPE_GEOFENCE);
				Bundle b = intent.getExtras();
				int status = b.getInt("status");
				if(status == 0){
					Toast.makeText(getApplicationContext(), "不在区域", Toast.LENGTH_SHORT).show();
					bean.setIn(false);
				}else{
					Toast.makeText(getApplicationContext(), "在区域内", Toast.LENGTH_SHORT).show();
					bean.setIn(true);
				}
				BmobPushMsgPresenter.getDefault().sendMessage(bean, EyesApplication.mMyUser.getBindedUID());
			}
		}
	};

	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		LatLng curP = new LatLng(arg0.getLatitude(), arg0.getLongitude());
		LatLng genfenceP = SPUtil.getGeofence();
		double distance = DistanceUtil.getDistance(curP, genfenceP);
		ELog.d("distance = "+distance);
		GeofenceBean bean = new GeofenceBean();
		bean.setType(PushMessageContants.MSG_TYPE_GEOFENCE);
		bean.setLat(curP.latitude);
		bean.setLng(curP.longitude);
		bean.setDistance(distance);
		if(distance <= SPUtil.getRadius()){
			//在电子围栏范围内
			bean.setIn(true);
		}else{
			//不在电子围栏范围内
			bean.setIn(false);
		}
		BmobPushMsgPresenter.getDefault().sendMessage(bean, EyesApplication.mMyUser.getBindedUID());
		EventBus.getDefault().post(bean);
	}
}
