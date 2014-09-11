package com.xmy.eyes.impl;

import com.xmy.eyes.bean.MyUser;
import com.xmy.eyes.bean.QQLoginResultBean;

public interface ILoginHandler {

	//QQ
	public void onQQLoginSuccess(QQLoginResultBean bean);
	
	//在Bmob上注册成功
	public void onSuccessRegistOnBmob(MyUser user);
}
