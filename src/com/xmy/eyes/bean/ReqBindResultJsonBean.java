package com.xmy.eyes.bean;

public class ReqBindResultJsonBean {

	private int type;
	private String myUid;
	private String myUserName;
	private String taUid;
	private String taUserName;
	private boolean agree;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMyUid() {
		return myUid;
	}
	public void setMyUid(String myUid) {
		this.myUid = myUid;
	}
	public String getMyUserName() {
		return myUserName;
	}
	public void setMyUserName(String myUserName) {
		this.myUserName = myUserName;
	}
	public String getTaUid() {
		return taUid;
	}
	public void setTaUid(String taUid) {
		this.taUid = taUid;
	}
	public String getTaUserName() {
		return taUserName;
	}
	public void setTaUserName(String taUserName) {
		this.taUserName = taUserName;
	}
	public boolean isAgree() {
		return agree;
	}
	public void setAgree(boolean agree) {
		this.agree = agree;
	}
	
	
	
}
