package com.asai24.golf.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asai24.golf.Constant;
import com.asai24.golf.db.Golf.Club;
import com.asai24.golf.db.Golf.Course;
import com.asai24.golf.db.Golf.CourseDelete;
import com.asai24.golf.db.Golf.HistoryCache;
import com.asai24.golf.db.Golf.Hole;
import com.asai24.golf.db.Golf.HoleDelete;
import com.asai24.golf.db.Golf.Player;
import com.asai24.golf.db.Golf.PlayerDelete;
import com.asai24.golf.db.Golf.Round;
import com.asai24.golf.db.Golf.RoundDelete;
import com.asai24.golf.db.Golf.RoundPlayer;
import com.asai24.golf.db.Golf.Score;
import com.asai24.golf.db.Golf.ScoreDelete;
import com.asai24.golf.db.Golf.ScoreDetail;
import com.asai24.golf.db.Golf.ScoreDetailDelete;
import com.asai24.golf.db.Golf.Tee;
import com.asai24.golf.db.Golf.TeeDelete;
import com.asai24.golf.db.Golf.TempScore;
import com.asai24.golf.db.Golf.TempScoreDetail;
import com.asai24.golf.utils.YgoLog;

public abstract class BaseGolfDatabase extends SQLiteOpenHelper {
	@SuppressWarnings("unused")
	private static final String TAG = "GolfDataBaseHelper";

	private static final String DATABASE_NAME = "golf.db";
	private static final int DATABASE_VERSION = 40;

	protected BaseGolfDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#getDatabaseName()
	 * we need it for backward compatibility under API version 14
	 */
	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
 		createPlayerTables(db);
		createClubTables(db);
		createScoreTables(db);
		createScoreDetailTables(db);
		createRoundTables(db);
		createCourseTables(db);
		createTeeTables(db);
		createHoleTables(db);
//		createTraceTables(db);
		createDeleteHistoryTables(db);
		createDeleteTriggers(db);
		createHistoryCacheTables(db);
		//loadSampleData(db);
		
