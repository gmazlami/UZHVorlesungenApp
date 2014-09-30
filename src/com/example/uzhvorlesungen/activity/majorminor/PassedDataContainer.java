package com.example.uzhvorlesungen.activity.majorminor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PassedDataContainer {

	
	public static ArrayList<String> majors;
	
	public static HashMap<String, String> studiesLinks;
	
	public static HashMap<String, String> getStudiesLinksForMajor(String majors){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		String mm = "";
		if(majors.contains("Hauptfach")){
			mm = "HF";
		}else if(majors.contains("Nebenfach")){
			mm = "NF";
		}
		Iterator<String> iter = studiesLinks.keySet().iterator();
		while (iter.hasNext()) {
			String string = (String) iter.next();
			if (string.contains(mm)) {
				resultMap.put(string, studiesLinks.get(string));
			}
		}
		return resultMap;
	}
	
	public static String getLinkForGroupChild(int group, String study){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		String major = majors.get(group);
		String postFix = "";
		if(major.contains("Hauptfach")){
			postFix = major.replace("Hauptfach", "HF");
		}else if(major.contains("Nebenfach")){
			postFix = major.replace("Nebenfach", "NF");
		}
		study =  study + " " + postFix;
		
		
		return studiesLinks.get(study);
	}
}
