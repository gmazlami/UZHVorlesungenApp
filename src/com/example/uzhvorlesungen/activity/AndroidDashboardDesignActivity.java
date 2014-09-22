package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.threading.FacultiesCallbackInterface;
import com.example.uzhvorlesungen.threading.ParsingFacultiesTitlesAsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AndroidDashboardDesignActivity extends Activity implements FacultiesCallbackInterface{
    
    private Map<String, List<String>> facultiesMap;
	private Map<String, String> titlesLinksMap;
	private ProgressDialog progress;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        getActionBar().setTitle("Fakultät wählen");
        progress = ProgressDialog.show(this, "Hole Daten","Bitte warten", true);
		ParsingFacultiesTitlesAsyncTask task = new ParsingFacultiesTitlesAsyncTask(this);
		task.execute();
        
        
        /**
         * Creating all buttons instances
         * */
        
        Button btnMNF = (Button) findViewById(R.id.btn_mnf);
        
        Button btnWWF = (Button) findViewById(R.id.btn_wwf);
        
        Button btnRWF = (Button) findViewById(R.id.btn_rwf);
        
        Button btnMedF = (Button) findViewById(R.id.btn_medf);

        Button btnPhF = (Button) findViewById(R.id.btn_phf);
        
        Button btnVetF = (Button) findViewById(R.id.btn_vetf);
        
        Button btnThF = (Button) findViewById(R.id.btn_thf);
        
        /**
         * Handling all button click events
         * */
        
        // Listening to News Feed button click
        btnMNF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String faculty = "Mathematisch-naturwissenschaftliche Fakultät";
				startTitlesActivity(faculty);
			}
		});
        
       // Listening Friends button click
        btnWWF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String faculty = "Wirtschaftswissenschaftliche Fakultät";
				startTitlesActivity(faculty);
			}
		});
        
        // Listening Messages button click
        btnRWF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String faculty = "Rechtswissenschaftliche Fakultät";
				startTitlesActivity(faculty);
			}
		});
        
        // Listening to Places button click
        btnMedF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String faculty = "Medizinische Fakultät";
				startTitlesActivity(faculty);
			}
		});
        
        // Listening to Events button click
        btnPhF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String faculty = "Philosophische Fakultät";
				startTitlesActivity(faculty);
			}
		});
        
        // Listening to Photos button click
        btnVetF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String faculty = "Vetsuisse-Fakultät";
				startTitlesActivity(faculty);
			}
		});
        
        btnThF.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String faculty = "Theologische Fakultät";
				startTitlesActivity(faculty);
			}
		});
    }

	@Override
	public void onTaskCompleted(ArrayList<String> faculties,
			Map<String, List<String>> map,
			Map<String, String> titlesMap) {
		
		facultiesMap = map;
		titlesLinksMap = titlesMap;
		progress.dismiss();
	}
	
	private void startTitlesActivity(String faculty){
		Intent intent = new Intent(getApplicationContext(),
				TitlesActivity.class);
		List<String> passedList = facultiesMap.get(faculty);
		List<String> linksList = new ArrayList<String>();
		
		for(int i =0; i < passedList.size(); i++){
			String title = passedList.get(i);
			linksList.add(titlesLinksMap.get(title));
//			System.out.println(title + "  :  " + titlesLinksMap.get(title));
		}
		
		Gson gson = new GsonBuilder().create();
		
		String serializedTitles = gson.toJson(passedList);
		intent.putExtra("titles", serializedTitles);
		
		String serializedLinks = gson.toJson(linksList);
		intent.putExtra("links", serializedLinks);
		startActivity(intent);
	}
}