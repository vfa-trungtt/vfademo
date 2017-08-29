package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Club;

import java.util.HashMap;

public class ClubCursor extends SQLiteCursor {
	private ClubCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
                       String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Club.CLUB_URL, Club.CLUB_URL);
		PROJECTION_MAP.put(Club._ID, Club._ID);
		PROJECTION_MAP.put(Club.CLUB_ID, Club.CLUB_ID);
		PROJECTION_MAP.put(Club.CLUB_EXT_TYPE, Club.CLUB_EXT_TYPE);
		PROJECTION_MAP.put(Club.CLUB_NAME, Club.CLUB_NAME);
		PROJECTION_MAP.put(Club.CLUB_ADDRESS, Club.CLUB_ADDRESS);
		PROJECTION_MAP.put(Club.CLUB_CITY, Club.CLUB_CITY);

		PROJECTION_MAP.put(Club.CLUB_COUNTRY, Club.CLUB_COUNTRY);
		PROJECTION_MAP.put(Club.CLUB_PHONE, Club.CLUB_PHONE);

		PROJECTION_MAP.put(Club.CLUB_LAT, Club.CLUB_LAT);
		PROJECTION_MAP.put(Club.CLUB_LNG, Club.CLUB_LNG);
		PROJECTION_MAP.put(Club.CLUB_MODIFIED_DATE, Club.CLUB_MODIFIED_DATE);
		PROJECTION_MAP.put(Club.CLUB_CREATED_DATE, Club.CLUB_CREATED_DATE);
		PROJECTION_MAP.put(Club.CLUB_DELETEED, Club.CLUB_DELETEED);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Club.TABLE_NAME);
		qb.setCursorFactory(new ClubCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new ClubCursor(db, masterQuery, editTable, query);
		}
	}
	public long getId() {
		return  getLong(getColumnIndexOrThrow(Club._ID));
		
	}
	public String getClubURL() {
		String clubUrl = getString(getColumnIndexOrThrow(Club.CLUB_URL));
		if (clubUrl == null) {
			clubUrl = "";
		}
		return clubUrl;
	}
	public String getClubID() {
		
		String clubStr = getString(getColumnIndexOrThrow(Club.CLUB_ID));
		if (clubStr == null) {
			clubStr = "";
		}
		return clubStr;
	}
	public String getClubName() {
		String clubStr = getString(getColumnIndexOrThrow(Club.CLUB_NAME));
		if (clubStr == null) {
			clubStr = "";
		}
		return clubStr;
	}
	public String getClubCountry() {
		String clubStr = getString(getColumnIndexOrThrow(Club.CLUB_COUNTRY));
		if (clubStr == null) {
			clubStr = "";
		}
		return clubStr;
	}


	public String getClubCity() {
		String clubStr = getString(getColumnIndexOrThrow(Club.CLUB_CITY));
		if (clubStr == null) {
			clubStr = "";
		}
		return clubStr;
	}
	public String getClubAddress() {
		String clubStr = getString(getColumnIndexOrThrow(Club.CLUB_ADDRESS));
		if (clubStr == null) {
			clubStr = "";
		}
		return clubStr;
	}
	public String getClubExtType() {
		String clubStr = getString(getColumnIndexOrThrow(Club.CLUB_EXT_TYPE));
		if (clubStr == null) {
			clubStr = "";
		}
		return clubStr;
	}
	public String getClubPhone() {
		String clubStr = getString(getColumnIndexOrThrow(Club.CLUB_PHONE));
		if (clubStr == null) {
			clubStr = "";
		}
		return clubStr;
	}
	
	public double getClubLat() {
		double clubStr = getDouble(getColumnIndexOrThrow(Club.CLUB_LAT));
		
		return clubStr;
	}
	public double getClubLng() {
		double clubStr = getDouble(getColumnIndexOrThrow(Club.CLUB_LNG));
		return clubStr;
	}
	public long getCreated() {
		return getLong(getColumnIndexOrThrow(Club.CLUB_CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Club.CLUB_MODIFIED_DATE));
	}
	public long getDelFlag() {
		return getLong(getColumnIndexOrThrow(Club.CLUB_DELETEED));
	}
}
