package com.xmy.eyes.view;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.R;
import com.xmy.eyes.bean.SetGeofenceResultBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.presenter.IMainPresenter;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements IMainHandler,OnClickListener,OnEditorActionListener{

	private MapView mMapView;
	private EditText mET;
	private ImageButton mSearchIBtn;
	private Button mTestBtn;
	
	private IMainPresenter mPresenter;
	private BaiduMap mMap;
	private String mCurrentCity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		initEvent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void initView() {
		this.mMapView = (MapView)findViewById(R.id.main_map_view);
		this.mET = (EditText)findViewById(R.id.main_et);
		this.mSearchIBtn = (ImageButton)findViewById(R.id.main_search_ibtn);
		this.mTestBtn = (Button)findViewById(R.id.testBtn);
	}

	@Override
	protected void initData() {
		this.mMap = mMapView.getMap();
		this.mPresenter = new IMainPresenter(this);
		//发起百度定位
		this.mPresenter.requstLocate(false);
		EventBus.getDefault().register(this);
	}

	@Override
	protected void initEvent() {
		this.mSearchIBtn.setOnClickListener(this);
		this.mET.setOnEditorActionListener(this);
		this.mTestBtn.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 成功添加电子围栏
	 */
	@Override
	public void onSuccessAddBDGeofences() {
		dissmisWaitingDialog();
		showToast("电子围栏设置成功");
	}

	/**
	 * 成功定位，然后根据经纬度及城市在百度地图上标记所在位置
	 */
	@Override
	public void onLocated(double longitude, double latitude, String city) {
		this.mCurrentCity = city;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_search_ibtn:
			poiSearch();
			break;
		case R.id.testBtn:
			BmobQuery<BmobInstallation> query = new BmobQuery<BmobInstallation>();
			query.addWhereEqualTo("uid", EyesApplication.mMyUser.getBindedUID());
			EyesApplication.mBmobPushManager.setQuery(query);
			EyesApplication.mBmobPushManager.pushMessage("点对点测试");
			Intent intent = new Intent(MainActivity.this,TestActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 根据mET中的关键字进行搜索
	 */
	private void poiSearch(){
		String key = mET.getText().toString();
		if(!TextUtils.isEmpty(key) && key != null){
			mPresenter.searchSuggestionBDMap(key, mCurrentCity);
		}
	}

	/**
	 * POI详细搜索结果
	 */
	@Override
	public void onPOIDetailSearch(final PoiDetailResult result) {
		dissmisWaitingDialog();
		View view = getLayoutInflater().inflate(R.layout.radius_view, null);
		TextView tv = (TextView)view.findViewById(R.id.radius_view_tv);
		final EditText et = (EditText)view.findViewById(R.id.radius_view_et);
		tv.setText(result.getAddress());
		new AlertDialog.Builder(MainActivity.this).setTitle(R.string.radius_title)
		.setView(view)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				int radius = Integer.valueOf(et.getText().toString());
				OverlayOptions opt = new CircleOptions()
				.center(result.getLocation())
				.radius(radius)
				.stroke(new Stroke(5, Color.parseColor("#FFFFFF")))
				.fillColor(Color.parseColor("#55669dd3"));
				mMap.addOverlay(opt);
				mPresenter.setDBGeofence(result.getLocation().longitude, result.getLocation().latitude, radius);
				showWaitingDialog();
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
		
	}

	/**
	 * POI搜索结果
	 */
	@Override
	public void onPOISeach(PoiResult result) {
		if(result != null){
			mMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mMap);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			mMap.setOnMarkerClickListener(overlay);
		}else{
			ELog.e("poi search result = null");
		}
		
	}

	/**
	 * 根据关键字百度地图给出的搜索建议
	 */
	@Override
	public void onSuggestionSearch(final SuggestionResult result) {
		String[] data = new String[result.getAllSuggestions().size()];
		for(SuggestionInfo info : result.getAllSuggestions()){
			data[result.getAllSuggestions().indexOf(info)] = info.city+info.district+info.key;
		}
		new AlertDialog.Builder(this)
		.setItems(data, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPresenter.searchPOIByKey(result.getAllSuggestions().get(which).key
						, result.getAllSuggestions().get(which).city);
				dialog.dismiss();
			}
		})
		.show();
	}


	private class MyPoiOverlay extends PoiOverlay{

		public MyPoiOverlay(BaiduMap arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean onPoiClick(final int arg0) {
			showWaitingDialog();
			PoiInfo info = getPoiResult().getAllPoi().get(arg0);
			mPresenter.searchPOIDetail(info.uid);
			return true;
		}
		
	}


	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_SEARCH:
			poiSearch();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onGeofenceExit(double distance) {
		showDebugToast("exit:"+distance);
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = new Notification(R.drawable.search_btn, "离开围栏", 0);
		RemoteViews view = new RemoteViews(getPackageName(), R.layout.notifacation_view);
		view.setTextViewText(R.id.notification_view_tv, "离开围栏");
		notif.contentView = view;
		notif.flags = Notification.FLAG_NO_CLEAR;
		manager.notify(1, notif);
	}

	@Override
	public void onGeofenceIn(double distance) {
		showDebugToast("in:"+distance);
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = new Notification(R.drawable.search_btn, "进入围栏", 0);
		RemoteViews view = new RemoteViews(getPackageName(), R.layout.notifacation_view);
		view.setTextViewText(R.id.notification_view_tv, "进入围栏");
		notif.contentView = view;
		notif.flags = Notification.FLAG_NO_CLEAR;
		manager.notify(1, notif);
	}
	
	/**
	 * 给对方设置电子围栏的结果
	 * @param bean
	 */
	public void onEventMainThread(SetGeofenceResultBean bean){
		dissmisWaitingDialog();
		if(bean.isResult()){
			showDebugToast("给对方设置电子围栏成功");
			mPresenter.saveGeoFenceInfo(MainActivity.this, bean);
		}
	}
}

