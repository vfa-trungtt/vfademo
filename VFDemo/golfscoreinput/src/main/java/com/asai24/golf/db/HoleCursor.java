package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;


import com.asai24.golf.db.Golf.Hole;

import java.util.HashMap;

public class HoleCursor extends SQLiteCursor {
	public HoleCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static HashMap<String, String> PROJECTION_MAP;
	private static HashMap<String, String> PROJECTION_MAP2;

	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(Hole._ID, Hole._ID);
		PROJECTION_MAP.put(Hole.TEE_ID, Hole.TEE_ID);
		PROJECTION_MAP.put(Hole.HOLE_NUMBER, Hole.HOLE_NUMBER);
		PROJECTION_MAP.put(Hole.PAR, Hole.PAR);
		PROJECTION_MAP.put(Hole.WOMEN_PAR, Hole.WOMEN_PAR);
		PROJECTION_MAP.put(Hole.HANDICAP, Hole.HANDICAP);
		PROJECTION_MAP.put(Hole.WOMEN_HANDICAP, Hole.WOMEN_HANDICAP);
		PROJECTION_MAP.put(Hole.YARD, Hole.YARD);
		PROJECTION_MAP.put(Hole.LATITUDE, Hole.LATITUDE);
		PROJECTION_MAP.put(Hole.LONGITUDE, Hole.LONGITUDE);
		PROJECTION_MAP.put(Hole.CREATED_DATE, Hole.CREATED_DATE);
		PROJECTION_MAP.put(Hole.MODIFIED_DATE, Hole.MODIFIED_DATE);

		PROJECTION_MAP2 = new HashMap<String, String>();
		PROJECTION_MAP2.put(Hole._ID, "h2." + Hole._ID);
		PROJECTION_MAP2.put(Hole.TEE_ID, "h2." + Hole.TEE_ID);
		PROJECTION_MAP2.put(Hole.HOLE_NUMBER, "h2." + Hole.HOLE_NUMBER);
		PROJECTION_MAP2.put(Hole.PAR, "h2." + Hole.PAR);
		PROJECTION_MAP2.put(Hole.WOMEN_PAR, "h2." + Hole.WOMEN_PAR);
		PROJECTION_MAP2.put(Hole.HANDICAP, "h2." + Hole.HANDICAP);
		PROJECTION_MAP2.put(Hole.WOMEN_HANDICAP, "h2." + Hole.WOMEN_HANDICAP);
		PROJECTION_MAP2.put(Hole.YARD, "h2." + Hole.YARD);
		PROJECTION_MAP2.put(Hole.LATITUDE, "h2." + Hole.LATITUDE);
		PROJECTION_MAP2.put(Hole.LONGITUDE, "h2." + Hole.LONGITUDE);
		PROJECTION_MAP2.put(Hole.CREATED_DATE, "h2." + Hole.CREATED_DATE);
		PROJECTION_MAP2.put(Hole.MODIFIED_DATE, "h2." + Hole.MODIFIED_DATE);
	}

	private static String nextHoleTables = Hole.TABLE_NAME + " h1" + " JOIN "
			+ Hole.TABLE_NAME + " h2" + " ON h1." + Hole.TEE_ID + " = h2."
			+ Hole.TEE_ID + " AND (h1." + Hole.HOLE_NUMBER + " + 1) = h2."
			+ Hole.HOLE_NUMBER + "";

	private static String previousHoleTables = Hole.TABLE_NAME + " h1"
			+ " JOIN " + Hole.TABLE_NAME + " h2" + " ON h1." + Hole.TEE_ID
			+ " = h2." + Hole.TEE_ID + " AND (h1." + Hole.HOLE_NUMBER
			+ " - 1) = h2." + Hole.HOLE_NUMBER + "";

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Hole.TABLE_NAME);
		qb.setCursorFactory(new HoleCursor.Factory());
		return qb;
	}

	public static SQLiteQueryBuilder getNextHoleQueryBuilder(long holeId) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP2);
		qb.setTables(nextHoleTables);
		qb.setCursorFactory(new HoleCursor.Factory());
		qb.appendWhere("h1." + Hole._ID + " = " + holeId);
		return qb;
	}

	public static SQLiteQueryBuilder getPreviousHoleQueryBuilder(long holeId) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP2);
		qb.setTables(previousHoleTables);
		qb.setCursorFactory(new HoleCursor.Factory());
		qb.appendWhere("h1." + Hole._ID + " = " + holeId);
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new HoleCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		return getLong(getColumnIndexOrThrow(Hole._ID));
	}

	public long getTeeId() {
		return getLong(getColumnIndexOrThrow(Hole.TEE_ID));
	}

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

	public int getMenPar() {
		return getInt(getColumnIndexOrThrow(Hole.PAR));
	}

	public int getWomenPar() {
		return getInt(getColumnIndexOrThrow(Hole.WOMEN_PAR));
	}

	public int getHandicap() {
//		if (GolfApplication.isOwnerMale()) {
//			return getInt(getColumnIndexOrThrow(Hole.HANDICAP));
//		} else {
//			return getInt(getColumnIndexOrThrow(Hole.WOMEN_HANDICAP));
//		}
        return getInt(getColumnIndexOrThrow(Hole.WOMEN_HANDICAP));
	}

	public int getMenHandicap() {
		return getInt(getColumnIndexOrThrow(Hole.HANDICAP));
	}

	public int getWomenHandicap() {
		return getInt(getColumnIndexOrThrow(Hole.WOMEN_HANDICAP));
	}

	// 表示用のハンディキャップ文字列
	public String getHandicapDisplay() {
		int handicap;
//		if (GolfApplication.isOwnerMale()) {
//			handicap = getInt(getColumnIndexOrThrow(Hole.HANDICAP));
//		} else {
//			handicap = getInt(getColumnIndexOrThrow(Hole.WOMEN_HANDICAP));
//		}

        handicap = getInt(getColumnIndexOrThrow(Hole.WOMEN_HANDICAP));

		if (handicap == com.asai24.golf.domain.Hole.HANDICAP_UNNOWN) {
			return "-";
		} else {
			return handicap + "";
		}
	}

	public int getYard() {
		return getInt(getColumnIndexOrThrow(Hole.YARD));
	}

	public double getLatitude() {
		return getDouble(getColumnIndexOrThrow(Hole.LATITUDE));
	}

	public double getLongitude() {
		return getDouble(getColumnIndexOrThrow(Hole.LONGITUDE));
	}

	public long getCreated() {
		return getLong(getColumnIndexOrThrow(Hole.CREATED_DATE));
	}

	public long getModified() {
		return getLong(getColumnIndexOrThrow(Hole.MODIFIED_DATE));
	}

	public boolean hasLocationInfo() {
		if((isNull(getColumnIndexOrThrow(Hole.LATITUDE)) || isNull(getColumnIndexOrThrow(Hole.LONGITUDE)))){
			return false;
		}else{
			if(getLatitude()==0 && getLongitude()==0){
				return false;
			}else{
				return true;
			}
		}
	}
}
