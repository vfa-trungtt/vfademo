package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.HistoryCache;

import java.util.HashMap;

public class HistoryCacheCursor extends SQLiteCursor {
	private HistoryCacheCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(HistoryCache._ID, HistoryCache._ID);
		PROJECTION_MAP.put(HistoryCache.CLUB_NAME, HistoryCache.CLUB_NAME);
		PROJECTION_MAP.put(HistoryCache.PLAYING, HistoryCache.PLAYING);
		PROJECTION_MAP.put(HistoryCache.TOTAL_PUTT, HistoryCache.TOTAL_PUTT);
		PROJECTION_MAP.put(HistoryCache.TOTAL_SHOT, HistoryCache.TOTAL_SHOT);
		PROJECTION_MAP.put(HistoryCache.PLAYDATE, HistoryCache.PLAYDATE);
		/*ThuNA 2013/03/06 ADD -S*/
		PROJECTION_MAP.put(HistoryCache.GORA_SCORE_ID, HistoryCache.GORA_SCORE_ID);
		/*ThuNA 2013/03/06 ADD -E*/
		
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(HistoryCache.TABLE_NAME);
		qb.setCursorFactory(new HistoryCacheCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new HistoryCacheCursor(db, masterQuery, editTable, query);
		}
	}
	
	public String getId() {
		String roundID = getString(getColumnIndexOrThrow(HistoryCache._ID));
		if (roundID == null) {
			roundID = "";
		}
		return roundID;
	}
	public String getClubName() {
		
		String roundName = getString(getColumnIndexOrThrow(HistoryCache.CLUB_NAME));
		if (roundName == null) {
			roundName = "";
		}
		return roundName;
	}
	
	public int getTotalPutt() {
		return getInt(getColumnIndexOrThrow(HistoryCache.TOTAL_PUTT));
		
	}
	public int getTotalShot() {
		return getInt(getColumnIndexOrThrow(HistoryCache.TOTAL_SHOT));
	}


	public long getPlayDate() {
		return getLong(getColumnIndexOrThrow(HistoryCache.PLAYDATE));
	}
	
}
