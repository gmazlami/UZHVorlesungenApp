package com.example.uzhvorlesungen.activity;

import java.util.ArrayList;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.threading.CallBackInterface;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class TitlesActivity extends Activity implements CallBackInterface{

	private ListView mListView = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.titlesList);
        
        Intent intent = getIntent();
        String json = intent.getExtras().getString("test");
        
        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(json, ArrayList.class);
        
        Toast.makeText(getApplicationContext(), list.get(0), 20000).show();
    }
	@Override
	public void onTaskCompleted() {
		// TODO Auto-generated method stub
		
	}
}
