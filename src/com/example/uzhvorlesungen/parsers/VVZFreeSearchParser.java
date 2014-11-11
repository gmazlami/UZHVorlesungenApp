package com.example.uzhvorlesungen.parsers;

import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class VVZFreeSearchParser {

	private String baseURL = "http://www.vorlesungen.uzh.ch/";
	private String urlParamString = "/suche.html?searchBy=e-titel&searchInFakultaet=*&searchString=";
	private String postFix = "&submit=Suchen";
	private String semester;
	
	private String completeResourceUrl = null;
	
	public VVZFreeSearchParser(String semester, String query){
		this.semester = semester;
		String correctedQuery = query.replace(" ", "+");
		completeResourceUrl = baseURL + semester + urlParamString + correctedQuery + postFix;
	}
	
	
	public Map<String,String> getLectureLinks() throws ParserException{
		Map<String,String> lectureTitlesLinks = new HashMap<String,String>();
		
		/*
		 * Parse the links /suche/...
		 */
		Parser parser = new Parser(completeResourceUrl);
		TagNameFilter tagNameFilter = new TagNameFilter("tr");
		NodeList nodeList = parser.parse(tagNameFilter);
		for(int i = 6; i < nodeList.size(); i++){
			Node current = nodeList.elementAt(i);
			Tag aHref = (Tag) current.getFirstChild().getNextSibling();
			String tdString = aHref.toHtml().trim();
			String[] splitArray = tdString.split("\"");
			String link = baseURL + semester + "/" + splitArray[1];
			String title = aHref.toPlainTextString().trim();
			lectureTitlesLinks.put(title, link);
		}
		
		
		Map<String,String> resultMap = new HashMap<String, String>();
		
		/*
		 * On the /suche/... page, find the link to /sm-2934928734..
		 */
		
		for (Map.Entry<String, String> entry : lectureTitlesLinks.entrySet()){
			String link = entry.getValue();
			parser = new Parser(link);
			NodeList nl = parser.parse(tagNameFilter);
			Node node = nl.elementAt(15);
			String trString = node.toHtml().trim();
			if(trString.contains("Bestandteil")){
				String[] splitArray = trString.split("\"");
				link = baseURL + semester + "/suche/" + splitArray[1];
				resultMap.put(Utils.fixUmlauts(entry.getKey()), link);
			}
		}
		
		return resultMap;
	}
	
	
}
