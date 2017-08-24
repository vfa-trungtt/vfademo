package com.asai24.golf.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import com.asai24.golf.db.Golf.Club;
import com.asai24.golf.db.Golf.Course;
import com.asai24.golf.db.Golf.Round;
import com.asai24.golf.db.Golf.Tee;

import java.util.HashMap;

public class RoundCursor extends SQLiteCursor {
	public RoundCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	private static final String COL_ROUND_ID = "_id";
	private static final String COL_COURSE_ID = "course_id";
	private static final String COL_TEE_ID = "tee_id";
	private static final String COL_CLUB_NAME = "club_name";
    private static final String COL_CLUB_EXT_ID="ext_id";
    private static final String COL_CLUB_EXT_TYPE="ext_type";
	private static final String COL_COURSE_NAME = "course_name";
	private static final String COL_TEE_NAME = "tee_name";
	private static final String COL_RESULT_ID = "result_id";// 2009-06-11
	private static final String COL_YOURGOLF_ID = "yourgolf_id";
	private static final String COL_CREATED_DATE = "created_date";
	private static final String COL_MODIFIED_DATE = "modified_date";
	private static final String COL_UPDATE_DATE = "update_date";
	private static final String COL_HALL = "hole";
	private static final String COL_HALL_COUNT = "hole_count";
	private static final String COL_WEATHER = "weather";

	private static final String PLAYING = "playing";
	private static final String COL_ROUND_DEL = "round_del";
	private static final String COL_ADDRESS = "address";
    private static final String COL_LIVE_ENTRY_ID = "live_entry_id";
	private static final String COL_LIVE_ID = "live_id";

	
	public static HashMap<String, String> PROJECTION_MAP;
	static {
		PROJECTION_MAP = new HashMap<String, String>();
		PROJECTION_MAP.put(COL_ROUND_ID, "r1." + Round._ID + " AS "
				+ COL_ROUND_ID);
		PROJECTION_MAP.put(COL_COURSE_ID, "c1." + Course._ID + " AS "
				+ COL_COURSE_ID);
		PROJECTION_MAP.put(COL_TEE_ID, "t1." + Tee._ID + " AS " + COL_TEE_ID);
		PROJECTION_MAP.put(COL_CLUB_NAME, "c1." + Course.CLUB_NAME + " AS "
				+ COL_CLUB_NAME);
		PROJECTION_MAP.put(COL_COURSE_NAME, "c1." + Course.COURSE_NAME + " AS "
				+ COL_COURSE_NAME);
		PROJECTION_MAP.put(COL_TEE_NAME, "t1." + Tee.NAME + " AS "
				+ COL_TEE_NAME);
		PROJECTION_MAP.put(COL_RESULT_ID, "r1." + Round.RESULT_ID + " AS "
				+ COL_RESULT_ID);
		PROJECTION_MAP.put(COL_YOURGOLF_ID, "r1." + Round.YOURGOLF_ID + " AS "
				+ COL_YOURGOLF_ID);
		PROJECTION_MAP.put(COL_CREATED_DATE, "r1." + Round.CREATED_DATE
				+ " AS " + COL_CREATED_DATE);
		PROJECTION_MAP.put(COL_MODIFIED_DATE, "r1." + Round.MODIFIED_DATE
				+ " AS " + COL_MODIFIED_DATE);
		PROJECTION_MAP.put(COL_HALL, "r1." + Round.HALL
				+ " AS " + COL_HALL);
		PROJECTION_MAP.put(COL_HALL_COUNT, "r1." + Round.HALL_COUNT
				+ " AS " + COL_HALL_COUNT);
		PROJECTION_MAP.put(COL_WEATHER, "r1." + Round.WEATHER
				+ " AS " + COL_WEATHER);
		PROJECTION_MAP.put(COL_UPDATE_DATE, "r1." + Round.UPDATE_DATE
				+ " AS " + COL_UPDATE_DATE);
		PROJECTION_MAP.put(PLAYING, "r1." + Round.PLAYING
				+ " AS " + PLAYING);
		PROJECTION_MAP.put(COL_ROUND_DEL, "r1." + Round.ROUND_DELETE
				+ " AS " + COL_ROUND_DEL);
		PROJECTION_MAP.put(COL_ADDRESS, "cl1." + Club.CLUB_ADDRESS
				+ " AS " + COL_ADDRESS);
        PROJECTION_MAP.put(COL_CLUB_EXT_ID, "cl1." + Club.CLUB_ID + " AS "
                + COL_CLUB_EXT_ID);
        PROJECTION_MAP.put(COL_CLUB_EXT_TYPE, "cl1." + Club.CLUB_EXT_TYPE + " AS "
                + COL_CLUB_EXT_TYPE);
        PROJECTION_MAP.put(COL_LIVE_ENTRY_ID, "r1." + Round.LIVE_ENTRY_ID
                + " AS " + COL_LIVE_ENTRY_ID);
		PROJECTION_MAP.put(COL_LIVE_ID, "r1." + Round.LIVE_ID
				+ " AS " + COL_LIVE_ID);
	}

