package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Score;
import com.asai24.golf.db.Golf.ScoreDetail;

import java.util.HashMap;

public class TotalShotByClubCursor extends SQLiteCursor {
	public TotalShotByClubCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static final String COL_CLUB = ScoreDetail.CLUB;

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		String totalShot = "sd." + COL_CLUB;

		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(COL_CLUB, "COUNT(" + totalShot + ") AS " + COL_CLUB);
	}

	public static SQLiteQueryBuilder getQueryBuilder(long roundId,
			long playerId, String club) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Score.TABLE_NAME + " sc" + " JOIN "
				+ ScoreDetail.TABLE_NAME + " sd" + " ON sc." + Score._ID
				+ " = sd." + ScoreDetail.SCORE_ID + "");
		qb.appendWhere("sc." + Score.ROUND_ID + " = " + roundId + " AND sc."
				+ Score.PLAYER_ID + "=" + playerId + " " + " AND sd."
				+ ScoreDetail.CLUB + " = '" + club + "' ");

		qb.setCursorFactory(new TotalShotByClubCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new TotalShotByClubCursor(db, masterQuery, editTable, query);
		}
	}

	public int getTotalShotByClub() {
		return getInt(getColumnIndexOrThrow(COL_CLUB));
	}
}
