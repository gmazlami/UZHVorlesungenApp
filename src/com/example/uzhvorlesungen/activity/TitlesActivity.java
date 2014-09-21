package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.majorminor.MajorMinorActivity;
import com.example.uzhvorlesungen.threading.CallBackInterface;
import com.example.uzhvorlesungen.threading.FacultiesCallbackInterface;
import com.example.uzhvorlesungen.threading.ParsingFacultiesTitlesAsyncTask;
import com.example.uzhvorlesungen.threading.ParsingTitlesCategoriesAsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TitlesActivity extends Activity implements FacultiesCallbackInterface{

	private ListView mListView = null;
	private ArrayList<String> mListTitles = null;
	private ArrayList<String> mListLinks = null;
	private ParsingTitlesCategoriesAsyncTask asyncTask = null;
	
	private Map<String, List<String>> majorStudiesMap = null;
	private Map<String, String> studiesLinksMap = null;
	private ArrayList<String> majorList = null;
	private ProgressDialog progress;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);
        getActionBar().setTitle("Abschlüsse");
        
        //find UI views
        mListView = (ListView) findViewById(R.id.titlesList);
        
        //get data from previous activity
        Intent intent = getIntent();
        String jsonTitles = intent.getExtras().getString("titles");
        String jsonLinks = intent.getExtras().getString("links");
        Gson gson = new Gson();
        mListTitles = gson.fromJson(jsonTitles, ArrayList.class);
        mListLinks = gson.fromJson(jsonLinks,ArrayList.class);
        
        //display data from previous activity on list
        String[] array = new String[mListTitles.size()];
        array = mListTitles.toArray(array);
        mListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, array));
    
        //set listener for list item clicking
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				progress = ProgressDialog.show(TitlesActivity.this, "Hole Daten","Bitte warten", true);
				
				String link = mListLinks.get(position);

//				Toast.makeText(getApplicationContext(), link, Toast.LENGTH_LONG).show();
				asyncTask = new ParsingTitlesCategoriesAsyncTask(TitlesActivity.this, link);
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
		studiesLinksMap = studiesMap;
		
		Gson gson = new GsonBuilder().create();
		
		String serializedMajors = gson.toJson(majorList);
		String serializedMajorStudiesMap = gson.toJson(majorStudiesMap);
		String serializedStudiesLinksMap = gson.toJson(studiesLinksMap);
		
		
		progress.dismiss();
		
		
		Intent intent  = new Intent(getApplicationContext(), MajorMinorActivity.class);
		intent.putExtra("majors", serializedMajors);
		intent.putExtra("links", serializedStudiesLinksMap);
		intent.putExtra("studies", serializedMajorStudiesMap);
		startActivity(intent);
		
	}
}
