package com.example.uzhvorlesungen.threading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.util.ParserException;

import android.os.AsyncTask;

import com.example.uzhvorlesungen.parsers.VVZMMParser;
import com.example.uzhvorlesungen.parsers.VVZStudiesParser;

public class ParsingMMAsyncTask extends AsyncTask<String, Void, HashMap<String,String>> {

	private MMCallBackInterface callback;
	private String URL;
	private Map<String, List<String>> facultiesMap;
	private Map<String, String> titlesLinksMap;
	private ArrayList<String> list;
	
	public ParsingMMAsyncTask(MMCallBackInterface Icallback, String link){
		this.callback = Icallback;
		this.URL = link;
	}
	
	@Override
	protected HashMap<String,String> doInBackground(String... params) {
		String URLPrefix = "www.vorlesungen.uzh.ch/HS14/";
		// TODO: avoid hardcoding those links. Include config files instead.
		HashMap<String,String> map = null;
		try {
			if(!URL.contains("http")){
				URL = "http://" + URL;
			}
			VVZMMParser parser = new VVZMMParser(URL, URLPrefix);
			map  = parser.parseMajorMinor();
			VVZStudiesParser scndparser = new VVZStudiesParser(URL, URLPrefix);
			facultiesMap = scndparser.parseFaculties();
			titlesLinksMap = scndparser.parseStudies();
			
		} catch (ParserException e) {
			e.printStackTrace();
			// TODO: add internet error handling
		}
		
		list = new ArrayList<String>();

		if (facultiesMap != null) {
			String[] arr = Arrays.copyOf(facultiesMap.keySet().toArray(),
					facultiesMap.size(), String[].class);
			for (int i = 0; i < arr.length; i++) {
				list.add(arr[i]);
			}
		}
		
		return map;
	}
	
	@Override
	protected void onPostExecute(HashMap<String,String> map){
		callback.onTaskCompleted(map,list,facultiesMap,titlesLinksMap);
	}

	
}
