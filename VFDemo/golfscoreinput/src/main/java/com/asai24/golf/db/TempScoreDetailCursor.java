package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.TempScoreDetail;

import java.util.HashMap;

public class TempScoreDetailCursor extends SQLiteCursor {
	public TempScoreDetailCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(TempScoreDetail._ID, TempScoreDetail._ID);
		PROJECTION_MAP.put(TempScoreDetail.SCORE_ID, TempScoreDetail.SCORE_ID);
		// PROJECTION_MAP.put(TempScoreDetail.SHOT_TYPE, TempScoreDetail.SHOT_TYPE);
		PROJECTION_MAP.put(TempScoreDetail.SHOT_NUMBER, TempScoreDetail.SHOT_NUMBER);
		PROJECTION_MAP.put(TempScoreDetail.CLUB, TempScoreDetail.CLUB);
		PROJECTION_MAP.put(TempScoreDetail.SHOT_RESULT, TempScoreDetail.SHOT_RESULT);
		PROJECTION_MAP.put(TempScoreDetail.GPS_LATITUDE, TempScoreDetail.GPS_LATITUDE);
		PROJECTION_MAP
				.put(TempScoreDetail.GPS_LONGITUDE, TempScoreDetail.GPS_LONGITUDE);
		PROJECTION_MAP.put(TempScoreDetail.CREATED_DATE, TempScoreDetail.CREATED_DATE);
		PROJECTION_MAP
				.put(TempScoreDetail.MODIFIED_DATE, TempScoreDetail.MODIFIED_DATE);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(TempScoreDetail.TABLE_NAME);
		qb.setCursorFactory(new TempScoreDetailCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new TempScoreDetailCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(TempScoreDetail._ID));
	}

	public long getScoreId() {
		return getLong(getColumnIndexOrThrow(TempScoreDetail.SCORE_ID));
	}

	// public int getShotType() {
	// return getInt(getColumnIndexOrThrow(TempScoreDetail.SHOT_TYPE));
	// }
	public int getShotNumber() {
		return getInt(getColumnIndexOrThrow(TempScoreDetail.SHOT_NUMBER));
	}

	public String getClub() {
		String club = getString(getColumnIndexOrThrow(TempScoreDetail.CLUB));
		if (club == null) {
			club = "";
		}
		return club;
	}

	public String getShotResult() {
		String shotResult = getString(getColumnIndexOrThrow(TempScoreDetail.SHOT_RESULT));
		if (shotResult == null) {
			shotResult = "";
		}
		return shotResult;
	}

	public double getLatitude() {
		return getDouble(getColumnIndexOrThrow(TempScoreDetail.GPS_LATITUDE));
	}

	public double getLongitude() {
		return getDouble(getColumnIndexOrThrow(TempScoreDetail.GPS_LONGITUDE));
	}

	public long getCreated() {
		return getLong(getColumnIndexOrThrow(TempScoreDetail.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(TempScoreDetail.MODIFIED_DATE));
	}
}
