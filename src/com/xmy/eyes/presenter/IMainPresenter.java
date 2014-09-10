package com.xmy.eyes.presenter;

import android.content.Context;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.impl.IMainHandler;

public class IMainPresenter implements OnAddBDGeofencesResultListener{

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
	public void setDBGeofence(double x,double y,int radius){
		BDGeofence fence = new BDGeofence.Builder()
		.setGeofenceId(Contants.GEOFENCE_ID)//设置电子围栏的ID
		.setCircularRegion(x, y,radius)//设置围栏x,y经纬度及半径
		.setExpirationDruation(Contants.GEOFENCE_DURATION)//设置围栏有效时间，单位毫秒（最长可以设置1个月）
		.setCoordType("bd09ll")//设置坐标类型
		.build();
		EyesApplication.mGeofenceClient.addBDGeofence(fence, this);
	}

	/**
	 * 成功添加百度围栏的回调
	 */
	@Override
	public void onAddBDGeofencesResult(int arg0, String arg1) {
		if(arg0 == BDLocationStatusCodes.SUCCESS){
			//围栏创建成功
			mHandler.onSuccessAddBDGeofences();
		}
	}
	
	/**
	 * 发起百度定位
	 * @param ctx
	 */
	public void requstLocate(Context ctx){
		final LocationClient client = new LocationClient(ctx);
		BDLocationListener listener = new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				ELog.d("longtitude="+arg0.getLongitude()+"&latitude="+arg0.getLatitude()+"&city="+arg0.getCity());
				mHandler.onLocated(arg0.getLongitude(), arg0.getLatitude(),arg0.getCity());
				//一定要再成功定位后停止client，要不然会循环定位
				client.stop();
			}
		};
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5s
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		client.setLocOption(option);
		client.registerLocationListener(listener);
		client.start();
		client.requestLocation();
	}
	
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
	
	/**
	 * 根据百度给出的搜索建议地址转换为地理编码
	 * @param info
	 */
//	public void getGeoCode(SuggestionInfo info){
//		GeoCoder coder = GeoCoder.newInstance();
//		coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
//			
//			@Override
//			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
//				
//				//获取反响地理编码结果
//			}
//			
//			@Override
//			public void onGetGeoCodeResult(GeoCodeResult arg0) {
//				//获取地址编码结果
//				if(arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR){
//					//没有检测到结果
//				}
//				mHandler.onGetGeoCode(arg0);
//			}
//		});
//		coder.geocode(new GeoCodeOption().city(info.city).address(info.district+info.key));
//	}
}
