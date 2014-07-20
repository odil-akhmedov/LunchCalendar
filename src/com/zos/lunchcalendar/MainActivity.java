package com.zos.lunchcalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.R.string;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat") public class MainActivity extends Activity {
	final String TAG = "MainActivity";
	private int viewType = 1; // 0 - for list view, 1 for grid view

	private Bundle settings;

	CustomAdapter adapter;
	ArrayList<String> result = new ArrayList<String>();

	private ArrayList<DailyMenu> menuForMonth = new ArrayList<DailyMenu>();
	public DailyMenu menuForOneDay = new DailyMenu();

	private String url = "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";
	private JSONCalendarParser obj;
	SharedPreferences sharedPreferences;
	
	Set<String> preferredMealsFromArray = new HashSet<String>();
	ArrayList<String> preferredMeals = new ArrayList<String>();
	String preferredDay; //Day before, same day...
	
	Date notificationTime;
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// if (settings != null){
		// viewType = settings.getInt("VIEW_TYPE");
		// } else
		// viewType = 0;

		if (savedInstanceState != null) {
			Log.v(TAG, "onRestoreInstanceState called");
			Log.v(TAG, "Values restored in onRestoreInstanceState = "
					+ savedInstanceState.getInt("VIEWTYPE"));
		}

		switch (viewType) {
		case 0:
			setContentView(R.layout.activity_main_list);
			break;
		case 1:
			setContentView(R.layout.activity_main_grid);
			break;
		}

		startParsing();
		// adapter = new CalendarAdapters(getApplicationContext());
		//Notifications
		
		String startTime = "2014-07-20 16:14:20";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long notifyTime;
		Date date = null;
		try {
			date = sdf.parse(startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		notifyTime = date.getTime();
		
		
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		preferredMealsFromArray = sharedPreferences.getStringSet(
				"PreferredMeals", null);
		
		
		/*NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		String longText = "Meals you saved is going to be served soon";

		PendingIntent pIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, new Intent(), // add this
				PendingIntent.FLAG_UPDATE_CURRENT);

		Notification n = new Notification.Builder(this)
				.setContentTitle("Your favorite meal spotted!")
				.setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent).setAutoCancel(true)
				.addAction(R.drawable.ic_launcher, "More", pIntent)
				.setStyle(new Notification.BigTextStyle().bigText(longText))
				.setShowWhen(true).setWhen(notifyTime).build();

		notificationManager.notify(0, n);*/
		
		//part 1 – notification icon alert
		int icon = R.drawable.ic_launcher;
		// a ticker text message or sound or vibration or flashlight can be used for alert
		CharSequence ticker = "Your favorite meal spotted!";
		long showAt = System.currentTimeMillis(); //immediately
		//long showAt = notifyTime;
		
		Notification notification = new Notification(icon, ticker, showAt);
		//part 2 – associate notification message with details shown on message expansion to activity/intent
		CharSequence notificationTitle = "Notification:";
		CharSequence notificationMessage = "SMS Received.";
		Intent intent = new Intent(this, Activity.class);
		PendingIntent objPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		Context ctx = getApplicationContext();
	
		notification.setLatestEventInfo(ctx, notificationTitle, notificationMessage, objPendingIntent);
		//part 3 – register the notification message with notification manager
		final int notificationIdentifier = 101; //an unique number set by developer to identify a notification, using this notification can be updated/replaced
		NotificationManager notificationManager = (NotificationManager)
		getSystemService(Context.NOTIFICATION_SERVICE);
		
		notificationManager.notify(notificationIdentifier, notification);


	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		viewType = savedInstanceState.getInt("VIEWTYPE");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("VIEWTYPE", viewType);
	}

	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v(TAG, "onRestart method called");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume method called");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause method called");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.v(TAG, "onDestroy method called");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	// Options menu
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		Intent i;
		switch (menuItem.getItemId()) {
		case R.id.listViewMode: // do something
			viewType = 0; // List
			i = new Intent(this, MainActivity.class);
			i.putExtra("VIEW_TYPE", viewType);
			startActivity(i);
			return true;

		case R.id.gridViewMode:
			viewType = 1; // Grid
			i = new Intent(this, MainActivity.class);
			i.putExtra("VIEW_TYPE", viewType);
			startActivity(i);
			return true;
		case R.id.settings:
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	public void open(View view) {
		obj = new JSONCalendarParser(url, getApplicationContext());
		obj.fetchJSON();
		// Toast.makeText(getApplicationContext(), "msg msg",
		// Toast.LENGTH_SHORT).show();
		while (obj.parsingComplete);

		result = obj.getContentFromJson();

		menuForMonth = obj.getMenuFromJson();
		switch (viewType) {
		case 0:
			CustomAdapter adapter = new CustomAdapter(this,
					R.layout.custom_row_textview, menuForMonth, viewType);
			ListView listView = (ListView) findViewById(R.id.listViewMain);

			listView.setAdapter(adapter);
			break;
		case 1:
			CustomAdapter adapter2 = new CustomAdapter(this,
					R.layout.custom_grid_textview, menuForMonth, viewType);
			GridView g = (GridView) findViewById(R.id.gridViewMain);
			g.setAdapter(adapter2);
			// g.setAdapter(new ArrayAdapter<DailyMenu>(this,
			// R.layout.grid_cell, menuForMonth));
			// g.setOnItemClickListener(this);
			break;
		}

		/*
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, result);
		 * 
		 * ListView menuList = (ListView) findViewById(R.id.listView1);
		 * menuList.setAdapter(adapter);
		 */
		// menuList.setOnItemClickListener(this);

	}

	public void startParsing() {
		obj = new JSONCalendarParser(url, getApplicationContext());
		obj.fetchJSON();

		while (obj.parsingComplete);

		// result = obj.getContentFromJson();

		menuForMonth = obj.getMenuFromJson();
		switch (viewType) {
		case 0:
			CustomAdapter adapter = new CustomAdapter(this,
					R.layout.custom_row_textview, menuForMonth, viewType);
			ListView listView = (ListView) findViewById(R.id.listViewMain);
			listView.setAdapter(adapter);
			break;
		case 1:
			CustomAdapter adapter2 = new CustomAdapter(this,
					R.layout.custom_grid_textview, menuForMonth, viewType);
			GridView g = (GridView) findViewById(R.id.gridViewMain);
			g.setAdapter(adapter2);
			
						
				
			
			// g.setOnItemClickListener(this);
			break;
		}
	}
}