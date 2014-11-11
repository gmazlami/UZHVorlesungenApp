package com.example.uzhvorlesungen.activity;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.activity.majorminor.MajorMinorActivity;
import com.example.uzhvorlesungen.callbacks.FreeSearchCallbackInterface;
import com.example.uzhvorlesungen.threading.ParsingFreeSearchAsyncTask;
import com.google.gson.Gson;

public class FreeSearchActivity extends Activity implements FreeSearchCallbackInterface{

	protected ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freesearch);
        
        final EditText searchField = (EditText) findViewById(R.id.searchText);
        searchField.setHint("Suchbegriff eingeben..");
        
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progress = ProgressDialog.show(FreeSearchActivity.this, getString(R.string.gathering_data),getString(R.string.please_wait), true);
				String query = searchField.getText().toString();
				ParsingFreeSearchAsyncTask asyncTask = new ParsingFreeSearchAsyncTask(query, FreeSearchActivity.this);
				asyncTask.execute();
				
			}
		});
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



	@Override
	public void onTaskCompleted(Map<String, String> lectureTitlesLinksMap) {
		if(lectureTitlesLinksMap == null){ //if map is null this means the query failed --> show error message
			Toast.makeText(getApplicationContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();
		}else{
			Gson gson = new Gson();
			String serializedMap = gson.toJson(lectureTitlesLinksMap);
			Intent intent = new Intent(getApplicationContext(), LecturesActivity.class);
			intent.putExtra(MajorMinorActivity.EXTRA_LECTURES, serializedMap);
			intent.putExtra(MajorMinorActivity.EXTRA_STUDY, "Suchergebnisse");
			progress.dismiss();
			startActivity(intent);
		}
	}

}
