package com.example.uzhvorlesungen.threading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.htmlparser.util.ParserException;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.uzhvorlesungen.parsers.VVZStudiesParser;

public class ParsingTitlesCategoriesAsyncTask extends
		AsyncTask<String, Void, ArrayList<String>>{

	private FacultiesCallback callback;
	private Map<String, List<String>> facultiesMap = null;
	private Map<String, String> titlesLinksMap = null;
	private String URL = null;
	
	
	public ParsingTitlesCategoriesAsyncTask(FacultiesCallback callbackClass, String link) {
		this.callback = callbackClass;
		this.URL = link;
	}

	@Override
	protected ArrayList<String> doInBackground(String... params) {
//		String URL = "http://www.vorlesungen.uzh.ch/HS14/lehrangebot.html";
		String URLPrefix = "www.vorlesungen.uzh.ch/HS14/";
		// TODO: avoid hardcoding those links. Include config files instead.

		try {
			if(!URL.contains("http")){
				URL = "http://" + URL;
			}
			VVZStudiesParser parser = new VVZStudiesParser(URL, URLPrefix);
			facultiesMap = parser.parseFaculties();
			titlesLinksMap = parser.parseStudies();
		} catch (ParserException e) {
			e.printStackTrace();
			// TODO: add internet error handling
		}

		ArrayList<String> list = new ArrayList<String>();

		if (facultiesMap != null) {
			String[] arr = Arrays.copyOf(facultiesMap.keySet().toArray(),
					facultiesMap.size(), String[].class);
			for (int i = 0; i < arr.length; i++) {
				list.add(arr[i]);
			}
		}
		return list;
	}

	@Override
	protected void onPostExecute(ArrayList<String> list) {
		callback.onTaskCompleted(list, facultiesMap, titlesLinksMap);
	}

}
