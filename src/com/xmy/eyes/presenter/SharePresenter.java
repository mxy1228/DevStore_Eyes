package com.xmy.eyes.presenter;

import java.util.HashMap;

import android.content.Context;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;

import com.xmy.eyes.impl.IShareHandler;

public class SharePresenter {

	private IShareHandler mHandler;
	
	public SharePresenter(IShareHandler handler){
		this.mHandler = handler;
	}
	
	/**
	 * ��������΢��
	 */
	public void share(Context ctx){
		ShareParams params = new ShareParams();
		params.setTitle("�������");
		params.setTitleUrl("http://www.baidu.com");
		params.setText("�������");
		params.setSite("�ٶ�");
		params.setSiteUrl("http://www.baidu.com");
		Platform qzone = ShareSDK.getPlatform(QZone.NAME);
		qzone.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				mHandler.onSharedSuccess();
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		qzone.share(params);
	}
}
