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
	CalendarAdapters adapter;
	String result[] = new String[50];

	//private String url1 = "http://api.openweathermap.org/data/2.5/weather?q=newyork";
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
	   //ListView lv = (ListView)findViewById(R.id.listView1);
	   //lv.setAdapter(adapter);
	   
	   //jsonContent.setText(url1);
       obj = new JSONCalendarParser(url1, getApplicationContext());
       obj.fetchJSON();
       //Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
       while(obj.parsingComplete);
      
      //jsonContent.setText(obj.getContentFromJson().toString());
      
      //String result[] = obj.getContentFromJson();
     
       result = obj.getContentFromJson();
     
       System.out.println("Result = " + result);
      
       //jsonContent.setText(result[0]);
       
       String[] separated = result[0].split("[0-9].");
       
       System.out.println("separated = " + separated);
       
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    		  	android.R.layout.simple_list_item_1, result/*obj.getContentFromJson().toString()*/);

		ListView menuList = (ListView) findViewById(R.id.listView1);
		menuList.setAdapter(adapter);
		//menuList.setOnItemClickListener(this);
   }
}