package com.example.uzhvorlesungen.activity;

import com.example.uzhvorlesungen.R;
import com.example.uzhvorlesungen.threading.CallBackInterface;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class TitlesActivity extends Activity implements CallBackInterface{

	private ListView mListView = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.titlesList);
    }
	@Override
	public void onTaskCompleted() {
		// TODO Auto-generated method stub
		
	}
}