		createRoundPlayerTables(db);
	}

	private void createPlayerTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Player.TABLE_NAME 
				+ " (" + Player._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Player.SERVER_ID + " CHAR(100),"
				+ Player.NAME + " TEXT," 
				+ Player.OWNNER_FLAG + " INTEGER,"
				+ Player.CREATED_DATE + " INTEGER," 
				+ Player.MODIFIED_DATE+ " INTEGER,"
				+ Player.DEL_FLAG +" BOOL DEFAULT 0,"
				+ Player.PLAYER_HDCP +" INTEGER );");
	}
	
	private void createRoundPlayerTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + RoundPlayer.TABLE_NAME 
				+ " (" + RoundPlayer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ RoundPlayer.ROUND_ID + " INTEGER,"
				+ RoundPlayer.PLAYER_ID + " INTEGER," 
				+ RoundPlayer.PLAYER_HDCP + " INTEGER DEFAULT " + Constant.DEFAULT_HDCP_VALUE + ","
                + RoundPlayer.LIVE_PLAYER_ID + " TEXT DEFAULT '',"
                + RoundPlayer.CREATED_DATE + " INTEGER,"
				+ RoundPlayer.MODIFIED_DATE+ " INTEGER,"
                + RoundPlayer.PLAYER_GOAL + " INTEGER DEFAULT " + Constant.DEFAULT_GOAL_VALUE + ");");
	}

	private void createScoreTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Score.TABLE_NAME + " (" + Score._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Score.PLAYER_ID
				+ " INTEGER," + Score.ROUND_ID + " INTEGER," + Score.HOLE_ID
				+ " INTEGER," + Score.HOLE_SCORE + " INTEGER," + Score.GAME_SCORE+ " INTEGER," 
				+ Score.FAIRWAY + " TEXT," 
				+ Score.TEE_OFF_CLUB + " TEXT," 
				+ Score.SAND_SHOT + " BOOL DEFAULT 0," 
				+ Score.OB + " INTEGER DEFAULT 0," 
				+ Score.WATER_HAZARD + " INTEGER DEFAULT 0,"
				+ Score.PUTT_DISABLED + " BOOL DEFAULT 0," 
				+ Score.CREATED_DATE + " INTEGER," 
				+ Score.MODIFIED_DATE + " INTEGER);");
	}

	
	
	private void createScoreDetailTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "
				+ ScoreDetail.TABLE_NAME
				+ " ("
				+ ScoreDetail._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ScoreDetail.SCORE_ID
				+ " INTEGER,"
				+
				// ScoreDetail.SHOT_TYPE + " INTEGER," +
				ScoreDetail.SHOT_NUMBER + " INTEGER,"
				+ ScoreDetail.GPS_LATITUDE + " REAL,"
				+ ScoreDetail.GPS_LONGITUDE + " REAL," + ScoreDetail.CLUB
				+ " TEXT," + ScoreDetail.SHOT_RESULT + " TEXT,"
				+ ScoreDetail.CREATED_DATE + " INTEGER,"
				+ ScoreDetail.MODIFIED_DATE + " INTEGER);");
	}
	//=-----------LAMTT create table club------------
	private void createClubTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Club.TABLE_NAME 
				+ " (" + Club._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ Club.CLUB_ID +" CHAR(50),"
				+ Club.CLUB_EXT_TYPE +" TEXT," 
				+ Club.CLUB_NAME +" TEXT,"
				+ Club.CLUB_COUNTRY +" TEXT,"

				+ Club.CLUB_CITY +" TEXT,"
				+ Club.CLUB_ADDRESS +" TEXT,"
				+ Club.CLUB_URL +" TEXT,"
				+ Club.CLUB_PHONE +" TEXT,"

				+ Club.CLUB_LAT +" DOUBLE,"
				+ Club.CLUB_LNG +" DOUBLE,"
				+ Club.CLUB_CREATED_DATE +" INTEGER,"
				+ Club.CLUB_MODIFIED_DATE +" INTEGER,"
				+ Club.CLUB_DELETEED +" INTEGER"
				+ ");");
	}
	
	
	//=-----------LAMTT create table club------------
	private void createHistoryCacheTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + HistoryCache.TABLE_NAME 
				+ " (" + HistoryCache._ID +" CHAR(100) ,"
				+ HistoryCache.CLUB_NAME +" TEXT," 
				+ HistoryCache.TOTAL_PUTT +" INTEGER,"
				+ HistoryCache.TOTAL_SHOT +" INTEGER,"
				+ HistoryCache.PLAYING +" BOOL DEFAULT 0,"
				+ HistoryCache.PLAYDATE +" INTEGER,"
				+ HistoryCache.GORA_SCORE_ID + " TEXT"
				+ ");");
	}
	
	
	//------- LAMTT add temp table------//
	public  void createTempScoreTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE  " + TempScore.TABLE_NAME + " (" + TempScore._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TempScore.PLAYER_ID
				+ " INTEGER," + TempScore.ROUND_ID + " INTEGER,"
				+ TempScore.HOLE_ID + " INTEGER," 
				+ TempScore.HOLE_SCORE + " INTEGER," 
				+ TempScore.GAME_SCORE + " INTEGER," 
				
				+ TempScore.FAIRWAY + " TEXT," 
				+ TempScore.TEE_OFF_CLUB + " TEXT," 
				+ TempScore.SAND_SHOT + " BOOL," 
				+ TempScore.OB + " INTEGER," 
				+ TempScore.WATER_HAZARD + " INTEGER," 
				+ TempScore.PUTT_DISABLED + " BOOL," 
				
				+ TempScore.CREATED_DATE + " INTEGER," + TempScore.MODIFIED_DATE
				+ " INTEGER);");
	}
	public  void createTempScoreDetailTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE   "
				+ TempScoreDetail.TABLE_NAME
				+ " ("
				+ TempScoreDetail._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ TempScoreDetail.SCORE_ID
				+ " INTEGER,"
				+
				// ScoreDetail.SHOT_TYPE + " INTEGER," +
				TempScoreDetail.SHOT_NUMBER + " INTEGER,"
				+ TempScoreDetail.GPS_LATITUDE + " REAL,"
				+ TempScoreDetail.GPS_LONGITUDE + " REAL," + TempScoreDetail.CLUB
				+ " TEXT," + TempScoreDetail.SHOT_RESULT + " TEXT,"
				+ TempScoreDetail.CREATED_DATE + " INTEGER,"
				+ TempScoreDetail.MODIFIED_DATE + " INTEGER);");
	}
	
	//------- End LAMTT add temp table------//
	private void createRoundTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Round.TABLE_NAME + " (" + Round._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Round.COURSE_ID
				+ " INTEGER," + Round.TEE_ID + " INTEGER," + Round.RESULT_ID
				+ " INTEGER," + Round.YOURGOLF_ID 
				+ " TEXT," +  Round.HALL 
				+ " TEXT," +  Round.WEATHER 
				+ " TEXT," +  Round.LIVE_ENTRY_ID
				+ " TEXT," +  Round.LIVE_ID
				+ " TEXT," +  Round.UPDATE_DATE
				+ " TEXT," +  Round.CREATED_DATE 
				+ " INTEGER," + Round.MODIFIED_DATE
				+ " INTEGER," + Round.HALL_COUNT
				+ " INTEGER,"+ Round.PLAYING 
				+ " BOOL DEFAULT 0 ,"+ Round.ROUND_DELETE 
				+ " BOOL DEFAULT 0 );");
	}

	private void createCourseTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Course.TABLE_NAME + " (" + Course._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Course.COURSE_NAME+ " TEXT,"
				+ Course.CLUB_ID +" INTEGER,"
				+ Course.CLUB_NAME + " TEXT," 
				+ Course.COURSE_OOB_ID + " TEXT," 
				+ Course.COURSE_YOURGOLF_ID + " TEXT,"
				+ Course.DEL_FLAG + " BOOLEAN,"//NAMLH add del flag
				+ Course.CREATED_DATE + " INTEGER," + Course.MODIFIED_DATE
				+ " INTEGER);");
	}

	private void createTeeTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Tee.TABLE_NAME + " (" + Tee._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Tee.NAME + " TEXT,"
				+ Tee.TEE_OOB_ID + " TEXT," + Tee.COURSE_ID + " INTEGER,"
				+ Tee.CREATED_DATE + " INTEGER," + Tee.MODIFIED_DATE
				+ " INTEGER);");
	}

	private void createHoleTables(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Hole.TABLE_NAME + " (" + Hole._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Hole.TEE_ID
				+ " INTEGER," + Hole.HOLE_NUMBER + " INTEGER," + Hole.PAR
				+ " INTEGER," + Hole.WOMEN_PAR + " INTEGER," + Hole.YARD
				+ " INTEGER," + Hole.HANDICAP + " INTEGER,"
				+ Hole.WOMEN_HANDICAP + " INTEGER," + Hole.LATITUDE + " REAL,"
				+ Hole.LONGITUDE + " REAL," + Hole.CREATED_DATE + " INTEGER,"
				+ Hole.MODIFIED_DATE + " INTEGER);");
	}

	private Cursor GetHoleItems(SQLiteDatabase db,int courseId){
		String sqlStr=" select    h.latitude,h.longitude  from " + Round.TABLE_NAME +" r left join "+Hole.TABLE_NAME +" h on r."+Round.TEE_ID+" = h."+Hole.TEE_ID ;
		sqlStr += " where r."+Round.COURSE_ID +"="+courseId ;
		return (Cursor)db.rawQuery(sqlStr, null);
		
	}
	private long insertRowClubByCourse(SQLiteDatabase db,Cursor cusor ,int opp,int yourgolf,double lat,double ln)
	{
//		StringBuffer sbclub = new StringBuffer();
//		sbclub.append("INSERT");
//		sbclub.append("    INTO");
//		sbclub.append("        CLUB(");
//		sbclub.append("            _id");
//		sbclub.append("            ,ext_id");
//		sbclub.append("            ,ext_type");
//		sbclub.append("            ,name");	
//		sbclub.append("            ,country");
//
//		sbclub.append("            ,city");
//		sbclub.append("            ,address");
//		sbclub.append("            ,url");
//		sbclub.append("            ,phone");
//	
//		sbclub.append("            ,lat");
//		sbclub.append("            ,lng");
//		sbclub.append("            ,created");
//		sbclub.append("            ,modified");
//		sbclub.append("            ,del_flag");
//		sbclub.append("        )");
//		sbclub.append("    VALUES");
//		sbclub.append("        (");
//		sbclub.append("            "+index);
//		sbclub.append("            ,'"+ (opp > 0 ?opp:yourgolf) +"'" );
//		sbclub.append("            ,'"+ (opp > 0?Constant.EXT_TYPE_OOBGOLF:Constant.EXT_TYPE_YOURGOLF)  + "'");
//		sbclub.append("            ,'"+ cusor.getString(cusor.getColumnIndex(Course.CLUB_NAME)) +"'");
//
//		sbclub.append("            ,''");
//		sbclub.append("            ,''");
//		sbclub.append("            ,''");
//		sbclub.append("            ,''");
//		sbclub.append("            ,''");
//		sbclub.append("            ,"+lat);
//		sbclub.append("            ,"+ln);
//		sbclub.append("            ,"+ cusor.getLong(cusor.getColumnIndex(Course.CREATED_DATE)));
//		sbclub.append("            ,"+ cusor.getLong(cusor.getColumnIndex(Course.MODIFIED_DATE)));
//		sbclub.append("            ,0");
//		sbclub.append("        )");
//		db.execSQL(sbclub.toString());

        YgoLog.i(TAG,"insertRowClubByCourse " + (opp > 0? Constant.EXT_TYPE_OOBGOLF: Constant.EXT_TYPE_YOURGOLF) );
		ContentValues values = new ContentValues();
		values.put(Club.CLUB_ID, (opp > 0 ?opp:yourgolf) );
		values.put(Club.CLUB_EXT_TYPE, (opp > 0? Constant.EXT_TYPE_OOBGOLF: Constant.EXT_TYPE_YOURGOLF)  );
		values.put(Club.CLUB_NAME, cusor.getString(cusor.getColumnIndex(Course.CLUB_NAME)));
		values.put(Club.CLUB_COUNTRY,"");
		values.put(Club.CLUB_CITY, "");
		values.put(Club.CLUB_ADDRESS, "");
		values.put(Club.CLUB_URL, "");
		values.put(Club.CLUB_PHONE, "");

		values.put(Club.CLUB_LAT, lat);
		values.put(Club.CLUB_LNG, ln);
		values.put(Club.CLUB_CREATED_DATE, cusor.getLong(cusor.getColumnIndex(Course.CREATED_DATE)));
		values.put(Club.CLUB_MODIFIED_DATE, cusor.getLong(cusor.getColumnIndex(Course.MODIFIED_DATE)));
		values.put(Club.CLUB_DELETEED, 0);
		
		return db.insert(Club.TABLE_NAME, null, values);
		
		
		
	}
	
	public void loadSampleData(SQLiteDatabase db) {
		
		StringBuffer sbclub = new StringBuffer();
		sbclub.append("INSERT");
		sbclub.append("    INTO");
		sbclub.append("        CLUB(");
		sbclub.append("            _id");
		sbclub.append("            ,ext_id");
		sbclub.append("            ,ext_type");
		sbclub.append("            ,name");	
		sbclub.append("            ,country");

		sbclub.append("            ,city");
		sbclub.append("            ,address");
		sbclub.append("            ,url");
		sbclub.append("            ,phone");
	
		sbclub.append("            ,lat");
		sbclub.append("            ,lng");
		sbclub.append("            ,created");
		sbclub.append("            ,modified");
		sbclub.append("            ,del_flag");
		sbclub.append("        )");
		sbclub.append("    VALUES");
		sbclub.append("        (");
		sbclub.append("            1");
		sbclub.append("            ,'1'");
		sbclub.append("            ,'yourgolf（yourgolf のコースとしてください）'");
		sbclub.append("            ,'"+ Constant.CLUB_NAME_DEFAULT+"'");

		sbclub.append("            ,'JP '");
		sbclub.append("            ,'Tokyo'");
		sbclub.append("            ,'Yoyogi, Shibuya-ku, Tokyo '");
		sbclub.append("            ,'http://www.yourgolf-online.com/'");
		sbclub.append("            ,'123456789 '");
		sbclub.append("            ,35.683831");
		sbclub.append("            ,139.702187");
		sbclub.append("            ,1239583605006");
		sbclub.append("            ,1239583605006");
		sbclub.append("            ,0");
		sbclub.append("        )");
		db.execSQL(sbclub.toString());
		// create course01
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT");
		sb.append("    INTO");
		sb.append("        COURSES(");
		sb.append("            club_id");
		sb.append("            ,club_name");
		sb.append("            ,course_oob_id");
		sb.append("            ,created");
		sb.append("            ,modified");
		sb.append("        )");
		sb.append("    VALUES");
		sb.append("        (");
		sb.append("        1");
		sb.append("            ,'Your Golf Course(Sample)'");
		sb.append("            ,'123456'");
		sb.append("            ,1239583605006");
		sb.append("            ,1239583605006");
		sb.append("        )");
		db.execSQL(sb.toString());
		
		// create tees
		sb = new StringBuffer();
		sb
				.append("INSERT INTO TEES ( name, tee_oob_id, course_id, created, modified ) VALUES ('regular','46363',1,1241859989706,1241859989706)");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb
				.append("INSERT INTO TEES ( name, tee_oob_id, course_id, created, modified ) VALUES ('back','46362',1,1241859989706,1241859989706)");
		db.execSQL(sb.toString());

		// create holes
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 18; j++) {

				sb = new StringBuffer();
				sb.append("INSERT");
				sb.append("    INTO");
				sb.append("        HOLES(");
				sb.append("            tee_id");
				sb.append("            ,hole_number");
				sb.append("            ,par");
				sb.append("            ,women_par");
				sb.append("            ,yard");
				sb.append("            ,handicap");
				sb.append("            ,women_handicap");
				sb.append("            ,latitude");
				sb.append("            ,longitude");
				sb.append("            ,created");
				sb.append("            ,modified");
				sb.append("        )");
				sb.append("    VALUES");
				sb.append("        (");
				sb.append("            " + (i + 1));
				sb.append("            ," + (j + 1));
				sb.append("            ,4");
				sb.append("            ,4");
				sb.append("            ,400");
				sb.append("            ,10");
				sb.append("            ,10");
				sb.append("            ,35.48384187479905");
				sb.append("            ,139.50010299682617");
				sb.append("            ,1239583605141");
				sb.append("            ,1239583605141");
				sb.append("        )");
				db.execSQL(sb.toString());
			}
		}

		// create players

		// create round
		sb = new StringBuffer();
		sb
				.append("INSERT INTO ROUNDS (course_id, tee_id, created, modified,hole,weather,hole_count ) VALUES (1,1,1241859985826,1241859985826,'hole_one','sunny',18)");
		db.execSQL(sb.toString());

		// create score
		sb = new StringBuffer();
		sb
				.append("INSERT INTO SCORES (player_id, round_id, hole_id, hole_score, game_score, created, modified ) VALUES (1,1,1,5,0,1241859989623,1241859992405)");
		db.execSQL(sb.toString());

		// create score_details
		sb = new StringBuffer();
		// sb.append("INSERT INTO SCORE_DETAILS ( score_id, shot_type, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,null,1,35.48249757022695,139.4997100532055,1241859989706,1241859989707)");
		sb
				.append("INSERT INTO SCORE_DETAILS ( score_id, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,1,35.48249757022695,139.4997100532055,1241859989706,1241859989707)");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		// sb.append("INSERT INTO SCORE_DETAILS ( score_id, shot_type, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,1,2,35.48331769697405,139.50008153915405,1241859990176,1241859990176)");
		sb
				.append("INSERT INTO SCORE_DETAILS ( score_id, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,2,35.48331769697405,139.50008153915405,1241859990176,1241859990176)");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		// sb.append("INSERT INTO SCORE_DETAILS ( score_id, shot_type, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,1,3,35.48424374214978,139.5001244544983,1241859991010,1241859991010)");
		sb
				.append("INSERT INTO SCORE_DETAILS ( score_id, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,3,35.48424374214978,139.5001244544983,1241859991010,1241859991010)");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		// sb.append("INSERT INTO SCORE_DETAILS ( score_id, shot_type, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,1,4,35.48494263710419,139.50006008148193,1241859991763,1241859991763)");
		sb
				.append("INSERT INTO SCORE_DETAILS ( score_id, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,4,35.48494263710419,139.50006008148193,1241859991763,1241859991763)");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		// sb.append("INSERT INTO SCORE_DETAILS ( score_id, shot_type, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,1,5,35.48514356827877,139.5000386238098,1241859992418,1241859992418)");
		sb
				.append("INSERT INTO SCORE_DETAILS ( score_id, shot_number, gps_latitude, gps_longitude, created, modified ) VALUES (1,5,35.48514356827877,139.5000386238098,1241859992418,1241859992418)");
		db.execSQL(sb.toString());
	}

	private void fixHandicapData(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("UPDATE holes SET handicap = ? WHERE handicap = 0 OR handicap = -2147483648");
		db
				.execSQL(
						sb.toString(),
						new String[] { com.asai24.golf.domain.Hole.HANDICAP_UNNOWN
								+ "" });
	}

	private void addWomenColumnToHole(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE holes ADD COLUMN women_par INTEGER");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("ALTER TABLE holes ADD COLUMN women_handicap INTEGER");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb
				.append("UPDATE holes SET women_par = par ,women_handicap = handicap");
		db.execSQL(sb.toString());
	}

	private void deleteShotTypeColumn(SQLiteDatabase db) {
		// 現状のSQlite3ではDROP COLUMNが使用できないためテーブルのバックアップを取って作り直しを行う

		// score_detallsテーブルの名称変更
		StringBuffer sb = new StringBuffer();
		sb.append("alter table " + ScoreDetail.TABLE_NAME + " rename to temp;");
		db.execSQL(sb.toString());

		// 新たにscore_detallsテーブルの作り直し
		sb = new StringBuffer();
		sb.append("CREATE TABLE " + ScoreDetail.TABLE_NAME + " ("
				+ ScoreDetail._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ScoreDetail.SCORE_ID + " INTEGER," + ScoreDetail.SHOT_NUMBER
				+ " INTEGER," + ScoreDetail.GPS_LATITUDE + " REAL,"
				+ ScoreDetail.GPS_LONGITUDE + " REAL," + ScoreDetail.CLUB
				+ " TEXT," + ScoreDetail.SHOT_RESULT + " TEXT,"
				+ ScoreDetail.CREATED_DATE + " INTEGER,"
				+ ScoreDetail.MODIFIED_DATE + " INTEGER);");
		db.execSQL(sb.toString());

		// 旧テーブルから新テーブルに必要な値のコピー
		sb = new StringBuffer();
		sb.append("insert into " + ScoreDetail.TABLE_NAME + " select "
				+ ScoreDetail._ID + "," + ScoreDetail.SCORE_ID + ","
				+ ScoreDetail.SHOT_NUMBER + "," + ScoreDetail.GPS_LATITUDE
				+ "," + ScoreDetail.GPS_LONGITUDE + "," + ScoreDetail.CLUB
				+ "," + ScoreDetail.SHOT_RESULT + ","
				+ ScoreDetail.CREATED_DATE + "," + ScoreDetail.MODIFIED_DATE
				+ " from temp;");
		db.execSQL(sb.toString());

		// 旧テーブルの削除
		sb = new StringBuffer();
		sb.append("drop table temp;");
		db.execSQL(sb.toString());
	}

	private void addCourseYourGolfIdColumn(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE courses ADD COLUMN course_yourgolf_id INTEGER");
		db.execSQL(sb.toString());
	}

	private void createDeleteHistoryTables(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE " + PlayerDelete.TABLE_NAME + " (");
		sb.append("    " + PlayerDelete.DELETED_ID + " INTEGER NOT NULL");
		sb.append("    , " + PlayerDelete.DELETED_DATE + " INTEGER NOT NULL");
		sb.append(");");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TABLE " + RoundDelete.TABLE_NAME + " (");
		sb.append("    " + RoundDelete.DELETED_ID + " INTEGER NOT NULL");
		sb.append("    , " + RoundDelete.DELETED_DATE + " INTEGER NOT NULL");
		sb.append(");");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TABLE " + CourseDelete.TABLE_NAME + " (");
		sb.append("    " + CourseDelete.DELETED_ID + " INTEGER NOT NULL");
		sb.append("    , " + CourseDelete.DELETED_DATE + " INTEGER NOT NULL");
		sb.append(");");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TABLE " + TeeDelete.TABLE_NAME + " (");
		sb.append("    " + TeeDelete.DELETED_ID + " INTEGER NOT NULL");
		sb.append("    , " + TeeDelete.DELETED_DATE + " INTEGER NOT NULL");
		sb.append(");");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TABLE " + HoleDelete.TABLE_NAME + " (");
		sb.append("    " + HoleDelete.DELETED_ID + " INTEGER NOT NULL");
		sb.append("    , " + HoleDelete.DELETED_DATE + " INTEGER NOT NULL");
		sb.append(");");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TABLE " + ScoreDelete.TABLE_NAME + " (");
		sb.append("    " + ScoreDelete.DELETED_ID + " INTEGER NOT NULL");
		sb.append("    , " + ScoreDelete.DELETED_DATE + " INTEGER NOT NULL");
		sb.append(");");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TABLE " + ScoreDetailDelete.TABLE_NAME + " (");
		sb.append("    " + ScoreDetailDelete.DELETED_ID + " INTEGER NOT NULL");
		sb.append("    , " + ScoreDetailDelete.DELETED_DATE
				+ " INTEGER NOT NULL");
		sb.append(");");
		db.execSQL(sb.toString());
	}

	private void createDeleteTriggers(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TRIGGER trace_player_delete AFTER DELETE ON "
				+ Player.TABLE_NAME);
		sb.append(" FOR EACH ROW");
		sb.append(" BEGIN");
		sb.append("   INSERT INTO");
		sb.append("     " + PlayerDelete.TABLE_NAME);
		sb.append("     (" + PlayerDelete.DELETED_ID + ", "
				+ PlayerDelete.DELETED_DATE + ")");
		sb.append("     VALUES");
		sb.append("     (old._id, STRFTIME('%s000', DATETIME('now')))");
		sb.append("   ;");
		sb.append(" END;");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TRIGGER trace_round_delete AFTER DELETE ON "
				+ Round.TABLE_NAME);
		sb.append(" FOR EACH ROW");
		sb.append(" BEGIN");
		sb.append("   INSERT INTO");
		sb.append("     " + RoundDelete.TABLE_NAME);
		sb.append("     (" + RoundDelete.DELETED_ID + ", "
				+ RoundDelete.DELETED_DATE + ")");
		sb.append("     VALUES");
		sb.append("     (old._id, STRFTIME('%s000', DATETIME('now')))");
		sb.append("   ;");
		sb.append(" END;");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TRIGGER trace_course_delete AFTER DELETE ON "
				+ Course.TABLE_NAME);
		sb.append(" FOR EACH ROW");
		sb.append(" BEGIN");
		sb.append("   INSERT INTO");
		sb.append("     " + CourseDelete.TABLE_NAME);
		sb.append("     (" + CourseDelete.DELETED_ID + ", "
				+ CourseDelete.DELETED_DATE + ")");
		sb.append("     VALUES");
		sb.append("     (old._id, STRFTIME('%s', DATETIME('now')))");
		sb.append("   ;");
		sb.append(" END;");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TRIGGER trace_tee_delete AFTER DELETE ON "
				+ Tee.TABLE_NAME);
		sb.append(" FOR EACH ROW");
		sb.append(" BEGIN");
		sb.append("   INSERT INTO");
		sb.append("     " + TeeDelete.TABLE_NAME);
		sb.append("     (" + TeeDelete.DELETED_ID + ", "
				+ TeeDelete.DELETED_DATE + ")");
		sb.append("     VALUES");
		sb.append("     (old._id, STRFTIME('%s000', DATETIME('now')))");
		sb.append("   ;");
		sb.append(" END;");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TRIGGER trace_hole_delete AFTER DELETE ON "
				+ Hole.TABLE_NAME);
		sb.append(" FOR EACH ROW");
		sb.append(" BEGIN");
		sb.append("   INSERT INTO");
		sb.append("     " + HoleDelete.TABLE_NAME);
		sb.append("     (" + HoleDelete.DELETED_ID + ", "
				+ HoleDelete.DELETED_DATE + ")");
		sb.append("     VALUES");
		sb.append("     (old._id, STRFTIME('%s000', DATETIME('now')))");
		sb.append("   ;");
		sb.append(" END;");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TRIGGER trace_score_delete AFTER DELETE ON "
				+ Score.TABLE_NAME);
		sb.append(" FOR EACH ROW");
		sb.append(" BEGIN");
		sb.append("   INSERT INTO");
		sb.append("     " + ScoreDelete.TABLE_NAME);
		sb.append("     (" + ScoreDelete.DELETED_ID + ", "
				+ ScoreDelete.DELETED_DATE + ")");
		sb.append("     VALUES");
		sb.append("     (old._id, STRFTIME('%s000', DATETIME('now')))");
		sb.append("   ;");
		sb.append(" END;");
		db.execSQL(sb.toString());

		sb = new StringBuffer();
		sb.append("CREATE TRIGGER trace_score_detail_delete AFTER DELETE ON "
				+ ScoreDetail.TABLE_NAME);
		sb.append(" FOR EACH ROW");
		sb.append(" BEGIN");
		sb.append("   INSERT INTO");
		sb.append("     " + ScoreDetailDelete.TABLE_NAME);
		sb.append("     (" + ScoreDetailDelete.DELETED_ID + ", "
				+ ScoreDetailDelete.DELETED_DATE + ")");
		sb.append("     VALUES");
		sb.append("     (old._id, STRFTIME('%s000', DATETIME('now')))");
		sb.append("   ;");
		sb.append(" END;");
		db.execSQL(sb.toString());
	}
	
	private void addGameScoreColumnToScore(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE scores ADD COLUMN game_score INTEGER");
		db.execSQL(sb.toString());
	}
	private void addYourGolfIdColumnToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE rounds ADD COLUMN yourgolf_id TEXT");
		db.execSQL(sb.toString());
	}
	
	private void addPlayerHdcpColumnToPlayer(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE players ADD COLUMN player_hdcp INTEGER");
		db.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +


		if (oldVersion < 27) {
			// handicapデータ修正
			fixHandicapData(db);

			// women用カラム追加 & デフォルト値セット
			addWomenColumnToHole(db);
		}

		if (oldVersion < 28) {
			// score_detailsテーブルのshot_typeカラム削除
			deleteShotTypeColumn(db);
			// coursesテーブルにcourse_yourgolf_idカラム追加
			addCourseYourGolfIdColumn(db);
		}

		if (oldVersion < 29) {
			createDeleteHistoryTables(db);
			createDeleteTriggers(db);
		}
		
		if (oldVersion < 30) {
			addGameScoreColumnToScore(db);
			addYourGolfIdColumnToRound(db);
		}
		if(oldVersion < 31)
		{
			CreateVersion32(db);
			updateVersion31(db);
			//updateRevertDateColumTable(db);
			
		}
		if(oldVersion < 32)
		{
			db.execSQL("DROP TABLE IF EXISTS "+ Club.TABLE_NAME);
			createClubTables(db);
			updateVersion31(db);
			updateDateColumTable(db);
			
		}
		if(oldVersion < 33)
		{
			
			createHistoryCacheTables(db);
			addPlayerIdServerColum(db);
			addUpdateDateColumnToRound(db);
			updateScoreDetailColumClub(db);
			updateRevertDateColumTable(db);
			updateRoundPlayingColumTable(db);
			if(!fineRoundUploadAll(db))
			{
				updateRoundYourGolfIDColumTable(db);
			}
			
		}
		if(oldVersion < 34)
		{
			
			addColumScoreOnV3(db);
			if(exists(db,TempScore.TABLE_NAME)==true)
				addColumScoreTempOnV3(db);
		}
		if(oldVersion < 35) {
			fixDeletedPlayingRound(db);
		}
		if(oldVersion < 36) {
			addPuttDisabledColumnOnScoreTable(db);
		}
		if(oldVersion < 37){
			addGoraScoreIdOnHistoryCacheTable(db);
		}
//		if(oldVersion < 38){
//			addPlayerHdcpColumnToPlayer(db);
//			createRoundPlayerTables(db);
//		}
		if(oldVersion < 39){
            addPlayerHdcpColumnToPlayer(db);
            createRoundPlayerTables(db);
			addLiveEntryIdToRound(db);
			addLiveIdToRound(db);
//            addLivePlayerIdToRoundPlayer(db);
		}
        // CanNC - Add new column into RoundPlayer
        if (oldVersion < 40) {
            addGoalColumnOnRoundPlayer(db);
        }
	}
	
	private boolean exists(SQLiteDatabase db,String table) {
	    try {
	    	db.query(table, null,null, null, null, null, null);
	         return true;
	    } catch (Exception e) {
	         return false;
	    }
	}

	private void CreateVersion32(SQLiteDatabase db)
	{
		createClubTables(db);
		addClubIDColumnToCourse(db);
		addDelColumnToCourse(db);
		addDelColumnToPlayer(db);
		addHoleColumnToRound(db);
		addWeatherColumnToRound(db);
		addHoleCountColumnToRound(db);
		addPlayingColumnToRound(db);
		addDelColumnToRound(db);
		
		
	}
	/**
	 * @author LAMTT
	 */
	private void updateVersion31(SQLiteDatabase db)
	{
//		createClubTables(db);
//		addClubIDColumnToCourse(db);
//		addDelColumnToCourse(db);
//		addDelColumnToPlayer(db);
//		addHoleColumnToRound(db);
//		addWeatherColumnToRound(db);
//		addHoleCountColumnToRound(db);
//		addPlayingColumnToRound(db);
//		addDelColumnToRound(db);
		// Insert data//
		String sqlStr=" select * from " +Course.TABLE_NAME  ;
		Cursor course=(Cursor)db.rawQuery(sqlStr, null);
		course.moveToFirst();
		int count=course.getCount();
		if(count>0)
		{
			for(int i=0;i<count;i++)
			{
				Cursor cusorGetItem= GetHoleItems(db,course.getInt(course.getColumnIndex(Course._ID)));
				int opp=0;
				int yourgolf=0;
				double lat=0;
				double ln=0;
				try{
					opp=course.getInt(course.getColumnIndex(Course.COURSE_OOB_ID));
					yourgolf=course.getInt(course.getColumnIndex(Course.COURSE_YOURGOLF_ID));
				}catch (Exception e) {
					// TODO: handle exception
					yourgolf=course.getInt(course.getColumnIndex(Course.COURSE_YOURGOLF_ID));
				}
				if(cusorGetItem.moveToFirst())
				{
					lat=cusorGetItem.getDouble(0);
					ln=cusorGetItem.getDouble(1);
				}
				long clubid =insertRowClubByCourse(db,course,opp, yourgolf,lat,ln);
				//updateColumTable(db, Course.TABLE_NAME, Course.CLUB_ID, course.getInt(course.getColumnIndex(Course._ID)), i+1);
				updateColumTable(db, Course.TABLE_NAME, Course.CLUB_ID, clubid, course.getInt(course.getColumnIndex(Course._ID)));
				int itemCount=cusorGetItem.getCount();
				if(itemCount == 9)
					updateRoundColumTable(db,Round.TABLE_NAME,Round.HALL_COUNT,itemCount,course.getInt(course.getColumnIndex(Course._ID)));
//				if(yourgolf>0)
//					updateColumTable(db,Round.TABLE_NAME,Round.YOURGOLF_ID,yourgolf,i+1);
				course.moveToNext();
			}
		}
	}
	//LAMTT update colum//
	private boolean fineRoundUploadAll(SQLiteDatabase db) {
		String sqlStr=" select   *  from " + Round.TABLE_NAME +" where " +Round.YOURGOLF_ID +" = '' or "+Round.YOURGOLF_ID +" is null ";
		
		Cursor result= (Cursor)db.rawQuery(sqlStr, null);
		if(result.getCount()>0){
			result.close();
			return true;
		}
		result.close();
		return false;
	}
	//LAMTT update colum//
	private void updateRoundYourGolfIDColumTable(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("update  "+Round.TABLE_NAME+ " SET  " +Round.YOURGOLF_ID +"= "+Round.YOURGOLF_ID +"||'_old_data' ");
		db.execSQL(sb.toString());
	}
	//LAMTT update colum//
	private void updateRoundPlayingColumTable(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("update  "+Round.TABLE_NAME+ " SET  " +Round.PLAYING +"= 0  where "+Round.PLAYING +"= 1");
		db.execSQL(sb.toString());
	}
	
	private void updateScoreDetailColumClub(SQLiteDatabase db)
	{
		
		String sql="update "+ScoreDetail.TABLE_NAME + " set "+ScoreDetail.CLUB ;
			sql +=" = case ";
			sql += " when " + ScoreDetail.CLUB +" = 'Driver'  then  'dr'";
			sql += " when " + ScoreDetail.CLUB +" = '3 wood'  then  '3w'";
			sql += " when " + ScoreDetail.CLUB +" = '4 wood'  then  '4w'";
			sql += " when " + ScoreDetail.CLUB +" = '5 wood'  then  '5w'";
			sql += " when " + ScoreDetail.CLUB +" = '7 wood'  then  '7w'";
			sql += " when " + ScoreDetail.CLUB +" = 'hybrid'  then  'hy'";
			sql += " when " + ScoreDetail.CLUB +" = '2 iron'  then  '2i'";
			sql += " when " + ScoreDetail.CLUB +" = '3 iron'  then  '3i'";
			sql += " when " + ScoreDetail.CLUB +" = '4 iron'  then  '4i'";
			sql += " when " + ScoreDetail.CLUB +" = '5 iron'  then  '5i'";
			sql += " when " + ScoreDetail.CLUB +" = '6 iron'  then  '6i'";
			sql += " when " + ScoreDetail.CLUB +" = '7 iron'  then  '7i'";
			sql += " when " + ScoreDetail.CLUB +" = '8 iron'  then  '8i'";
			sql += " when " + ScoreDetail.CLUB +" = '9 iron'  then  '9i'";
			sql += " when " + ScoreDetail.CLUB +" = 'Pitching wedge'  then  'pw'";
			sql += " when " + ScoreDetail.CLUB +" = 'Gap wedge'  then  'gw'";
			sql += " when " + ScoreDetail.CLUB +" = 'Sand wedge'  then  'sw'";
			sql += " when " + ScoreDetail.CLUB +" = 'Lob wedge'  then  'lw'";
			sql += " when " + ScoreDetail.CLUB +" = 'Putter'  then  'pt'";
			sql += " when " + ScoreDetail.CLUB +" = 'Putter(no count)'  then  'ptt'";
			sql +=" else null end";
		StringBuffer sb = new StringBuffer();
		sb.append(sql);
		db.execSQL(sb.toString());
	}
	//LAMTT update colum//
	private void updateDateColumTable(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("update  "+Round.TABLE_NAME+ " SET  " +Round.CREATED_DATE +"="+Round.CREATED_DATE+ "+ 86400000" );
		db.execSQL(sb.toString());
	}
	private void updateRevertDateColumTable(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("update  "+Round.TABLE_NAME+ " SET  " +Round.CREATED_DATE +"="+Round.CREATED_DATE+ "- 86400000" );
		db.execSQL(sb.toString());
	}
	//LAMTT update colum//
	private void updateRoundColumTable(SQLiteDatabase db,String tableName,String colum,Object value,int rowId) {
        YgoLog.i(TAG,"updateRoundColumTable value=" + value);
		StringBuffer sb = new StringBuffer();
		sb.append("update  "+tableName+ " SET  " +colum +"="+value +" where "+Round.COURSE_ID +"="+rowId);
		db.execSQL(sb.toString());
	}
	//LAMTT update colum//
	private void updateColumTable(SQLiteDatabase db,String tableName,String colum,Object value,int rowId) {
        YgoLog.i(TAG,"updateRoundColumTable value=" + value);
		StringBuffer sb = new StringBuffer();
		sb.append("update  "+tableName+ " SET  " +colum +"="+value +" where "+Round._ID +"="+rowId);
		db.execSQL(sb.toString());
	}
	
	//LAMTT add colum//
	private void addPlayerIdServerColum(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Player.TABLE_NAME+" ADD COLUMN "+Player.SERVER_ID+" CHAR(100) DEFAULT ''");
		db.execSQL(sb.toString());
	}
	private void addClubIDColumnToCourse(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Course.TABLE_NAME+" ADD COLUMN "+Course.CLUB_ID+" INTEGER DEFAULT 0");
		db.execSQL(sb.toString());
	}
	
	private void addDelColumnToCourse(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Course.TABLE_NAME+" ADD COLUMN "+Course.DEL_FLAG+" BOOL DEFAULT "+0);
		db.execSQL(sb.toString());
	}
	private void addDelColumnToPlayer(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Player.TABLE_NAME+" ADD COLUMN "+Player.DEL_FLAG+" BOOL DEFAULT "+0);
		db.execSQL(sb.toString());
	}
	private void addHoleColumnToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Round.TABLE_NAME+" ADD COLUMN "+Round.HALL+" TEXT DEFAULT 'hole_one'");
		db.execSQL(sb.toString());
	}
	private void addWeatherColumnToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Round.TABLE_NAME+" ADD COLUMN "+Round.WEATHER+" TEXT DEFAULT ''");
		db.execSQL(sb.toString());
	}
	private void addHoleCountColumnToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Round.TABLE_NAME+" ADD COLUMN "+Round.HALL_COUNT+" INTEGER DEFAULT 18");
		db.execSQL(sb.toString());
	}
	private void addUpdateDateColumnToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Round.TABLE_NAME+" ADD COLUMN "+Round.UPDATE_DATE+" TEXT ");
		db.execSQL(sb.toString());
	}
	private void addPlayingColumnToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Round.TABLE_NAME+" ADD COLUMN "+Round.PLAYING+" BOOL DEFAULT "+0);
		db.execSQL(sb.toString());
	}
	private void addDelColumnToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Round.TABLE_NAME+" ADD COLUMN "+Round.ROUND_DELETE+" BOOL DEFAULT "+0 );
		
		db.execSQL(sb.toString());
	}
    private void addLiveEntryIdToRound(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("ALTER TABLE " + Round.TABLE_NAME + " ADD COLUMN " + Round.LIVE_ENTRY_ID + " TEXT DEFAULT ''");
        db.execSQL(sb.toString());
    }

	private void addLiveIdToRound(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE " + Round.TABLE_NAME + " ADD COLUMN " + Round.LIVE_ID + " TEXT DEFAULT ''");
		db.execSQL(sb.toString());
	}
    private void addLivePlayerIdToRoundPlayer(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("ALTER TABLE " + RoundPlayer.TABLE_NAME + " ADD COLUMN " + RoundPlayer.LIVE_PLAYER_ID + " TEXT DEFAULT ''");
        db.execSQL(sb.toString());
    }
	private void addColumScoreOnV3(SQLiteDatabase db)
	{
		addFairwayColumnScore(db);
		addFairwayClubColumnScore(db);
		addGBColumnScore(db);
		addWHColumnScore(db);
		addOBColumnScore(db);
	}
	private void addColumScoreTempOnV3(SQLiteDatabase db)
	{
		addFairwayColumnScoreTemp(db);
		addFairwayClubColumnScoreTemp(db);
		addGBColumnScoreTemp(db);
		addWHColumnScoreTemp(db);
		addOBColumnScoreTemp(db);
	}
	private void addFairwayColumnScore(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Score.TABLE_NAME+" ADD COLUMN "+Score.FAIRWAY+" TEXT DEFAULT  ''" );
		db.execSQL(sb.toString());
	}
	private void addFairwayClubColumnScore(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Score.TABLE_NAME+" ADD COLUMN "+Score.TEE_OFF_CLUB+" TEXT DEFAULT ''" );
		db.execSQL(sb.toString());
	}
	private void addGBColumnScore(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Score.TABLE_NAME+" ADD COLUMN "+Score.SAND_SHOT+" BOOL DEFAULT "+0 );
		db.execSQL(sb.toString());
	}
	private void addWHColumnScore(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Score.TABLE_NAME+" ADD COLUMN "+Score.WATER_HAZARD+" INTEGER DEFAULT 0" );
		db.execSQL(sb.toString());
	}
	private void addOBColumnScore(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Score.TABLE_NAME+" ADD COLUMN "+Score.OB+" INTEGER DEFAULT 0" );
		db.execSQL(sb.toString());
	}
	
	//temp score//
	private void addFairwayColumnScoreTemp(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+TempScore.TABLE_NAME+" ADD COLUMN "+TempScore.FAIRWAY+" TEXT DEFAULT  ''" );
		db.execSQL(sb.toString());
	}
	private void addFairwayClubColumnScoreTemp(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+TempScore.TABLE_NAME+" ADD COLUMN "+TempScore.TEE_OFF_CLUB+" TEXT DEFAULT ''" );
		db.execSQL(sb.toString());
	}
	private void addGBColumnScoreTemp(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+TempScore.TABLE_NAME+" ADD COLUMN "+TempScore.SAND_SHOT+" BOOL DEFAULT "+0 );
		db.execSQL(sb.toString());
	}
	private void addWHColumnScoreTemp(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+TempScore.TABLE_NAME+" ADD COLUMN "+TempScore.WATER_HAZARD+" INTEGER DEFAULT 0" );
		db.execSQL(sb.toString());
	}
	private void addOBColumnScoreTemp(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+TempScore.TABLE_NAME+" ADD COLUMN "+TempScore.OB+" INTEGER DEFAULT 0" );
		db.execSQL(sb.toString());
	}

    private void addGoalColumnOnRoundPlayer(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("ALTER TABLE " + RoundPlayer.TABLE_NAME + " ADD COLUMN " + RoundPlayer.PLAYER_GOAL + " INTEGER DEFAULT 99" );
        db.execSQL(sb.toString());
    }
	
	//--------End------
	//-----LAMTT add delete method-----
	public void dropTempScoreDetailTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS "
				+ TempScoreDetail.TABLE_NAME);
	}
	public void dropTempScoreTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS "
				+ TempScore.TABLE_NAME);
	}
	
	//----------End-------------
	
	protected void createDataTables(SQLiteDatabase db) {
		createCourseTables(db);
		createTeeTables(db);
		createHoleTables(db);
		createPlayerTables(db);
		createRoundTables(db);
		createScoreTables(db);
		createScoreDetailTables(db);
		createClubTables(db);
		createDeleteHistoryTables(db);
		createDeleteTriggers(db);
	}
	
	/**
	 * fix data for bug #4197
	 * it was possible to delete playing round at version 4.5.0, so now fix deleted playing round.
	 */
	private void fixDeletedPlayingRound(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(Round.PLAYING, false);
		String whereClause = Round.ROUND_DELETE + " = 1 AND " + Round.PLAYING + " = 1";
		db.update(Round.TABLE_NAME, values, whereClause, null);
	}
	
	private void addPuttDisabledColumnOnScoreTable(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE "+Score.TABLE_NAME+" ADD COLUMN "+Score.PUTT_DISABLED+" INTEGER DEFAULT 0");
		db.execSQL(sb.toString());
		
		// if user is editing round, we have to add column to tmp table.
		try {
			sb = new StringBuffer();
			sb.append("ALTER TABLE "+TempScore.TABLE_NAME+" ADD COLUMN "+TempScore.PUTT_DISABLED+" INTEGER DEFAULT 0");
			db.execSQL(sb.toString());
		} catch(Exception e) {
		}
	}
	private void addGoraScoreIdOnHistoryCacheTable(SQLiteDatabase db){
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("ALTER TABLE " + HistoryCache.TABLE_NAME+" ADD COLUMN " + HistoryCache.GORA_SCORE_ID + " TEXT DEFAULT ''");
			db.execSQL(sb.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
