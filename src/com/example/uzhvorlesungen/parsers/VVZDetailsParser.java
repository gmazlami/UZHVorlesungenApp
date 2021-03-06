package com.example.uzhvorlesungen.parsers;


import java.util.ArrayList;
import java.util.HashMap;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.example.uzhvorlesungen.model.BeginEndLocation;
import com.example.uzhvorlesungen.model.Lecture;

public class VVZDetailsParser {

	private String resourceURL = null;

	public VVZDetailsParser(String url, String prefix) {
		this.resourceURL = url;
	}

	public Lecture parse() throws ParserException {

		//check if there is a lehrveranstaltungen section
		
		Parser parser = new Parser();
		parser.setResource(resourceURL);
		boolean bottomExists = checkBottomSectionExists(parser);
		String docent = null;
		if(bottomExists){
			parser = new Parser();
			parser.setResource(resourceURL);
			docent = parseDocent(parser);
		}else{
			docent = "";
		}

		parser = new Parser();
		parser.setResource(resourceURL);
		boolean regular = parseRegularity(parser);
		
		parser = new Parser();
		parser.setResource(resourceURL);
		String[] pointsDescExam = parsePointsDescriptionExam(parser);

		parser = new Parser();
		parser.setResource(resourceURL);
		String title = parseTitle(parser);
		title = Utils.fixUmlauts(title);

		HashMap<String, BeginEndLocation> locationAndTime =  new HashMap<String, BeginEndLocation>();
		if(bottomExists){
			if(!resourceURL.contains("details")){
				parser = new Parser();
				parser.setResource(resourceURL);
				String link = parseDetailsLink(parser);
				Parser locationParser = new Parser(correctDetailsLink(resourceURL, link));
				locationAndTime = parseLocationAndTime(locationParser);
			}else{
				Parser locationParser = new Parser(correctAlreadyDetailsLink(resourceURL));
				locationAndTime = parseLocationAndTime(locationParser);
			}
		}else{
			locationAndTime = parseEmptyLocationAndTime(parser);
		}
		Lecture lecture =  new  Lecture(title, pointsDescExam[1], docent, pointsDescExam[2], pointsDescExam[0], locationAndTime);
		lecture.setRegular(regular);
		return lecture;
	
	}
	
	private boolean checkBottomSectionExists(Parser parser) throws ParserException{
		HasAttributeFilter filter = new HasAttributeFilter("class","ornate vvzDetails");
		NodeList nl = parser.parse(filter);
		if(nl.size() == 0){
			return false;
		}else{
			return true;
		}
	}

	private Object[] getDayBeginEndTimeLocation(Node trNode){
		if(trNode == null || trNode.getFirstChild() == null){
			Object[] array = {"Nach Ankündigung", new BeginEndLocation("", "", null)};
			return array;
		}
			
			

		String dayString = trNode.getFirstChild().toPlainTextString().trim().split(" ")[0].trim();
		String beginTime = trNode.getFirstChild().getNextSibling().toPlainTextString().trim().split("-")[0].trim();
		String endTime = trNode.getFirstChild().getNextSibling().toPlainTextString().trim().split("-")[1].trim();
		ArrayList<String> locations = new ArrayList<String>();
		Node locationNode = trNode.getFirstChild().getNextSibling().getNextSibling().getFirstChild();
		
		while(locationNode != null){
			String str = locationNode.toPlainTextString().trim();
			if(!str.equals(",")){
				str = str.trim();
				locations.add(str);
			}
			locationNode = locationNode.getNextSibling();
		}
		BeginEndLocation bel = new BeginEndLocation(beginTime, endTime, locations);
		Object[] arr = {dayString,bel}; 
		return arr;
	}
	
	public HashMap<String, BeginEndLocation> parseLocationAndTime(Parser parser) throws ParserException{
		TagNameFilter filter = new TagNameFilter("tr");
		NodeList nl = parser.parse(filter);
		Node root = nl.elementAt(1);
		getDayBeginEndTimeLocation(root);
		HashMap<String, BeginEndLocation> map = new HashMap<String, BeginEndLocation>();
		
		boolean all = false;
		int i = 1;
		
		while(!all){
			Node currentTermin = nl.elementAt(i);
			Object[] array = getDayBeginEndTimeLocation(currentTermin);
			if(map.containsKey((String)array[0])){
				all = true;
			}else{
				map.put((String)array[0],(BeginEndLocation)array[1]);
				i++;
			}
		}
		
		
		return map;
	}
	
	public HashMap<String, BeginEndLocation> parseEmptyLocationAndTime(
			Parser parser) throws ParserException {
		HashMap<String, BeginEndLocation> map = new HashMap<String, BeginEndLocation>();
		Object[] array = getDayBeginEndTimeLocation(null);
		map.put((String) array[0], (BeginEndLocation) array[1]);

		return map;
	}
	

