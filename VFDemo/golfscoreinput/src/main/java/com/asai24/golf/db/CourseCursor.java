package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Course;
import com.asai24.golf.db.Golf.Course.ExtType;

import java.util.HashMap;

public class CourseCursor extends SQLiteCursor {
	private CourseCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
                         String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Course._ID, Course._ID);
		PROJECTION_MAP.put(Course.COURSE_NAME, Course.COURSE_NAME);
		PROJECTION_MAP.put(Course.CLUB_ID, Course.CLUB_ID);
		PROJECTION_MAP.put(Course.CLUB_NAME, Course.CLUB_NAME);
		PROJECTION_MAP.put(Course.COURSE_OOB_ID, Course.COURSE_OOB_ID);
		PROJECTION_MAP.put(Course.COURSE_YOURGOLF_ID, Course.COURSE_YOURGOLF_ID);
		PROJECTION_MAP.put(Course.DEL_FLAG, Course.DEL_FLAG);//NAMLH add del flag
		PROJECTION_MAP.put(Course.CREATED_DATE, Course.CREATED_DATE);
		PROJECTION_MAP.put(Course.MODIFIED_DATE, Course.MODIFIED_DATE);
		
	}
	
	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Course.TABLE_NAME);
		qb.setCursorFactory(new CourseCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new CourseCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(Course._ID));
	}
	public long getClubId() {
		return getLong(getColumnIndexOrThrow(Course.CLUB_ID));
	}
	public String getClubName() {
		String clubName = getString(getColumnIndexOrThrow(Course.CLUB_NAME));
		if (clubName == null) {
			clubName = "";
		}
		return clubName;
	}

	public String getCourseName() {
		String courseName = getString(getColumnIndexOrThrow(Course.COURSE_NAME));
		if (courseName == null) {
			courseName = "";
		}
		return courseName;
	}

	public String getClubCourseName() {
		String clubName = getClubName();
		String courseName = getCourseName();
		// Log.d("debug","club:"+clubName +"/course:"+courseName);
		if (courseName == null || courseName.equals("")) {
			return clubName;
		} else {
			return clubName + " - " + courseName;
		}
	}

	public String getOobId() {
		return getString(getColumnIndexOrThrow(Course.COURSE_OOB_ID));
	}

	public String getYourGolfId() {
		return getString(getColumnIndexOrThrow(Course.COURSE_YOURGOLF_ID));
	}

	public String getExtId() {
		if (getOobId() != null) {
			return getOobId();
		} else if (getYourGolfId() != null) {
			return getYourGolfId();
		} else {
			return null;
		}
	}

	public String getExtType() {
		if (getOobId() != null) {
			return ExtType.OobGolf.toString();
		} else if (getYourGolfId() != null) {
			return ExtType.YourGolf.toString();
		} else {
			return null;
		}
	}

	public long getCreated() {
		return getLong(getColumnIndexOrThrow(Course.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Course.MODIFIED_DATE));
	}
	public long getDeleteFlag() {
		return getLong(getColumnIndexOrThrow(Course.DEL_FLAG));
	}
	
}
