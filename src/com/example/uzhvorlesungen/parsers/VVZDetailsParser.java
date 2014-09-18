package com.example.uzhvorlesungen.parsers;


import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class VVZDetailsParser {

	private String resourceURL = null;

	public VVZDetailsParser(String url, String prefix) {
		this.resourceURL = url;
	}

	public Lecture parse() throws ParserException {

		Parser parser = new Parser();
		parser.setResource(resourceURL);
		String docent = parseDocent(parser);

		parser = new Parser();
		parser.setResource(resourceURL);
		String[] pointsDescExam = parsePointsDescriptionExam(parser);

		parser = new Parser();
		parser.setResource(resourceURL);
		String title = parseTitle(parser);

		parser = new Parser();
		parser.setResource(resourceURL);
		String link = parseDetailsLink(parser);

		Parser locationParser = new Parser(
				correctDetailsLink(resourceURL, link));
		String[] locationAndTime = parseLocation(locationParser);

		return new Lecture(title, pointsDescExam[1], locationAndTime[1],
				locationAndTime[2], locationAndTime[0], docent,
				locationAndTime[3], pointsDescExam[2], pointsDescExam[0]);
	}

	public String[] parseLocation(Parser parser) throws ParserException {
		TagNameFilter f = new TagNameFilter("td");
		NodeList nl = parser.parse(f);
		String day = nl.elementAt(0).toPlainTextString().trim().split(" ")[0];
		String beginTime = nl.elementAt(1).toPlainTextString().trim()
				.split("-")[0].trim();
		String endTime = nl.elementAt(1).toPlainTextString().trim().split("-")[1]
				.trim();
		String room = nl.elementAt(2).toPlainTextString();
		String[] arr = { day, beginTime, endTime, room };
		return arr;
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
		return Utils.fixUmlauts(nl.elementAt(0).getParent()
				.getPreviousSibling().getFirstChild().getFirstChild()
				.toPlainTextString());
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
		return pointsDescExam;
	}

	public String parseDocent(Parser parser) throws ParserException {
		HasAttributeFilter f = new HasAttributeFilter("style",
				"white-space: nowrap");
		NodeList nl = parser.parse(f);
		Node node = nl.elementAt(0).getParent().getParent().getNextSibling();
		return Utils.fixUmlauts(node.toPlainTextString().trim());
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

		// TODO: implement Übungen crawling
		str = node.toPlainTextString().trim();
		System.out.println(str);
		String[] dayTime = str.split(" ");
		String[] time = dayTime[1].split("-");
		String[] dayBeginEndTime = { dayTime[0], time[0], time[1] };
		return dayBeginEndTime;
	}

	public String[] parseTimeAndDay(Parser parser) {

		return null;
	}

	private String correctDetailsLink(String prefix, String postfix) {
		String newPostfix = postfix.replace(".details.html", ".termine.html");
		return prefix.replace(".modveranst.html", "") + "/" + newPostfix;
	}
}
