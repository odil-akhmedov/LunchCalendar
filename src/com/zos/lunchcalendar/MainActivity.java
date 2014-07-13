package com.zos.lunchcalendar;

import java.util.ArrayList;

import com.wsu.flashcardsaok.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String viewType;

	CustomAdapter adapter;
	ArrayList<String> result = new ArrayList<String>();

	private ArrayList<DailyMenu> menuForMonth = new ArrayList<DailyMenu>();
	public DailyMenu menuForOneDay = new DailyMenu();

	private String url1 = "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";
	private JSONCalendarParser obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// adapter = new CalendarAdapters(getApplicationContext());

	}
	
	//Options menu
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		Toast.makeText(this, "Next will be " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
		switch (menuItem.getItemId()) {
		case R.id.addition: // do something
			operation = '+';
			return true;
		case R.id.substraction:
			operation = '-';
			return true;
		case R.id.multiplication: // do something else
			operation = '*';
			return true;
		case R.id.division: // do something else
			operation = '/';
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}


	public void open(View view) {
		obj = new JSONCalendarParser(url1, getApplicationContext());
		obj.fetchJSON();
		// Toast.makeText(getApplicationContext(), "msg msg",
		// Toast.LENGTH_SHORT).show();
		while (obj.parsingComplete)
			;

		result = obj.getContentFromJson();

		menuForMonth = obj.getMenuFromJson();
		CustomAdapter adapter = new CustomAdapter(this,
				R.layout.custom_row_textview, menuForMonth);

		ListView listView = (ListView) findViewById(R.id.listView1);

		listView.setAdapter(adapter);

		/*
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, result);
		 * 
		 * ListView menuList = (ListView) findViewById(R.id.listView1);
		 * menuList.setAdapter(adapter);
		 */
		// menuList.setOnItemClickListener(this);

	}
}