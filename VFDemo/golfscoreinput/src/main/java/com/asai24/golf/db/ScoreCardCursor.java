package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.GolfApplication;
import com.asai24.golf.db.Golf.Hole;
import com.asai24.golf.db.Golf.Score;

import java.util.HashMap;

public class ScoreCardCursor extends SQLiteCursor {
	public ScoreCardCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;
	private static final String COL_SCORE_ID = "score_id";
	private static final String COL_HOLE_ID = "hole_id";

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Hole._ID, "h1." + Hole._ID + " AS " + COL_HOLE_ID);
		PROJECTION_MAP.put(Hole.TEE_ID, Hole.TEE_ID);
		PROJECTION_MAP.put(Hole.HOLE_NUMBER, Hole.HOLE_NUMBER);
		PROJECTION_MAP.put(Hole.PAR, Hole.PAR);
		PROJECTION_MAP.put(Hole.WOMEN_PAR, Hole.WOMEN_PAR);
		PROJECTION_MAP.put(Hole.YARD, Hole.YARD);
		PROJECTION_MAP.put(Score.HOLE_SCORE, Score.HOLE_SCORE);
		PROJECTION_MAP.put(Score.PLAYER_ID, Score.PLAYER_ID);
		PROJECTION_MAP.put(Score.ROUND_ID, Score.ROUND_ID);
		PROJECTION_MAP.put(Score.GAME_SCORE, Score.GAME_SCORE);
		PROJECTION_MAP.put(Score.TEE_OFF_CLUB, Score.TEE_OFF_CLUB);
		PROJECTION_MAP.put(Score.FAIRWAY, Score.FAIRWAY);
		PROJECTION_MAP.put(Score.SAND_SHOT, Score.SAND_SHOT);
		PROJECTION_MAP.put(Score.OB, Score.OB);
		PROJECTION_MAP.put(Score.WATER_HAZARD, Score.WATER_HAZARD);
		PROJECTION_MAP.put(Score.PUTT_DISABLED, Score.PUTT_DISABLED);
		PROJECTION_MAP.put("s1." + Score._ID, "s1." + Score._ID + " AS "
				+ COL_SCORE_ID);// 2009-06-11
	}

	public static SQLiteQueryBuilder getQueryBuilder(
			HashMap<String, String> option) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Hole.TABLE_NAME + " h1 LEFT OUTER JOIN "
				+ Score.TABLE_NAME + " s1 ON (h1." + Hole._ID + "=s1."
				+ Score.HOLE_ID + " AND " + option.get("SQLOnClause") + ")");
		qb.setCursorFactory(new ScoreCardCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new ScoreCardCursor(db, masterQuery, editTable, query);
		}
	}

	public long getHoleId() {
		// return getLong(getColumnIndexOrThrow(Hole._ID));
		return getLong(getColumnIndexOrThrow(COL_HOLE_ID));
	}

	public int getHoleScore() {
		return getInt(getColumnIndexOrThrow(Score.HOLE_SCORE));
	}

	public long getRoundId() {
		return getLong(getColumnIndexOrThrow(Score.ROUND_ID));
	}

	public long getPlayerId() {
		return getLong(getColumnIndexOrThrow(Score.PLAYER_ID));
	}

	public long getCreated() {
		return getLong(getColumnIndexOrThrow(Score.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Score.MODIFIED_DATE));
	}

	/*
	 * 2009-05--08 Add
	 */
	public int getHoleNumber() {
		return getInt(getColumnIndexOrThrow(Hole.HOLE_NUMBER));
	}

	public int getPar() {
//		if (GolfApplication.isOwnerMale()) {
//			return getInt(getColumnIndexOrThrow(Hole.PAR));
//		} else {
//			return getInt(getColumnIndexOrThrow(Hole.WOMEN_PAR));
//		}
        return getInt(getColumnIndexOrThrow(Hole.WOMEN_PAR));
	}

	// 2009-06-11
	public long getScoreId() {
		return getLong(getColumnIndexOrThrow(COL_SCORE_ID));
	}

	public int getYard() {
		return getInt(getColumnIndexOrThrow(Hole.YARD));
	}
	
//	public int getGameScore() {
//		return getInt(getColumnIndexOrThrow(Score.GAME_SCORE));
//	}
	public String getGameScore() {
		return getString(getColumnIndexOrThrow(Score.GAME_SCORE));
	}
	
	public String getTeeOffClub() {
		return getString(getColumnIndexOrThrow(Score.TEE_OFF_CLUB));
	}
	
	public String getFairway() {
		return getString(getColumnIndexOrThrow(Score.FAIRWAY));
	}
	
	public int getSandShot() {
		return getInt(getColumnIndexOrThrow(Score.SAND_SHOT));
	}
	
	public int getOb() {
		return getInt(getColumnIndexOrThrow(Score.OB));
	}
	
	public int getWaterHazard() {
		return getInt(getColumnIndexOrThrow(Score.WATER_HAZARD));
	}
	public boolean isPuttDisabled() {
		return getInt(getColumnIndexOrThrow(Score.PUTT_DISABLED)) == 1;
	}
}
