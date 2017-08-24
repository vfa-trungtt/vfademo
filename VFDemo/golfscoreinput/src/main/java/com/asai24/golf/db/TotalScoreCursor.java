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

public class TotalScoreCursor extends SQLiteCursor {
	public TotalScoreCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static final String COL_PLAYER_ID = "player_id";
	private static final String COL_TOTAL_STROKES = "total_strokes";
	private static final String COL_TOTAL_SCORE = "total_score";
	private static final String COL_PUTT_DISABLED = "putt_disabled";

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		String playerId = "s1." + Score.PLAYER_ID;
		String holeShot = "s1." + Score.HOLE_SCORE;
		String holeScore = "s1." + Score.HOLE_SCORE + " - " + "h1.";
		String puttDisabled = "s1." + Score.PUTT_DISABLED;
//		if (GolfApplication.isOwnerMale()) {
//			holeScore += Hole.PAR;
//		} else {
//			holeScore += Hole.WOMEN_PAR;
//		}

        holeScore += Hole.WOMEN_PAR;
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(COL_PLAYER_ID, playerId + " AS " + COL_PLAYER_ID);
		PROJECTION_MAP.put(COL_TOTAL_STROKES, "SUM(" + holeShot + ") AS " + COL_TOTAL_STROKES);
		PROJECTION_MAP.put(COL_TOTAL_SCORE, "SUM(" + holeScore + ") AS " + COL_TOTAL_SCORE);
		PROJECTION_MAP.put(COL_PUTT_DISABLED, "MAX(" + puttDisabled + ") AS " + COL_PUTT_DISABLED);
	}

//	public static SQLiteQueryBuilder getQueryBuilder(long roundId) {
//		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//		qb.setProjectionMap(PROJECTION_MAP);
//		qb.setTables(Score.TABLE_NAME + " s1" + " JOIN " + Hole.TABLE_NAME
//				+ " h1" + " ON s1." + Score.HOLE_ID + " = h1." + Hole._ID + "");
//		qb.appendWhere("s1." + Score.ROUND_ID + " = " + roundId);
//
//		qb.setCursorFactory(new TotalScoreCursor.Factory());
//		return qb;
//	}
	
	public static SQLiteQueryBuilder getQueryBuilder(long roundId) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Score.TABLE_NAME + " s1" + " JOIN " + Hole.TABLE_NAME
				+ " h1" + " ON s1." + Score.HOLE_ID + " = h1." + Hole._ID + "");
		qb.appendWhere("s1." + Score.ROUND_ID + " = " + roundId + " AND s1."
				+ Score.HOLE_SCORE + ">" + 0 + " " );

		qb.setCursorFactory(new TotalScoreCursor.Factory());
		return qb;
	}

//	public static SQLiteQueryBuilder getQueryBuilder(long roundId, long playerId) {
//		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//		qb.setProjectionMap(PROJECTION_MAP);
//		qb.setTables(Score.TABLE_NAME + " s1" + " JOIN " + Hole.TABLE_NAME
//				+ " h1" + " ON s1." + Score.HOLE_ID + " = h1." + Hole._ID + "");
//		qb.appendWhere("s1." + Score.ROUND_ID + " = " + roundId + " AND s1."
//				+ Score.PLAYER_ID + "=" + playerId + " " );
//
//		qb.setCursorFactory(new TotalScoreCursor.Factory());
//		return qb;
//	}
	public static SQLiteQueryBuilder getQueryBuilder(long roundId, long playerId) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Score.TABLE_NAME + " s1" + " JOIN " + Hole.TABLE_NAME
				+ " h1" + " ON s1." + Score.HOLE_ID + " = h1." + Hole._ID + "");
		qb.appendWhere("s1." + Score.ROUND_ID + " = " + roundId + " AND s1."
				+ Score.PLAYER_ID + "=" + playerId  + " AND s1."
				+ Score.HOLE_SCORE + ">" + 0 + " " );

		qb.setCursorFactory(new TotalScoreCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new TotalScoreCursor(db, masterQuery, editTable, query);
		}
	}

	public long getPlayerId() {
		return getLong(getColumnIndexOrThrow(COL_PLAYER_ID));
	}

	public int getTotalStrokes() {
		try{
			return getInt(getColumnIndexOrThrow(COL_TOTAL_STROKES));
		}
		catch (Exception e) {
			return 0;
		}
	}

	public int getTotalScore() {
		return getInt(getColumnIndexOrThrow(COL_TOTAL_SCORE));
	}
	
	public boolean isPuttDisabled() {
		if(getCount() > 0) {
			return getInt(getColumnIndexOrThrow(COL_PUTT_DISABLED)) == 1;
		}else{
			return false;
		}
	}
}
