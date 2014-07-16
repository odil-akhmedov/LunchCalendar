package com.zos.lunchcalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
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

public class SettingsActivity extends ActionBarActivity {

	
	private JSONCalendarParser obj;
	private ArrayList<String> mealsListText = new ArrayList<String>();
	private ArrayList<DailyMenu> menuForMonth = new ArrayList<DailyMenu>();
	
	TextView mealsList;
	MultiSelectionSpinner daySpinner;
	ListView mealsListView;
	Spinner timeSpinner;

	ArrayList<String> preferredMeals = new ArrayList<String>();
	String preferredTime;
	ArrayList<String> preferredDays = new ArrayList<String>();
	
	Set preferredMealsFromArray = new HashSet();
	Set preferredDaysFromArray = new HashSet();

	Button save;

	private String url = "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_settings);
		// mealsList = (TextView)findViewById(R.id.mealsList);

		String[] daysArray = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday" };
		daySpinner = (MultiSelectionSpinner) findViewById(R.id.daySpinner);
		daySpinner.setItems(daysArray);

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
		ArrayList<String> mealz = new ArrayList<String>();

		HashSet<String> hs = new HashSet<String>();

		for (int i = 0; i < mealsListText.size(); i++) {
			String mealNumOne = mealsListText.get(i);
			String[] mealsAll = mealNumOne.split("[[0-9]]+[.]+[' ']");

			for (int j = 1; j < mealsAll.length; j++) {
				mealsAll[j] = mealsAll[j].replace(" or ", ";");
				mealz.add(mealsAll[j]);
			}
			hs.addAll(mealz);
			mealz.clear();
			mealz.addAll(hs);
		}

		mealsListView = (ListView) findViewById(R.id.favoriteMeals);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, mealz);
		mealsListView.setAdapter(adapter);

		mealsListView.setItemsCanFocus(false);
		mealsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		timeSpinner = (Spinner) findViewById(R.id.timeSpinner);


		save = (Button) findViewById(R.id.saveSettings);
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
					}
				}
				
				/** Save to shared preferences **/
				preferredTime = timeSpinner.getSelectedItem().toString();
			
				preferredDays = daySpinner.getSelectedArrayStrings();
				
				savePreferences("PreferredMeals", preferredMeals);
				savePreferences("PreferredTime", preferredTime);
				savePreferences("PreferredDays", preferredDays);
				
				/*savePreferences("CheckBox_Value", checkBox.isChecked());
				if (checkBox.isChecked())
					savePreferences("storedName", editText.getText().toString());*/

				finish();

			}
		});
		loadSavedPreferences();

	}

	@SuppressLint("NewApi") 
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		preferredMealsFromArray = sharedPreferences.getStringSet("PreferredMeals", null);
		Iterator<String> itr = preferredMealsFromArray.iterator();
		
        while(itr.hasNext()){	//from hashSet to ArrayString
            preferredMeals.add(itr.next());
        }
        
        preferredTime = sharedPreferences.getString("PreferredTime", "06:00");
        
        
        preferredDaysFromArray = sharedPreferences.getStringSet("PreferredDays", null);
		Iterator<String> itr2 = preferredDaysFromArray.iterator();
		
        while(itr.hasNext()){	//from hashSet to ArrayString
            preferredDays.add(itr2.next());
        }
        
		/*boolean checkBoxValue = sharedPreferences.getBoolean("CheckBox_Value", false);
		String name = sharedPreferences.getString("storedName", "YourName");
		if (checkBoxValue) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}

		editText.setText(name);*/
	}

	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	@SuppressLint("NewApi") 
	private void savePreferences(String key, ArrayList<String> value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Set preferredMealsSet = new HashSet();
		
		for (int i = 0; i < value.size(); i++)
			preferredMealsSet.add(value.get(i));
		
		Editor editor = sharedPreferences.edit();
		editor.putStringSet("PreferredMeals", preferredMealsSet);
		editor.commit();
	}


	/*
	 * public void onClick(View v) { // TODO Auto-generated method stub
	 * savePreferences("CheckBox_Value", checkBox.isChecked()); if
	 * (checkBox.isChecked()) savePreferences("storedName",
	 * editText.getText().toString());
	 * 
	 * finish(); }
	 */

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
		String s = daySpinner.getSelectedItemsAsString();
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}
}
