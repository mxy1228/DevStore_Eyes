package com.xmy.eyes.impl;

import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;

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
	public void onLocated(double longitude,double latitude,String city);
	
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
	 * �뿪����Χ��
	 */
	public void onGeofenceExit(double distance);
	
	/**
	 * �������Χ��
	 */
	public void onGeofenceIn(double distance);
	
}
