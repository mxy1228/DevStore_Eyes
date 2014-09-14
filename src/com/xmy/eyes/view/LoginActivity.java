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
			intent.setClass(LoginActivity.this,MainActivity.class);
		}else{
			//还未绑定
			intent.setClass(LoginActivity.this,BindActivity.class);
		}
		startActivity(intent);
	}

	/**
	 * QQ信息在小米结构化存储注册成功
	 * @param bean
	 */
//	public void onEventMainThread(QQLoginOnMiResultBean bean){
//		//在小米成功注册后，查询云存储上该QQ号是否已注册过
//		this.mPresenter.registInMiDB(bean);
//	}
	
	/**
	 * 查询在云存储上是否已经注册
	 * @param bean
	 */
//	public void onEventMainThread(QQRegistOnMiResultBean bean){
//		if(bean.ismRegisted()){
//			//如果已经注册
//			//TODO
//		}
//	}
}
