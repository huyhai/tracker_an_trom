package com.aditi.familytracker;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.model.LatLng;
import com.hotdeal.connection.JsonObjectInterface;

public class HotdealUtilities {
	public static String FORMAT_DATE = "dd/MM/yyyy";
	private static RequestQueue mVolleyQueue;
	private static JsonObjectRequest jsonObjRequest;


	public static double calculationByDistance(LatLng StartP, LatLng EndP) {
		try {
			int Radius = 6371;// radius of earth in Km
			double lat1 = StartP.latitude;
			double lat2 = EndP.latitude;
			double lon1 = StartP.longitude;
			double lon2 = EndP.longitude;
			double dLat = Math.toRadians(lat2 - lat1);
			double dLon = Math.toRadians(lon2 - lon1);
			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
			double c = 2 * Math.asin(Math.sqrt(a));
			double valueResult = Radius * c;
			double km = valueResult / 1;
			DecimalFormat newFormat = new DecimalFormat("####");
			int kmInDec = Integer.valueOf(newFormat.format(km));
			double meter = valueResult % 1000;
			int meterInDec = Integer.valueOf(newFormat.format(meter));
			// Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec +
			// " Meter   " + meterInDec);
			return Radius * c;
		} catch (Exception e) {
			return 0;
		}

	}

	public static double round(double value, int places) {
		try {
			if (places < 0)
				throw new IllegalArgumentException();

			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		} catch (Exception e) {
			return 0;
		}

	}

