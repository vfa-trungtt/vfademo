package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Score;
import com.asai24.golf.db.Golf.TempScore;

import java.util.HashMap;

public class TempScoreCursor extends SQLiteCursor {
	public TempScoreCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(TempScore._ID, TempScore._ID);
		PROJECTION_MAP.put(TempScore.HOLE_SCORE, TempScore.HOLE_SCORE);
		PROJECTION_MAP.put(TempScore.HOLE_ID, TempScore.HOLE_ID);
		PROJECTION_MAP.put(TempScore.PLAYER_ID, TempScore.PLAYER_ID);
		PROJECTION_MAP.put(TempScore.ROUND_ID, TempScore.ROUND_ID);
		PROJECTION_MAP.put(TempScore.GAME_SCORE, TempScore.GAME_SCORE);
		
		PROJECTION_MAP.put(TempScore.FAIRWAY , TempScore.FAIRWAY);
		PROJECTION_MAP.put(TempScore.TEE_OFF_CLUB, TempScore.TEE_OFF_CLUB);
		PROJECTION_MAP.put(TempScore.SAND_SHOT, TempScore.SAND_SHOT);
		PROJECTION_MAP.put(TempScore.OB, TempScore.OB);
		PROJECTION_MAP.put(TempScore.WATER_HAZARD, TempScore.WATER_HAZARD);
		PROJECTION_MAP.put(TempScore.PUTT_DISABLED, TempScore.PUTT_DISABLED);
		
		PROJECTION_MAP.put(TempScore.CREATED_DATE, TempScore.CREATED_DATE);
		PROJECTION_MAP.put(TempScore.MODIFIED_DATE, TempScore.MODIFIED_DATE);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(TempScore.TABLE_NAME);
		qb.setCursorFactory(new TempScoreCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new TempScoreCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(TempScore._ID));
	}

	public int getHoleScore() {
		return getInt(getColumnIndexOrThrow(TempScore.HOLE_SCORE));
	}

	public long getHoleId() {
		return getLong(getColumnIndexOrThrow(TempScore.HOLE_ID));
	}

	public long getRoundId() {
		return getLong(getColumnIndexOrThrow(TempScore.ROUND_ID));
	}

	public long getPlayerId() {
		return getLong(getColumnIndexOrThrow(TempScore.PLAYER_ID));
	}
	
//	public int getGameScore() {
//		return getInt(getColumnIndexOrThrow(TempScore.GAME_SCORE));
//	}
	public String getGameScore() {
		return getString(getColumnIndexOrThrow(TempScore.GAME_SCORE));
	}
	
	public String getFairway() {
		return getString(getColumnIndexOrThrow(Score.FAIRWAY));
	}
	
	public String getFairwayClub() {
		return getString(getColumnIndexOrThrow(Score.TEE_OFF_CLUB));
	}
	
	public int getGB() {
		return getInt(getColumnIndexOrThrow(Score.SAND_SHOT));
	}
	
	public int getWH() {
		return getInt(getColumnIndexOrThrow(Score.WATER_HAZARD));
	}
	
	public int getOB() {
		return getInt(getColumnIndexOrThrow(Score.OB));
	}
	public boolean isPuttDisabled() {
		return getInt(getColumnIndexOrThrow(Score.PUTT_DISABLED)) == 1;
	}

	public long getCreated() {
		return getLong(getColumnIndexOrThrow(TempScore.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(TempScore.MODIFIED_DATE));
	}
}
