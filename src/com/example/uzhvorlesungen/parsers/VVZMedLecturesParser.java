package com.example.uzhvorlesungen.parsers;

import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
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
		HashMap<String, String> map = new HashMap<String, String>();
		Parser parser = new Parser();
		parser.setResource(resourceURL);
		
		TagNameFilter filter = new TagNameFilter("tr");
		NodeList nl = parser.parse(filter);
		Node firstTrNode = nl.elementAt(1);
		Node current = firstTrNode;
		
		while(current.getNextSibling() != null){
			Node studyNameNode = current.getFirstChild().getNextSibling().getFirstChild();
			map.put(studyNameNode.toPlainTextString(), getLinkForNode(studyNameNode));
			current = current.getNextSibling();
		}


		return map;
	}
	
	private String getLinkForNode(Node studyNode){
		if(studyNode instanceof Tag){
			Tag aTag = (Tag) studyNode;
			return (prefix +"/"+ aTag.getAttribute("href"));
		}
		return null;
	}
	
}
