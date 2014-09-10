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
import com.xmy.eyes.R;
import com.xmy.eyes.impl.IMainHandler;
import com.xmy.eyes.presenter.IMainPresenter;

public class MainActivity extends BaseActivity implements IMainHandler,OnClickListener,OnEditorActionListener{

	private MapView mMapView;
	private EditText mET;
	private ImageButton mSearchIBtn;
	
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
	}

	@Override
	protected void initData() {
		this.mMap = mMapView.getMap();
		this.mPresenter = new IMainPresenter(this);
		//����ٶȶ�λ
		this.mPresenter.requstLocate(this);
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
	 * �ɹ���ӵ���Χ��
	 */
	@Override
	public void onSuccessAddBDGeofences() {
		dissmisWaitingDialog();
		showToast("����Χ�����óɹ�");
	}

	/**
	 * �ɹ���λ��Ȼ����ݾ�γ�ȼ������ڰٶȵ�ͼ�ϱ������λ��
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

		default:
			break;
		}
	}
	
	/**
	 * ����mET�еĹؼ��ֽ�������
	 */
	private void poiSearch(){
		String key = mET.getText().toString();
		if(!TextUtils.isEmpty(key) && key != null){
			mPresenter.searchSuggestionBDMap(key, mCurrentCity);
		}
	}

	/**
	 * POI��ϸ�������
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
	 * POI�������
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
	 * ���ݹؼ��ְٶȵ�ͼ��������������
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
}

