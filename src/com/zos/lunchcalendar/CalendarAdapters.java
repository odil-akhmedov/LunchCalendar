package com.zos.lunchcalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarAdapters extends BaseAdapter {

	private Context mContext;
	private Vector<String> vector;

	public CalendarAdapters(Context context) {
		mContext = context;
		vector = new Vector<String>();
		// Creating the AssetManager object and open "myTestFile.txt" for read.
		AssetManager am = mContext.getAssets();
		StringBuilder buf = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					am.open("JSON3.json")));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				// Here we are printing each line at a time from the file we
				// read
				buf.append(sCurrentLine);
				System.out.println(sCurrentLine);
				// Insert the file content line by line into the Vector
				vector.add(sCurrentLine);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vector.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		TextView txt = new TextView(mContext);
		txt.setText(vector.get(position));
		return txt;
	}

}
