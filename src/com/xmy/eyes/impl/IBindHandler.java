package com.xmy.eyes.impl;

import com.xmy.eyes.bean.MyUser;

public interface IBindHandler {

	//��Bmob������
	public void onSearch(MyUser user);
	
	//��Bmob���޸İ���Ϣ
	public void onBind(boolean result);
}
