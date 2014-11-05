package com.example.uzhvorlesungen.parsers;

public class Utils {

	
	public static String fixUmlauts(String input) {
		String newString = null;
		newString = input.replace("&Auml;", "Ä");
		newString = newString.replace("&auml;", "ä");
		newString = newString.replace("&Uuml;", "Ü");
		newString = newString.replace("&uuml;", "ü");
		newString = newString.replace("&Ouml;", "Ö");
		newString = newString.replace("&ouml;", "ö");
		newString = newString.replace("&nbsp;", " ");
		newString = newString.replace("&amp;", "&");
		newString = newString.replace("&quot;", "\"");
		return newString;
	}
}
