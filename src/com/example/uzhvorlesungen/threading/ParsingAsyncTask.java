package com.example.uzhvorlesungen.threading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.htmlparser.util.ParserException;

import com.example.uzhvorlesungen.parsers.VVZStudiesParser;

import android.os.AsyncTask;
import android.widget.TextView;

public class ParsingAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

	private FacultiesCallback callback;
	private Map<String, List<String>> facultiesMap = null;
	
	public ParsingAsyncTask(FacultiesCallback callbackClass){
		this.callback = callbackClass;
	}
	
	@Override
	protected ArrayList<String> doInBackground(String... params) {
		String URL = "http://www.vorlesungen.uzh.ch/HS14/lehrangebot.html";
		String URLPrefix = "www.vorlesungen.uzh.ch/HS14/";
		
		try {
			VVZStudiesParser parser = new VVZStudiesParser(URL, URLPrefix);
			facultiesMap = parser.parseFaculties();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> list = new ArrayList<String>();
		
		if(facultiesMap != null){
			String[] arr = Arrays.copyOf(facultiesMap.keySet().toArray(),facultiesMap.size(), String[].class);
			for(int i = 0; i<arr.length;i++){
				list.add(arr[i]);
//				list.add(facultiesMap.get(arr[i]));
		}
			
			
			
		}
		return list;
	}
	
	@Override
	protected void onPostExecute(ArrayList<String> list){
		callback.onTaskCompleted(list,facultiesMap);
	}

}