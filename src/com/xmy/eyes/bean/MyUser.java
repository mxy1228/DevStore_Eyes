package com.xmy.eyes.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class MyUser extends BmobObject implements Serializable{

	private String uid;
	private String bind;
	private String username;
	private String installationId;
	private String bindInstallationId;
	
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
	public String getInstallationId() {
		return installationId;
	}
	public void setInstallationId(String installationId) {
		this.installationId = installationId;
	}
	public String getBindInstallationId() {
		return bindInstallationId;
	}
	public void setBindInstallationId(String bindInstallationId) {
		this.bindInstallationId = bindInstallationId;
	}
	
}
