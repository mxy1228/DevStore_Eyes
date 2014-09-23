package com.xmy.eyes.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.R;
import com.xmy.eyes.bean.GeofenceBean;
import com.xmy.eyes.bean.GeofenceStateChangeBean;
import com.xmy.eyes.bean.RequestLocateResultBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.impl.IShareHandler;
import com.xmy.eyes.presenter.IMainPresenter;
import com.xmy.eyes.presenter.NotificationPresenter;
import com.xmy.eyes.presenter.SharePresenter;
import com.xmy.eyes.util.SPUtil;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements OnClickListener,IMainHandler,IShareHandler{

	private Button mBtn;
	private TextView mGeofenceTV;
	private TextView mBindTV;
	private TextView mRadiusTV;
	private TextView mDistanceTV;
	private Button mShareBtn;
	
	private IMainPresenter mPresenter;
	private SharePresenter mSharePresenter;
	private SharedPreferences mSp;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	protected void initView() {
		this.mBtn = (Button)findViewById(R.id.main_btn);
		this.mGeofenceTV = (TextView)findViewById(R.id.main_geofence_tv);
		this.mBindTV = (TextView)findViewById(R.id.main_bind_tv);
		this.mRadiusTV = (TextView)findViewById(R.id.main_radius_tv);
		this.mDistanceTV = (TextView)findViewById(R.id.main_distance_tv);
		this.mBtn.setVisibility(EyesApplication.mMyUser.getIsFenced() ? View.GONE : View.VISIBLE);
		this.mShareBtn = (Button)findViewById(R.id.main_share_btn);
	}

	@Override
	protected void initData() {
		this.mPresenter = new IMainPresenter(this);
		this.mSharePresenter = new SharePresenter(this);
		initTVData();
		EventBus.getDefault().register(this);
		//如果用户已经有经纬度及半径的属性并且是被设置围栏的一方，则直接开启围栏
		if(EyesApplication.mMyUser.getRadius() != null && EyesApplication.mMyUser.getIsFenced()){
			this.mPresenter.setAndStartBDGeofence(this
					, Double.valueOf(EyesApplication.mMyUser.getLng())
					, Double.valueOf(EyesApplication.mMyUser.getLat())
					, Integer.valueOf(EyesApplication.mMyUser.getRadius()));
		}
		this.mSp = SPUtil.getUserInfoSP();
	}

	@Override
	protected void initEvent() {
		this.mBtn.setOnClickListener(this);
		this.mSp.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				initTVData();
			}
		});
		this.mShareBtn.setOnClickListener(this);
	}
	
	private void initTVData(){
		this.mBindTV.setText(getString(R.string.current_bind_user, EyesApplication.mMyUser.getBind()));
		this.mRadiusTV.setText(getString(R.string.current_radius, EyesApplication.mMyUser.getRadius()));
		this.mGeofenceTV.setText(getString(R.string.geofence_center,getString(R.string.searching)));
		this.mDistanceTV.setText(R.string.locating);
		mPresenter.getGeoCode(Double.valueOf(EyesApplication.mMyUser.getLat()), Double.valueOf(EyesApplication.mMyUser.getLng()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn:
			Intent intent = new Intent(MainActivity.this,MapActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			break;
		case R.id.main_share_btn:
			mSharePresenter.share(MainActivity.this);
			break;
		default:
			break;
		}
	}

	/**
	 * 接收到对方发来的位置信息
	 * @param bean
	 */
	public void onEventMainThread(GeofenceBean bean){
		this.mDistanceTV.setText(getString(R.string.current_distance, EyesApplication.mMyUser.getBind(),bean.getDistance()));
	}

	@Override
	public void onSuccessAddBDGeofences() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSuggestionSearch(SuggestionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPOIDetailSearch(PoiDetailResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPOISeach(PoiResult result) {
		// TODO Auto-generated method stub
		
	}
	
	public void onEventMainThread(RequestLocateResultBean bean){
		this.mDistanceTV.setText(getString(R.string.current_distance, "",Double.valueOf(bean.getDistance()).intValue()+""));
	}

	@Override
	public void onLocated(BDLocation location, double distance) {
		this.mDistanceTV.setText(getString(R.string.current_distance, "",Double.valueOf(distance).intValue()+""));
	}

	/**
	 * 反向地理编码查询结果
	 */
	@Override
	public void onGeoCodeResult(ReverseGeoCodeResult result) {
		this.mGeofenceTV.setText(getString(R.string.geofence_center,result.getAddress()));
	}

	/**
	 * 相对围栏位置状态改变
	 */
	@Override
	public void onGeofenceStateChanged(GeofenceStateChangeBean bean) {
		new NotificationPresenter().showNotification(bean, MainActivity.this);
	}

	@Override
	public void setAndStartGeoFenceSuccess() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 分享成功
	 */
	@Override
	public void onSharedSuccess() {
		showToast(R.string.share_success);
	}
	
}
