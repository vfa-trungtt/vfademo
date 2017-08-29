package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

import com.asai24.golf.db.Golf.Player;
import com.asai24.golf.db.Golf.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerHoleScoreCursor extends SQLiteCursor {
	public PlayerHoleScoreCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Player._ID, "p." + Player._ID);
		PROJECTION_MAP.put(Player.NAME, "p." + Player.NAME);
		PROJECTION_MAP.put(Score.HOLE_SCORE, "s." + Score.HOLE_SCORE);
		PROJECTION_MAP.put(Score.GAME_SCORE, "s." + Score.GAME_SCORE);
	}

	public static SQLiteQueryBuilder getQueryBuilder(long roundId, long holeId,
			long[] playerIds) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Player.TABLE_NAME + " p" + " LEFT JOIN "
				+ Score.TABLE_NAME + " s" + " ON p." + Player._ID + " = s."
				+ Score.PLAYER_ID + " AND s." + Score.ROUND_ID + " = "
				+ roundId + " AND s." + Score.HOLE_ID + " = " + holeId + "");
		if (playerIds != null) {
			List<String> ids = new ArrayList<String>();
			for (long id : playerIds) {
				ids.add(Long.toString(id));
			}

			qb.appendWhere("p." + Player._ID + " IN (");
			qb.appendWhere(TextUtils.join(",", ids));
			qb.appendWhere(")");
		}
		qb.setCursorFactory(new PlayerHoleScoreCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new PlayerHoleScoreCursor(db, masterQuery, editTable, query);
		}
	}

	public long getPlayerId() {
		return getLong(getColumnIndexOrThrow(Player._ID));
	}

	public String getPlayerName() {
		return getString(getColumnIndexOrThrow(Player.NAME));
	}

	public int getHoleStrokes() {
		return getInt(getColumnIndexOrThrow(Score.HOLE_SCORE));
	}
	
	public int getGameScore() {
		return getInt(getColumnIndexOrThrow(Score.GAME_SCORE));
	}
}
