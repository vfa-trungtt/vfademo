package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Score;

import java.util.HashMap;

public class LastModifiedScoreCursor extends SQLiteCursor {
	public LastModifiedScoreCursor(SQLiteDatabase db,
                                   SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Score.MODIFIED_DATE, "MAX(" + Score.MODIFIED_DATE
				+ ") AS " + Score.MODIFIED_DATE);
	}

	public static SQLiteQueryBuilder getQueryBuilder(long roundId) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Score.TABLE_NAME);
		qb.appendWhere(Score.ROUND_ID + " = " + roundId);
		qb.setCursorFactory(new LastModifiedScoreCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new LastModifiedScoreCursor(db, masterQuery, editTable,
					query);
		}
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Score.MODIFIED_DATE));
	}
}
