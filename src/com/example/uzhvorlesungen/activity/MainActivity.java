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
import com.example.uzhvorlesungen.Utils;
import com.example.uzhvorlesungen.threading.FacultiesCallback;
import com.example.uzhvorlesungen.threading.ParsingAsyncTask;


public class MainActivity extends Activity implements FacultiesCallback {

	private ListView list = null;
	private Map<String, List<String>> facultiesMap = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView1);
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(getApplicationContext(), TitlesActivity.class);
				startActivity(intent);

			
			}
        	
		});
        
        ParsingAsyncTask task = new ParsingAsyncTask(this);
        task.execute();
        
//        String[] array = Utils.faculties;
//        
//        
//        
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, array);
//        list.setAdapter(adapter);

    
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
	public void onTaskCompleted(ArrayList<String> faculties, Map<String, List<String>> map) {
		facultiesMap = map;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, faculties);
		list.setAdapter(adapter);
	}
}
