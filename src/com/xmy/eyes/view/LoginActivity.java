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
	}

	@Override
	protected void initEvent() {
		this.mLoginBtn.setOnClickListener(this);
	}

	/**
	 * �ɹ���Bmob��ע��
	 */
	@Override
	public void onSuccessRegistOnBmob(MyUser user) {
		EyesApplication.mMyUser = user;
		Intent intent = new Intent();
		intent.putExtra("user", user);
		if(user.getBind() != null){
			//�Ѿ��ɹ���
			if(user.getRadius() == null ){
				//�����û����Χ��������ת��MapActivity
				intent.setClass(LoginActivity.this,MapActivity.class);
			}else{
				//����Ѿ�������Χ��������ת��MainActivity
				intent.setClass(LoginActivity.this, MainActivity.class);
			}
		}else{
			//��δ��
			intent.setClass(LoginActivity.this,BindActivity.class);
		}
		startActivity(intent);
		LoginActivity.this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			this.mPresenter.baiduLogin(this);
			break;

		default:
			break;
		}
	}

}
