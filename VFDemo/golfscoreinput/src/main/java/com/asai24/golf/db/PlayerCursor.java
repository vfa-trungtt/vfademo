package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Player;

import java.util.HashMap;

public class PlayerCursor extends SQLiteCursor {
	public PlayerCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
                        String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Player._ID, Player._ID);
		PROJECTION_MAP.put(Player.SERVER_ID, Player.SERVER_ID);
		PROJECTION_MAP.put(Player.NAME, Player.NAME);
		PROJECTION_MAP.put(Player.OWNNER_FLAG, Player.OWNNER_FLAG);
		PROJECTION_MAP.put(Player.CREATED_DATE, Player.CREATED_DATE);
		PROJECTION_MAP.put(Player.MODIFIED_DATE, Player.MODIFIED_DATE);
		PROJECTION_MAP.put(Player.DEL_FLAG, Player.DEL_FLAG);
		PROJECTION_MAP.put(Player.PLAYER_HDCP, Player.PLAYER_HDCP);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Player.TABLE_NAME);
		qb.setCursorFactory(new PlayerCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new PlayerCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(Player._ID));
	}
	public String getServerId() {
		return getString(getColumnIndexOrThrow(Player.SERVER_ID));
	}
	public String getName() {
		return getString(getColumnIndexOrThrow(Player.NAME));
	}

	public long getOwnerFlg() {
		return getLong(getColumnIndexOrThrow(Player.OWNNER_FLAG));
	}

	public String getCreated() {
		return getString(getColumnIndexOrThrow(Player.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Player.MODIFIED_DATE));
	}
	public long getDelFlag() {
		return getLong(getColumnIndexOrThrow(Player.DEL_FLAG));
	}
	public float getPlayerHdcp() {
		return getFloat(getColumnIndexOrThrow(Player.PLAYER_HDCP));
	}
}
