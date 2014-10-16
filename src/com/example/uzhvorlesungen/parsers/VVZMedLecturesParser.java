package com.example.uzhvorlesungen.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class VVZMedLecturesParser {

	private String resourceURL = null;
	private String prefix = null;

	public VVZMedLecturesParser(String url) {
		this.resourceURL = url;
		this.prefix = resourceURL.split(".veranstaltungen.html")[0];
	}

	
	public Map<String, String> parseSites() throws ParserException{
		HashMap<String, String> map = new HashMap<String,String>();
		List<String> pages = parsePages();
		for(String page : pages){
			Parser parser = new Parser();
			parser.setResource(page);
			
			TagNameFilter filter = new TagNameFilter("tr");
			NodeList nl = parser.parse(filter);
			Node firstTrNode = nl.elementAt(1);
			Node current = firstTrNode;
			
			while(current != null){
				if(current.getNextSibling() != null){
					Node studyNameNode = current.getFirstChild().getNextSibling().getFirstChild();
					map.put(Utils.fixUmlauts(studyNameNode.toPlainTextString()), getLinkForNode(studyNameNode));
				}
				current = current.getNextSibling();
			}
		}
		

		return map;
	}
	
	private ArrayList<String> parsePages() throws ParserException{
		ArrayList<String> pageList = new ArrayList<String>();
		Parser parser = new Parser();
		pageList.add(resourceURL);
		parser.setResource(resourceURL);
		HasAttributeFilter filter = new HasAttributeFilter("class", "pagination-current");
		NodeList nl = parser.parse(filter);
		Node currentPageNode = nl.elementAt(0).getNextSibling();
		int page = 2;
		while(currentPageNode != null){
			pageList.add(createPageLink(page));
			page++;
			currentPageNode = currentPageNode.getNextSibling();
		}
		
		System.out.println(pageList);
		return pageList;
	}
	
	private String createPageLink(int page){
		return resourceURL + "?sortBy=&sortOrder=ascending&page=" + page;
	}
	
	private String getLinkForNode(Node studyNode){
		if(studyNode instanceof Tag){
			Tag aTag = (Tag) studyNode;
			return (prefix +"/"+ aTag.getAttribute("href"));
		}
		return null;
	}
	
}
