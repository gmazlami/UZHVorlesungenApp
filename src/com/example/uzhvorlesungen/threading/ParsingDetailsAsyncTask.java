package com.example.uzhvorlesungen.threading;

import org.htmlparser.util.ParserException;

import com.example.uzhvorlesungen.parsers.Lecture;
import com.example.uzhvorlesungen.parsers.VVZDetailsParser;

import android.os.AsyncTask;

public class ParsingDetailsAsyncTask extends
		AsyncTask<String, Void, Lecture> {

	private String URL;
	private DetailsCallbackInterface callback;
	private final String URLPrefix = "www.vorlesungen.uzh.ch/HS14/";
	private Lecture lec;
	
	
	public ParsingDetailsAsyncTask(String link, DetailsCallbackInterface callback){
		this.callback = callback;
		this.URL = link;
		if(!URL.contains("http")){
			URL = "http://" + URL;
		}
	}
	
	@Override
	protected Lecture doInBackground(String... params) {

		VVZDetailsParser parser = new VVZDetailsParser(URL, URLPrefix);
		try{
			lec = parser.parse();
		}catch(ParserException e){
			e.printStackTrace();
		}
		return lec;
	}
	
	@Override
	protected void onPostExecute(Lecture lecture){
		callback.onTaskCompleted(lecture);
	}

}
