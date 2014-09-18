package com.example.uzhvorlesungen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.htmlparser.util.ParserException;

import com.example.uzhvorlesungen.parsers.VVZStudiesParser;

import android.os.AsyncTask;
import android.widget.TextView;

public class ParsingAsyncTask extends AsyncTask<String, Void, String> {

	private TextView text = null;
	
	public ParsingAsyncTask(TextView text){
		this.text = text;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String URL = "http://www.vorlesungen.uzh.ch/HS14/lehrangebot.html";
		String URLPrefix = "www.vorlesungen.uzh.ch/HS14/";
		Map<String, String> titlesLinksMap = null;
		
		try {
			VVZStudiesParser parser = new VVZStudiesParser(URL, URLPrefix);
			titlesLinksMap = parser.parseStudies();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> list = new ArrayList<String>();
		if(titlesLinksMap != null){
			String[] arr = Arrays.copyOf(titlesLinksMap.keySet().toArray(),titlesLinksMap.size(), String[].class);
			for(int i = 0; i<arr.length;i++){
				list.add(titlesLinksMap.get(arr[i]));
			}
			
			
			
		}
		return list.toString();
	}
	
	@Override
	protected void onPostExecute(String str){
		text.setText(str);
	}

}
