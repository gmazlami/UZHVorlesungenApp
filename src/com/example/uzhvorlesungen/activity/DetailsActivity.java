package com.example.uzhvorlesungen.activity;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.parsers.Lecture;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends Activity {

	Gson gson;
	Lecture lecture;
	TextView textDescription;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		String serialized = getIntent().getStringExtra("details");

		gson = new Gson();
		lecture = gson.fromJson(serialized, Lecture.class);

		TextView title = (TextView) findViewById(R.id.titleTextView);
		title.setText(lecture.getTitle());

		textDescription = (TextView) findViewById(R.id.textDescription);
		textDescription.setVisibility(View.GONE);
		textDescription.setText(lecture.getDescription());
		
		TextView dayTimeTextView = (TextView) findViewById(R.id.dayTime);
		dayTimeTextView.setText(lecture.getDay() + " um " + lecture.getBeginTime() + "-" + lecture.getEndTime());
	}

	/**
	 * onClick handler
	 */
	public void toggle_contents(View v) {
		textDescription.setVisibility(textDescription.isShown() ? View.GONE
				: View.VISIBLE);
	}
}
