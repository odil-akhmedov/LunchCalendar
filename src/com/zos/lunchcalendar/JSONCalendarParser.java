package com.zos.lunchcalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

public class JSONCalendarParser {
	final String TAG = "ParsingActivity";
	
	private String content[] = new String[50];
	private DailyMenu[] menuForMonth = new DailyMenu[50];
	
	private String urlString = null;
	private Context myContext;
	public volatile boolean parsingComplete = true;

	public JSONCalendarParser(String url, Context context) {
		this.urlString = url;
		this.myContext = context;
	}

	public String[] getContentFromJson() {
		return content;
	}


	@SuppressLint("NewApi")
	public void readAndParseJSON(String in) {
		try {
			JSONObject reader = new JSONObject(in);

			JSONObject sys = reader.getJSONObject("feed");
			JSONArray array = sys.getJSONArray("entry");

			for (int i = 0; i < array.length(); i++) { //initializing the monthly menu
				JSONObject product = new JSONObject(array.getJSONObject(i).getString("title"));
				content[i] = product.getString("$t");
				
				menuForMonth[i].title = product.getString("$t"); //basically is the list of food
				
				JSONObject id = new JSONObject(array.getJSONObject(i).getString("id"));
				menuForMonth[i].id = id.getString("$t");
				
				JSONArray startEndDate = new JSONArray(array.getJSONObject(i).getJSONArray("gd$when"));
				menuForMonth[i].startTime = startEndDate.getString(1); 	//In array from Json index 1 is startTime
				menuForMonth[i].endTime = startEndDate.getString(0);	//And index 0 is endTime
				
			}

			parsingComplete = false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void fetchJSON() {
		
		  Thread thread = new Thread(new Runnable() {
		  
		  @Override public void run() { try { URL url = new URL(urlString);
		  HttpURLConnection conn = (HttpURLConnection) url .openConnection();
		  conn.setReadTimeout(10000); conn.setConnectTimeout(15000);
		  conn.setRequestMethod("GET"); conn.setDoInput(true);
		  
		  conn.connect(); InputStream stream = conn.getInputStream();
		  
		  String data = convertStreamToString(stream);
		  
		  readAndParseJSON(data); stream.close();
		  
		  } catch (Exception e) { e.printStackTrace(); } } });
		  
		  thread.start();
		 
		String data = loadJSONFromAsset();

		readAndParseJSON(data);
	}

	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public String loadJSONFromAsset() {
		String json = "blallblbalablba";
		try {

			AssetManager am = myContext.getAssets();
			StringBuilder buf = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					am.open("JSON.json")));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				// Here we are printing each line at a time from the file we
				// read
				buf.append(sCurrentLine);
				System.out.println(sCurrentLine);

			}
			json = buf.toString().replaceAll("[\n\r]", "");
			System.out.println("Json_str" + json);
		} catch (IOException ex) {
			Log.v(TAG, "NullPointer? called");
			ex.printStackTrace();
			return null;
		}
		return json;

	}
}