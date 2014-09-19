package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.threading.CallBackInterface;
import com.google.gson.Gson;

public class TitlesActivity extends Activity implements CallBackInterface{

	private ListView mListView = null;
	private ArrayList<String> mList = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);
        mListView = (ListView) findViewById(R.id.titlesList);
        
        
        Intent intent = getIntent();
        String json = intent.getExtras().getString("test");
        
        Gson gson = new Gson();
        mList = gson.fromJson(json, ArrayList.class);
        
        String[] array = new String[mList.size()];
        array = mList.toArray(array);
        mListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_row_item, array));
    }
	@Override
	public void onTaskCompleted() {
		// TODO Auto-generated method stub
		
	}
}
