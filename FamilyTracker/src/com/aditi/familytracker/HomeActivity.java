package com.aditi.familytracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
//import android.support.v4.app.FragmentManager;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends FragmentActivity implements OnMapClickListener {

	private ArrayList<String> menuList;
	private ImageView msgbtn;
	private ImageView menuclick;
	private TextView headername;
	String selectedSub;
	private String[] menu;
	int clIDLenght;
	int integerNumber;
	private Integer[] menuIcons;
	private DrawerLayout dLayout;
	private ImageView useronclick, sendsms;
	private ListView dList;
	private MyApp objects;

	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	private GoogleMap googleMap;
	double latitude = 25.3176532;
	double longitude = 82.9739064;

	Location myLocation;
	double gkplatitude = 26.7588;
	double gkplongitude = 83.3697;
	double deoslatitude = 26.5000;
	double deoslongitude = 83.7900;
	// TextView tvLocInfo;
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		// dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		objects = (MyApp) getApplicationContext();
		menuclick = (ImageView) findViewById(R.id.manuopt);
		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		dList = (ListView) findViewById(R.id.list_slidermenu);
		gps = new GPSTracker(this);
		if (gps.canGetLocation()) {

			try {
				googleMap = ((SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map))).getMap();
				googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				googleMap.getUiSettings().setCompassEnabled(true);
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);
				googleMap.getUiSettings().setAllGesturesEnabled(true);
				googleMap.setTrafficEnabled(true);

				// new AUTOSuggestion().execute();
				LatLng toPosition = new LatLng(gps.getLatitude(), gps.getLongitude());
				// drawCircle(toPosition);

				googleMap.addMarker(new MarkerOptions().position(toPosition).title(" My Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				LatLng toPosition1 = new LatLng(latitude, longitude);
				googleMap.addMarker(new MarkerOptions().position(toPosition1).title("Vanaras Distt").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				// new AUTOSuggestion().execute();
				LatLng toPosition2 = new LatLng(gkplatitude, gkplongitude);
				googleMap.addMarker(new MarkerOptions().position(toPosition2).title("Gorakhpur Distt").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

				LatLng toPosition3 = new LatLng(deoslatitude, deoslongitude);
				googleMap.addMarker(new MarkerOptions().position(toPosition3).title("Deoria Distt").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 18.5f));

				googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

					@Override
					public void onMapLongClick(LatLng arg0) {
						// TODO Auto-generated method stub
						PolylineOptions polylineOptions = new PolylineOptions();
						polylineOptions.add(arg0);
						polylineOptions.add(new LatLng(latitude, longitude));
						googleMap.addPolyline(polylineOptions);
						long tol = getDistanceBetweenPoints(latitude, longitude, gps.getLatitude(), gps.getLongitude() / 1000);
						Toast.makeText(getApplicationContext(), "" + tol + "  ", 1000).show();
					}
				});

				// ****************************************************

				final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
				// Toast.makeText(getBaseContext(),
				// manager.getAllProviders().toString(),Toast.LENGTH_LONG).show();
				Log.i("LocationManager:---->", "" + manager);
				if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Toast.makeText(getBaseContext(), "GPS is OFF!", Toast.LENGTH_LONG).show();

					// creates a alert box with 2 buutons and redirects the
					// application
					// to gps setting page

					// AlertDialog.Builder builder = new
					// AlertDialog.Builder(this);
					// builder.setTitle("Location may not be Accurate.. !!!")
					// .setMessage(
					// "Do you want to enable GPS satellites or Use wireless networks for finding your current location.")
					// .setCancelable(false)
					// .setPositiveButton("Yes",
					// new DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog,
					// int id) {
					// startActivity(new Intent(
					// android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					// Toast.makeText(getBaseContext(),"GPS Satellites",
					// Toast.LENGTH_LONG).show();
					// }
					// })
					// .setNegativeButton("No",
					// new DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog,
					// int id) {
					// dialog.cancel();
					// }
					// });
					// AlertDialog alert = builder.create();
					// alert.show();

				}

				else {
					Toast.makeText(getBaseContext(), "GPS is ON!", Toast.LENGTH_LONG).show();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert(this, 0);
		}

		double total = calculateDistance(gkplatitude, gkplongitude, latitude, longitude);
		Log.e("Dis", "" + total);

		try {
			menu = new String[] { "Around Me", "LOGOUT" };
			menuList = new ArrayList<String>();
			menuList.addAll(Arrays.asList(menu));
			menuIcons = new Integer[] { R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_home, R.drawable.ic_home,
					R.drawable.ic_home, R.drawable.ic_home };

			CustomListAdapter adapter1 = new CustomListAdapter(HomeActivity.this, menu, menuIcons);

			// adapter = new
			// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);

			dList.setAdapter(adapter1);
			dList.setSelector(android.R.color.darker_gray);
		} catch (Exception e) {
			e.printStackTrace();
			// crashreposrMail("menu array and drawer layout  block"+e);
		}

		// dList.setSelector(android.R.color.primary_text_light_nodisable);

		dList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				try {
					// TODO Auto-generated method stub
					// "Home","","","","","","","","","LOGOUT"
					String po = menu[position - 1];

					if (po.equals("LOGOUT")) {
						// android.os.Process.killProcess(android.os.Process.myPid());
						// System.exit(1);
						Intent logout = new Intent(HomeActivity.this, LoginActivity.class);
						startActivity(logout);
						finish();

					} else if (po.equals("Around Me")) {
						// new AllRegiter().execute();
						DataManager.getInstance().getAllUser(HomeActivity.this, true, true, new NotifyDataListener() {

							@Override
							public void onNotify(int id) {
								if (id == RESULT_OK) {
									try {
										JSONArray jArr = new JSONArray(result);
										for (int i = 0; i < jArr.length(); i++) {
											JSONObject jObj = jArr.getJSONObject(i);
											String lt = jObj.getString("lat");
											String lng = jObj.getString("lng");
											String nam = jObj.getString("name");
											LatLng toPosition = new LatLng(Double.parseDouble(lt), Double.parseDouble(lng));
											googleMap.addMarker(new MarkerOptions().position(toPosition).title(nam).icon(BitmapDescriptorFactory.fromResource(R.drawable.manicons)));
											LatLng toPosition1 = new LatLng(latitude, longitude);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							}
						});

					}

					dLayout.closeDrawers();
				} catch (Exception e) {
					e.printStackTrace();
					// crashreposrMail("drawer Items Click  block"+e);
				}
			}

		});
		try {
			LayoutInflater inflater = getLayoutInflater();
			ViewGroup header = (ViewGroup) inflater.inflate(R.layout.drawer_header, dList, false);
			TextView name = (TextView) header.findViewById(R.id.name);
			name.setText(objects.getNameOfLoginUser());
			dList.addHeaderView(header, null, false);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	// Km Convert 2 GEO lat lon

	public static double calculateDistance(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {

		float results[] = new float[1];

		try {
			Location.distanceBetween(fromLatitude, fromLongitude, toLatitude, toLongitude, results);
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
		}

		int dist = (int) results[0];
		if (dist <= 0)
			return 0D;

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		results[0] /= 1000D;
		String distance = decimalFormat.format(results[0]);
		double d = Double.parseDouble(distance);
		return d;
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	private void createGeofence(double latitude, double longitude, int radius, String geofenceType, String title) {
		Marker stopMarker = googleMap.addMarker(new MarkerOptions().draggable(true).position(new LatLng(latitude, longitude)).title(title).infoWindowAnchor(10, 10).draggable(true)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		// googleMap.addCircle(new CircleOptions().center(new LatLng(latitude,
		// longitude)).radius(radius).strokeColor(Color.parseColor("#ffff00")).fillColor(Color.parseColor("#B2A9F6")));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {
			Toast.makeText(getApplicationContext(), "isGooglePlayServicesAvailable SUCCESS", Toast.LENGTH_LONG).show();
		} else {
			Log.e("play service", "" + GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices));
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		}

	}

	private void drawCircle(LatLng point) {

		// Instantiating CircleOptions to draw a circle around the marker
		CircleOptions circleOptions = new CircleOptions();
		// Specifying the center of the circle
		circleOptions.center(point);
		// Radius of the circle
		circleOptions.radius(20);
		// Border color of the circle
		circleOptions.strokeColor(Color.BLACK);

		// Fill color of the circle
		circleOptions.fillColor(0x30ff0000);

		// Border width of the circle
		circleOptions.strokeWidth(2);

		// Adding the circle to the GoogleMap
		googleMap.addCircle(circleOptions);

	}

	@Override
	public void onMapClick(LatLng point) {
		// tvLocInfo.setText(point.toString());
		CircleOptions circleOptions = new CircleOptions().center(point) // set
																		// center
				.radius(10000) // set radius in meters
				.fillColor(Color.TRANSPARENT) // default
				.strokeColor(Color.BLUE).strokeWidth(5);

		Circle myCircle = myMap.addCircle(circleOptions);
		myMap.animateCamera(CameraUpdateFactory.newLatLng(point));
	}

	long getDistanceBetweenPoints(double lat1, double lng1, double lat2, double lng2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		long distanceInMeters = Math.round(6371000 * c);
		return distanceInMeters * 1000;
	}

/*	class AllRegitera extends AsyncTask<String, String, String> {
		ProgressDialog pd;
		String responseStr;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(HomeActivity.this);
			pd.setTitle("Please wait...");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				String url = MyApp.serverURL + MyApp.listAll;
				responseStr = CustomHttpClient.executeHttpGet(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return responseStr;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			try {
				JSONArray jArr = new JSONArray(result);
				for (int i = 0; i < jArr.length(); i++) {
					JSONObject jObj = jArr.getJSONObject(i);
					String lt = jObj.getString("lat");
					String lng = jObj.getString("lng");
					String nam = jObj.getString("name");
					LatLng toPosition = new LatLng(Double.parseDouble(lt), Double.parseDouble(lng));
					googleMap.addMarker(new MarkerOptions().position(toPosition).title(nam).icon(BitmapDescriptorFactory.fromResource(R.drawable.manicons)));
					LatLng toPosition1 = new LatLng(latitude, longitude);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}*/

}