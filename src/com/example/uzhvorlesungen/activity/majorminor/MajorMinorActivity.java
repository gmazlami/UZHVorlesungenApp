package com.example.uzhvorlesungen.activity.majorminor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.uzhvorlesungen.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MajorMinorActivity extends Activity {

	private ExpandableListAdapter listAdapter;
	private HashMap<String, List<String>> majorsStudies;
	private HashMap<String, String> studiesLinks;
	private List<String> majors;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        String majorsExtra = getIntent().getExtras().getString("majors");
        majors = gson.fromJson(majorsExtra, ArrayList.class);

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
        }
    }
    
    
    
}
