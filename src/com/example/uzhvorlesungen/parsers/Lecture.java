package com.example.uzhvorlesungen.parsers;

public class Lecture {

	private String title;
	private String description;
	private String beginTime;
	private String endTime;
	private String day;
	private String docent;
	private String location;
	private String exam;
	private String points;
	
	
	public Lecture(String title, String description, String beginTime,
			String endTime, String day, String docent, String location,
			String exam, String points) {
		super();
		this.title = title;
		this.description = description;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.day = day;
		this.docent = docent;
		this.location = location;
		this.exam = exam;
		this.points = points;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDocent() {
		return docent;
	}

	public void setDocent(String docent) {
		this.docent = docent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getExam() {
		return exam;
	}

	public void setExam(String exam) {
		this.exam = exam;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}
	
	
	public String toString(){
		return "Lecture: {"+title+","+location+","+day+","+beginTime+","+endTime+","+docent+","+points+","+"}" ;
		
	}
		

	

	
}
