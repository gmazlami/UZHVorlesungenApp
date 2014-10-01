package com.example.uzhvorlesungen.activity.majorminor;

import java.util.HashMap;
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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.LecturesActivity;
import com.example.uzhvorlesungen.threading.LecturesCallbackInterface;
import com.example.uzhvorlesungen.threading.ParsingLecturesAsyncTask;
import com.example.uzhvorlesungen.threading.ParsingMedLecturesAsyncTask;
import com.google.gson.Gson;

public class MajorMinorActivity extends Activity implements LecturesCallbackInterface{

	private ExpandableListAdapter listAdapter;
	private HashMap<String, List<String>> majorsStudies;
	private HashMap<String, String> studiesLinks;
	private List<String> majors;
	private Gson gson = null;
	private ProgressDialog progress;
	
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        majors = PassedDataContainer.majors;
        if(majors.size() < 2){
        	setContentView(R.layout.activity_major_minor_single);
        	ListView singleList = (ListView) findViewById(R.id.alternativeMajorsList);
        	
        	String studiesLinksExtra = getIntent().getExtras().getString("links");
        	String majorsStudiesExtra = getIntent().getExtras().getString("studies");
        	majorsStudies = gson.fromJson(majorsStudiesExtra, HashMap.class);
        	studiesLinks = gson.fromJson(studiesLinksExtra, HashMap.class);
        	
        	List<String> studies = majorsStudies.get(majors.get(0));
        	
        	String[] array = new String[studies.size()];
        	array = studies.toArray(array);
        	
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, array);
        	singleList.setAdapter(adapter);
        	
        	singleList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView textView = (TextView) view;
					String study = textView.getText().toString();
					String link = studiesLinks.get(study);
					
					if(PassedDataContainer.bscMscMed == true){
						progress = ProgressDialog.show(MajorMinorActivity.this, "Hole Daten", "Bitte warten.",true);
						ParsingMedLecturesAsyncTask asyncTask = new ParsingMedLecturesAsyncTask(MajorMinorActivity.this, link);
						asyncTask.execute();
					}else{
						progress = ProgressDialog.show(MajorMinorActivity.this, "Hole Daten", "Bitte warten.",true);
						ParsingLecturesAsyncTask asyncTask = new ParsingLecturesAsyncTask(link, MajorMinorActivity.this);
						asyncTask.execute();
					}
				}
        		
			});

        	
        }else{
        	
        	setContentView(R.layout.activity_major_minor);
        	getActionBar().setTitle("Hauptfach / Nebenfach");
        	
        	String studiesLinksExtra = getIntent().getExtras().getString("links");
        	String majorsStudiesExtra = getIntent().getExtras().getString("studies");
        	majorsStudies = gson.fromJson(majorsStudiesExtra, HashMap.class);
        	studiesLinks = gson.fromJson(studiesLinksExtra, HashMap.class);
        	
        	ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.expandableListView1);
        	
        	listAdapter = new ExpandableListAdapter(this, majors, majorsStudies);
        	
        	// setting list adapter
        	expandableList.setAdapter(listAdapter);
        	
        	expandableList.setOnChildClickListener(new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					TextView textView = (TextView) v;
					String study = textView.getText().toString();
					String link = PassedDataContainer.getLinkForGroupChild(groupPosition, study);
					progress = ProgressDialog.show(MajorMinorActivity.this, "Hole Daten", "Bitte warten.",true);
					ParsingLecturesAsyncTask asyncTask = new ParsingLecturesAsyncTask(link, MajorMinorActivity.this);
					asyncTask.execute();
					
					return false;
				}
			});
        }
    }

	@Override
	public void onTaskCompleted(Map<String, String> map) {
		String serializedMap = gson.toJson(map);
		Intent intent = new Intent(getApplicationContext(), LecturesActivity.class);
		intent.putExtra("lectures", serializedMap);
		progress.dismiss();
		startActivity(intent);
	}
    
    
    
}
