package com.example.uzhvorlesungen.model;

import java.util.ArrayList;

public class BeginEndLocation {

	public BeginEndLocation(String begin, String end,ArrayList<String> locations) {
		super();
		this.begin = begin;
		this.end = end;
		this.locations = locations;
	}

	public ArrayList<String> locations;
	public String begin;
	public String end;
	
	
	@Override
	public String toString() {
		return "BeginEndLocation [locations=" + locations + ", begin=" + begin
				+ ", end=" + end + "]";
	}
	
	
}
