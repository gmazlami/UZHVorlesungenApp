package com.example.uzhvorlesungen.activity;

import java.util.HashMap;
import java.util.Map;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.majorminor.MajorMinorActivity;
import com.example.uzhvorlesungen.activity.majorminor.PassedDataContainer;
import com.example.uzhvorlesungen.callbacks.DetailsCallbackInterface;
import com.example.uzhvorlesungen.model.Lecture;
import com.example.uzhvorlesungen.threading.ParsingDetailsAsyncTask;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LecturesActivity extends Activity implements DetailsCallbackInterface{

	Map<String, String> map;
	ProgressDialog progress;
	Gson gson;
	EditText searchText = null;
	private ArrayAdapter<String> adapter;
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        String study = getIntent().getStringExtra(MajorMinorActivity.EXTRA_STUDY);
        getActionBar().setTitle(study);
        
        ListView listView = (ListView) findViewById(R.id.listView1);
        
        gson = new Gson();
        String serialized = getIntent().getStringExtra(MajorMinorActivity.EXTRA_LECTURES);
        map = gson.fromJson(serialized, HashMap.class);
        
        String[] lectureArray = new String[map.keySet().size()];
        lectureArray = map.keySet().toArray(lectureArray);
        
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, lectureArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				progress = ProgressDialog.show(LecturesActivity.this, getString(R.string.gathering_data), getString(R.string.please_wait), true);
				TextView textView = (TextView) view;
				String lecture = textView.getText().toString();
				String link = map.get(lecture);
				ParsingDetailsAsyncTask asyncTask = new ParsingDetailsAsyncTask(link, LecturesActivity.this);
				asyncTask.execute();
			}
        	
		});
        
        searchText = (EditText) findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				LecturesActivity.this.adapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onTaskCompleted(Lecture lecture) {
		progress.dismiss();
		PassedDataContainer.passedLecture = lecture;
		Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
		startActivity(intent);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
	        switch (item.getItemId()) {
	        case android.R.id.home: 
	            onBackPressed();
	            return true;
	        }

	    return super.onOptionsItemSelected(item);
	}
	
}
