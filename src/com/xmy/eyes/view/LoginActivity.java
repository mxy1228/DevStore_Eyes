package com.xmy.eyes.view;

import com.xmy.eyes.bean.QQLoginOnMiResultBean;
import com.xmy.eyes.bean.QQLoginResultBean;
import com.xmy.eyes.bean.QQRegistOnMiResultBean;
import com.xmy.eyes.impl.ILoginHandler;
import com.xmy.eyes.presenter.ILoginPresenter;

import de.greenrobot.event.EventBus;

import android.os.Bundle;

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
		this.mPresenter.tencentLogin(this);
		EventBus.getDefault().register(this);
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
		this.mPresenter.loginOnMi(bean);
	}

	/**
	 * QQ��Ϣ��С�׽ṹ���洢ע��ɹ�
	 * @param bean
	 */
	public void onEventMainThread(QQLoginOnMiResultBean bean){
		//��С�׳ɹ�ע��󣬲�ѯ�ƴ洢�ϸ�QQ���Ƿ���ע���
		this.mPresenter.registInMiDB(bean);
	}
	
	/**
	 * ��ѯ���ƴ洢���Ƿ��Ѿ�ע��
	 * @param bean
	 */
	public void onEventMainThread(QQRegistOnMiResultBean bean){
		if(bean.ismRegisted()){
			//����Ѿ�ע��
			//TODO
		}
	}
}
