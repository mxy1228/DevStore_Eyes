package com.xmy.eyes.bean;

import cn.bmob.v3.BmobObject;

public class MyUser extends BmobObject {

	private String uid;
	private String bind;
	private String username;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getBind() {
		return bind;
	}
	public void setBind(String bind) {
		this.bind = bind;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
