package com.xmy.eyes.bean;

public class GeofenceBean {

	private double lat;
	private double lng;
	private double distance;
	private boolean in;
	private int type;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public boolean isIn() {
		return in;
	}
	public void setIn(boolean in) {
		this.in = in;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	
}