	public static SQLiteQueryBuilder getQueryBuilder() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Round.TABLE_NAME + " r1" + "," + Course.TABLE_NAME + " c1"
				+ "," + Tee.TABLE_NAME + " t1" + "," + Club.TABLE_NAME + " cl1" + "");
		qb.appendWhere("r1." + Round.COURSE_ID + " = " + "c1." + Course._ID);
		qb.appendWhere(" AND " + "r1." + Round.TEE_ID + " = " + "t1."+ Tee._ID);
		qb.appendWhere(" AND " + "c1." + Course.CLUB_ID + " = " + "cl1."+ Club._ID);

		qb.setCursorFactory(new RoundCursor.Factory());
		return qb;
	}

	public static SQLiteQueryBuilder getQueryBuilder2() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setProjectionMap(PROJECTION_MAP);
		qb.setTables(Round.TABLE_NAME + " r1" + "");


		qb.setCursorFactory(new RoundCursor.Factory());
		return qb;
	}

	private static class Factory implements SQLiteDatabase.CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new RoundCursor(db, masterQuery, editTable, query);
		}
	}

	public long getId() {
		try {
			return getLong(getColumnIndexOrThrow(COL_ROUND_ID));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public long getCourseId() {
		try {
			return getLong(getColumnIndexOrThrow(COL_COURSE_ID));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public long getTeeId() {
		try {
			return getLong(getColumnIndexOrThrow(COL_TEE_ID));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public String getClubName() {
		try {
			return getString(getColumnIndexOrThrow(COL_CLUB_NAME));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}



    public String getColClubExtId() {
        try {
            return getString(getColumnIndexOrThrow(COL_CLUB_EXT_ID));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    public String getColClubExtType() {
        try {
            return getString(getColumnIndexOrThrow(COL_CLUB_EXT_TYPE));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }
	public int getHallCount() {
		try {
			return getInt(getColumnIndexOrThrow(COL_HALL_COUNT));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public String getHall() {
		try {
			return getString(getColumnIndexOrThrow(COL_HALL));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public String getWeather() {
		try {
			return getString(getColumnIndexOrThrow(COL_WEATHER));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public long getRoundDel() {
		try {
			return getLong(getColumnIndexOrThrow(COL_ROUND_DEL));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	public String getCourseName() {
		try {
			return getString(getColumnIndexOrThrow(COL_COURSE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public String getClubCourseName() {
		String clubName = getClubName();
		String courseName = getCourseName();

		if (courseName == null || courseName.equals("")) {
			return clubName;
		} else {
			return clubName + " - " + courseName;
		}
	}

	public String getTeeName() {
		try {
			return getString(getColumnIndexOrThrow(COL_TEE_NAME));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	public long getCreated() {
		try {
			return getLong(getColumnIndexOrThrow(COL_CREATED_DATE));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public long getModified() {
		try {
			return getLong(getColumnIndexOrThrow(COL_MODIFIED_DATE));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public long getResultId() {
		try {
			return getLong(getColumnIndexOrThrow(COL_RESULT_ID));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	public String getYourGolfId() {
		try {
			return getString(getColumnIndexOrThrow(COL_YOURGOLF_ID));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public String getClubAddress() {
		try {
			return getString(getColumnIndexOrThrow(COL_ADDRESS));
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		
	}
	public String getUpdateDate() {
		try {
			return getString(getColumnIndexOrThrow(COL_UPDATE_DATE));
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		
	}

    public String getLiveEntryId() {
        try {
            return getString(getColumnIndexOrThrow(COL_LIVE_ENTRY_ID));
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }

    }

	public String getLiveId() {
		try {
			return getString(getColumnIndexOrThrow(COL_LIVE_ID));
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		
	}
}
