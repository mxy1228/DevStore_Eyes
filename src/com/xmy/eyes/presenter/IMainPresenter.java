package com.xmy.eyes.presenter;

import org.json.JSONObject;

import android.content.Context;

import cn.bmob.v3.listener.SaveListener;
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
import com.xmy.eyes.bean.GeofenceBean;
import com.xmy.eyes.bean.SetGeofenceResultBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.util.SPUtil;

public class IMainPresenter{

	private IMainHandler mHandler;
	
	public IMainPresenter(IMainHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * ���ðٶȵ���Χ��
	 * API�ĵ���http://developer.baidu.com/map/index.php?title=android-locsdk/guide/v4-2
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void setDBGeofence(double lon,double lat,int radius){
		try {
			//�öԷ���������Χ��
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
	
	public void setAndStartBDGeofence(Context context,double lon,double lat,int radius){
		//������Χ�������ݱ��浽SP��
		SPUtil.saveGeofence(lon, lat, radius);
		requstLocate(true);
		try {
			SetGeofenceResultBean bean = new SetGeofenceResultBean();
			bean.setType(PushMessageContants.MSG_TYPE_GEOFENCE_RESULT);
			bean.setResult(true);
			bean.setLat(lat);
			bean.setLng(lon);
			bean.setRadius(radius);
			BmobPushMsgPresenter.getDefault().sendMessage(bean,EyesApplication.mMyUser.getBindedUID());
			//Ȼ��Χ����Ϣ���浽Bmob��������
			EyesApplication.mMyUser.setLat(lat+"");
			EyesApplication.mMyUser.setLng(lon+"");
			EyesApplication.mMyUser.setRadius(radius+"");
			EyesApplication.mMyUser.setIsFenced(true);
			EyesApplication.mMyUser.update(context, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					ELog.d("setAndStartBDGeofence:onSuccess");
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
//		.setGeofenceId(Contants.GEOFENCE_ID)//���õ���Χ����ID
//		.setCircularRegion(lon, lat,radius)//����Χ��x,y��γ�ȼ��뾶
//		.setExpirationDruation(Contants.GEOFENCE_DURATION)//����Χ����Чʱ�䣬��λ���루���������1���£�
//		.setCoordType("bd09ll")//������������
//		.build();
//		EyesApplication.mGeofenceClient.addBDGeofence(fence, this);
	}

	/**
	 * �ɹ���Ӱٶ�Χ���Ļص�
	 */
//	@Override
//	public void onAddBDGeofencesResult(int arg0, String arg1) {
//		if(arg0 == BDLocationStatusCodes.SUCCESS){
//			//Χ�������ɹ�����Χ�������Է�������Ϣ����֪���óɹ�
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
	 * ����ٶȶ�λ
	 * @param ctx
	 * @param loop �Ƿ�ѭ����λ
	 */
	public void requstLocate(final boolean loop){
		final LocationClient client = new LocationClient(EyesApplication.mContext);
		BDLocationListener listener = new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				ELog.d("longtitude="+arg0.getLongitude()+"&latitude="+arg0.getLatitude()+"&city="+arg0.getCity());
				if(loop){
					//ѭ����λ������λ�ú�Χ���ռ����
					LatLng curP = new LatLng(arg0.getLatitude(), arg0.getLongitude());
					caculateDistance(curP);
				}else{
					//ֻ��λһ�Σ�����λ������ظ���ͼ��
					mHandler.onLocated(arg0.getLongitude(), arg0.getLatitude(),arg0.getCity());
					//һ��Ҫ�ٳɹ���λ��ֹͣclient��Ҫ��Ȼ��ѭ����λ
					client.stop();
				}
			}
		};
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(15000);//���÷���λ����ļ��ʱ��Ϊ15s
		option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
		client.setLocOption(option);
		client.registerLocationListener(listener);
		client.start();
		client.requestLocation();
	}
	
	/**
	 * ���㵱ǰλ�ú͵���Χ���еĿռ����
	 * @param p
	 */
	public void caculateDistance(LatLng p){
		LatLng geofenceP = SPUtil.getGeofence();
		double distance = DistanceUtil.getDistance(p, geofenceP);
		GeofenceBean bean = new GeofenceBean();
		bean.setType(PushMessageContants.MSG_TYPE_GEOFENCE);
		bean.setLat(p.latitude);
		bean.setLng(p.longitude);
		bean.setDistance(distance);
		if(distance <= SPUtil.getRadius()){
			//�ڵ���Χ����Χ��
			mHandler.onGeofenceIn(distance);
			bean.setIn(true);
		}else{
			//���ڵ���Χ����Χ��
			mHandler.onGeofenceExit(distance);
			bean.setIn(false);
		}
		BmobPushMsgPresenter.getDefault().sendMessage(bean, EyesApplication.mMyUser.getBindedUID());
	}
	
	/**
	 * ���ݹؼ��ֺͳ��У�ʹ�ðٶȵ�ͼ��������
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
	 * ���ݹؼ��ֺͳ��н���POI����
	 * @param key
	 * @param city
	 */
	public void searchPOIByKey(String key,String city){
		PoiSearch search = PoiSearch.newInstance();
		search.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult arg0) {
				if(arg0.error == SearchResult.ERRORNO.NO_ERROR){
					//��ȡPOI�������
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
	 * ������ϸ��Ϣ
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
					//��ȡPlace����ҳ���
					mHandler.onPOIDetailSearch(arg0);
				}
			}
		});
		search.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));
	}

//	@Override
//	public void onGeofenceExit(String arg0) {
//		//�˳�Χ��������������Ϣ�����˺�
//		try {
//			JSONObject obj = new JSONObject();
//			obj.put(PushMessageContants.MSG_TYPE_GEOFENCE+"", EyesApplication.mMyUser.getBind()+"�뿪Χ��");
//			BmobPushMsgPresenter.getDefault().sendMessage(obj,EyesApplication.mMyUser.getBindedUID());
//		} catch (Exception e) {
//			ELog.e(e);
//		}
//	}
//
//	@Override
//	public void onGeofenceTrigger(String arg0) {
//		//����Χ��������������Ϣ�����˺�
//		try {
//			JSONObject obj = new JSONObject();
//			obj.put(PushMessageContants.MSG_TYPE_GEOFENCE+"", EyesApplication.mMyUser.getBind()+"����Χ��");
//			BmobPushMsgPresenter.getDefault().sendMessage(obj,EyesApplication.mMyUser.getBindedUID());
//		} catch (Exception e) {
//			ELog.e(e);
//		}
//	}
	
	/**
	 * �������Χ��������Ϣ
	 * @param bean
	 */
	public void saveGeoFenceInfo(Context ctx,SetGeofenceResultBean bean){
		EyesApplication.mMyUser.setLat(bean.getLat()+"");
		EyesApplication.mMyUser.setLng(bean.getLng()+"");
		EyesApplication.mMyUser.setRadius(bean.getRadius()+"");
		EyesApplication.mMyUser.setIsFenced(false);
		EyesApplication.mMyUser.update(ctx, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				ELog.d("saveGeoFenceInfo:success");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				ELog.e("saveGeoFenceInfo:onFailure"+":"+arg0+":"+arg1);
			}
		});
	}
}
