package com.example.uzhvorlesungen.threading;

import java.util.Map;

import org.htmlparser.util.ParserException;

import android.os.AsyncTask;

import com.example.uzhvorlesungen.parsers.VVZLecturesParser;


public class ParsingLecturesAsyncTask extends AsyncTask<String, Void, Map<String,String>>{

	
	private String URL;
	private final String URLPrefix = "www.vorlesungen.uzh.ch/HS14/";
	private LecturesCallbackInterface callback;
	
	public ParsingLecturesAsyncTask(String link, LecturesCallbackInterface callback){
		this.URL = link;
		this.callback = callback;
	}
	
	@Override
	protected Map<String, String> doInBackground(String... params) {
		if(!URL.contains("http")){
			URL = "http://" + URL;
		}
		VVZLecturesParser parser = new VVZLecturesParser(URL, URLPrefix);
		Map<String, String> resultMap = null ;
		try{
			resultMap = parser.parse();
		}catch(ParserException e){
			e.printStackTrace();
			//TODO: better exception handling
		}
		return resultMap;
	}
	
	
	@Override
	protected void onPostExecute(Map<String,String> map) {
		callback.onTaskCompleted(map);
	}


}
