package com.xmy.eyes.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.xmy.eyes.R;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.presenter.IMainPresenter;

public class TestActivity extends BaseActivity implements IMainHandler{

	private TextView mTV;
	private EditText mET;
	private Button mBtn;
	
	private IMainPresenter mPresenter;
	private double mLon;
	private double mLat;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.test);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	protected void initView() {
		this.mTV = (TextView)findViewById(R.id.test_tv);
		this.mET = (EditText)findViewById(R.id.test_et);
		this.mBtn = (Button)findViewById(R.id.test_btn);
	}

	@Override
	protected void initData() {
		this.mPresenter = new IMainPresenter(this);
//		this.mPresenter.requstLocate(this);
	}

	@Override
	protected void initEvent() {
		this.mBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				mPresenter.setAndStartBDGeofence(mLon, mLat, Integer.valueOf(mET.getText().toString()));
			}
		});
	}

	@Override
	public void onSuccessAddBDGeofences() {
		mTV.setText("Œß¿∏…Ë÷√≥…π¶");
	}

	@Override
	public void onLocated(double longitude, double latitude, String city) {
		mTV.setText("lon="+longitude+":lat="+latitude);
		mLon = longitude;
		mLat = latitude;
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
	public void onGeofenceExit(double distance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGeofenceIn(double distance) {
		// TODO Auto-generated method stub
		
	}


}
