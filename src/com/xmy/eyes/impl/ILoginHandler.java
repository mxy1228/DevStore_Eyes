package com.xmy.eyes.impl;

import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.bean.QQLoginResultBean;

public interface ILoginHandler {

	//QQ
	public void onQQLoginSuccess(QQLoginResultBean bean);
	
	//��Bmob��ע��ɹ�
	public void onSuccessRegistOnBmob(MyUser user);
}
