package com.example.uzhvorlesungen.activity.majorminor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.uzhvorlesungen.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class MajorMinorActivity extends Activity {

	private ExpandableListAdapter listAdapter;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	private HashMap<String, List<String>> majorsStudies;
	private HashMap<String, String> studiesLinks;
	private List<String> majors;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_minor);
        getActionBar().setTitle("Hauptfach / Nebenfach");
        
        String majorsExtra = getIntent().getExtras().getString("majors");
        String studiesLinksExtra = getIntent().getExtras().getString("links");
        String majorsStudiesExtra = getIntent().getExtras().getString("studies");
        
        Gson gson = new Gson();
        
        majorsStudies = gson.fromJson(majorsStudiesExtra, HashMap.class);
        studiesLinks = gson.fromJson(studiesLinksExtra, HashMap.class);
        majors = gson.fromJson(majorsExtra, ArrayList.class);
        
        ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.expandableListView1);
        
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, majors, majorsStudies);
        
        // setting list adapter
        expandableList.setAdapter(listAdapter);
    }
    
    
    
    private void getListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
    }
    
    
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");
 
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");
 
        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");
 
        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");
 
        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
