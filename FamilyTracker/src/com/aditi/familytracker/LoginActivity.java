package com.aditi.familytracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	Button loginbtn;
	ImageView manuopt;
	TextView signbtn;
	private EditText userName, pas;
	MyApp obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		userName = (EditText) findViewById(R.id.usename);
		obj = (MyApp) getApplicationContext();
		pas = (EditText) findViewById(R.id.pas);

		loginbtn = (Button) findViewById(R.id.loginbtn);
		signbtn = (TextView) findViewById(R.id.signbtn);
		loginbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// new LoginUser().execute();
				login(userName.getText().toString(), pas.getText().toString());
				// Intent home=new
				// Intent(LoginActivity.this,HomeActivity.class);
				// startActivity(home);
				// finish();
			}
		});
		signbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent home = new Intent(LoginActivity.this, SignupActivtiy.class);
				startActivity(home);
				finish();
			}
		});
	}

	public void sign(View v) {
		Intent home = new Intent(LoginActivity.this, SignupActivtiy.class);
		startActivity(home);
		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void login(String usser, String passs) {
		DataManager.getInstance().login(this, true, true, new NotifyDataListener() {

			@Override
			public void onNotify(int id) {
				if (id == NOTIFY_OK) {
					obj.setNameOfLoginUser(userName.getText().toString());
					String uId = arrObj.getString("user_id");
					obj.setUserId(uId);
					Intent i = new Intent(LoginActivity.this, HomeActivity.class);
					startActivity(i);
					finish();
				} else {

				}

			}
		}, usser, passs);

	}

	// class LoginUser extends AsyncTask<String, String, String> {
	//
	// String response;
	// ProgressDialog pd;
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// pd = new ProgressDialog(LoginActivity.this);
	// pd.setTitle("Please wait...");
	// pd.show();
	//
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	// // TODO Auto-generated method stub
	//
	// try {
	// String Url = MyApp.serverURL;
	// String UrlParam = Url + MyApp.loginUser;
	//
	// ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	// param.add(new BasicNameValuePair("email",
	// userName.getText().toString()));
	//
	// param.add(new BasicNameValuePair("password", pas.getText().toString()));
	//
	// response = CustomHttpClient.executeHttpPost(UrlParam, param);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return response;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// try {
	// JSONArray jObj = new JSONArray(result);
	// int len = jObj.length();
	//
	// // String res=jObj.getString("");
	// if (len == 1) {
	// obj.setNameOfLoginUser(userName.getText().toString());
	// JSONObject arrObj = jObj.getJSONObject(0);
	// String uId = arrObj.getString("user_id");
	// obj.setUserId(uId);
	// Intent i = new Intent(LoginActivity.this, HomeActivity.class);
	// startActivity(i);
	// finish();
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }

}
