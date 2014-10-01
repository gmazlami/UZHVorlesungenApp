package com.example.uzhvorlesungen.threading;

import java.util.Map;

import org.htmlparser.util.ParserException;

import android.os.AsyncTask;

import com.example.uzhvorlesungen.callbacks.LecturesCallbackInterface;
import com.example.uzhvorlesungen.parsers.VVZMedLecturesParser;

public class ParsingMedLecturesAsyncTask extends AsyncTask<String, Void, Map<String, String>>{

	private LecturesCallbackInterface callback;
	private String URL;
	
	public ParsingMedLecturesAsyncTask(LecturesCallbackInterface iCallback, String resourceURL){
		this.callback = iCallback;
		this.URL = resourceURL;
	}
	
	@Override
	protected Map<String, String> doInBackground(String... params) {
		if(!URL.contains("http")){
			URL = "http://" + URL;
		}

		VVZMedLecturesParser parser = new VVZMedLecturesParser(URL);
		Map<String, String> resultMap = null ;
		try{
			resultMap = parser.parseSites();
		}catch(ParserException e){
			e.printStackTrace();
			//TODO: better exception handling
		}
		return resultMap;
	}

	
	@Override
	protected void onPostExecute(Map<String, String> map){
		callback.onTaskCompleted(map);
	}
}
