package com.xmy.eyes.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xmy.eyes.ELog;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.R;
import com.xmy.eyes.bean.GeofenceStateChangeBean;
import com.xmy.eyes.bean.RequestLocateResultBean;
import com.xmy.eyes.bean.SetGeofenceResultBean;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.presenter.IMainPresenter;

import de.greenrobot.event.EventBus;

public class MapActivity extends BaseActivity implements IMainHandler,OnClickListener,OnEditorActionListener{

	private MapView mMapView;
	private EditText mET;
	private ImageButton mSearchIBtn;
//	private Button mTestBtn;
	
	private IMainPresenter mPresenter;
	private BaiduMap mMap;
	private String mCurrentCity;
	private Marker mUserMarker;
	private Marker mGeofenceMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
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
//		this.mTestBtn = (Button)findViewById(R.id.testBtn);
	}

	@Override
	protected void initData() {
		this.mMap = mMapView.getMap();
		this.mPresenter = new IMainPresenter(this);
		if(EyesApplication.mMyUser.getRadius() != null && !EyesApplication.mMyUser.getIsFenced()){
			//如果是发起定位的一方，则请求对方现在的位置信息
			this.mPresenter.requestTaLocate();
		}else{
			//如果是被围栏的一方，则将自己的位置信息发送给对方
			this.mPresenter.requestMyLocate();
		}
		EventBus.getDefault().register(this);
		if(EyesApplication.mMyUser.getLat() != null && EyesApplication.mMyUser.getLng() != null){
			LatLng geofence = new LatLng(Double.valueOf(EyesApplication.mMyUser.getLat()), Double.valueOf(EyesApplication.mMyUser.getLng()));
			OverlayOptions opt = new CircleOptions()
			.center(geofence)
			.radius(Integer.valueOf(EyesApplication.mMyUser.getRadius()))
			.stroke(new Stroke(5, Color.parseColor("#FFFFFF")))
			.fillColor(Color.parseColor("#55669dd3"));
			mMap.addOverlay(opt);
		}
	}

	@Override
	protected void initEvent() {
		this.mSearchIBtn.setOnClickListener(this);
		this.mET.setOnEditorActionListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		EventBus.getDefault().unregister(this);
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
	}

	/**
	 * 成功定位，然后根据经纬度及城市在百度地图上标记所在位置
	 */
	@Override
	public void onLocated(BDLocation location, double distance) {
		this.mCurrentCity = location.getCity();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_search_ibtn:
			poiSearch();
			break;
//		case R.id.testBtn:
//			BmobQuery<BmobInstallation> query = new BmobQuery<BmobInstallation>();
//			query.addWhereEqualTo("uid", EyesApplication.mMyUser.getBindedUID());
//			EyesApplication.mBmobPushManager.setQuery(query);
//			EyesApplication.mBmobPushManager.pushMessage("点对点测试");
////			Intent intent = new Intent(MapActivity.this,TestActivity.class);
////			startActivity(intent);
//			break;
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
		new AlertDialog.Builder(MapActivity.this).setTitle(R.string.radius_title)
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


	/**
	 * 给对方设置电子围栏的结果
	 * @param bean
	 */
	public void onEventMainThread(SetGeofenceResultBean bean){
		dissmisWaitingDialog();
		if(bean.isResult()){
			showDebugToast(R.string.set_geofence_success);
			mPresenter.saveGeoFenceInfo(MapActivity.this, bean);
		}
	}
	
	/**
	 * 接收到对方的定位结果
	 * @param bean
	 */
	public void onEventMainThread(RequestLocateResultBean bean){
		if(mUserMarker != null){
			mUserMarker.remove();
		}
		this.mCurrentCity = bean.getCity();
		LatLng location = new LatLng(bean.getLat(), bean.getLng());
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bd_point);
		OverlayOptions opt = new MarkerOptions().position(location).icon(bitmap);
		mUserMarker = (Marker)(mMap.addOverlay(opt));
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(location);
		mMap.animateMapStatus(u);
		LatLng geofence = new LatLng(Double.valueOf(EyesApplication.mMyUser.getLat()), Double.valueOf(EyesApplication.mMyUser.getLng()));
		double distance = DistanceUtil.getDistance(location, geofence);
		ELog.v("distance = "+distance);
	}

	/**
	 * 反向地理编码查询结果
	 */
	@Override
	public void onGeoCodeResult(ReverseGeoCodeResult result) {
		
	}

	@Override
	public void onGeofenceStateChanged(GeofenceStateChangeBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAndStartGeoFenceSuccess() {
		// TODO Auto-generated method stub
		
	}
}

