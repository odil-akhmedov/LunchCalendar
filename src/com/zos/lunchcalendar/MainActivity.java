package com.zos.lunchcalendar;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	CustomAdapter adapter;
	String result[] = new String[50];
	private DailyMenu[] menuForMonth = new DailyMenu[50];
	public DailyMenu menuForDay = new DailyMenu();
	
	private String url1 = "http://www.google.com/calendar/feeds/gqccak2junkb7eup9ls76k919c@group.calendar.google.com/public/full?alt=json&orderby=starttime&max-results=15&singleevents=true&sortorder=ascending&futureevents=true";
	private JSONCalendarParser obj;
	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
     
      //adapter = new CalendarAdapters(getApplicationContext());
     }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items 
      //to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

   public void open(View view){
       obj = new JSONCalendarParser(url1, getApplicationContext());
       obj.fetchJSON();
       //Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
       while(obj.parsingComplete);
    
       result = obj.getContentFromJson();
       
       //menuForMonth = obj.getMenuFromJson();
       //menuForOneDay = obj.getMenuForOneDay();
     
       //System.out.println("Date = " + menuForMonth[0].startTime);
       
       
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    		  	android.R.layout.simple_list_item_1, result);

		ListView menuList = (ListView) findViewById(R.id.listView1);
		menuList.setAdapter(adapter);
		//menuList.setOnItemClickListener(this);
		      
		/*CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_row_textview,
				menuForMonth);

		ListView listView = (ListView) findViewById(R.id.listView1);

		listView.setAdapter(adapter);*/
		
   }
}