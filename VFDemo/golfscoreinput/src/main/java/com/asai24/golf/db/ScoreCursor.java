package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Score;

import java.util.HashMap;

public class ScoreCursor extends SQLiteCursor {
	public ScoreCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Score._ID, Score._ID);
		PROJECTION_MAP.put(Score.HOLE_SCORE, Score.HOLE_SCORE);
		PROJECTION_MAP.put(Score.HOLE_ID, Score.HOLE_ID);
		PROJECTION_MAP.put(Score.PLAYER_ID, Score.PLAYER_ID);
		PROJECTION_MAP.put(Score.ROUND_ID, Score.ROUND_ID);
		PROJECTION_MAP.put(Score.GAME_SCORE, Score.GAME_SCORE);
		PROJECTION_MAP.put(Score.FAIRWAY , Score.FAIRWAY);
		PROJECTION_MAP.put(Score.TEE_OFF_CLUB, Score.TEE_OFF_CLUB);
		PROJECTION_MAP.put(Score.SAND_SHOT, Score.SAND_SHOT);
		PROJECTION_MAP.put(Score.OB, Score.OB);
		PROJECTION_MAP.put(Score.WATER_HAZARD, Score.WATER_HAZARD);
		PROJECTION_MAP.put(Score.PUTT_DISABLED, Score.PUTT_DISABLED);
		
		PROJECTION_MAP.put(Score.CREATED_DATE, Score.CREATED_DATE);
		PROJECTION_MAP.put(Score.MODIFIED_DATE, Score.MODIFIED_DATE);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Score.TABLE_NAME);
		qb.setCursorFactory(new ScoreCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new ScoreCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(Score._ID));
	}

	public int getHoleScore() {
		return getInt(getColumnIndexOrThrow(Score.HOLE_SCORE));
	}

	public long getHoleId() {
		return getLong(getColumnIndexOrThrow(Score.HOLE_ID));
	}

	public long getRoundId() {
		return getLong(getColumnIndexOrThrow(Score.ROUND_ID));
	}

	public long getPlayerId() {
		return getLong(getColumnIndexOrThrow(Score.PLAYER_ID));
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
//	public int getGameScore() {
//		return getInt(getColumnIndexOrThrow(Score.GAME_SCORE));
//	}
	public String getGameScore() {
		return getString(getColumnIndexOrThrow(Score.GAME_SCORE));
	}
	public boolean isPuttDisabled() {
		return getInt(getColumnIndexOrThrow(Score.PUTT_DISABLED)) == 1;
	}
	
	public long getCreated() {
		return getLong(getColumnIndexOrThrow(Score.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Score.MODIFIED_DATE));
	}
}
