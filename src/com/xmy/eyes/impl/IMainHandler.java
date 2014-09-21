package com.xmy.eyes.impl;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xmy.eyes.bean.GeofenceStateChangeBean;

public interface IMainHandler {

	/**
	 * 成功添加百度围栏
	 */
	public void onSuccessAddBDGeofences();
	
	/**
	 * 成功百度定位
	 * @param longitude
	 * @param latitude
	 * @param city
	 */
	public void onLocated(BDLocation location,double distance);
	
	/**
	 * 根据关键字进行地点搜索
	 * @param result
	 */
	public void onSuggestionSearch(SuggestionResult result);
	
	/**
	 * POI详细搜索
	 * @param result
	 */
	public void onPOIDetailSearch(PoiDetailResult result);
	
	/**
	 * POI搜索
	 * @param result
	 */
	public void onPOISeach(PoiResult result);
	
	/**
	 * 反向地理编码查询结果
	 * @param result
	 */
	public void onGeoCodeResult(ReverseGeoCodeResult result);
	
	/**
	 * 相对地理围栏的位置有改变
	 * @param bean
	 */
	public void onGeofenceStateChanged(GeofenceStateChangeBean bean);
	
}
