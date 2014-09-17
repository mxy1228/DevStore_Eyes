package com.xmy.eyes.view;

import android.content.Intent;
import android.os.Bundle;

import cn.bmob.push.BmobPush;

import com.xmy.eyes.Contants;
import com.xmy.eyes.EyesApplication;
import com.xmy.eyes.bean.MyBmobInstallation;
import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.bean.QQLoginResultBean;
import com.xmy.eyes.impl.ILoginHandler;
import com.xmy.eyes.presenter.ILoginPresenter;

public class LoginActivity extends BaseActivity implements ILoginHandler{

	private ILoginPresenter mPresenter;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		this.mPresenter = new ILoginPresenter(this);
		this.mPresenter.baiduLogin(this);
//		EventBus.getDefault().register(this);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	/**
	 * QQ登录成功回调
	 */
	@Override
	public void onQQLoginSuccess(QQLoginResultBean bean) {
		
	}

	/**
	 * 成功在Bmob上注册
	 */
	@Override
	public void onSuccessRegistOnBmob(MyUser user) {
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

}