	private boolean parseRegularity(Parser parser) throws ParserException{
		TagNameFilter f = new TagNameFilter("acronym");
		NodeList nl = parser.parse(f);
		Node reguralityNode = nl.elementAt(0).getParent().getNextSibling().getFirstChild();
		String regularity = Utils.fixUmlauts(reguralityNode.toPlainTextString());
		if(regularity.contains("unregelmässig")){
			return false;
		}else{
			return true;
		}
	}
	
	public String parseDetailsLink(Parser parser) throws ParserException {
		TagNameFilter f = new TagNameFilter("acronym");
		NodeList nl = parser.parse(f);
		Node detailsLink = nl.elementAt(0).getParent().getPreviousSibling()
				.getFirstChild();
		String link = null;

		if (detailsLink instanceof Tag) {
			Tag detailsLinkTag = (Tag) detailsLink;
			link = detailsLinkTag.getAttribute("href");
		}

		return link;
	}

	public String parseTitle(Parser parser) throws ParserException {
		TagNameFilter f = new TagNameFilter("acronym");
		NodeList nl = parser.parse(f);
		if(nl.size() == 0){
			parser = new Parser(resourceURL);
			TagNameFilter tagFilter = new TagNameFilter("tr");
			NodeList nodeList = parser.parse(tagFilter);
			if(nodeList.elementAt(15).toPlainTextString().contains("nein") || nodeList.elementAt(15).toPlainTextString().contains("ja")){
				return nodeList.elementAt(16).getFirstChild().getNextSibling().getFirstChild().toPlainTextString().trim();
			}else{
				return nodeList.elementAt(15).getFirstChild().getNextSibling().getFirstChild().toPlainTextString().trim();
			}
		}else{
			return Utils.fixUmlauts(nl.elementAt(0).getParent()
					.getPreviousSibling().getFirstChild().getFirstChild()
					.toPlainTextString());
		}
		
	}

	public String[] parsePointsDescriptionExam(Parser parser)
			throws ParserException {
		TagNameFilter f = new TagNameFilter("td");
		NodeList nl = parser.parse(f);
		String[] pointsDescExam = new String[3];
		pointsDescExam[0] = Utils.fixUmlauts(nl.elementAt(1)
				.toPlainTextString().trim()); // points
		pointsDescExam[1] = Utils.fixUmlauts(nl.elementAt(3)
				.toPlainTextString().trim()); // description
		pointsDescExam[2] = Utils.fixUmlauts(nl.elementAt(9)
				.toPlainTextString().trim()); // exam info
		if(Float.parseFloat(pointsDescExam[0]) > 45){
			pointsDescExam[0] = " ";
		}
		return pointsDescExam;
	}

	public String parseDocent(Parser parser) throws ParserException {
		HasAttributeFilter f = new HasAttributeFilter("style",
				"white-space: nowrap");
		NodeList nl = parser.parse(f);
		Node node = nl.elementAt(0);
		if(node != null){
			if(node.getParent() != null && node.getParent().getParent() != null && node.getParent().getParent().getNextSibling() != null){
				node = node.getParent().getParent().getNextSibling();
				return Utils.fixUmlauts(node.toPlainTextString().trim());
			}
		}
			StringBuilder sb = new StringBuilder();
			parser = new Parser(resourceURL);
			TagNameFilter filter = new TagNameFilter("tr");
			NodeList nodeList = parser.parse(filter);
			Node firstTr = nodeList.elementAt(0);
			Node docentNode = firstTr.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling();
			docentNode = docentNode.getFirstChild().getNextSibling().getFirstChild();

			
			while(docentNode != null){
				sb.append(docentNode.toPlainTextString().trim());
				docentNode = docentNode.getNextSibling();
			}
			
			return Utils.fixUmlauts(sb.toString().trim());
		
		
	}

	public String[] parseDayTime(Parser parser) throws ParserException {
		HasAttributeFilter f = new HasAttributeFilter("style",
				"white-space: nowrap");
		NodeList nl = parser.parse(f);

		Node node = null;
		String str = null;
		node = nl.elementAt(0);

		if (node.getNextSibling() != null) {
			node = nl.elementAt(2);
		}

		// TODO: implement �bungen crawling
		str = node.toPlainTextString().trim();
		String[] dayTime = str.split(" ");
		String[] time = dayTime[1].split("-");
		String[] dayBeginEndTime = { dayTime[0], time[0], time[1] };
		return dayBeginEndTime;
	}

	private String correctDetailsLink(String prefix, String postfix) {
		String newPostfix = postfix.replace(".details.html", ".termine.html");
		return prefix.replace(".modveranst.html", "") + "/" + newPostfix;
	}
	
	private String correctAlreadyDetailsLink(String link){
		return link.replace(".details.html", ".termine.html");
	}
}
