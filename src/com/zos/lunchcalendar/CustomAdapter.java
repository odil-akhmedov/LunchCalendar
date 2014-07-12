package com.zos.lunchcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<DailyMenu> {

	private Context context;
	private DailyMenu[] data;
	private LayoutInflater layoutInflater;

	public CustomAdapter(Context context, int textViewResourceId,
			DailyMenu[] lunch_data) {
		super(context, textViewResourceId, lunch_data);

		// store constructor parameters
		this.context = context;
		this.data = lunch_data;
		// store objects that will be used multiple times
		this.layoutInflater = LayoutInflater.from(this.context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// if convertView isn't populated, use LayoutInflater to
		// create a new row layout
		View row = convertView;
		if (row == null) {
			row = this.layoutInflater.inflate(R.layout.custom_row_textview, null);
		}
		// extract data
		DailyMenu item = this.data[position];
		// assign the caption
		
		TextView lunchDate = (TextView) row.findViewById(R.id.showDate);
		lunchDate.setText(item.getStartTime());
		
		TextView lunchTitle = (TextView) row.findViewById(R.id.showTitle);
		lunchTitle.setText(item.getTitle());
		// assign the icon
		//ImageView lunchIcon = (ImageView) row.findViewById(R.id.imageview);

		//lunchIcon.setImageResource(item.getIcon());
		return row;
	}

}
