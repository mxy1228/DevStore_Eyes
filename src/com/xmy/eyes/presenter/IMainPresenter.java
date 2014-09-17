package com.xmy.eyes.presenter;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.PushMessageContants;
import com.xmy.eyes.bean.RequestLocateBean;
import com.xmy.eyes.bean.RequestLocateResultBean;
import com.xmy.eyes.bean.SetGeofenceResultBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.service.GeoFenceService;
import com.xmy.eyes.util.SPUtil;
import com.xmy.eyes.view.MainActivity;

public class IMainPresenter{

	private IMainHandler mHandler;
	
	public IMainPresenter(IMainHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * 设置百度电子围栏
	 * API文档：http://developer.baidu.com/map/index.php?title=android-locsdk/guide/v4-2
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void setDBGeofence(double lon,double lat,int radius){
		try {
			//让对方开启电子围栏
			JSONObject obj = new JSONObject();
			obj.put(PushMessageContants.TYPE, PushMessageContants.MSG_TYPE_SET_GEOFENCE);
			obj.put(PushMessageContants.LON, lon);
			obj.put(PushMessageContants.LAT, lat);
			obj.put(PushMessageContants.RADIUS, radius);
			BmobPushMsgPresenter.getDefault().sendMessage(obj,EyesApplication.mMyUser.getBindedUID());
		} catch (Exception e) {
			ELog.e(e);
		}
	}
	
	/**
	 * 设置并启动电子围栏
	 * @param context
	 * @param lon
	 * @param lat
	 * @param radius
	 */
	public void setAndStartBDGeofence(Context context,final double lng,final double lat,final int radius){
		//将电子围栏的数据保存到SP中
		SPUtil.saveGeofence(lng, lat, radius);
		requestMyLocate();
//		Intent intent = new Intent(context,GeoFenceService.class);
//		intent.putExtra("lat", lat);
//		intent.putExtra("lng", lng);
//		intent.putExtra("radius", radius);
//		context.startService(intent);
		try {
			SetGeofenceResultBean bean = new SetGeofenceResultBean();
			bean.setType(PushMessageContants.MSG_TYPE_GEOFENCE_RESULT);
			bean.setResult(true);
			bean.setLat(lat);
			bean.setLng(lng);
			bean.setRadius(radius);
			BmobPushMsgPresenter.getDefault().sendMessage(bean,EyesApplication.mMyUser.getBindedUID());
			//然后将围栏信息保存到Bmob数据中心
			EyesApplication.mMyUser.setLat(lat+"");
			EyesApplication.mMyUser.setLng(lng+"");
			EyesApplication.mMyUser.setRadius(radius+"");
			EyesApplication.mMyUser.setIsFenced(true);
			EyesApplication.mMyUser.update(context, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					ELog.d("setAndStartBDGeofence:onSuccess");
					SPUtil.saveGeofence(lng, lat, radius);
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					ELog.e("setAndStartBDGeofence:onFailure:"+arg0+":"+arg1);
				}
			});
		} catch (Exception e) {
			ELog.e(e);
		}
//		BDGeofence fence = new BDGeofence.Builder()
//		.setGeofenceId(Contants.GEOFENCE_ID)//设置电子围栏的ID
//		.setCircularRegion(lon, lat,radius)//设置围栏x,y经纬度及半径
//		.setExpirationDruation(Contants.GEOFENCE_DURATION)//设置围栏有效时间，单位毫秒（最长可以设置1个月）
//		.setCoordType("bd09ll")//设置坐标类型
//		.build();
//		EyesApplication.mGeofenceClient.addBDGeofence(fence, this);
	}

	/**
	 * 成功添加百度围栏的回调
	 */
//	@Override
//	public void onAddBDGeofencesResult(int arg0, String arg1) {
//		if(arg0 == BDLocationStatusCodes.SUCCESS){
//			//围栏创建成功后开启围栏并给对方推送消息，告知设置成功
//			EyesApplication.mGeofenceClient.registerGeofenceTriggerListener(this);
//			EyesApplication.mGeofenceClient.start();
//			mHandler.onSuccessAddBDGeofences();
//			try {
//				JSONObject obj = new JSONObject();
//				obj.put(PushMessageContants.TYPE, PushMessageContants.MSG_TYPE_GEOFENCE_RESULT);
//				obj.put(PushMessageContants.RESULT, true);
//				BmobPushMsgPresenter.getDefault().sendMessage(obj,EyesApplication.mMyUser.getBindedUID());
//			} catch (Exception e) {
//				ELog.e(e);
//			}
//		}
//	}
	
	/**
	 * 请求对方的位置
	 */
	public void requestTaLocate(){
		RequestLocateBean bean = new RequestLocateBean();
		bean.setType(PushMessageContants.MSG_TYPE_REQUEST_LOCATE);
		BmobPushMsgPresenter.getDefault().sendMessage(bean, EyesApplication.mMyUser.getBindedUID());
	}
	
	
	/**
	 * 发起百度定位
	 * @param ctx
	 * @param loop 是否循环定位
	 */
	public void requestMyLocate(){
		final LocationClient client = new LocationClient(EyesApplication.mContext);
		BDLocationListener listener = new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				ELog.d("longtitude="+arg0.getLongitude()+"&latitude="+arg0.getLatitude()+"&city="+arg0.getCity());
				RequestLocateResultBean bean = new RequestLocateResultBean();
				bean.setType(PushMessageContants.MSG_TYPE_REQUEST_LOCATE_RESULT);
				bean.setLat(arg0.getLatitude());
				bean.setLng(arg0.getLongitude());
				bean.setCity(arg0.getCity());
				LatLng geoFenceLatLng = SPUtil.getGeofence();
				LatLng curLatLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				double distance = DistanceUtil.getDistance(geoFenceLatLng,curLatLng);
				bean.setDistance(distance);
				BmobPushMsgPresenter.getDefault().sendMessage(bean, EyesApplication.mMyUser.getBindedUID());
				mHandler.onLocated(arg0, distance);
			}
		};
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Device_Sensors);
		option.setCoorType("bd09ll");
		option.setScanSpan(15000);//设置发起定位请求的间隔时间为15s
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		client.setLocOption(option);
		client.registerLocationListener(listener);
		client.start();
		client.requestLocation();
	}
	
	/**
	 * 计算当前位置和电子围栏中的空间距离
	 * @param p
	 */
