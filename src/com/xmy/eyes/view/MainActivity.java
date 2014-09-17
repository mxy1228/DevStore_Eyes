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
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.R;
import com.xmy.eyes.bean.GeofenceBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.presenter.IMainPresenter;
import com.xmy.eyes.util.SPUtil;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements OnClickListener,IMainHandler{

	private TextView mTV;
	private Button mBtn;
	private TextView mCurTV;
	
	private IMainPresenter mPresenter;
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
		this.mTV = (TextView)findViewById(R.id.main_geofence_tv);
		this.mBtn = (Button)findViewById(R.id.main_btn);
		this.mCurTV = (TextView)findViewById(R.id.main_cur_tv);
		this.mBtn.setVisibility(EyesApplication.mMyUser.getIsFenced() ? View.GONE : View.VISIBLE);
	}

	@Override
	protected void initData() {
		StringBuilder sb = new StringBuilder();
		sb.append("��ǰ�û���"+EyesApplication.mMyUser.getUsername());
		sb.append("\n");
		sb.append("���û���"+EyesApplication.mMyUser.getBind());
		sb.append("\n");
		sb.append("Χ��lat="+EyesApplication.mMyUser.getLat()+":lng="+EyesApplication.mMyUser.getLng());
		sb.append("\n");
		sb.append("�뾶��"+EyesApplication.mMyUser.getRadius());
		this.mTV.setText(sb.toString());
		EventBus.getDefault().register(this);
		this.mPresenter = new IMainPresenter(this);
		//����û��Ѿ��о�γ�ȼ��뾶�����Բ����Ǳ�����Χ����һ������ֱ�ӿ���Χ��
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
				StringBuilder sb = new StringBuilder();
				sb.append("��ǰ�û���"+EyesApplication.mMyUser.getUsername());
				sb.append("\n");
				sb.append("���û���"+EyesApplication.mMyUser.getBind());
				sb.append("\n");
				sb.append("Χ��lat="+SPUtil.getGeofence().latitude+":lng="+SPUtil.getGeofence().longitude);
				sb.append("\n");
				sb.append("�뾶��"+SPUtil.getRadius());
				mTV.setText(sb.toString());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn:
			Intent intent = new Intent(MainActivity.this,MapActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * ���յ��Է�������λ����Ϣ
	 * @param bean
	 */
	public void onEventMainThread(GeofenceBean bean){
		StringBuilder sb = new StringBuilder();
		sb.append("===============");
		sb.append("\n");
		sb.append("Current lat="+bean.getLat()+":lng="+bean.getLng());
		sb.append("\n");
		sb.append("���룺"+bean.getDistance());
		sb.append("\n");
		mCurTV.setText(sb.toString());
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

	@Override
	public void onLocated(BDLocation location, double distance) {
		StringBuilder sb = new StringBuilder();
		sb.append("===============");
		sb.append("\n");
		sb.append("Current lat="+location.getLatitude()+":lng="+location.getLongitude());
		sb.append("\n");
		sb.append("���룺"+distance);
		sb.append("\n");
		mCurTV.setText(sb.toString());
	}
	
}
