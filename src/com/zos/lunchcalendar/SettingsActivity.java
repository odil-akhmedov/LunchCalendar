package com.zos.lunchcalendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import android.support.v7.app.ActionBarActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
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

	TextView mealsList;
	private JSONCalendarParser obj;
	private ArrayList<String> mealsListText = new ArrayList<String>();
	private ArrayList<DailyMenu> menuForMonth = new ArrayList<DailyMenu>();

	ListView mealsListView;

	Button save;

	private String url = "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_settings);
		// mealsList = (TextView)findViewById(R.id.mealsList);

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

		while (obj.parsingComplete)
			;

		mealsListText = obj.getContentFromJson();
		ArrayList<String> mealz = new ArrayList<String>();

		// add elements to al, including duplicates
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

		// mealsList.setText(mealz.get(1));

		mealsListView = (ListView) findViewById(R.id.favoriteMeals);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, mealz);
		mealsListView.setAdapter(adapter);

		mealsListView.setItemsCanFocus(false);
		mealsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		save = (Button) findViewById(R.id.saveSettings);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				for (int i = 0; i < mealsListView.getCount(); i++) {
					View vListSortOrder;
					vListSortOrder = mealsListView.getChildAt(i);

					EditText edit = (EditText) vListSortOrder
							.findViewById(R.id.share_comment_edit);
					String temp = edit.getText().toString();
				}

				Toast.makeText(getBaseContext(),
						"EditText Value, checkbox value and other values",
						Toast.LENGTH_SHORT).show();
			}
		});

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
