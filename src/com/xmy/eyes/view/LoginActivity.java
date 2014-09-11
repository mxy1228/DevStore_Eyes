package com.xmy.eyes.view;

import android.content.Intent;
import android.os.Bundle;

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
	 * QQ��¼�ɹ��ص�
	 */
	@Override
	public void onQQLoginSuccess(QQLoginResultBean bean) {
		
	}

	/**
	 * �ɹ���Bmob��ע��
	 */
	@Override
	public void onSuccessRegistOnBmob(MyUser user) {
		if(user.getBind() != null){
			//�Ѿ��ɹ���
			
		}else{
			//��δ��
			Intent intent = new Intent(LoginActivity.this,BindActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * QQ��Ϣ��С�׽ṹ���洢ע��ɹ�
	 * @param bean
	 */
//	public void onEventMainThread(QQLoginOnMiResultBean bean){
//		//��С�׳ɹ�ע��󣬲�ѯ�ƴ洢�ϸ�QQ���Ƿ���ע���
//		this.mPresenter.registInMiDB(bean);
//	}
	
	/**
	 * ��ѯ���ƴ洢���Ƿ��Ѿ�ע��
	 * @param bean
	 */
//	public void onEventMainThread(QQRegistOnMiResultBean bean){
//		if(bean.ismRegisted()){
//			//����Ѿ�ע��
//			//TODO
//		}
//	}
}
