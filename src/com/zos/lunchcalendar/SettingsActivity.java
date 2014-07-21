package com.zos.lunchcalendar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class SettingsActivity extends ActionBarActivity {

	private JSONCalendarParser obj;
	private ArrayList<String> mealsListText = new ArrayList<String>();
	private ArrayList<DailyMenu> menuForMonth = new ArrayList<DailyMenu>();

	TextView mealsList;
	// MultiSelectionSpinner daySpinner;
	MultiSelectionSpinner mealSpinner;
	Spinner daySpinner;
	ListView mealsListView;
	Spinner timeSpinner;

	// Here we save preferences
	ArrayList<String> preferredMeals = new ArrayList<String>();
	String preferredTime;
	// ArrayList<String> preferredDays = new ArrayList<String>();
	String preferredDay;

	// get values from shared prefs
	Set<String> preferredMealsFromArray = new HashSet<String>();
	Set<String> preferredDaysFromArray = new HashSet<String>();
	Set<String> timeToNotify = new HashSet<String>();

	Button save;
	private PendingIntent pendingIntent;

	private String url = "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";

	// 4vtl6sns8n6apup80ns7lrcd08@group.calendar.google.com
	// private String url =
	// "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_settings);
		// mealsList = (TextView)findViewById(R.id.mealsList);

		String[] daysArray = { "Two day Before", "A day before", "Same day" };
		// daySpinner = (MultiSelectionSpinner) findViewById(R.id.daySpinner);
		// daySpinner.setItems(daysArray);

		timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
		daySpinner = (Spinner) findViewById(R.id.daySpinner);

		AssetManager am = getApplicationContext().getAssets();
		/*
		 * try { if (am.open("JSON.json") != null){ obj = new
		 * JSONCalendarParser("JSON.json", getApplicationContext(), true);
		 * obj.loadJSONFromAsset();
		 * 
		 * while (obj.parsingComplete);
		 * 
		 * mealsListText = obj.getContentFromJson(); } else { obj = new
		 * JSONCalendarParser(url, getApplicationContext()); obj.fetchJSON();
		 * while (obj.parsingComplete); menuForMonth = obj.getMenuFromJson(); }
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * //read from url obj = new JSONCalendarParser(url,
		 * getApplicationContext()); obj.fetchJSON(); while
		 * (obj.parsingComplete); menuForMonth = obj.getMenuFromJson();
		 */

		obj = new JSONCalendarParser("JSON.json", getApplicationContext(), true);
		obj.fetchJSON();

		while (obj.parsingComplete);

		mealsListText = obj.getContentFromJson();
		menuForMonth = obj.getMenuFromJson();

		// getting actual lunch items
		// yes we need to use regular expressions
		ArrayList<String> mealz = new ArrayList<String>();

		HashSet<String> hs = new HashSet<String>();

		for (int i = 0; i < menuForMonth.size(); i++) {
			String mealNumOne = menuForMonth.get(i).getTitle();
			//System.out.println("Start time = " + menuForMonth.get(i).getStartTime());
			String[] mealsAll = mealNumOne.split("[[0-9]]+[.]+[' ']");

			for (int j = 1; j < mealsAll.length; j++) {
				mealsAll[j] = mealsAll[j].replace(" or ", ";");
				mealz.add(mealsAll[j]);
			}
			hs.addAll(mealz);
			mealz.clear();
			mealz.addAll(hs);
		}

		// String[] mealArr = new String[mealz.size()];
		// mealArr = mealz.toArray(mealArr);

		mealSpinner = (MultiSelectionSpinner) findViewById(R.id.mealSpinner);
		mealSpinner.setItems(mealz);

		// We can use multispinner, and then grab the results from the spinner
		// titles
		mealsListView = (ListView) findViewById(R.id.favoriteMeals);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, mealz);
		mealsListView.setAdapter(adapter);

		mealsListView.setItemsCanFocus(false);
		mealsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		save = (Button) findViewById(R.id.saveSettings);
		loadSavedPreferences();

		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				SparseBooleanArray sparseBooleanArray = mealsListView
						.getCheckedItemPositions();

				for (int i = 0; i < mealsListView.getCount(); i++) {
					if (sparseBooleanArray.get(i) == true) {
						preferredMeals.add(mealsListView.getItemAtPosition(i)
								.toString());
					} else {
						preferredMeals.remove(mealsListView
								.getItemAtPosition(i).toString());
					}
				}

				String s = mealSpinner.getSelectedItemsAsString();
				ArrayList<String> d = mealSpinner.getSelectedArrayStrings();

				// Toast.makeText(getApplicationContext(), s,
				// Toast.LENGTH_LONG).show();

				// preferredMeals = d;

				preferredTime = timeSpinner.getSelectedItem().toString();

				preferredDay = daySpinner.getSelectedItem().toString();

				/** Save to shared preferences **/

				savePreferences("PreferredMeals", preferredMeals);
				savePreferences("PreferredTime", preferredTime);
				savePreferences("PreferredDay", preferredDay);

				setNotificationTime();
				finish();

			}
		});

	}

	protected void setNotificationTime() {
		// TODO Auto-generated method stub

		String startTime = "";// = "2014-07-20 21:08:00";
		long notifyTime = 0;
		for (int i = 0; i < preferredMeals.size(); i++) {
			for (int j = 0; j < menuForMonth.size(); j++) {
				if (preferredMeals.get(i).contains(menuForMonth.get(j).getTitle())) {
					startTime = menuForMonth.get(i).getStartTime();
					notifyTime = convertToTimeStamp(startTime,
							"yyyy-MM-dd");
				}
			}
		}

		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 * 
		 * Date date = null; try { date = sdf.parse(startTime); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } notifyTime = date.getTime();
		 */
		System.out.println("NotifyTime1 = " + startTime);
		System.out.println("NotifyTime2 = " + notifyTime);

		Calendar calendar = Calendar.getInstance();

		calendar.set(2014, 6, 20, 20, 58, 4); // omg months start from 0 o_O

		Intent myIntent = new Intent(SettingsActivity.this, MyReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0,
				myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, notifyTime, pendingIntent);
		// alarmManager.set(AlarmManager.RTC,
		// System.currentTimeMillis(),pendingIntent);
	}

	private long convertToTimeStamp(String time, String format) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long notifyTime;
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyTime = date.getTime();
		return notifyTime;
	}

	@SuppressLint("NewApi")
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		preferredMealsFromArray = sharedPreferences.getStringSet(
				"PreferredMeals", null);

		for (String str : preferredMealsFromArray)
			preferredMeals.add(str);

		if (sharedPreferences.contains("PreferredMeals")) {
			int count = this.mealsListView.getAdapter().getCount();
			for (int i = 0; i < count; i++) {
				String currentItem = (String) this.mealsListView.getAdapter()
						.getItem(i);
				if (preferredMeals.contains(currentItem)) {
					this.mealsListView.setItemChecked(i, true);
				} else {
					this.mealsListView.setItemChecked(i, false);
				}
			}
		}

		// loading for preferred time
		preferredTime = sharedPreferences.getString("PreferredTime", "06:00");

		@SuppressWarnings("unchecked")
		ArrayAdapter<String> timeAdap = (ArrayAdapter<String>) timeSpinner
				.getAdapter(); // cast to an ArrayAdapter

		int timePosition = timeAdap.getPosition(preferredTime);

		timeSpinner.setSelection(timePosition);

		// Loading for preferred days
		preferredDay = sharedPreferences.getString("PreferredDay", "");

		@SuppressWarnings("unchecked")
		ArrayAdapter<String> dayAdap = (ArrayAdapter<String>) daySpinner
				.getAdapter(); // cast to an ArrayAdapter

		int dayPosition = dayAdap.getPosition(preferredDay);

		daySpinner.setSelection(dayPosition);

		/*
		 * preferredDaysFromArray = sharedPreferences.getStringSet(
		 * "PreferredDays", null); for (String str2 : preferredDaysFromArray)
		 * preferredDays.add(str2);
		 */

	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	@SuppressLint("NewApi")
	private void savePreferences(String key, ArrayList<String> value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Set<String> preferredMealsSet = new HashSet<String>();

		for (int i = 0; i < value.size(); i++)
			preferredMealsSet.add(value.get(i));

		for (int i = 0; i < preferredMeals.size(); i++) {
			if (menuForMonth.contains(preferredMeals.get(i))) {
				int index = menuForMonth.indexOf(preferredMeals.get(i));
				String startTime = menuForMonth.get(index).getStartTime();
				// String startTime = "2014-07-20";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = null;
				try {
					date = sdf.parse(startTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(date.getTime());
				// timeToNotify.add(date.getTime());
				// if e.g. the user wants to be notified the day before
				// at 8:00 am, then we have to calculate date.getTime() -
				// (24-8)*3600*1000s

			}
		}

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putStringSet("PreferredMeals", preferredMealsSet);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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

	public void onClickDD(View v) {
		String s = mealSpinner.getSelectedItemsAsString();
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}
}
