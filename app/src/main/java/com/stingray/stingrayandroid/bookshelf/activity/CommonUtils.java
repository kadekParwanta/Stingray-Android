package com.stingray.stingrayandroid.bookshelf.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.stingray.stingrayandroid.Constants;
import com.stingray.stingrayandroid.R;

import javax.net.ssl.HttpsURLConnection;

public class CommonUtils {

	public static Context m_context;
	static Bitmap m_bitmap;
	private static ProgressDialog m_progressDialog;

	/*
	 * Call the Webservice read the Json response and return the response in
	 * string.
	 * http://180.211.110.195/php-projects/vachanamrut/webservice/ListOfBooks
	 */
	
	public static String parseJSON(String p_url) {
		JSONObject jsonObject = null;
		String json = null;

		try {
			// Create a new HTTP Client
			URL url = new URL(p_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) conn.getContent()));
			json = reader.readLine();
			// System.err.println("JSON Response--->" + json);
			// Instantiate a JSON object from the request response
			// jsonObject = new JSONObject(json);

		} catch (Exception e) {
			// In your production code handle any errors and catch the
			// individual exceptions
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * This method check Internet connectivity.
	 * 
	 * @return true if Internet connection is available otherwise it returns
	 *         false.
	 */

	public static boolean IsNetConnected(Context p_conContext) {
		boolean NetConnected = false;
		p_conContext = m_context;
		try {
			ConnectivityManager connectivity = (ConnectivityManager) p_conContext
					.getSystemService(p_conContext.CONNECTIVITY_SERVICE);

			if (connectivity == null) {
				NetConnected = false;
			} else {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();

				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							NetConnected = true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			NetConnected = false;
		}

		return NetConnected;
	}

	public static boolean checkConnection(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			Toast.makeText(context, "No Network available.", Toast.LENGTH_LONG).show();
			// Log.e("TAG", "checkConnection - no connection found");
			return false;
		} else
			return true;
	}

	/*
	 * Call the Webservice read the Json response and return the response in
	 * string.
	 */
	public static String parseFBResponse(String p_url) {
		JSONObject jsonObject = null;
		String json = null;
		try {
			URL url = new URL(p_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) conn.getContent()));
			json = reader.readLine();
			System.err.println("JSON Response--->" + json);
			// Instantiate a JSON object from the request response
			jsonObject = new JSONObject(json);

		} catch (Exception e) {
			// In your production code handle any errors and catch the
			// individual exceptions
			e.printStackTrace();
		}
		return json;
	}

	public static Bitmap loadImage(String image_location) {

		URL imageURL = null;

		try {
			imageURL = new URL(image_location);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream inputStream = connection.getInputStream();

			m_bitmap = BitmapFactory.decodeStream(inputStream);// Convert to
																// bitmap

			// image_view.setImageBitmap(m_bitmap);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return m_bitmap;
	}

	public static void setStringSharedPref(Context mContext, String spKey, String value) {
		SharedPreferences m_sharePref = mContext.getSharedPreferences("SHARED_PREFS",
				Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = m_sharePref.edit();
		editor.putString(spKey, value);
		editor.commit();
	}

	public static String getStringSharedPref(Context mContext, String spKey, String value) {
		System.out.println("sr key " + spKey + ", sr value : " + value);

		SharedPreferences SP_ECARD = mContext.getSharedPreferences("SHARED_PREFS",
				Context.MODE_PRIVATE);
		String spValue = SP_ECARD.getString(spKey, value);
		return spValue;

	}

	public static void setBooleanSharedPref(Context p_context, String p_key, boolean p_value) {
		SharedPreferences m_sharePref = p_context.getSharedPreferences("SHARED_PREFS",
				Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = m_sharePref.edit();
		editor.putBoolean(p_key, p_value);
		editor.commit();
	}

	public static boolean getBooleanSharedPref(Context p_context, String p_key, boolean p_value) {

		SharedPreferences SP_ECARD = p_context.getSharedPreferences("SHARED_PREFS",
				Context.MODE_PRIVATE);
		boolean spValue = SP_ECARD.getBoolean(p_key, p_value);
		return spValue;
	}

	public static void showProgress(Context p_context, String p_mesg) {
		m_progressDialog = new ProgressDialog(p_context);
		m_progressDialog.setCancelable(false);
		m_progressDialog.setTitle(p_context.getResources().getString(R.string.app_name));
		m_progressDialog.setMessage(p_mesg);
		m_progressDialog.show();
	}

	public static void dismissDialog() {
		m_progressDialog.dismiss();
	}
}
