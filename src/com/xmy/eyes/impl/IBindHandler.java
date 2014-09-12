package com.xmy.eyes.impl;

import com.xmy.eyes.bean.MyUser;

public interface IBindHandler {

	//在Bmob上搜索
	public void onSearch(MyUser user);
	
	//在Bmob上修改绑定信息
	public void onBind(boolean result);
}
