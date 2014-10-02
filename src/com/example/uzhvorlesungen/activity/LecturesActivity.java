package com.example.uzhvorlesungen.activity;

import java.util.HashMap;
import java.util.Map;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.callbacks.DetailsCallbackInterface;
import com.example.uzhvorlesungen.model.Lecture;
import com.example.uzhvorlesungen.threading.ParsingDetailsAsyncTask;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LecturesActivity extends Activity implements DetailsCallbackInterface{

	Map<String, String> map;
	ProgressDialog progress;
	Gson gson;
	
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ListView listView = (ListView) findViewById(R.id.listView1);
        
        gson = new Gson();
        String serialized = getIntent().getStringExtra("lectures");
        map = gson.fromJson(serialized, HashMap.class);
        
        String[] lectureArray = new String[map.keySet().size()];
        lectureArray = map.keySet().toArray(lectureArray);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, lectureArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				progress = ProgressDialog.show(LecturesActivity.this, "Hole Daten", "Bitte warten", true);
				TextView textView = (TextView) view;
				String lecture = textView.getText().toString();
				String link = map.get(lecture);
				ParsingDetailsAsyncTask asyncTask = new ParsingDetailsAsyncTask(link, LecturesActivity.this);
				asyncTask.execute();
			}
        	
		});
	}

	@Override
	public void onTaskCompleted(Lecture lecture) {
		progress.dismiss();
		String serialized = gson.toJson(lecture);
		
		Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
		intent.putExtra("details", serialized);
		startActivity(intent);
		
	}
	
}
