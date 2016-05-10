package com.aditi.familytracker;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;


public class UserModel implements Serializable {
	private String name;
	private String userID;
	private double lat;
	private double lng;

	public UserModel() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {
		this.setUserID(HotdealUtilities.getDataString(jSonInfo, "categoryId"));
		this.setName(HotdealUtilities.getDataString(jSonInfo, "name"));
		this.setLat(HotdealUtilities.getDataDouble(jSonInfo, "categoryId"));
		this.setLng(HotdealUtilities.getDataDouble(jSonInfo, "name"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

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
}
