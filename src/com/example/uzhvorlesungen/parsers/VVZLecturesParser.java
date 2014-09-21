package com.example.uzhvorlesungen.parsers;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class VVZLecturesParser {

	private String resourceURL = null;
	private String URLprefix = null;
	private HashMap<String, String> linkMap = new HashMap<String, String>();

	public VVZLecturesParser(String url, String prefix) {
		this.resourceURL = url;
		this.URLprefix = prefix;
	}

	public Map<String, String> parse() throws ParserException{
		ArrayList<String> linkList = parseSites();
		for(int i=0; i < linkList.size(); i++){
			parseLectures(linkList.get(i));
		}
		return linkMap;
	}
	
	private ArrayList<String> parseSites() throws ParserException{
		ArrayList<String> links = new ArrayList<String>();
		Parser parser = new Parser();
		parser.setResource(resourceURL);
		HasAttributeFilter filter = new HasAttributeFilter("class", "pages");
		NodeList nl = parser.parse(filter);
		
		Node pageNode = nl.elementAt(0);
		if(pageNode != null){
			int numOfPages = pageNode.getChildren().size() - 2;
			
			for(int i = 1 ; i < numOfPages ; i++){
				Node page = pageNode.getChildren().elementAt(i);
				
				if(page instanceof Tag){
					String link = ((Tag) page).getAttribute("href");
					link = link.replace("&amp;", "&");
					link = link.split("\\?")[1];
					link = resourceURL + "?"+ link;
					
					links.add(link);
				}
			}
			
			return links;
		}else{
			ArrayList<String> list = new ArrayList<String>();
			list.add(resourceURL);
			return list;
		}
	}
	
	private Map<String, String> parseLectures(String baselink) throws ParserException {
		Parser parser = new Parser();
		parser.setResource(baselink);
		HasAttributeFilter f = new HasAttributeFilter("class", "internal");
		NodeList nl = parser.parse(f);
		
		String str = null;
		String link = null;
		Node node = null;

		for (int i = 0; i < nl.size(); i++) {
			node = nl.elementAt(i);
			str = nl.elementAt(i).toPlainTextString().trim();

			if (node instanceof Tag) {
				link = ((Tag) node).getAttribute("href");
				link = link.substring(6);
				linkMap.put(Utils.fixUmlauts(str), URLprefix + link);
			}
		}

		return linkMap;
	}
}

