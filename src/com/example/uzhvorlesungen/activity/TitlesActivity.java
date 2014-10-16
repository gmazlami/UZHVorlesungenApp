package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.majorminor.MajorMinorActivity;
import com.example.uzhvorlesungen.activity.majorminor.PassedDataContainer;
import com.example.uzhvorlesungen.callbacks.FacultiesCallbackInterface;
import com.example.uzhvorlesungen.callbacks.MMCallBackInterface;
import com.example.uzhvorlesungen.threading.ParsingMMAsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TitlesActivity extends Activity implements FacultiesCallbackInterface, MMCallBackInterface{

	public static final String EXTRA_LINKS ="links";
	public static final String EXTRA_MAJORS = "majors";
	public static final String EXTRA_STUDIES = "studies";
	public static final String EXTRA_TITLE = "title";
	
	private ListView mListView = null;
	private List<String> mListTitles = null;
	private List<String> mListLinks = null;
	private ParsingMMAsyncTask asyncTask = null;
	
	private Map<String, List<String>> majorStudiesMap = null;
	private Map<String, String> studiesLinksMap = null;
	private ArrayList<String> majorList = null;
	private ProgressDialog progress;
	private String title;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);
        getActionBar().setTitle(getString(R.string.titles));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        
        //find UI views
        mListView = (ListView) findViewById(R.id.titlesList);
        
        //get data from previous activity
        mListTitles = PassedDataContainer.passedTitles;
        mListLinks = PassedDataContainer.passedTitlesLinks;
        
        //display data from previous activity on list
        String[] array = new String[mListTitles.size()];
        array = mListTitles.toArray(array);
        mListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, array));
    
        //set listener for list item clicking
        mListView.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				progress = ProgressDialog.show(TitlesActivity.this, getString(R.string.gathering_data),getString(R.string.please_wait), true);
				TextView textView = (TextView) view;
				title = textView.getText().toString();
				if(title.contains("Bachelor") && title.contains("Humanmedizin") || title.contains("medizin")){
					PassedDataContainer.bscMscMed = true;
				}else if(title.contains("Master") && title.contains("Humanmedizin") || title.contains("medizin")){
					PassedDataContainer.bscMscMed = true;
				}
				String link = mListLinks.get(position);

				asyncTask = new ParsingMMAsyncTask(TitlesActivity.this, link);
				asyncTask.execute();
			}
		});
    }


	@Override
	public void onTaskCompleted(ArrayList<String> majors,
			Map<String, List<String>> map,
			Map<String, String> studiesMap) {
		
		majorStudiesMap = map;
		majorList = majors;
		PassedDataContainer.majors = majors;
		studiesLinksMap = studiesMap;
		
		Gson gson = new GsonBuilder().create();
		
		String serializedMajors = gson.toJson(majorList);
		String serializedMajorStudiesMap = gson.toJson(majorStudiesMap);
		String serializedStudiesLinksMap = gson.toJson(studiesLinksMap);
		
		progress.dismiss();
		
		
		Intent intent  = new Intent(getApplicationContext(), MajorMinorActivity.class);
		intent.putExtra(EXTRA_MAJORS, serializedMajors);
		intent.putExtra(EXTRA_LINKS, serializedStudiesLinksMap);
		intent.putExtra(EXTRA_STUDIES, serializedMajorStudiesMap);
		startActivity(intent);
		
	}


	@Override
	public void onTaskCompleted(HashMap<String, String> map,
			ArrayList<String> faculties,
			Map<String, List<String>> facultiesMap,
			Map<String, String> titlesMap) {

		majorStudiesMap = facultiesMap;
		majorList = faculties;
		studiesLinksMap = titlesMap;

		
		PassedDataContainer.majors = faculties;
		PassedDataContainer.studiesLinks = map;
		
		Gson gson = new GsonBuilder().create();
		
		String serializedMajors = gson.toJson(majorList);
		String serializedMajorStudiesMap = gson.toJson(majorStudiesMap);
		String serializedStudiesLinksMap = gson.toJson(studiesLinksMap);
		
		progress.dismiss();
		
		
		Intent intent  = new Intent(getApplicationContext(), MajorMinorActivity.class);
		intent.putExtra(EXTRA_MAJORS, serializedMajors);
		intent.putExtra(EXTRA_LINKS, serializedStudiesLinksMap);
		intent.putExtra(EXTRA_STUDIES, serializedMajorStudiesMap);
		intent.putExtra(EXTRA_TITLE, title);
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
