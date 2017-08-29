package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.ScoreDetail;

import java.util.HashMap;

public class ScoreDetailCursor extends SQLiteCursor {
	public ScoreDetailCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(ScoreDetail._ID, ScoreDetail._ID);
		PROJECTION_MAP.put(ScoreDetail.SCORE_ID, ScoreDetail.SCORE_ID);
		// PROJECTION_MAP.put(ScoreDetail.SHOT_TYPE, ScoreDetail.SHOT_TYPE);
		PROJECTION_MAP.put(ScoreDetail.SHOT_NUMBER, ScoreDetail.SHOT_NUMBER);
		PROJECTION_MAP.put(ScoreDetail.CLUB, ScoreDetail.CLUB);
		PROJECTION_MAP.put(ScoreDetail.SHOT_RESULT, ScoreDetail.SHOT_RESULT);
		PROJECTION_MAP.put(ScoreDetail.GPS_LATITUDE, ScoreDetail.GPS_LATITUDE);
		PROJECTION_MAP
				.put(ScoreDetail.GPS_LONGITUDE, ScoreDetail.GPS_LONGITUDE);
		PROJECTION_MAP.put(ScoreDetail.CREATED_DATE, ScoreDetail.CREATED_DATE);
		PROJECTION_MAP
				.put(ScoreDetail.MODIFIED_DATE, ScoreDetail.MODIFIED_DATE);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(ScoreDetail.TABLE_NAME);
		qb.setCursorFactory(new ScoreDetailCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new ScoreDetailCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(ScoreDetail._ID));
	}

	public long getScoreId() {
		return getLong(getColumnIndexOrThrow(ScoreDetail.SCORE_ID));
	}

	// public int getShotType() {
	// return getInt(getColumnIndexOrThrow(ScoreDetail.SHOT_TYPE));
	// }
	public int getShotNumber() {
		return getInt(getColumnIndexOrThrow(ScoreDetail.SHOT_NUMBER));
	}

	public String getClub() {
		String club = getString(getColumnIndexOrThrow(ScoreDetail.CLUB));
		if (club == null) {
			club = "";
		}
		return club;
	}

	public String getShotResult() {
		String shotResult = getString(getColumnIndexOrThrow(ScoreDetail.SHOT_RESULT));
		if (shotResult == null) {
			shotResult = "";
		}
		return shotResult;
	}

	public double getLatitude() {
		return getDouble(getColumnIndexOrThrow(ScoreDetail.GPS_LATITUDE));
	}

	public double getLongitude() {
		return getDouble(getColumnIndexOrThrow(ScoreDetail.GPS_LONGITUDE));
	}

	public long getCreated() {
		return getLong(getColumnIndexOrThrow(ScoreDetail.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(ScoreDetail.MODIFIED_DATE));
	}
}
