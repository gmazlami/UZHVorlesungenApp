package com.example.uzhvorlesungen.activity.majorminor;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.LecturesActivity;
import com.example.uzhvorlesungen.activity.TitlesActivity;
import com.example.uzhvorlesungen.callbacks.LecturesCallbackInterface;
import com.example.uzhvorlesungen.threading.ParsingLecturesAsyncTask;
import com.example.uzhvorlesungen.threading.ParsingMedLecturesAsyncTask;
import com.google.gson.Gson;

public class MajorMinorActivity extends Activity implements LecturesCallbackInterface{

	public static String EXTRA_LECTURES = "lectures";
	public static String EXTRA_STUDY = "study";
	
	private String study;
	private ExpandableListAdapter listAdapter;
	private HashMap<String, List<String>> majorsStudies;
	private HashMap<String, String> studiesLinks;
	private List<String> majors;
	private Gson gson = null;
	private ProgressDialog progress;
	private String title;
	
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        majors = PassedDataContainer.majors;
        title = getIntent().getStringExtra(TitlesActivity.EXTRA_TITLE);
        getActionBar().setTitle(title);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if(majors.size() < 2){
        	setContentView(R.layout.activity_major_minor_single);
        	ListView singleList = (ListView) findViewById(R.id.alternativeMajorsList);
        	
        	String studiesLinksExtra = getIntent().getExtras().getString(TitlesActivity.EXTRA_LINKS);
        	String majorsStudiesExtra = getIntent().getExtras().getString(TitlesActivity.EXTRA_STUDIES);
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
					study = textView.getText().toString();
					String link = studiesLinks.get(study);
					System.out.println("----------studiesLinks: "+studiesLinks);
					
					if(PassedDataContainer.bscMscMed == true){
						progress = ProgressDialog.show(MajorMinorActivity.this, getString(R.string.gathering_data), getString(R.string.please_wait),true);
						ParsingMedLecturesAsyncTask asyncTask = new ParsingMedLecturesAsyncTask(MajorMinorActivity.this, link);
						asyncTask.execute();
					}else{
						progress = ProgressDialog.show(MajorMinorActivity.this, getString(R.string.gathering_data), getString(R.string.please_wait),true);
						ParsingLecturesAsyncTask asyncTask = new ParsingLecturesAsyncTask(link, MajorMinorActivity.this);
						asyncTask.execute();
					}
				}
        		
			});

        	
        }else{
        	
        	setContentView(R.layout.activity_major_minor);
        	getActionBar().setTitle(getString(R.string.actionbar_majorminor));
        	
        	String studiesLinksExtra = getIntent().getExtras().getString(TitlesActivity.EXTRA_LINKS);
        	String majorsStudiesExtra = getIntent().getExtras().getString(TitlesActivity.EXTRA_STUDIES);
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
					study = textView.getText().toString();
					String link = PassedDataContainer.getLinkForGroupChild(groupPosition, study);
					progress = ProgressDialog.show(MajorMinorActivity.this, getString(R.string.gathering_data), getString(R.string.please_wait),true);
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
		intent.putExtra(EXTRA_LECTURES, serializedMap);
		intent.putExtra(EXTRA_STUDY, study);
		progress.dismiss();
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
