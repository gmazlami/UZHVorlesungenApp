package com.example.uzhvorlesungen.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UZHSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME ="ch.uzh.gmazlami.lectures.db";

	public static final String TABLE_LECTURE = "lecture";
	public static final String LECTURE_ID = "id";
	public static final String LECTURE_TITLE = "title";
	public static final String LECTURE_DESC = "description";
	public static final String LECTURE_DOCENT = "docent";
	public static final String LECTURE_EXAM = "exam";
	public static final String LECTURE_POINTS = "points";
	
	public static final String TABLE_TERMIN = "termin";
	public static final String TERMIN_ID = "id";
	public static final String TERMIN_DAY = "day";
	public static final String TERMIN_BEGIN = "begintime";
	public static final String TERMIN_END = "endtime";
	public static final String TERMIN_LOCATIONS = "locations";
	public static final String TERMIN_LEC_ID = "lecture_id";
	
	private static final String DATABASE_CREATE_LECTURE_STATEMENT = "CREATE TABLE lecture(" +
																	"id INTEGER PRIMARY KEY," +
																	"title TEXT NOT NULL,"+
																	"description TEXT,"+
																	"docent TEXT,"+
																	"exam TEXT,"+
																	"points TEXT"+
																	");";
	
	private static final String DATABASE_CREATE_TERMIN_STATEMENT ="CREATE TABLE termin(" +
																	"id INTEGER PRIMARY KEY,"+
																	"day TEXT NOT NULL,"+
																	"begintime TEXT NOT NULL,"+
																	"endtime TEXT NOT NULL,"+
																	"locations TEXT NOT NULL,"+
																	"lecture_id INTEGER,"+
																	"FOREIGN KEY(lecture_id) REFERENCES lecture(id)"+
																	");";
	
	public UZHSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_LECTURE_STATEMENT);
		db.execSQL(DATABASE_CREATE_TERMIN_STATEMENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO: implement correct onupgrade method
	}
	
	

}
