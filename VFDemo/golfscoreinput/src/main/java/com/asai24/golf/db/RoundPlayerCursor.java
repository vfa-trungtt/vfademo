package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.RoundPlayer;

import java.util.HashMap;

public class RoundPlayerCursor extends SQLiteCursor {
	public RoundPlayerCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(RoundPlayer._ID, RoundPlayer._ID);
		PROJECTION_MAP.put(RoundPlayer.ROUND_ID, RoundPlayer.ROUND_ID);
		PROJECTION_MAP.put(RoundPlayer.PLAYER_ID, RoundPlayer.PLAYER_ID);
		PROJECTION_MAP.put(RoundPlayer.PLAYER_HDCP, RoundPlayer.PLAYER_HDCP);
        PROJECTION_MAP.put(RoundPlayer.LIVE_PLAYER_ID, RoundPlayer.LIVE_PLAYER_ID);
		PROJECTION_MAP.put(RoundPlayer.CREATED_DATE, RoundPlayer.CREATED_DATE);
		PROJECTION_MAP.put(RoundPlayer.MODIFIED_DATE, RoundPlayer.MODIFIED_DATE);
        PROJECTION_MAP.put(RoundPlayer.PLAYER_GOAL, RoundPlayer.PLAYER_GOAL);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(RoundPlayer.TABLE_NAME);
		qb.setCursorFactory(new RoundPlayerCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new RoundPlayerCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(RoundPlayer._ID));
	}
	public long getRoundId() {
		return getLong(getColumnIndexOrThrow(RoundPlayer.ROUND_ID));
	}
	public long getPlayerId() {
		return getLong(getColumnIndexOrThrow(RoundPlayer.PLAYER_ID));
	}

	public float getPlayerHdcp() {
		return getFloat(getColumnIndexOrThrow(RoundPlayer.PLAYER_HDCP));
	}

    public String getLivePlayerId() {
        return getString(getColumnIndexOrThrow(RoundPlayer.LIVE_PLAYER_ID));
    }

	public long getCreated() {
		return getLong(getColumnIndexOrThrow(RoundPlayer.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(RoundPlayer.MODIFIED_DATE));
	}

    public int getPlayerGoal() {
        return getInt(getColumnIndexOrThrow(RoundPlayer.PLAYER_GOAL));
    }
}
