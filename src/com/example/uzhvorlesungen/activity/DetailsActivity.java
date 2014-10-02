package com.example.uzhvorlesungen.activity;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.model.BeginEndLocation;
import com.example.uzhvorlesungen.model.Lecture;
import com.google.gson.Gson;

public class DetailsActivity extends Activity {

	Gson gson;
	Lecture lecture;
	TextView textDescription;
	TextView textExam;
	ImageButton btnDescUp;
	ImageButton btnDescDown;
	ImageButton btnExUp;
	ImageButton btnExDown;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		String serialized = getIntent().getStringExtra("details");

		gson = new Gson();
		lecture = gson.fromJson(serialized, Lecture.class);

		btnDescDown = (ImageButton) findViewById(R.id.dscButtonDown);
		btnDescDown.setVisibility(View.VISIBLE);
		btnDescUp = (ImageButton) findViewById(R.id.dscButtonUp);
		btnDescUp.setVisibility(View.GONE);
		
		btnExDown = (ImageButton) findViewById(R.id.exButtonDown);
		btnExDown.setVisibility(View.VISIBLE);
		btnExUp = (ImageButton) findViewById(R.id.exButtonUp);
		btnExUp.setVisibility(View.GONE);
		
		TextView title = (TextView) findViewById(R.id.titleTextView);
		title.setText(lecture.getTitle());

		textDescription = (TextView) findViewById(R.id.textDescription);
		textDescription.setVisibility(View.GONE);
		textDescription.setText(lecture.getDescription());
		
		textExam = (TextView) findViewById(R.id.textExamInfo);
		textExam.setVisibility(View.GONE);
		textExam.setText(lecture.getExam());
		
		TextView dayTimeTextView = (TextView) findViewById(R.id.dayTime);
		dayTimeTextView.setText(createDayTimeText(lecture.getDayBeginEndTime()));
	
		TextView pointsTextView = (TextView) findViewById(R.id.pointsTextView);
		pointsTextView.setText(lecture.getPoints() + " ECTS");
		
		TextView docentTextView = (TextView) findViewById(R.id.docentTextView);
		docentTextView.setText(lecture.getDocent());
		
		TextView locationTextView = (TextView) findViewById(R.id.roomTextView);
		locationTextView.setText(createLocationText(lecture.getDayBeginEndTime()));
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar, menu);
	    return true;
	}
	
	/**
	 * onClick handler
	 */
	public void toggle_contents(View v) {
		if(textDescription.isShown()){
			textDescription.setVisibility(View.GONE);
			btnDescUp.setVisibility(View.GONE);
			btnDescDown.setVisibility(View.VISIBLE);
		}else{
			textDescription.setVisibility(View.VISIBLE);
			btnDescDown.setVisibility(View.GONE);
			btnDescUp.setVisibility(View.VISIBLE);
			btnDescUp.setBackgroundResource(R.drawable.ic_action_navigation_collapse);
		}
	}
	
	public void toggle_contents_exam(View v){
		if(textExam.isShown()){
			textExam.setVisibility(View.GONE);
			btnExUp.setVisibility(View.GONE);
			btnExDown.setVisibility(View.VISIBLE);
		}else{
			textExam.setVisibility(View.VISIBLE);
			btnExDown.setVisibility(View.GONE);
			btnExUp.setVisibility(View.VISIBLE);
			btnExUp.setBackgroundResource(R.drawable.ic_action_navigation_collapse);
		}
	}
	
	private String createDayTimeText(HashMap<String, BeginEndLocation> map){
		StringBuilder sb = new StringBuilder();
		for (String iterator : map.keySet()) {
			if(iterator.equals("Nach Ankündigung")){
				return "keine Angaben";
			}
			BeginEndLocation bel =  map.get(iterator);
			sb.append(iterator).append(" : ").append(bel.begin).append(" - ").append(bel.end).append("\n");
		}
		String str = sb.toString();
		str = str.substring(0, str.length()-1);
		return str;
	}
	
	private String createLocationText(HashMap<String, BeginEndLocation> map){
		BeginEndLocation location = null;
		for(BeginEndLocation bel : map.values()){
			location = bel;
			break;
		}
		if(location == null || location.locations == null){
			return "keine Angaben";
		}
		StringBuilder sb = new StringBuilder();
		for(String loc : location.locations){
			sb.append(loc).append("\n");
		}
		String str = sb.toString();
		str = str.substring(0,str.length()-1);
		return str;
	}
}
