package com.xmy.eyes.bean;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;

public class MyBmobInstallation extends BmobInstallation {

	
	public MyBmobInstallation(Context arg0) {
		super(arg0);
	}
	private String uid;
	private String username;
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
