package com.aditi.familytracker;

public interface NotifyDataListener {
	
	public static final int NOTIFY_OK = 0;
	public static final int NOTIFY_FAILED = 1;
	
	public void onNotify(int id);

}
