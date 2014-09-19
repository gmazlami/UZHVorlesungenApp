package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.threading.FacultiesCallback;
import com.example.uzhvorlesungen.threading.ParsingFacultiesTitlesAsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends Activity implements FacultiesCallback {

	private ListView list = null;
	private Map<String, List<String>> facultiesMap = null;
	private ArrayList<String> facultiesList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(facultiesMap != null){
					Intent intent = new Intent(getApplicationContext(),
							TitlesActivity.class);
//					intent.putExtra(name, value)
					
					String faculty = facultiesList.get(position);
					List<String> passedList = facultiesMap.get(faculty);

					Gson gson = new GsonBuilder().create();
					String serialized = gson.toJson(passedList);

					intent.putExtra("test", serialized);
					startActivity(intent);
				}

			}

		});

		ParsingFacultiesTitlesAsyncTask task = new ParsingFacultiesTitlesAsyncTask(this);
		task.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTaskCompleted(ArrayList<String> faculties,
			Map<String, List<String>> map) {
		facultiesMap = map;
		facultiesList = faculties;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.list_row_item, faculties);
		list.setAdapter(adapter);
	}
}
