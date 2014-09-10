package com.xmy.eyes.view;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.xmy.eyes.EyesApplication;

import android.os.Bundle;

public class LoginActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		EyesApplication.mTencent.login(this, "get_simple_userinfo", new IUiListener() {
			
			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Object arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

}
