package com.xmy.eyes.impl;

import com.baidu.location.BDLocation;
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
	
	
}
