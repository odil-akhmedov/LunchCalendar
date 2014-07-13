package com.zos.lunchcalendar;

import java.io.IOException;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SettingsActivity extends ActionBarActivity {

	TextView mealsList;
	private JSONCalendarParser obj;
	private ArrayList<String> mealsListText = new ArrayList<String>();
	private ArrayList<DailyMenu> menuForMonth = new ArrayList<DailyMenu>();
	
	private String url = "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		mealsList = (TextView)findViewById(R.id.mealsList);
		
		/*AssetManager am = getApplicationContext().getAssets();
		try {
			if (am.open("JSON.json") != null){
				obj = new JSONCalendarParser("JSON.json", getApplicationContext(), true);
				obj.loadJSONFromAsset();
				
				while (obj.parsingComplete);

				mealsListText = obj.getContentFromJson();
			}
			else {
				obj = new JSONCalendarParser(url, getApplicationContext());
				obj.fetchJSON();
				while (obj.parsingComplete);
				menuForMonth = obj.getMenuFromJson();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		obj = new JSONCalendarParser("JSON.json", getApplicationContext(), true);
		obj.fetchJSON();
		
		while (obj.parsingComplete);

		mealsListText = obj.getContentFromJson();
		
		mealsList.setText(mealsListText.get(0));
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
}
