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
	TextView textExam;
	
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
		
		textExam = (TextView) findViewById(R.id.textExamInfo);
		textExam.setVisibility(View.GONE);
		textExam.setText(lecture.getExam());
		
		TextView dayTimeTextView = (TextView) findViewById(R.id.dayTime);
		dayTimeTextView.setText(lecture.getDay()+" "+ lecture.getBeginTime() + "-" + lecture.getEndTime());
	
		TextView pointsTextView = (TextView) findViewById(R.id.pointsTextView);
		pointsTextView.setText(lecture.getPoints() + " ECTS");
		
		TextView docentTextView = (TextView) findViewById(R.id.docentTextView);
		docentTextView.setText(lecture.getDocent());
		
		TextView locationTextView = (TextView) findViewById(R.id.roomTextView);
		locationTextView.setText(lecture.getLocation());
	
	}

	/**
	 * onClick handler
	 */
	public void toggle_contents(View v) {
		textDescription.setVisibility(textDescription.isShown() ? View.GONE
				: View.VISIBLE);
	}
	
	public void toggle_contents_exam(View v){
		textExam.setVisibility(textExam.isShown() ? View.GONE : View.VISIBLE);
	}
}
