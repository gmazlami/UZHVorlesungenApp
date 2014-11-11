package com.example.uzhvorlesungen.threading;

import java.util.Map;

import org.htmlparser.util.ParserException;

import android.os.AsyncTask;

import com.example.uzhvorlesungen.callbacks.FreeSearchCallbackInterface;
import com.example.uzhvorlesungen.data.GlobalAppData;
import com.example.uzhvorlesungen.parsers.VVZFreeSearchParser;

public class ParsingFreeSearchAsyncTask extends
		AsyncTask<String, Void, Map<String, String>> {

	private String URL;
	private String query;
	private FreeSearchCallbackInterface callback;

	public ParsingFreeSearchAsyncTask(String query,FreeSearchCallbackInterface callback) {
		this.callback = callback;
		this.query = query;
	}

	@Override
	protected Map<String, String> doInBackground(String... params) {
		try{
			VVZFreeSearchParser parser = new VVZFreeSearchParser(GlobalAppData.SEMESTER_PREFERENCE, query);
			return parser.getLectureLinks();
		}catch(ParserException e){
			return null;
		}
	}

	@Override
	protected void onPostExecute(Map<String, String> lectureTitlesLinksMap) {
		callback.onTaskCompleted(lectureTitlesLinksMap);
	}

}
