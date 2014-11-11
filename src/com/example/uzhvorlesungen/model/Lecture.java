package com.example.uzhvorlesungen.model;

import java.util.HashMap;

public class Lecture {

	private String title;
	private String description;
	private String docent;
	private String exam;
	private String points;
	private HashMap<String, BeginEndLocation> dayBeginEndTimeLocation;
	private boolean regular = true;
	
	
	public Lecture(String title, String description,String docent,String exam, String points, HashMap<String, BeginEndLocation> dayBeginEndTimeMap) {
		super();
		this.title = title;
		this.description = description;
		this.docent = docent;
		this.exam = exam;
		this.points = points;
		this.dayBeginEndTimeLocation = dayBeginEndTimeMap;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getRegularity(){
		return this.regular;
	}
	
	public void setRegular(boolean reg){
		this.regular = reg;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap<String, BeginEndLocation> getDayBeginEndTime() {
		return dayBeginEndTimeLocation;
	}

	public void setDayBeginEndTime(
			HashMap<String, BeginEndLocation> dayBeginEndTime) {
		this.dayBeginEndTimeLocation = dayBeginEndTime;
	}

	public String getDocent() {
		return docent;
	}

	public void setDocent(String docent) {
		this.docent = docent;
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
		return title;
		
	}
		


	

	
}
