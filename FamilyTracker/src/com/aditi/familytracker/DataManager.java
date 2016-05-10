package com.aditi.familytracker;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hotdeal.connection.JsonObjectInterface;
import com.hotdeal.connection.VolleyRequestCustom;

public class DataManager {
	private static DataManager ourInstance = new DataManager();
	VolleyRequestCustom jsonObjRequest;
	private final String TAG_REQUEST = "fuck_bitch";
	private RequestQueue mVolleyQueue;
	private String errorMess = "f";
	private String KEY_ERROR = "error";
	private String KEY_DATA = "data";

	public String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		DataManager.message = message;
	}

	private static String message = "";

	public static DataManager getInstance() {
		return ourInstance;
	}

	private DataManager() {

	}

	public void showProgress(final Context activity) {

		// mProgress = ProgressDialog.show(activity, "", "Loading...");
		// int v = R.layout.chochay;
		// mProgress.setContentView(v);
		// mProgress = ProgressDialog.show(activity, "", "", true, false);

		try {
			if (!isShowPro) {
				isShowPro = true;
				((Activity) activity).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						showDialog(activity);
					}
				});

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	ProgressDialog pd;

	public void showDialog(Context ac) {
		try {
			if (!((Activity) ac).isFinishing()) {
				pd = new ProgressDialog(ac);
				pd.setTitle("Please wait...");
				pd.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void stopProgress() {
		try {
			pd.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}

		isShowPro = false;
	}

	private RequestQueue getVolleyQueue(Context ac) {
		if (null == mVolleyQueue) {
			return mVolleyQueue = Volley.newRequestQueue(ac);
		}
		return mVolleyQueue;
	}

	private boolean isShowPro = false;
	private HashMap<String, String> parametersReload;

	public void callServer(final Context activity, final HashMap<String, String> parameters, final boolean showPro, final boolean isPost, final JsonObjectInterface jsonObjectInterface) {
		HotdealUtilities.showALog(parameters.toString());
		if (showPro && !isShowPro) {
			showProgress(activity);

		}
		// jsonObjRequest=new VolleyRequestCustom(Request.Method.POST,
		// ConstantValue.URL_SERVER, parameters, responseListen, errorListen);
		jsonObjRequest = new VolleyRequestCustom(Request.Method.POST, MyApp.serverURL, parameters, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				HotdealUtilities.showALog(response.toString());
				try {
					try {
						setMessage(response.getString("message").toString());
					} catch (Exception e) {
						e.printStackTrace();
					}

					jsonObjectInterface.callResultJOb(activity, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (showPro)
					stopProgress();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Handle your error types accordingly.For Timeout & No
				// connection error, you can show 'retry' button.
				// For AuthFailure, you can re login with user
				// credentials.
				// For ClientError, 400 & 401, Errors happening on
				// client side when sending api request.
				// In this case you can check how client is forming the
				// api and debug accordingly.
				// For ServerError 5xx, you can do retry or handle
				// accordingly.
				if (error instanceof NetworkError) {
					setMessage(errorMess + " (Network Error)");
				} else if (error instanceof ClientError) {
					setMessage(errorMess + " (Client Error)");
				} else if (error instanceof ServerError) {
					setMessage(errorMess + " (Server Error)");
				} else if (error instanceof AuthFailureError) {
					setMessage(errorMess + " (AuthFailure Error)");
				} else if (error instanceof ParseError) {
					setMessage(errorMess + " (Parse Error)");
				} else if (error instanceof NoConnectionError) {
					setMessage(errorMess + " (No Connection Error)");
				} else if (error instanceof TimeoutError) {
					setMessage(errorMess + " (Timeout Error)");
				}
				// HotdealUtilities.showALog(error.getMessage());

				jsonObjectInterface.callResultJOb(activity, null);
				if (showPro)
					stopProgress();
			}
		});

		// Set a retry policy in case of SocketTimeout & ConnectionTimeout
		// Exceptions. Volley does retry for you if you have specified the
		// policy.

		jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(120000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// String tag = "";
		// try {
		// tag = activity.getClass().getSimpleName();
		// } catch (Exception e) {
		// }
		jsonObjRequest.setTag(TAG_REQUEST);
		getVolleyQueue(activity).add(jsonObjRequest);
	}

	HashMap<String, String> parameters;

	public HashMap<String, String> getDefauldParams(String API, Context ac) {
		parameters = new HashMap<String, String>();
		parameters.put("API", API);
		return parameters;
	}

	private void notifiUI(NotifyDataListener no, int value) {
		if (no != null) {
			no.onNotify(value);
		}
	}

	public void login(Context activity, boolean showPro, boolean isPost, final NotifyDataListener notifyDataListener, String email, String pass) {
		HashMap<String, String> builder = getDefauldParams("", activity);
		builder.put("email", email);
		builder.put("pass", pass);
		callServer(activity, builder, showPro, isPost, new JsonObjectInterface() {
			@Override
			public void callResultJOb(Context activity, JSONObject result) {
				try {
					if (result.getInt(KEY_ERROR) == 0) {
						JSONObject resultData = result.getJSONObject(KEY_DATA);
						JSONArray listJson = resultData.getJSONArray("listProduct");
						// for (int i = 0; i < listJson.length(); i++) {
						// JSONObject jSonOb = listJson.getJSONObject(i);
						// // HotdealUtilities.showALog(jSonOb.toString());
						// DetailsModel md = new DetailsModel();
						// md.setData(jSonOb);
						// listDealMap.add(md);
						// }
						notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_OK);
					} else {
						notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_FAILED);
					}

				} catch (Exception e) {
					notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_FAILED);
					e.printStackTrace();
				}
			}
		});

	}

	public void updateLocation(Context activity, boolean showPro, boolean isPost, final NotifyDataListener notifyDataListener, String user_id, String lat, String lng) {
		HashMap<String, String> builder = getDefauldParams("", activity);
		builder.put("user_id", user_id);
		builder.put("lat", lat);
		builder.put("lng", lng);
		callServer(activity, builder, showPro, isPost, new JsonObjectInterface() {
			@Override
			public void callResultJOb(Context activity, JSONObject result) {
				try {
					if (result.getInt(KEY_ERROR) == 0) {
						JSONObject resultData = result.getJSONObject(KEY_DATA);
						JSONArray listJson = resultData.getJSONArray("listProduct");
						// for (int i = 0; i < listJson.length(); i++) {
						// JSONObject jSonOb = listJson.getJSONObject(i);
						// // HotdealUtilities.showALog(jSonOb.toString());
						// DetailsModel md = new DetailsModel();
						// md.setData(jSonOb);
						// listDealMap.add(md);
						// }
						notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_OK);
					} else {
						notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_FAILED);
					}

				} catch (Exception e) {
					notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_FAILED);
					e.printStackTrace();
				}
			}
		});

	}

	public void getAllUser(Context activity, boolean showPro, boolean isPost, final NotifyDataListener notifyDataListener) {
		HashMap<String, String> builder = getDefauldParams("", activity);
		callServer(activity, builder, showPro, isPost, new JsonObjectInterface() {
			@Override
			public void callResultJOb(Context activity, JSONObject result) {
				try {
					if (result.getInt(KEY_ERROR) == 0) {
						JSONObject resultData = result.getJSONObject(KEY_DATA);
						JSONArray listJson = resultData.getJSONArray("listProduct");
						// for (int i = 0; i < listJson.length(); i++) {
						// JSONObject jSonOb = listJson.getJSONObject(i);
						// // HotdealUtilities.showALog(jSonOb.toString());
						// DetailsModel md = new DetailsModel();
						// md.setData(jSonOb);
						// listDealMap.add(md);
						// }
						notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_OK);
					} else {
						notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_FAILED);
					}

				} catch (Exception e) {
					notifiUI(notifyDataListener, NotifyDataListener.NOTIFY_FAILED);
					e.printStackTrace();
				}
			}
		});

	}
}
