package com.example.uzhvorlesungen.parsers;

import java.util.HashMap;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class VVZMMParser {

	String URLPrefix = null;
	String URL = null;

	HashMap<String, String> studiesMap = new HashMap<String, String>();
	HashMap<String, List<String>> facultyMap = new HashMap<String, List<String>>();

	public VVZMMParser(String url, String prefix)
			throws ParserException {
		URL = url;
		URLPrefix = prefix;
	}


	

	
	
	public  HashMap<String, String> parseMajorMinor()
			throws ParserException {
		HasAttributeFilter filter = new HasAttributeFilter("class",
				"linked noimage");
		Parser parser = new Parser();
		parser.setResource(URL);

		HashMap<String, HashMap<String, String>> majorMinorMap = new HashMap<String, HashMap<String, String>>();
		NodeList nl = parser.parse(filter);
		HashMap<String, String> linksMap = new HashMap<String, String>();
		for (int i = 0; i < nl.size(); i++) {

			Node rootMajorMinor = nl.elementAt(i);
			Node majorMinor = rootMajorMinor.getFirstChild();
			Node firstStudy = rootMajorMinor.getNextSibling().getFirstChild();

			String majorMinorString = majorMinor.toPlainTextString().trim();

			Node current = firstStudy;

			while (current != null) {

				String currentString = current.toPlainTextString().trim();
				if (!currentString.contains("Dieses Studienprogramm")
						&& !currentString.contains("vom neuen")) {
					String str = current.toHtml().trim();
					String[] first = str.split("A href=\"");
					String[] scnd = first[1].split("\" class=\"internal\">");
					String link = scnd[0];
					String postFix ="";
					
					if(majorMinorString.contains("Hauptfach")){
						postFix = majorMinorString.replace("Hauptfach", "HF");
					}else if(majorMinorString.contains("Nebenfach")){
						postFix = majorMinorString.replace("Nebenfach", "NF");
					}
					if (link.contains("../../")) {
						link = link.replace("../../", "");
						linksMap.put(currentString + " " + postFix, URLPrefix + link);
					}
				}
				current = current.getNextSibling();
			}
		}
		return linksMap;
	}
}
