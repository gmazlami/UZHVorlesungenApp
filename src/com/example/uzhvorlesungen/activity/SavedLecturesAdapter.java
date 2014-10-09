package com.example.uzhvorlesungen.activity;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SavedLecturesAdapter extends ArrayAdapter<String> {

	public SavedLecturesAdapter(Context context, int resource,int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);

		
	}

}