	public static void openMaps(Activity ac, LatLng ll, String add) {
		try {

			// mo map voi dan duong realtime
			// Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
			// list.get(id).getLatt() + "," + list.get(id).getLont());

			// mo mao voi diem bat dau va ket thuc
			// Uri gmmIntentUri =
			// Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345");

			Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + add);
//			Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + ll.latitude + "," + ll.longitude + " (" + add + ")");

			// mo mao voi label
			// Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=loc:"
			// +
			// ll.latitude + "," + ll.longitude + " (" +add + ")");
			// showALog(gmmIntentUri.toString());

			HotdealUtilities.showALog(gmmIntentUri.toString());
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			ac.startActivity(mapIntent);
		} catch (Exception e) {
//			showToast1("Không ", duration, ac)
		}
	}


	public static void makeHttpRequest(final Context c, String builder, final JsonObjectInterface jsonObjectInterface) {
		mVolleyQueue = Volley.newRequestQueue(c);

		jsonObjRequest = new JsonObjectRequest(Request.Method.GET, builder.toString(), null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (jsonObjectInterface != null) {
					jsonObjectInterface.callResultJOb(c, response);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (error instanceof NetworkError) {
				} else if (error instanceof ClientError) {
				} else if (error instanceof ServerError) {
				} else if (error instanceof AuthFailureError) {
				} else if (error instanceof ParseError) {
				} else if (error instanceof NoConnectionError) {
				} else if (error instanceof TimeoutError) {
				}
				jsonObjectInterface.callResultJOb(c, null);
				HotdealUtilities.showALog(error.toString());
			}
		});

		jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		jsonObjRequest.setTag("ccccc");
		mVolleyQueue.add(jsonObjRequest);

	}


	public static Date getDateFromString(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return convertedDate;
	}

	public static void showKB(Activity ac, View v) {
		try {
			InputMethodManager imm = (InputMethodManager) ac.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ac.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}



	public static void openBrowzer(Activity ac, String urrl) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urrl));
		ac.startActivity(browserIntent);
	}

	public static String getFacebookImage(JSONObject ob) {
		JSONObject data = null;
		String url = "";
		try {
			data = ob.getJSONObject("data");
			url = data.getString("url");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;

	}

	public static LatLng getCurrentLocation(Context ac) {
		// check if GPS enabled
		GPSTracker gps = new GPSTracker(ac);
		LatLng srcGeoPoint;
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			srcGeoPoint = new LatLng(latitude, longitude);
		} else {
			// gps.showSettingsAlert();
			double latitude = -1;
			double longitude = -1;
			srcGeoPoint = new LatLng(latitude, longitude);
		}
		return srcGeoPoint;
	}

	public static boolean checkGPSEnable(Activity ac, int reques) {
		GPSTracker gps = new GPSTracker(ac);
		if (gps.canGetLocation()) {
			return true;
		} else {
			gps.showSettingsAlert(ac, reques);
			return false;
		}
	}



	protected static int getScreenHeight(Activity ac) {
		return ac.findViewById(android.R.id.content).getHeight();
	}

	public static void hideKb(Activity ac, View v) {
		try {
			InputMethodManager imm = (InputMethodManager) ac.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		} catch (Exception e) {
		}

	}



	/*public static void loadImage(String url, ImageView img, Activity ac) {
		try {
			if (MainV2.imageLoader != null) {
			} else {
				MainV2.imageLoader = ImageLoader.getInstance();
				MainV2.imageLoader.init(ImageLoaderConfiguration.createDefault(ac));
				// options = new DisplayImageOptions.Builder()
				// .showImageForEmptyUrl(placeholder)
				// .showStubImage(placeholder).cacheOnDisc()
				// .decodingType(DecodingType.MEMORY_SAVING).build();
				MainV2.options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.img_thumb).showImageForEmptyUri(R.drawable.img_thumb).showImageOnFail(R.drawable.img_thumb)
						.cacheInMemory(true).cacheOnDisc().considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
			}
			MainV2.imageLoader.displayImage(url, img, MainV2.options);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
*/

	public static Boolean checkSDT(String num) {
		try {
			String start = num.substring(0, 1);
			if (!start.equals("0")) {
				return false;
			}
			if (num.length() > 12) {
				return false;
			}
			if (num.length() < 10) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	public static String getQuery(HashMap<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		// result.append("?token=" + this.token);
		for (String key : parameters.keySet()) {

			if (first) {
				first = false;
				result.append("?");
			}

			result.append("&");

			result.append(URLEncoder.encode(key, "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(parameters.get(key), "UTF-8"));
		}
		HotdealUtilities.showLog(result.toString());
		return result.toString();
	}



	@SuppressWarnings("deprecation")
	public static void setWH(Activity ac) {
		try {
			Display display = ac.getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();
			MyApp.device_height = height;
			MyApp.device_width = width;
			showALog("width = " + width + "|height = " + height);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public static void setHRatingBar(View v, Activity ac) {
	// if (MyApp.isTab) {
	// if (MyApp.device_width >= 800 &&
	// ac.getResources().getDisplayMetrics().density <= 1.5) {
	// HotdealUtilities.setHeight(v, 52);
	// } else {
	// HotdealUtilities.setHeight(v, 38);
	// }
	//
	// } else {
	// HotdealUtilities.setHeight(v, 32);
	// }
	// }

	public static void setHeight(View paramView, double paramDouble) {
		try {
			ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
			localLayoutParams.height = (int) (MyApp.device_height / paramDouble);
			paramView.setLayoutParams(localLayoutParams);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void setHeightHardCode(View paramView, double paramDouble) {
		try {
			ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
			localLayoutParams.height = (int) paramDouble;
			paramView.setLayoutParams(localLayoutParams);
			paramView.requestLayout();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void setWidth(View paramView, double paramDouble) {
		ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
		localLayoutParams.width = (int) (MyApp.device_width / paramDouble);
		paramView.setLayoutParams(localLayoutParams);
	}

	public static void setWidthHardCode(View paramView, double paramDouble) {
		ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
		localLayoutParams.width = (int) paramDouble;
		paramView.setLayoutParams(localLayoutParams);
	}

	public static void setWidthHeight(View paramView, double paramDouble1, double paramDouble2) {
		ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
		localLayoutParams.width = (int) (MyApp.device_width / paramDouble1);
		localLayoutParams.height = (int) (MyApp.device_height / paramDouble2);
		paramView.setLayoutParams(localLayoutParams);
	}

	public static void setWidthHeightHardCode(View paramView, double paramDouble1, double paramDouble2) {
		ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
		localLayoutParams.width = (int) paramDouble1;
		localLayoutParams.height = (int) paramDouble2;
		paramView.setLayoutParams(localLayoutParams);
	}

	public static Bitmap drawTextToBitmap(Context mContext, int hinh, String mText) {
		try {
			Resources resources = mContext.getResources();
			float scale = resources.getDisplayMetrics().density;
			Bitmap bitmap = BitmapFactory.decodeResource(resources, hinh);
			android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
			// set default bitmap config if none
			if (bitmapConfig == null) {
				bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}
			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);
			Canvas canvas = new Canvas(bitmap);
			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// text color - #3D3D3D
			paint.setColor(Color.WHITE);
			// text size in pixels
			paint.setTextSize((int) (12 * scale));
			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
			// draw text to the Canvas center
			Rect bounds = new Rect();
			paint.getTextBounds(mText, 0, mText.length(), bounds);
			int x = (bitmap.getWidth() - bounds.width()) / 6;
			int y = (bitmap.getHeight() + bounds.height()) / 5;
			canvas.drawText(mText, x * scale, y * scale, paint);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return 0;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
		// sendMessage(MyApp.context, "SETH", String.valueOf(totalHeight +
		// (listView.getDividerHeight() * (listAdapter.getCount() - 1))));
		return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	}

	public static void setListViewHeightBasedOnChildrenGV(GridView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int tt = totalHeight / 2;
		params.height = tt + (listView.getVerticalSpacing() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void setListViewHeight(ExpandableListView listView) {
		ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.EXACTLY);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupItem = listAdapter.getGroupView(i, false, null, listView);
			groupItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += groupItem.getMeasuredHeight();
			for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
				View listItem = listAdapter.getChildView(i, j, false, null, listView);
				listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

				totalHeight += listItem.getMeasuredHeight();

			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		if (height < 10)
			height = 200;
		params.height = height;
		listView.setLayoutParams(params);
		listView.requestLayout();

	}

	public static void setExpandableListViewHeightOriginal(ExpandableListView listView, int group) {
		ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.EXACTLY);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupItem = listAdapter.getGroupView(i, false, null, listView);
			groupItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

			totalHeight += groupItem.getMeasuredHeight();

			if (((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group))) {
				for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
					View listItem = listAdapter.getChildView(i, j, false, null, listView);
					listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

					totalHeight += listItem.getMeasuredHeight();

				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		if (height < 10)
			height = 200;
		params.height = height;
		listView.setLayoutParams(params);
		listView.requestLayout();

	}

	public static void showLog(Object mess) {
		try {
			// Log.v("HAI", mess + "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void showALog(Object mess) {
		try {
			Log.e("HAIA", "" + mess);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void showALog(Object mess, Exception e1) {
		try {
			// Log.e("HAIA", "" + mess, e1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String formatMoney(Object money) {
		String a = "0";
		try {
			String d = new DecimalFormat("##,##0 đ").format(money);
			a = d.replaceAll(",", ".");
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Locale locale = new Locale("vi_VN");
		// NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		// return format.format(money);
		return a;
	}

	public static String formatNumber(double money) {
		String a = "0";
		try {
			DecimalFormat df = new DecimalFormat("##,##0");
			String d = df.format(money);
			a = d.replaceAll(",", ".");
		} catch (Exception e) {
		}
		// Locale locale = new Locale("vi_VN");
		// NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		// return format.format(money);
		return a;
	}

	public static String formatMoney(String money) {
		String a = "0";
		try {
			double money1 = Double.parseDouble(money);
			String d = new DecimalFormat("##,##0 đ").format(money1);
			a = d.replaceAll(",", ".");
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Locale locale = new Locale("vi_VN","");
		// NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		// return format.format(money1);
		return a;
	}

	public static String formatMoneyDiem(String money) {
		String a = "0";
		try {
			double money1 = Double.parseDouble(money);
			String d = new DecimalFormat("##,##0 điểm").format(money1);
			a = d.replaceAll(",", ".");
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Locale locale = new Locale("vi_VN","");
		// NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		// return format.format(money1);
		return a;
	}

	public static Currency getCurrency() {
		// Locale l = new Locale("vi_VN", "");
		return Currency.getInstance("vi_VN");

	}

	public static boolean checkInternetConnection(final Context context) {

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getDataString(JSONObject jSonOb, String key) {
		String result = "";
		try {
			if (!jSonOb.getString(key).equals(JSONObject.NULL)) {
				result = jSonOb.getString(key);
			}
		} catch (JSONException e) {
			// showALog("getDataStrign loi");
			// e.printStackTrace();
		}
		return result;
	}

	public static int getDataInt(JSONObject jSonOb, String key) {
		int result = -1;
		try {
			result = jSonOb.getInt(key);
		} catch (JSONException e) {
			// showALog("getDataStrign loi");
			// e.printStackTrace();
		}
		return result;
	}

	public static double getDataDouble(JSONObject jSonOb, String key) {
		double result = -1;
		try {
			result = jSonOb.getDouble(key);
		} catch (JSONException e) {
			// showALog("getDataStrign loi");
			// e.printStackTrace();
		}
		return result;
	}

	public static String check3GorWifi(Context c) {
		ConnectivityManager manager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		// For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		// For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		if (is3g) {
			return "3G";
		} else if (isWifi) {
			return "WIFI";
		} else {
			return "UNKNOW";
		}

	}

	public static long getCurrentTime() {
		long da = 0;
		da = System.currentTimeMillis() / 1000;
		return da;
	}

	public static String md5(final String s) {
		final String MD5 = "MD5";
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				String h = Integer.toHexString(0xFF & aMessageDigest);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getBuildVertion(Activity ac) {
		PackageInfo pInfo;
		String version = "-1";
		try {
			pInfo = ac.getPackageManager().getPackageInfo(ac.getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}


	public static void createEffectTouch(View v, final int inActiveDrawble, final int activeDrawble) {
		v.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(activeDrawble);
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_UP)
					v.setBackgroundResource(inActiveDrawble);
				return false;
			}
		});
	}

	public static void createEffectTouchBack(View v) {
		v.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(R.color.darkgrey);
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE)
					v.setBackgroundResource(R.drawable.transparent);
				return false;
			}
		});
	}

	public static void setClickAnim(final View v) {
		try {
			v.setBackgroundResource(R.color.darkgrey);
			v.post(new Runnable() {

				@Override
				public void run() {
					v.setBackgroundResource(R.drawable.transparent);

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void setClickAnimButon(final View v, final int click, final int affterclick) {
		try {
			v.setBackgroundResource(click);
			v.post(new Runnable() {

				@Override
				public void run() {
					v.setBackgroundColor(affterclick);

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void setClickAnimButon(final View v, final String color) {
		try {
			v.setBackgroundResource(R.color.darkgreylight);
			v.post(new Runnable() {

				@Override
				public void run() {
					v.setBackgroundColor(Color.parseColor(color));

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void showDialog(final String message, final Activity ac) {
		ac.runOnUiThread(new Runnable() {
			public void run() {
				showDialogOk(message, ac).show();
			}
		});
	}

	public static AlertDialog showDialogOk(final String message, final Context ac) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
		builder.setTitle(ac.getString(R.string.app_name));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(message).setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int id) {
				// Do nothing.
			}
		});

		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.setCanceledOnTouchOutside(true);
		return alert;
	}

	public static void showToast(final String message, final int duration, final Context ac) {
		try {
			((Activity) ac).runOnUiThread(new Runnable() {
				public void run() {
					// if (MyApp.isRunningApp) {
					showDialogOk(message, ac).show();
					// }

					// Toast toast = Toast.makeText(ac, message, duration);
					// toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					// toast.show();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void showToast1(final String message, final int duration, final Context ac) {
		try {
			((Activity) ac).runOnUiThread(new Runnable() {
				public void run() {
					// if (MyApp.isRunningApp) {
					Toast toast = Toast.makeText(ac, message, duration);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					// }

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void showToastThieuThongTin(final Activity ac) {
		ac.runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(ac, "Thiếu thông tin", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		});
	}

	public static void showToastNoInternet(final Activity ac) {
		ac.runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(ac, "Không có kết nối mạng, vui lòng kiểm tra lại kết nối tới internet của bạn", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		});
	}


	// public static void loadDataCache(final NotifyDataListener noti) {
	// db = new DatabaseHandler(MyApp.context);
	// DataManager2.getInstance().handleInfo(db.getCache(ConstantValue.GET_INFO),
	// new NotifyDataListener() {
	//
	// @Override
	// public void onNotify(int id) {
	// getListBannerCache(noti);
	//
	// }
	// });
	//
	// }
	//
	// private static void getListBannerCache(final NotifyDataListener noti) {
	// DataManager2.getInstance().handleListBanner(db.getCache(ConstantValue.GET_BANNER),
	// new NotifyDataListener() {
	//
	// @Override
	// public void onNotify(int id) {
	// getDealHomeCache(noti);
	//
	// }
	// });
	//
	// }
	//
	// protected static void getDealHomeCache(final NotifyDataListener noti) {
	// DataManager2.getInstance().handleListDealHome(db.getCache(ConstantValue.GET_DEAL_HOME),
	// new NotifyDataListener() {
	//
	// @Override
	// public void onNotify(int id) {
	// getListSlide(noti);
	// // getListAbout(noti);
	// }
	// });
	//
	// }
	//
	// protected static void getListAbout(final NotifyDataListener noti) {
	// DataManager2.getInstance().handleListAbout(db.getCache(ConstantValue.GET_ABOUT),
	// new NotifyDataListener() {
	//
	// @Override
	// public void onNotify(int id) {
	// getListSlide(noti);
	// }
	// });
	//
	// }
	//
	// protected static void getListSlide(final NotifyDataListener noti) {
	// DataManager2.getInstance().handleListCateSlide(db.getCache(ConstantValue.GET_LIST_CATE_SLIDE),
	// new NotifyDataListener() {
	//
	// @Override
	// public void onNotify(int id) {
	// getListLocation(noti);
	// }
	// });
	//
	// }
	//
	// protected static void getListLocation(final NotifyDataListener noti) {
	// DataManager2.getInstance().handleListLocation(db.getCache(ConstantValue.GET_LIST_LOCATION),
	// new NotifyDataListener() {
	//
	// @Override
	// public void onNotify(int id) {
	// noti.onNotify(1);
	// }
	// });
	//
	// }
	public static void showDialogCustomListView(final Context c, ArrayList<String> list) {
		if (list.size() <= 1) {
			callPhone(list.get(0), c);
			return;
		}
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(c);
		// builderSingle.setIcon(R.drawable.ic_launcher);
		// builderSingle.setTitle("Select One Name:-");

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.select_dialog_singlechoice);
		arrayAdapter.addAll(list);

		builderSingle.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String strName = arrayAdapter.getItem(which);
				callPhone(strName, c);
				// showToast(strName, Toast.LENGTH_SHORT, c);
				// AlertDialog.Builder builderInner = new AlertDialog.Builder(
				// c);
				// builderInner.setMessage(strName);
				// builderInner.setTitle("Your Selected Item is");
				// builderInner.setPositiveButton("Ok",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// dialog.dismiss();
				// }
				// });
				// builderInner.show();
			}
		});
		builderSingle.show();
	}

	public static void callPhone(String num, final Context c) {
		try {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + num));
			c.startActivity(intent);
		} catch (Exception e) {
		}

	}

	public static String nameAc;

	public static void startActivity(Activity ac, Class<?> class1, String data) {
		if (HotdealUtilities.checkInternetConnection(ac)) {
			// mTracker = MyApp.getDefaultTracker(ac);
			// mTracker.setScreenName(class1.getName());
			// mTracker.send(new HitBuilders.ScreenViewBuilder().build());
			nameAc = class1.getSimpleName();
			Intent intent = new Intent(ac, class1);
			intent.putExtra("DATAAC", data);
			ac.startActivity(intent);
		} else {
			showToast("Không có kết nối tới Internet", Toast.LENGTH_SHORT, ac);
			// HotdealUtilities.sendMessage(ac, ConstantValue.ACTION_NOINTERNET,
			// "");
		}

	}

	public static void startActivityClearStack(Context ac, Class<?> class1, String data) {
		Intent intent = new Intent(ac, class1);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("DATAAC", data);
		ac.startActivity(intent);
	}

	public static void startActivityClearStack(Activity ac, Class<?> class1) {
		Intent intent = new Intent(ac, class1);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		ac.startActivity(intent);
	}

	public static void startActivityForResult(Activity ac, Class<?> class1, int request, String data) {
		Intent intent = new Intent(ac, class1);
		intent.putExtra("DATAAC", data);
		ac.startActivityForResult(intent, request);
	}

	public static String getDataBundle(Activity ac) {
		String value = "";
		Bundle extras = ac.getIntent().getExtras();
		if (extras != null) {
			value = extras.getString("DATAAC");
		}
		return value;
	}

	public static long getLongFromDate(String date, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Date oldDate = null;
		try {
			oldDate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long oldMillis = (oldDate.getTime()) / 1000;
		return oldMillis;
	}

	public static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	public static String getDate(long milliSeconds, String dateFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(milliSeconds * 1000);
			return formatter.format(calendar.getTime());
		} catch (Exception e) {
			return "";
		}

	}

	// public static Date getDate12(long milliSeconds, String dateFormat) {
	// SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTimeInMillis(milliSeconds * 1000);
	// return formatter.format(calendar.getTime());
	// }

	public static String getDate1(long milliSeconds, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(new Date(milliSeconds * 1000));
		return dateString;
	}

	public static String getDateFromDate(Date milliSeconds, String dateFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			String dateString = formatter.format(milliSeconds);
			return dateString;
		} catch (Exception e) {
			return "";
		}

	}

	// public static int dpToPx(int dp) {
	// DisplayMetrics displayMetrics =
	// MyApp.context.getResources().getDisplayMetrics();
	// int px = Math.round(dp * (displayMetrics.xdpi /
	// DisplayMetrics.DENSITY_DEFAULT));
	// return px;
	// }
	//
	// public static int pxToDp(int px) {
	// DisplayMetrics displayMetrics =
	// MyApp.context.getResources().getDisplayMetrics();
	// int dp = Math.round(px / (displayMetrics.xdpi /
	// DisplayMetrics.DENSITY_DEFAULT));
	// return dp;
	// }

	public static String email;

	public static void getEmail(Activity ac) {
		AccountManager accountManager = AccountManager.get(ac);
		Account account = getAccount(accountManager);

		if (account == null) {
			email = null;
		} else {
			email = account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

	public static void wakeUpScreen(final Activity ac) {
		try {
			PowerManager pm = (PowerManager) ac.getApplicationContext().getSystemService(Context.POWER_SERVICE);
			WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
			wakeLock.acquire();
		} catch (Exception e) {
		}

	}

	public static String getKeyHashFacebook(Activity ac) {
		String keyhash = "";
		try {
			PackageInfo info = ac.getPackageManager().getPackageInfo(ac.getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				keyhash = Base64.encodeToString(md.digest(), Base64.DEFAULT);

			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return keyhash;
	}

	public static String getANDROID_ID(final Context ac) {
		return Secure.getString(ac.getContentResolver(), Secure.ANDROID_ID);
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	public static int getDeviceVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}


}
