package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Course;
import com.asai24.golf.db.Golf.Course.ExtType;
import com.asai24.golf.db.Golf.Tee;

import java.util.HashMap;

public class TeeCursor extends SQLiteCursor {
	private TeeCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;

	// course と結合
	private static HashMap<String, String> PROJECTION_MAP2;

	private static final String COL_EXT_TYPE = "ext_type";

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Tee._ID, Tee._ID);
		PROJECTION_MAP.put(Tee.NAME, Tee.NAME);
		PROJECTION_MAP.put(Tee.TEE_OOB_ID, Tee.TEE_OOB_ID);
		PROJECTION_MAP.put(Tee.COURSE_ID, Tee.COURSE_ID);
		PROJECTION_MAP.put(Tee.CREATED_DATE, Tee.CREATED_DATE);
		PROJECTION_MAP.put(Tee.MODIFIED_DATE, Tee.MODIFIED_DATE);

		StringBuffer extType = new StringBuffer();
		extType.append(" CASE");
		extType.append("     WHEN c1." + Course.COURSE_OOB_ID
				+ " IS NOT NULL THEN '" + ExtType.OobGolf + "'");
		extType.append("     WHEN c1." + Course.COURSE_YOURGOLF_ID
				+ " IS NOT NULL THEN '" + ExtType.YourGolf + "'");
		extType.append("     ELSE NULL");
		extType.append(" END");
		PROJECTION_MAP2 = new HashMap<String, String>();
		PROJECTION_MAP2.put(Tee._ID, "t1." + Tee._ID + " AS " + Tee._ID);
		PROJECTION_MAP2.put(Tee.NAME, "t1." + Tee.NAME + " AS " + Tee.NAME);
		PROJECTION_MAP2.put(Tee.TEE_OOB_ID, "t1." + Tee.TEE_OOB_ID + " AS "
				+ Tee.TEE_OOB_ID);
		PROJECTION_MAP2.put(Tee.COURSE_ID, "t1." + Tee.COURSE_ID + " AS "
				+ Tee.COURSE_ID);
		PROJECTION_MAP2.put(Tee.CREATED_DATE, "t1." + Tee.CREATED_DATE + " AS "
				+ Tee.CREATED_DATE);
		PROJECTION_MAP2.put(Tee.MODIFIED_DATE, "t1." + Tee.MODIFIED_DATE
				+ " AS " + Tee.MODIFIED_DATE);
		PROJECTION_MAP2.put(COL_EXT_TYPE, extType + " AS " + COL_EXT_TYPE);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Tee.TABLE_NAME);
		qb.setCursorFactory(new TeeCursor.Factory());
		return qb;
	}

	public static SQLiteQueryBuilder getQueryBuilder2() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP2);
		qb.setTables(Tee.TABLE_NAME + " t1" + ", " + Course.TABLE_NAME + " c1"
				+ "");
		qb.appendWhere("t1." + Tee.COURSE_ID + " = " + "c1." + Course._ID);
		qb.setCursorFactory(new TeeCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new TeeCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(Tee._ID));
	}

	public String getName() {
		return getString(getColumnIndexOrThrow(Tee.NAME));
	}

	public String getOobId() {
		return getString(getColumnIndexOrThrow(Tee.TEE_OOB_ID));
	}

	public String getExtId() {
		return getString(getColumnIndexOrThrow(Tee.TEE_OOB_ID));
	}

	public String getExtType() {
		return getString(getColumnIndexOrThrow(COL_EXT_TYPE));
	}

	public long getCourseId() {
		return getLong(getColumnIndexOrThrow(Tee.COURSE_ID));
	}

	public String getCreated() {
		return getString(getColumnIndexOrThrow(Tee.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Tee.MODIFIED_DATE));
	}
}
