package com.xmy.eyes.presenter;

import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
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
import com.xmy.eyes.Contants;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.impl.IMainHandler;

public class IMainPresenter implements OnAddBDGeofencesResultListener,OnGeofenceTriggerListener{

	private IMainHandler mHandler;
	private MyUser mMyUser;
	
	public IMainPresenter(IMainHandler handler,MyUser user){
		this.mHandler = handler;
		this.mMyUser = user;
	}
	
	/**
	 * ���ðٶȵ���Χ��
	 * API�ĵ���http://developer.baidu.com/map/index.php?title=android-locsdk/guide/v4-2
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void setDBGeofence(double x,double y,int radius){
		BDGeofence fence = new BDGeofence.Builder()
		.setGeofenceId(Contants.GEOFENCE_ID)//���õ���Χ����ID
		.setCircularRegion(x, y,radius)//����Χ��x,y��γ�ȼ��뾶
		.setExpirationDruation(Contants.GEOFENCE_DURATION)//����Χ����Чʱ�䣬��λ���루���������1���£�
		.setCoordType("bd09ll")//������������
		.build();
		EyesApplication.mGeofenceClient.addBDGeofence(fence, this);
	}

	/**
	 * �ɹ���Ӱٶ�Χ���Ļص�
	 */
	@Override
	public void onAddBDGeofencesResult(int arg0, String arg1) {
		if(arg0 == BDLocationStatusCodes.SUCCESS){
			//Χ�������ɹ�����Է�������Ϣ���öԷ���������Χ��
			
			
//			EyesApplication.mGeofenceClient.registerGeofenceTriggerListener(this);
//			EyesApplication.mGeofenceClient.start();
//			mHandler.onSuccessAddBDGeofences();
		}
	}
	
	/**
	 * ����ٶȶ�λ
	 * @param ctx
	 */
	public void requstLocate(Context ctx){
		final LocationClient client = new LocationClient(ctx);
		BDLocationListener listener = new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				ELog.d("longtitude="+arg0.getLongitude()+"&latitude="+arg0.getLatitude()+"&city="+arg0.getCity());
				mHandler.onLocated(arg0.getLongitude(), arg0.getLatitude(),arg0.getCity());
				//һ��Ҫ�ٳɹ���λ��ֹͣclient��Ҫ��Ȼ��ѭ����λ
				client.stop();
			}
		};
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5s
		option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
		client.setLocOption(option);
		client.registerLocationListener(listener);
		client.start();
		client.requestLocation();
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

	@Override
	public void onGeofenceExit(String arg0) {
		//�˳�Χ��������������Ϣ�����˺�
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("installationId", mMyUser.getBindInstallationId());
		EyesApplication.mBmobPushManager.setQuery(query);
		EyesApplication.mBmobPushManager.pushMessage(mMyUser.getBind()+"�뿪Χ��");
	}

	@Override
	public void onGeofenceTrigger(String arg0) {
		//����Χ��������������Ϣ�����˺�
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo("installationId", mMyUser.getBindInstallationId());
		EyesApplication.mBmobPushManager.setQuery(query);
		EyesApplication.mBmobPushManager.pushMessage(mMyUser.getBind()+"����Χ��");
	}
	
}
