package com.xmy.eyes.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class MyUser extends BmobObject implements Serializable{

	private String uid;
	private String bind;
	private String username;
//	private String installationId;
//	private String bindInstallationId;
	private String bindedUID;
	private Boolean isFenced;
	private String lat;
	private String lng;
	private String radius;
	
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
	public String getBindedUID() {
		return bindedUID;
	}
	public void setBindedUID(String bindedUID) {
		this.bindedUID = bindedUID;
	}
	public Boolean getIsFenced() {
		return isFenced;
	}
	public void setIsFenced(Boolean isFenced) {
		this.isFenced = isFenced;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	
	
//	public String getInstallationId() {
//		return installationId;
//	}
//	public void setInstallationId(String installationId) {
//		this.installationId = installationId;
//	}
//	public String getBindInstallationId() {
//		return bindInstallationId;
//	}
//	public void setBindInstallationId(String bindInstallationId) {
//		this.bindInstallationId = bindInstallationId;
//	}
	
}
