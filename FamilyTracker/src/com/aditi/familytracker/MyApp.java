package com.aditi.familytracker;

import android.app.Application;

public class MyApp extends Application {
	private String lat;
	private String lng;
	public static String serverURL="";
	private String userID;
	private String name;
	public static int device_height;
	public static int device_width;

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

	public String getUserId() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getNameOfLoginUser() {
		return name;
	}

	public void setNameOfLoginUser(String name) {
		this.name = name;
	}

}
