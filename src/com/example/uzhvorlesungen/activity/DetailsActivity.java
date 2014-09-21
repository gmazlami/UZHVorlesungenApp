package com.example.uzhvorlesungen.activity;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.parsers.Lecture;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;

public class DetailsActivity extends Activity {

	Gson gson;
	Lecture lecture;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String serialized = getIntent().getStringExtra("details");
       
        gson = new Gson();
        lecture = gson.fromJson(serialized, Lecture.class);
	}
}
