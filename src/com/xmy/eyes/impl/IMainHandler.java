package com.xmy.eyes.impl;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xmy.eyes.bean.GeofenceStateChangeBean;

public interface IMainHandler {

	/**
	 * �ɹ���Ӱٶ�Χ��
	 */
	public void onSuccessAddBDGeofences();
	
	/**
	 * �ɹ��ٶȶ�λ
	 * @param longitude
	 * @param latitude
	 * @param city
	 */
	public void onLocated(BDLocation location,double distance);
	
	/**
	 * ���ݹؼ��ֽ��еص�����
	 * @param result
	 */
	public void onSuggestionSearch(SuggestionResult result);
	
	/**
	 * POI��ϸ����
	 * @param result
	 */
	public void onPOIDetailSearch(PoiDetailResult result);
	
	/**
	 * POI����
	 * @param result
	 */
	public void onPOISeach(PoiResult result);
	
	/**
	 * �����������ѯ���
	 * @param result
	 */
	public void onGeoCodeResult(ReverseGeoCodeResult result);
	
	/**
	 * ��Ե���Χ����λ���иı�
	 * @param bean
	 */
	public void onGeofenceStateChanged(GeofenceStateChangeBean bean);
	
	/**
	 * �ɹ����ò���������Χ��
	 */
	public void setAndStartGeoFenceSuccess();
}
