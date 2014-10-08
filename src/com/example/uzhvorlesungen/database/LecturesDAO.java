package com.example.uzhvorlesungen.database;

import java.util.ArrayList;
import java.util.List;

import com.example.uzhvorlesungen.model.Lecture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	
	public List<String> getAllLectureTitles(){
		ArrayList<String> list = new ArrayList<String>();
		Cursor c = db.query(UZHSQLiteOpenHelper.TABLE_LECTURE, new String[]{UZHSQLiteOpenHelper.LECTURE_TITLE}, null, null, null, null, null);
		String lectureTitle;
		do{
			lectureTitle = c.getString(1);
			list.add(lectureTitle);
		}while(c.moveToNext());
		
		return list;
	}
	
	public void insertLecture(Lecture lecture){
		ContentValues values = new ContentValues();
		values.put(UZHSQLiteOpenHelper.LECTURE_TITLE, lecture.getTitle());
		values.put(UZHSQLiteOpenHelper.LECTURE_DESC, lecture.getDescription());
		values.put(UZHSQLiteOpenHelper.LECTURE_EXAM, lecture.getExam());
		values.put(UZHSQLiteOpenHelper.LECTURE_POINTS, lecture.getPoints());
		values.put(UZHSQLiteOpenHelper.LECTURE_DOCENT, lecture.getDocent());
		db.insert(UZHSQLiteOpenHelper.TABLE_LECTURE, null, values);
	}
	
	

}