//	public void caculateDistance(LatLng p){
//		LatLng geofenceP = SPUtil.getGeofence();
//		double distance = DistanceUtil.getDistance(p, geofenceP);
//		GeofenceBean bean = new GeofenceBean();
//		bean.setType(PushMessageContants.MSG_TYPE_GEOFENCE);
//		bean.setLat(p.latitude);
//		bean.setLng(p.longitude);
//		bean.setDistance(distance);
//		if(distance <= SPUtil.getRadius()){
//			//在电子围栏范围内
//			mHandler.onGeofenceIn(p.latitude,p.longitude,distance);
//			bean.setIn(true);
//		}else{
//			//不在电子围栏范围内
//			mHandler.onGeofenceExit(p.latitude,p.longitude,distance);
//			bean.setIn(false);
//		}
//		BmobPushMsgPresenter.getDefault().sendMessage(bean, EyesApplication.mMyUser.getBindedUID());
//	}
	
	/**
	 * 根据关键字和城市，使用百度地图进行搜索
	 * @param key
	 * @param city
	 */
	public void searchSuggestionBDMap(String key,String city){
		SuggestionSearch search = SuggestionSearch.newInstance();
		search.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
			
			@Override
			public void onGetSuggestionResult(SuggestionResult arg0) {
				mHandler.onSuggestionSearch(arg0);
			}
		});
		search.requestSuggestion(new SuggestionSearchOption().keyword(key).city(city));
	}
	
	/**
	 * 根据关键字和城市进行POI搜索
	 * @param key
	 * @param city
	 */
	public void searchPOIByKey(String key,String city){
		PoiSearch search = PoiSearch.newInstance();
		search.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult arg0) {
				if(arg0.error == SearchResult.ERRORNO.NO_ERROR){
					//获取POI检索结果
					mHandler.onPOISeach(arg0);
				}
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {
				
			}
		});
		search.searchInCity(new PoiCitySearchOption().city(city).keyword(key).pageNum(0));
	}
	
	/**
	 * 搜索详细信息
	 * @param uid
	 */
	public void searchPOIDetail(String uid){
		PoiSearch search = PoiSearch.newInstance();
		search.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {
				if(arg0.error == SearchResult.ERRORNO.NO_ERROR){
					ELog.d("onGetPoiDetailResult:"+arg0.getAddress()+":long="+arg0.getLocation().longitude+":lat="+arg0.getLocation().latitude);
					//获取Place详情页结果
					mHandler.onPOIDetailSearch(arg0);
				}
			}
		});
		search.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));
	}

//	@Override
//	public void onGeofenceExit(String arg0) {
//		//退出围栏，发送推送消息给绑定账号
//		try {
//			JSONObject obj = new JSONObject();
//			obj.put(PushMessageContants.MSG_TYPE_GEOFENCE+"", EyesApplication.mMyUser.getBind()+"离开围栏");
//			BmobPushMsgPresenter.getDefault().sendMessage(obj,EyesApplication.mMyUser.getBindedUID());
//		} catch (Exception e) {
//			ELog.e(e);
//		}
//	}
//
//	@Override
//	public void onGeofenceTrigger(String arg0) {
//		//进入围栏，发送推送消息给绑定账号
//		try {
//			JSONObject obj = new JSONObject();
//			obj.put(PushMessageContants.MSG_TYPE_GEOFENCE+"", EyesApplication.mMyUser.getBind()+"进入围栏");
//			BmobPushMsgPresenter.getDefault().sendMessage(obj,EyesApplication.mMyUser.getBindedUID());
//		} catch (Exception e) {
//			ELog.e(e);
//		}
//	}
	
	/**
	 * 保存电子围栏设置信息
	 * @param bean
	 */
	public void saveGeoFenceInfo(final Activity act,SetGeofenceResultBean bean){
		EyesApplication.mMyUser.setLat(bean.getLat()+"");
		EyesApplication.mMyUser.setLng(bean.getLng()+"");
		EyesApplication.mMyUser.setRadius(bean.getRadius()+"");
		EyesApplication.mMyUser.setIsFenced(false);
		EyesApplication.mMyUser.update(act, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				ELog.d("saveGeoFenceInfo:success");
				Intent intent = new Intent(act,MainActivity.class);
				act.startActivity(intent);
				act.finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ELog.e("saveGeoFenceInfo:onFailure"+":"+arg0+":"+arg1);
			}
		});
	}
}
