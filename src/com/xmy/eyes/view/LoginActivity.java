package com.xmy.eyes.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.R;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.impl.ILoginHandler;
import com.xmy.eyes.presenter.ILoginPresenter;

public class LoginActivity extends BaseActivity implements ILoginHandler,OnClickListener{

	private ILoginPresenter mPresenter;
	
	private Button mLoginBtn;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.login);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	protected void initView() {
		this.mLoginBtn = (Button)findViewById(R.id.login_btn);
	}

	@Override
	protected void initData() {
		this.mPresenter = new ILoginPresenter(this);
		this.mPresenter.checkUpdate(this);
	}

	@Override
	protected void initEvent() {
		this.mLoginBtn.setOnClickListener(this);
	}

	/**
	 * 成功在Bmob上注册
	 */
	@Override
	public void onSuccessRegistOnBmob(MyUser user) {
		dissmisWaitingDialog();
		EyesApplication.mMyUser = user;
		Intent intent = new Intent();
		intent.putExtra("user", user);
		if(user.getBind() != null){
			//已经成功绑定
			if(user.getRadius() == null ){
				//如果还没设置围栏，则跳转到MapActivity
				intent.setClass(LoginActivity.this,MapActivity.class);
			}else{
				//如果已经设置了围栏，则跳转到MainActivity
				intent.setClass(LoginActivity.this, MainActivity.class);
			}
		}else{
			//还未绑定
			intent.setClass(LoginActivity.this,BindActivity.class);
		}
		startActivity(intent);
		LoginActivity.this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			showWaitingDialog();
			this.mPresenter.baiduLogin(this);
			break;

		default:
			break;
		}
	}

}
