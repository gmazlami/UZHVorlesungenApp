package com.example.uzhvorlesungen.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.uzhvorlesungen.model.BeginEndLocation;
import com.example.uzhvorlesungen.model.Lecture;

public class LecturesDAO {
	
	private UZHSQLiteOpenHelper helper;
	private SQLiteDatabase db;
	
	public LecturesDAO(Context context){
		helper = new UZHSQLiteOpenHelper(context);
	}
	
	public void openDataBase(){
		db = helper.getWritableDatabase();
	}
	
	public void closeDataBase(){
		db.close();
	}
	
	public List<String> getAllTermins(){
		ArrayList<String> list = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT * FROM termin;", null);
		while(c.moveToNext()){
			list.add(c.getString(0));
		}
		System.out.println(list);
		return list;
	}
	
	public List<String> getAllLectureTitles(){
		ArrayList<String> list = new ArrayList<String>();
		Cursor c = db.rawQuery("SELECT title FROM lecture;", null);
		while(c.moveToNext()){
			list.add(c.getString(0));
		}
		return list;
	}
	
	public void deleteLecture(String title){
		int id = getLectureId(title);
		String idString = String.valueOf(id);
		db.delete(UZHSQLiteOpenHelper.TABLE_LECTURE, UZHSQLiteOpenHelper.LECTURE_ID +"="+idString, null);
		db.delete(UZHSQLiteOpenHelper.TABLE_TERMIN, UZHSQLiteOpenHelper.TERMIN_LEC_ID+"="+idString, null);
	}
	
	@SuppressLint("UseSparseArrays") public List<Lecture> getAllLectures(){
		Cursor cursor = db.rawQuery("SELECT lecture._id,lecture.title,lecture.description, lecture.docent, lecture.exam, lecture.points, termin.day, termin.begintime, termin.endtime, termin.locations FROM lecture,termin WHERE lecture._id = termin.lecture_id; ",null);
		HashMap<Integer, Lecture> lectureMap = new HashMap<Integer, Lecture>();
		int lastId = -1;
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			if(lastId == id){
				//no new lecture, but last lecture with different termin
				Lecture lecture = lectureMap.get(id);
				HashMap<String, BeginEndLocation> belMap = lecture.getDayBeginEndTime();
				
				String terminDay = cursor.getString(6);
				String terminBegin = cursor.getString(7);
				String terminEnd = cursor.getString(8);
				String terminLocations = cursor.getString(9);
				String[] locations = terminLocations.split("@");
				ArrayList<String> locationsList = new ArrayList<String>();
				for (int i = 0; i < locations.length; i++) {
					locationsList.add(locations[i]);
				}
				BeginEndLocation bel = new BeginEndLocation(terminBegin, terminEnd, locationsList);
				belMap.put(terminDay, bel);
				lecture.setDayBeginEndTime(belMap);
				lectureMap.put(id, lecture);
			}else{
				//new lecture
				String lectureTitle = cursor.getString(1);
				String lectureDesc = cursor.getString(2);
				String lectureDocent = cursor.getString(3);
				String lectureExam = cursor.getString(4);
				String lecturePoints = cursor.getString(5);
				String terminDay = cursor.getString(6);
				String terminBegin = cursor.getString(7);
				String terminEnd = cursor.getString(8);
				String terminLocations = cursor.getString(9);
				
				HashMap<String, BeginEndLocation> belMap = new HashMap<String, BeginEndLocation>();
				String[] locations = terminLocations.split("@");
				ArrayList<String> locationsList = new ArrayList<String>();
				for (int i = 0; i < locations.length; i++) {
					locationsList.add(locations[i]);
				}
				BeginEndLocation bel = new BeginEndLocation(terminBegin, terminEnd, locationsList);
				belMap.put(terminDay, bel);
				
				lectureMap.put(id, new Lecture(lectureTitle, lectureDesc, lectureDocent, lectureExam, lecturePoints, belMap));
				lastId = id;
			}

		}
		
		Iterator<Lecture> iter = lectureMap.values().iterator();
		ArrayList<Lecture> lectureList = new ArrayList<Lecture>();
		while(iter.hasNext()){
			Lecture lec =  iter.next();
			lectureList.add(lec);
		}
		return lectureList;
		
	}
	
	public int getLectureId(String lectureTitle){
		Cursor c = db.rawQuery("SELECT _id FROM lecture WHERE title = ?;", new String[]{lectureTitle});
		c.moveToFirst();
		return c.getInt(0);
	}
	public void insertLecture(Lecture lecture){
		ContentValues values = new ContentValues();
		values.put(UZHSQLiteOpenHelper.LECTURE_TITLE, lecture.getTitle());
		values.put(UZHSQLiteOpenHelper.LECTURE_DESC, lecture.getDescription());
		values.put(UZHSQLiteOpenHelper.LECTURE_EXAM, lecture.getExam());
		values.put(UZHSQLiteOpenHelper.LECTURE_POINTS, lecture.getPoints());
		values.put(UZHSQLiteOpenHelper.LECTURE_DOCENT, lecture.getDocent());
		db.insert(UZHSQLiteOpenHelper.TABLE_LECTURE, null, values);
		
		System.out.println(values.toString());
		
		int id = getLectureId(lecture.getTitle());
		
		//insert termin for the current lecture into separate termin table
		Map<String, BeginEndLocation> map = lecture.getDayBeginEndTime();
		Iterator<String> iter = map.keySet().iterator();
		
		while(iter.hasNext()){
			String dayKey = iter.next();
			BeginEndLocation bel = map.get(dayKey);
			String locations = constructLocations(bel.locations);
			ContentValues terminValues = new ContentValues();
			terminValues.put(UZHSQLiteOpenHelper.TERMIN_BEGIN, bel.begin);
			terminValues.put(UZHSQLiteOpenHelper.TERMIN_END, bel.end);
			terminValues.put(UZHSQLiteOpenHelper.TERMIN_DAY, dayKey);
			terminValues.put(UZHSQLiteOpenHelper.TERMIN_LOCATIONS, locations);
			terminValues.put(UZHSQLiteOpenHelper.TERMIN_LEC_ID, id);
			db.insert(UZHSQLiteOpenHelper.TABLE_TERMIN, null, terminValues);
		}

	}
	
	
	private String constructLocations(List<String> list){
		StringBuilder sb = new StringBuilder();
		for(String s : list){
			sb.append(s);
			sb.append("@");
		}
		return sb.toString();
	}
	

}
