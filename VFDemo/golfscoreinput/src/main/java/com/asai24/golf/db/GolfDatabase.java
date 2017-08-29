package com.asai24.golf.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

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
import com.asai24.golf.db.Golf.Round.COMPLETION_STATUS;
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
import com.asai24.golf.domain.ClubObj;
import com.asai24.golf.domain.HistoryObj;
import com.asai24.golf.domain.PlayerObj;
import com.asai24.golf.domain.RoundObj;
import com.asai24.golf.domain.ScoreDetailObj;
import com.asai24.golf.domain.ScoreObj;
import com.asai24.golf.utils.YgoLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GolfDatabase extends BaseGolfDatabase {
	
	private static final String TAG = "GolfDatabase";

	private static GolfDatabase mInstance = null;

	private static final double LATITUDE_NULL = -1;
	private static final double LONGITUDE_NULL = -1;
	final String FLG_NOT_OWNER = "0";
	final String FLG_OWNER = "1";

	private static final int MAX_SCORE = 15;

	private GolfDatabase(Context context) {
		super(context);
	}

	public static synchronized GolfDatabase getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new GolfDatabase(context.getApplicationContext());
		}
		return mInstance;
	}

	public CourseCursor getCourses() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = CourseCursor.getQueryBuilder();

		CourseCursor c = (CourseCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	public CourseCursor getCoursesOrderByCreated() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = CourseCursor.getQueryBuilder();
		String orderBy = " created DESC";
		CourseCursor c = (CourseCursor) qb.query(db, null, null, null, null,
				null, orderBy);
		c.moveToFirst();
		return c;
	}
	/**
	 * NAMLH
	 * 2012.05.10
	 * Get History course
	 * @return Cursor
	 */
	public CourseCursor getHistoryCoursesOrderByCreated() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = CourseCursor.getQueryBuilder();
		qb.appendWhere(Course.DEL_FLAG + "= 0");
		String orderBy = " created DESC";
		CourseCursor c = (CourseCursor) qb.query(db, null, null , null, null,
				null, orderBy);
		c.moveToFirst();
		return c;
	}

	public CourseCursor getCourse(long courseId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = CourseCursor.getQueryBuilder();
		qb.appendWhere(Course._ID + " = " + courseId);

		CourseCursor c = (CourseCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	public PlayerCursor getPlayersByIds(List<Long> playerIds) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();
		if (playerIds != null) {
			List<String> ids = new ArrayList<String>();
			for (long id : playerIds) {
				ids.add(Long.toString(id));
			}

			qb.appendWhere(Player._ID + " IN (");
			qb.appendWhere(TextUtils.join(",", ids));
			qb.appendWhere(")");
		}

		PlayerCursor c = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	/*
	 * @ 2009-05-11 ADD
	 */
	public PlayerCursor getPlayerById(long playerId) {
        SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();

		qb.appendWhere(Player._ID + " = " + playerId);

		PlayerCursor c = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
	
	public PlayerCursor getPlayerByServerId(String playerServerId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();

		qb.appendWhere(Player.SERVER_ID + " = '" + playerServerId + "'");

		PlayerCursor c = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		if(null != c){
			c.moveToFirst();
		}
		return c;
	}

	public PlayerCursor getPlayerHistory() {
		return getPlayerHistory(false);
	}
	public PlayerCursor getPlayerHistory(boolean ownnerFlag) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();

		qb.appendWhere(Player.DEL_FLAG + " = " + 0);
		qb.appendWhere(" AND "+Player.OWNNER_FLAG + " = " + (ownnerFlag==true?1:0));
		
		PlayerCursor c = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	} 
	public PlayerCursor getPlayers() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();

		PlayerCursor pc = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		pc.moveToFirst();
		return pc;
	}
	
	public RoundPlayerCursor getRoundPlayer() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundPlayerCursor.getQueryBuilder();

		RoundPlayerCursor pc = (RoundPlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		pc.moveToFirst();
		return pc;
	}
	
	public RoundPlayerCursor getRoundPlayerByRoundId(long roundId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundPlayerCursor.getQueryBuilder();

		qb.appendWhere(RoundPlayer.ROUND_ID + " = " + roundId );

		RoundPlayerCursor c = (RoundPlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		if(null != c){
			c.moveToFirst();
		}
		return c;
	}
	
	public RoundPlayerCursor getRoundPlayerById(long id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundPlayerCursor.getQueryBuilder();

		qb.appendWhere(RoundPlayer._ID + " = " + id );

		RoundPlayerCursor c = (RoundPlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		if(null != c){
			c.moveToFirst();
		}
		return c;
	}
	
	public RoundPlayerCursor getRoundPlayerByRoundIdAndPlayerId(long roundId, long playerId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundPlayerCursor.getQueryBuilder();

		qb.appendWhere(RoundPlayer.ROUND_ID + " = " + roundId );
		qb.appendWhere(" AND " + RoundPlayer.PLAYER_ID + " = " + playerId );

		RoundPlayerCursor c = (RoundPlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		if(null != c){
			c.moveToFirst();
		}
		return c;
	}
	
	public int updateAPIRound(RoundObj roundFromJson,String idserver) {
		
		ContentValues values = new ContentValues();
		values.put(Round.CREATED_DATE, roundFromJson.getPlayDate());
		values.put(Round.MODIFIED_DATE, getNow());
		values.put(Round.UPDATE_DATE, roundFromJson.getUpdateAt());
		values.put(Round.HALL, roundFromJson.getStartHole()==1? Constant.HOLE_ONE: Constant.HOLE_TEN);
		values.put(Round.WEATHER, roundFromJson.getWeather());
		String whereClause = Round.YOURGOLF_ID + " = ?";
		String[] whereArgs = new String[] { idserver };
		return getWritableDatabase().update(Round.TABLE_NAME, values,
				whereClause, whereArgs);
	}
	
	public void deleteAllDBByFlag() {
		
		deleteAllRoundDeleteByFlag();
		deleteAllClubDeleteByFlag();
		deleteAllCourseDeleteByFlag();
		deleteAllPlayerDeleteByFlag();
	}
	
	public int deleteAllClubDeleteByFlag() {
		
		ContentValues values = new ContentValues();
		values.put(Club.CLUB_DELETEED, true);
		return getWritableDatabase().update(Club.TABLE_NAME, values,
				null, null);
	}
	public int deleteAllCourseDeleteByFlag() {
		
		ContentValues values = new ContentValues();
		values.put(Course.DEL_FLAG, true);
		return getWritableDatabase().update(Course.TABLE_NAME, values,
				null, null);
	}
	public int deleteAllPlayerDeleteByFlag() {
		
		ContentValues values = new ContentValues();
		values.put(Player.DEL_FLAG, true);
		return getWritableDatabase().update(Player.TABLE_NAME, values,
				null, null);
	}
	public int deleteAllRoundDeleteByFlag() {
		
		ContentValues values = new ContentValues();
		values.put(Round.ROUND_DELETE, true);
		values.put(Round.PLAYING, false);
		return getWritableDatabase().update(Round.TABLE_NAME, values,
				null, null);
	}
	
	public long insertAPIRound(long courseId, long teeId, 
								String hall,String weather,
								long hole_count,String yourGolfID,
								long date_play,String updateAt, String liveEntryId, String liveId) {
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Round.COURSE_ID, courseId);
		values.put(Round.YOURGOLF_ID, yourGolfID);
		values.put(Round.TEE_ID, teeId);// 2009-05-27 bug178
		values.put(Round.CREATED_DATE, date_play);
		values.put(Round.MODIFIED_DATE, getNow());
		values.put(Round.UPDATE_DATE, updateAt);
		values.put(Round.HALL, hall);
		values.put(Round.WEATHER, weather);
		values.put(Round.HALL_COUNT,(long) hole_count);
        if(liveEntryId != null){
            values.put(Round.LIVE_ENTRY_ID, liveEntryId);
        }else{
            values.put(Round.LIVE_ENTRY_ID, "");
        }
		if(liveId != null){
			values.put(Round.LIVE_ID, liveId);
		}else{
			values.put(Round.LIVE_ID, "");
		}
		long roundId = db.insert(Round.TABLE_NAME, null, values);
		
		return roundId;
	}
	
	/**
	 * 新規にラウンドを作成します。さらに、一番最初のホール初期スコアを作成します。（スコアを作成しないと
	 * ラウンドとプレーヤーの紐付けが表現できないため）
	 */
	public long createRound(long courseId, long teeId, List<Long> playerIds,String hall,String weather,long date_play,long hole_count,long dateCreate, String liveEntryId, String liveId) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Round.COURSE_ID, courseId);
		values.put(Round.TEE_ID, teeId);// 2009-05-27 bug178
		values.put(Round.CREATED_DATE, date_play);
		values.put(Round.MODIFIED_DATE, getNow());
		values.put(Round.HALL, hall);
		values.put(Round.WEATHER, weather);
		values.put(Round.HALL_COUNT,(long) hole_count);
        values.put(Round.LIVE_ENTRY_ID, liveEntryId);
		values.put(Round.LIVE_ID, liveId);
		long roundId = db.insert(Round.TABLE_NAME, null, values);

		HoleCursor hc = getTeeHoles(teeId, 1);
		long holeId = hc.getId();
		hc.close();

		for (Long playerId : playerIds) {
			createScore(roundId, holeId, playerId, 0, "");
		}

		return roundId;
	}

	/**
	 * holeNumberが0のときは全てのホールを返します
	 */
	public HoleCursor getTeeHoles(long teeId, int holeNumber) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HoleCursor.getQueryBuilder();
		qb.appendWhere(Hole.TEE_ID + " = " + teeId);
		if (holeNumber != 0) {
			qb.appendWhere(" AND " + Hole.HOLE_NUMBER + " = " + holeNumber);
		}

		HoleCursor c = (HoleCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public HoleCursor getNextHole(long holeId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HoleCursor.getNextHoleQueryBuilder(holeId);

		HoleCursor c = (HoleCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public HoleCursor getPreviousHole(long holeId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HoleCursor.getPreviousHoleQueryBuilder(holeId);

		HoleCursor c = (HoleCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public HoleCursor getHole(long holeId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HoleCursor.getQueryBuilder();
		qb.appendWhere(Hole._ID + " = " + holeId);

		HoleCursor c = (HoleCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public HoleCursor getHoleByTeeId(long teeId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HoleCursor.getQueryBuilder();
		qb.appendWhere(Hole.TEE_ID + " = " + teeId);

		HoleCursor c = (HoleCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}
	//LAMTT update colum//
	public void updateAllRoundYourGolfIDColumTable() {
		SQLiteDatabase db = getReadableDatabase();
		StringBuffer sb = new StringBuffer();
		sb.append("update  "+Round.TABLE_NAME+ " SET  " +Round.YOURGOLF_ID +"= "+Round.YOURGOLF_ID +"||'_old_data' ");
		db.execSQL(sb.toString());
	}
	public PlayerHoleScoreCursor getPlayersStats(long roundId, long holeId,
			long[] playerIds) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerHoleScoreCursor.getQueryBuilder(roundId,
				holeId, playerIds);

		PlayerHoleScoreCursor c = (PlayerHoleScoreCursor) qb.query(db, null,
				null, null, null, null, null);
		c.moveToFirst();
		return c;
	}

	public ScoreCursor getScoreOrCreate(long roundId, long holeId, long playerId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.ROUND_ID + " = " + roundId);
		qb.appendWhere(" AND " + Score.HOLE_ID + " = " + holeId);
		qb.appendWhere(" AND " + Score.PLAYER_ID + " = " + playerId);

		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		if (c.getCount() == 0) {
			c.close();

			long newScoreId = createScore(roundId, holeId, playerId, 0, "");
			ScoreCursor c2 = getScore(newScoreId);
			c2.moveToFirst();
			return c2;
		} else {
			c.moveToFirst();
			return c;
		}
	}
	
	public ScoreCursor getScore(long roundId, long holeId, long playerId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.ROUND_ID + " = " + roundId);
		qb.appendWhere(" AND " + Score.HOLE_ID + " = " + holeId);
		qb.appendWhere(" AND " + Score.PLAYER_ID + " = " + playerId);

		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	public ScoreCursor getScore(long scoreId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score._ID + " = " + scoreId);

		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
//---LAMTT Add----
	public TempScoreCursor getTempScoreByRoundID(long temproundId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TempScoreCursor.getQueryBuilder();
		qb.appendWhere(TempScore.ROUND_ID + " = " + temproundId);

		TempScoreCursor c = (TempScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
	//---end---
	//---end---
	public ScoreCursor getScoresServer(long roundId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.ROUND_ID + " = " + roundId);
//		qb.appendWhere( " and "+Score.HOLE_SCORE + " > " + 0);
		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
	public ScoreCursor getScoresServer(long roundId, long playerId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.ROUND_ID + " = " + roundId +  " AND " + Score.PLAYER_ID + " = " + playerId);
//		qb.appendWhere( " and "+Score.HOLE_SCORE + " > " + 0);
		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
	public ScoreCursor getScores(long roundId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.ROUND_ID + " = " + roundId);
		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
	
	public ScoreCursor getScores(long roundId, long playerId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.ROUND_ID + " = " + roundId +  " AND " + Score.PLAYER_ID + " = " + playerId);

		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
	private long getHoleByTeeHoleNum(long numId,long teeId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HoleCursor.getQueryBuilder();
		qb.appendWhere(Hole.HOLE_NUMBER + " = " + numId +" and "+Hole.TEE_ID +" =" +teeId);

		HoleCursor c = (HoleCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c.getId();
	}
	public ScoreCardCursor getScoreCard(long roundId, long playerId, long teeId) {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, String> onClause = new HashMap<String, String>();
		onClause.put("SQLOnClause", Score.ROUND_ID + " = " + roundId + " AND "
				+ Score.PLAYER_ID + " = " + playerId + "  ");

		SQLiteQueryBuilder qb = ScoreCardCursor.getQueryBuilder(onClause);
		qb.appendWhere(Hole.TEE_ID + "=" + teeId);
		String orderBy = " hole_number ";
		ScoreCardCursor c = (ScoreCardCursor) qb.query(db, null, null, null,
				null, null, orderBy);
		c.moveToFirst();
		return c;
	}

	public long createScore(long roundId, long holeId, long playerId, int score, String gameScore) {
		return createScore( -1, roundId,  holeId,  playerId,  score,  gameScore);
	}
	
	public int updateGameScore(long roundId, long holeId, long playerId, int gameScore) {
		ContentValues values = new ContentValues();
		values.put(Score.GAME_SCORE, gameScore);
		values.put(Score.MODIFIED_DATE, getNow());
		
		String whereClause = Score. PLAYER_ID+ " = ?";
		whereClause +=" and "+ Score. ROUND_ID+ " = ?";
		whereClause +=" and "+ Score. HOLE_ID+ " = ?";
		String[] whereArgs = new String[] { String.valueOf(playerId),String.valueOf(roundId),String.valueOf(holeId) };
		return getWritableDatabase().update(Score.TABLE_NAME,  values,whereClause,whereArgs);
	}
	
	public int updateScoreOB(long roundId, long holeId, long playerId, long ob) {
		ContentValues values = new ContentValues();
		values.put(Score.OB, ob);
		values.put(Score.MODIFIED_DATE, getNow());
		
		String whereClause = Score. PLAYER_ID+ " = ?";
		whereClause +=" and "+ Score. ROUND_ID+ " = ?";
		whereClause +=" and "+ Score. HOLE_ID+ " = ?";
		String[] whereArgs = new String[] { String.valueOf(playerId),String.valueOf(roundId),String.valueOf(holeId) };
		return getWritableDatabase().update(Score.TABLE_NAME,  values,whereClause,whereArgs);
	}
	
	public int updateScoreWH(long roundId, long holeId, long playerId, long wh) {
		ContentValues values = new ContentValues();
		values.put(Score.WATER_HAZARD, wh);
		values.put(Score.MODIFIED_DATE, getNow());
		
		String whereClause = Score. PLAYER_ID+ " = ?";
		whereClause +=" and "+ Score. ROUND_ID+ " = ?";
		whereClause +=" and "+ Score. HOLE_ID+ " = ?";
		String[] whereArgs = new String[] { String.valueOf(playerId),String.valueOf(roundId),String.valueOf(holeId) };
		return getWritableDatabase().update(Score.TABLE_NAME,  values,whereClause,whereArgs);
	}
	
	public int updateScoreGB(long roundId, long holeId, long playerId, boolean gb) {
		ContentValues values = new ContentValues();
		values.put(Score.SAND_SHOT, gb);
		values.put(Score.MODIFIED_DATE, getNow());
		
		String whereClause = Score. PLAYER_ID+ " = ?";
		whereClause +=" and "+ Score. ROUND_ID+ " = ?";
		whereClause +=" and "+ Score. HOLE_ID+ " = ?";
		String[] whereArgs = new String[] { String.valueOf(playerId),String.valueOf(roundId),String.valueOf(holeId) };
		return getWritableDatabase().update(Score.TABLE_NAME,  values,whereClause,whereArgs);
	}
	
	public int updateScore(long roundId, long holeId, long playerId, String fairway, String fairwayClub) {
		ContentValues values = new ContentValues();
		values.put(Score.FAIRWAY, fairway);
		values.put(Score.TEE_OFF_CLUB, fairwayClub);
		values.put(Score.MODIFIED_DATE, getNow());
		
		String whereClause = Score. PLAYER_ID+ " = ?";
		whereClause +=" and "+ Score. ROUND_ID+ " = ?";
		whereClause +=" and "+ Score. HOLE_ID+ " = ?";
		String[] whereArgs = new String[] { String.valueOf(playerId),String.valueOf(roundId),String.valueOf(holeId) };
		return getWritableDatabase().update(Score.TABLE_NAME,  values,whereClause,whereArgs);
	}

    public int updateScore(long roundId, long holeId, long playerId,
                           String fairway, String fairwayClub,
                           boolean gb, long wh, long ob) {
        ContentValues values = new ContentValues();
        values.put(Score.FAIRWAY, fairway);
        values.put(Score.TEE_OFF_CLUB, fairwayClub);
        values.put(Score.MODIFIED_DATE, getNow());
        values.put(Score.SAND_SHOT, gb);
        values.put(Score.WATER_HAZARD, wh);
        values.put(Score.OB, ob);

        String whereClause = Score. PLAYER_ID+ " = ?";
        whereClause +=" and "+ Score. ROUND_ID+ " = ?";
        whereClause +=" and "+ Score. HOLE_ID+ " = ?";
        String[] whereArgs = new String[] { String.valueOf(playerId),String.valueOf(roundId),String.valueOf(holeId) };
        return getWritableDatabase().update(Score.TABLE_NAME,  values,whereClause,whereArgs);
    }
	
	public int updateScore(long roundId, long holeId, long playerId, int score, int gameScore) {
		ContentValues values = new ContentValues();
		values.put(Score.ROUND_ID, roundId);
		values.put(Score.HOLE_ID, holeId);
		values.put(Score.PLAYER_ID, playerId);
		values.put(Score.HOLE_SCORE, score);
		values.put(Score.GAME_SCORE, gameScore);
		values.put(Score.MODIFIED_DATE, getNow());

		String whereClause = Score. PLAYER_ID+ " = ?";
		whereClause +=" and "+ Score. ROUND_ID+ " = ?";
		whereClause +=" and "+ Score. HOLE_ID+ " = ?";
		String[] whereArgs = new String[] { String.valueOf(playerId),String.valueOf(roundId),String.valueOf(holeId) };
		return getWritableDatabase().update(Score.TABLE_NAME,  values,whereClause,whereArgs);
	}
	
	public long createScore(long scoreId,long roundId, long holeId, long playerId, int score, String gameScore,String fairway,String fairway_club,boolean gb,int wh,int ob,boolean puttDisabled) {
		ContentValues values = new ContentValues();
		if(scoreId != -1)
			values.put(TempScore._ID, scoreId);
		values.put(Score.ROUND_ID, roundId);
		values.put(Score.HOLE_ID, holeId);
		values.put(Score.PLAYER_ID, playerId);
		values.put(Score.HOLE_SCORE, score);
		values.put(Score.GAME_SCORE, gameScore);
		values.put(Score.FAIRWAY, fairway);
		values.put(Score.TEE_OFF_CLUB, fairway_club);
		values.put(Score.SAND_SHOT, gb);
		values.put(Score.WATER_HAZARD, wh);
		values.put(Score.OB, ob);
		values.put(Score.PUTT_DISABLED, puttDisabled);
		values.put(Score.CREATED_DATE, getNow());
		values.put(Score.MODIFIED_DATE, getNow());

		return getWritableDatabase().insert(Score.TABLE_NAME, null, values);
	}
	public long createScore(long scoreId,long roundId, long holeId, long playerId, int score, String gameScore) {
		ContentValues values = new ContentValues();
		if(scoreId != -1)
			values.put(TempScore._ID, scoreId);
		values.put(Score.ROUND_ID, roundId);
		values.put(Score.HOLE_ID, holeId);
		values.put(Score.PLAYER_ID, playerId);
		values.put(Score.HOLE_SCORE, score);
		values.put(Score.GAME_SCORE, gameScore);
		values.put(Score.CREATED_DATE, getNow());
		values.put(Score.MODIFIED_DATE, getNow());

		return getWritableDatabase().insert(Score.TABLE_NAME, null, values);
	}
	//------LAMTT create 11/04/2012-------
	public long insertRowTempScore(long scoreId,long roundId, long holeId, long playerId, int score, String gameScore) {
		ContentValues values = new ContentValues();
		values.put(TempScore._ID, scoreId);
		values.put(TempScore.ROUND_ID, roundId);
		values.put(TempScore.HOLE_ID, holeId);
		values.put(TempScore.PLAYER_ID, playerId);
		values.put(TempScore.HOLE_SCORE, score);
		values.put(TempScore.GAME_SCORE, gameScore);
		values.put(TempScore.CREATED_DATE, getNow());
		values.put(TempScore.MODIFIED_DATE, getNow());

		return getWritableDatabase().insert(TempScore.TABLE_NAME, null, values);
	}
	
	public long insertRowTempScore(long scoreId,long roundId, 
								long holeId, long playerId,
								int score, String gameScore,
								String fairway, String teeOffCLub,
								boolean sandShot, int waterHazard,
								int ob, boolean puttDisabled) {
		ContentValues values = new ContentValues();
		values.put(TempScore._ID, scoreId);
		values.put(TempScore.ROUND_ID, roundId);
		values.put(TempScore.HOLE_ID, holeId);
		values.put(TempScore.PLAYER_ID, playerId);
		values.put(TempScore.HOLE_SCORE, score);
		values.put(TempScore.GAME_SCORE, gameScore);
		values.put(TempScore.FAIRWAY, fairway);
		values.put(TempScore.TEE_OFF_CLUB, teeOffCLub);
		values.put(TempScore.SAND_SHOT, sandShot);
		values.put(TempScore.WATER_HAZARD, waterHazard);
		values.put(TempScore.OB, ob);
		values.put(TempScore.PUTT_DISABLED, puttDisabled);
		values.put(TempScore.CREATED_DATE, getNow());
		values.put(TempScore.MODIFIED_DATE, getNow());

		return getWritableDatabase().insert(TempScore.TABLE_NAME, null, values);
	}
	
	//------LAMTT create 25/07/2012-------
	public long insertRowHistoryCache(HistoryObj historyOpj) {
		ContentValues values = new ContentValues();

		values.put(HistoryCache._ID, historyOpj.getId());
		values.put(HistoryCache.CLUB_NAME, historyOpj.getClubName());
		values.put(HistoryCache.TOTAL_PUTT, historyOpj.getTotalPutt());
		values.put(HistoryCache.TOTAL_SHOT, historyOpj.getTotalShot());
		values.put(HistoryCache.PLAYDATE, historyOpj.getPlayDate());
		/*ThuNA 2013/03/06 ADD-S*/
		values.put(HistoryCache.GORA_SCORE_ID, historyOpj.getGora_score_id());
		/*ThuNA 2013/03/06 ADD-E*/
		return getWritableDatabase().insert(HistoryCache.TABLE_NAME, null, values);
	}
	public void deleteHistoryCacheByRoundID(String id) {
		
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = HistoryCache._ID + " = ?";
		String[] whereArgs = new String[] { "" + id };
		db.delete(HistoryCache.TABLE_NAME, whereClause, whereArgs);
	}
	
	public void deleteAllHistoryCache() {
		getWritableDatabase().delete(HistoryCache.TABLE_NAME, null,null);
	}
	public HistoryCacheCursor getTopDefaultHistory() {
		return getTopHistory(10);
	}
	public HistoryCacheCursor getTopHistory(int pageNum) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HistoryCacheCursor.getQueryBuilder();
		//String sortBy = " " + HistoryCache.PLAYDATE + " DESC";
		//qb.appendWhere(" " + HistoryCache.PLAYING + " = "+ false );
		String whereClause = HistoryCache.PLAYING + " = ?";
		String[] whereArgs = new String[] { "" + 0 };
		String limit= String.valueOf(0+","+pageNum);
		HistoryCacheCursor c = (HistoryCacheCursor) qb.query(db, null, whereClause, whereArgs, null, null,null,limit);
		
		c.moveToFirst();
		return c;
	}
	
	public HistoryCacheCursor getHistoryCacheByIdServer(String idServer) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HistoryCacheCursor.getQueryBuilder();
		qb.appendWhere(" " + HistoryCache._ID + "== '" + idServer + "'");
		
		HistoryCacheCursor c = (HistoryCacheCursor) qb.query(db, null, null, null, null, null,null,null);
		
		c.moveToFirst();
		return c;
	}
	
	//-----End--------
	public void updateScore(long id, int currentScore) {
		ContentValues values = new ContentValues();
		values.put(Score.HOLE_SCORE, currentScore);
		values.put(Score.MODIFIED_DATE, getNow());

		String whereClause = Score._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(Score.TABLE_NAME, values, whereClause, whereArgs);
	}
	
	public void updateScore(long id, int currentScore, boolean puttDisabled) {
		ContentValues values = new ContentValues();
		values.put(Score.HOLE_SCORE, currentScore);
		values.put(Score.MODIFIED_DATE, getNow());
		values.put(Score.PUTT_DISABLED, puttDisabled);

		String whereClause = Score._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(Score.TABLE_NAME, values, whereClause, whereArgs);
	}
	
	public void updateGameScore(long id, int gameScore) {
		ContentValues values = new ContentValues();
		values.put(Score.GAME_SCORE, gameScore);
		values.put(Score.MODIFIED_DATE, getNow());

		String whereClause = Score._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(Score.TABLE_NAME, values, whereClause,
				whereArgs);
	}

	public void updateHole(long id, Integer yard, Integer par) {
		ContentValues values = new ContentValues();
		values.put(Hole.YARD, yard);
//		if (GolfApplication.isOwnerMale()) {
//			values.put(Hole.PAR, par);
//		} else {
//			values.put(Hole.WOMEN_PAR, par);
//		}

        values.put(Hole.WOMEN_PAR, par);
		values.put(Hole.MODIFIED_DATE, getNow());

		String whereClause = Hole._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(Hole.TABLE_NAME, values, whereClause,
				whereArgs);
	}

	/**
	 * スコアをプラス1します # 上限を超える数は加算できないようにします。
	 */
	public void plusScore(long scoreId) {
		ScoreCursor scoreCursor = getScore(scoreId);

		// update score
		int currentScore = scoreCursor.getHoleScore() + 1;
		if (currentScore <= MAX_SCORE) {
			scoreId = scoreCursor.getId();
			updateScore(scoreId, currentScore);

			createScoreDetail(scoreId, currentScore);
		}
		scoreCursor.close();
	}

	/**
	 * 位置情報つきで、スコアをプラス1します # 上限を超える数は加算できないようにします。
	 */
	public void plusScore(long scoreId, double latitude, double longitude) {
		ScoreCursor scoreCursor = getScore(scoreId);

		// update score
		int currentScore = scoreCursor.getHoleScore() + 1;
		if (currentScore <= MAX_SCORE) {
			scoreId = scoreCursor.getId();
			updateScore(scoreId, currentScore);

			createScoreDetail(scoreId, currentScore, latitude, longitude);
		}
		scoreCursor.close();
	}

	public void plusScoreWithClub(long scoreId, double latitude,
			double longitude, String club) {
		ScoreCursor scoreCursor = getScore(scoreId);

		// update score
		int currentScore = scoreCursor.getHoleScore() + 1;
		if (currentScore <= MAX_SCORE) {
			scoreId = scoreCursor.getId();
			updateScore(scoreId, currentScore);
			createScoreDetail(scoreId, currentScore, latitude, longitude, club);
		}
		scoreCursor.close();
	}

	public void plusScoreWithClub(long scoreId, String club) {
		ScoreCursor scoreCursor = getScore(scoreId);

		int currentScore = scoreCursor.getHoleScore() + 1;
		if (currentScore <= MAX_SCORE) {
			scoreId = scoreCursor.getId();
			if(club.equals(Constant.PUTT)) {
				updateScore(scoreId, currentScore, false);
			}else{
				updateScore(scoreId, currentScore);
			}
			createScoreDetail(scoreId, currentScore, club);
		}
		scoreCursor.close();
	}

	public void plusScoreWithClubAndResult(long scoreId, double latitude,
			double longitude, String club, String result) {
		ScoreCursor scoreCursor = getScore(scoreId);

		// update score
		int currentScore = scoreCursor.getHoleScore() + 1;
		if (currentScore <= MAX_SCORE) {
			scoreId = scoreCursor.getId();
			updateScore(scoreId, currentScore);
			createScoreDetail(scoreId, currentScore, latitude, longitude, club,
					result);
		}
		scoreCursor.close();
	}

	public void plusScoreWithClubAndResult(long scoreId, String club,
			String result) {
		ScoreCursor scoreCursor = getScore(scoreId);

		// update score
		int currentScore = scoreCursor.getHoleScore() + 1;
		if (currentScore <= MAX_SCORE) {
			scoreId = scoreCursor.getId();
			updateScore(scoreId, currentScore);
			createScoreDetail(scoreId, currentScore, club, result);
		}
		scoreCursor.close();
	}

	// 任意のShotNumberのScoreDetailデータの新規追加
	public Boolean insertScoreDetail(long scoreId, int shotNo, String club,
			String result) {
		Boolean ret = false;
		ScoreCursor scoreCursor = getScore(scoreId);

		// update score
		int currentScore = scoreCursor.getHoleScore() + 1;
		if (currentScore <= MAX_SCORE) {
			scoreId = scoreCursor.getId();
			updateScore(scoreId, currentScore); // ホールスコアの更新

			ScoreDetailCursor sdcs = getScoreDetailsByScoreId(scoreId);
			while (!sdcs.isAfterLast()) {
				if (sdcs.getShotNumber() >= shotNo) {
					updateShotNumber(sdcs.getId(), sdcs.getShotNumber() + 1);
				}
				sdcs.moveToNext();
			}
			sdcs.close();

			createScoreDetail(scoreId, shotNo, club, result); // ScoreDetailデータの新規追加
			ret = true;
		}
		scoreCursor.close();
		return ret;
	}

	/**
	 * スコアをマイナス1します。 0未満にはしません。
	 */
	public long minusScore(long scoreId) {
		ScoreCursor scoreCursor = getScore(scoreId);

		int currentScore;
		if (scoreCursor.getHoleScore() == 0) {
			currentScore = 0;
		} else {
			currentScore = scoreCursor.getHoleScore() - 1;
			deleteScoreDetail(scoreId, currentScore + 1);
			scoreId = scoreCursor.getId();
			updateScore(scoreId, currentScore);
		}

		scoreCursor.close();
		return currentScore;
	}

	private void deleteScoreDetail(long scoreId, int shotNumber) {
		String whereClause = ScoreDetail.SCORE_ID + " = ?" + " AND "
				+ ScoreDetail.SHOT_NUMBER + " = ?" + "";
		String[] whereArgs = new String[] { "" + scoreId, "" + shotNumber };
		getWritableDatabase().delete(ScoreDetail.TABLE_NAME, whereClause,
				whereArgs);
	}

	public void deleteScoreDetail(long scoreDetailId) {
		String whereClause = ScoreDetail._ID + " = ?";
		String[] whereArgs = new String[] { "" + scoreDetailId };
		getWritableDatabase().delete(ScoreDetail.TABLE_NAME, whereClause,
				whereArgs);
	}

	public void deleteScoreDetails(long scoreId) {
		String whereClause = ScoreDetail.SCORE_ID + " = ?";
		String[] whereArgs = new String[] { "" + scoreId };
		getWritableDatabase().delete(ScoreDetail.TABLE_NAME, whereClause,
				whereArgs);
	}

	public ScoreDetailCursor getScoreDetailsByScoreId(long scoreId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreDetailCursor.getQueryBuilder();
		qb.appendWhere(ScoreDetail.SCORE_ID + " = " + scoreId);

		ScoreDetailCursor c = (ScoreDetailCursor) qb.query(db, null, null,
				null, null, null, ScoreDetail.SHOT_NUMBER);
		c.moveToFirst();
		return c;
	}
	//------LAMTT-----
	public int getPuttScoreDetailByScoreId(long scoreId) {
		SQLiteDatabase db = getReadableDatabase();
		String sqlStr="select count(*) as countputt from "+ScoreDetail.TABLE_NAME +" dt inner join "+Score.TABLE_NAME+" s  on s."+ Score._ID+"=dt."+ScoreDetail.SCORE_ID+"    where   s."+Score.PLAYER_ID +"=1 and  s."+Score.ROUND_ID +"="+scoreId +" and dt."+ScoreDetail.CLUB +" like '%"+ Constant.PUTT+"%'";
		Cursor c = (Cursor) db.rawQuery(sqlStr, null);
		//c.moveToFirst();
		if(c.moveToFirst()){
			int result=c.getInt(0);
			c.close();
			return result;
		}
		c.close();
		return 0;
	}
	public TempScoreDetailCursor getTempScoreDetailsByScoreId(long Id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TempScoreDetailCursor.getQueryBuilder();
		qb.appendWhere(TempScoreDetail.SCORE_ID + " = " + Id);

		TempScoreDetailCursor c = (TempScoreDetailCursor) qb.query(db, null, null,
				null, null, null, TempScoreDetail.SHOT_NUMBER);
		c.moveToFirst();
		return c;
	}
	//------------
	public long createScoreDetail(long scoreId, int shotNumber) {
		return createScoreDetail(scoreId, shotNumber, LATITUDE_NULL,
				LONGITUDE_NULL);
	}
	
// ---END
	// public long createScoreDetail(long scoreId, int shotType, int shotNumber)
	// {
	// return createScoreDetail(scoreId, shotType, shotNumber, LATITUDE_NULL,
	// LONGITUDE_NULL);
	// }

	public long updateScoreDetails(long scoreId,  int shotNumber,
			double latitude, double longitude, String club, String result) {
		ContentValues scoreDetailValues = new ContentValues();
		if (club != null && club.length() > 0) {
			scoreDetailValues.put(ScoreDetail.CLUB, club);
		}
		if (result != null && result.length() > 0) {
			scoreDetailValues.put(ScoreDetail.SHOT_RESULT, result);
		}
		if (latitude != LATITUDE_NULL && longitude != LONGITUDE_NULL) {
			scoreDetailValues.put(ScoreDetail.GPS_LATITUDE, latitude);
			scoreDetailValues.put(ScoreDetail.GPS_LONGITUDE, longitude);
		}
		scoreDetailValues.put(ScoreDetail.CREATED_DATE, getNow());
		scoreDetailValues.put(ScoreDetail.MODIFIED_DATE, getNow());
		String whereClause = ScoreDetail.SCORE_ID + " = ?";
		whereClause +=" and "+ ScoreDetail. SHOT_NUMBER+ " = ?";
		String[] whereArgs = new String[] { String.valueOf(scoreId),String.valueOf(shotNumber) };
		return getWritableDatabase().update(ScoreDetail.TABLE_NAME,  scoreDetailValues ,whereClause,whereArgs);
	}
	public long createScoreDetail(long scoreId, int shotNumber,
			double latitude, double longitude) {
		return createScoreDetail(scoreId, Integer.MIN_VALUE, shotNumber,
				latitude, longitude);
	}

	public long createScoreDetail(long scoreId, int shotType, int shotNumber,
			double latitude, double longitude) {
		return createScoreDetail(scoreId, shotType, shotNumber, latitude,
				longitude, null, null);
	}

	public long createScoreDetail(long scoreId, int shotNumber,
			double latitude, double longitude, String club) {
		return createScoreDetail(scoreId, Integer.MIN_VALUE, shotNumber,
				latitude, longitude, club, null);
	}

	public long createScoreDetail(long scoreId, int shotNumber, String club) {
		return createScoreDetail(scoreId, Integer.MIN_VALUE, shotNumber,
				LATITUDE_NULL, LONGITUDE_NULL, club, null);
	}

	public long createScoreDetail(long scoreId, int shotNumber,
			double latitude, double longitude, String club, String result) {
		return createScoreDetail(scoreId, Integer.MIN_VALUE, shotNumber,
				latitude, longitude, club, result);
	}

	public long createScoreDetail(long scoreId, int shotNumber, String club,
			String result) {
		return createScoreDetail(scoreId, Integer.MIN_VALUE, shotNumber,
				LATITUDE_NULL, LONGITUDE_NULL, club, result);
	}
	public long createScoreDetail(long scoreId, int shotType, int shotNumber,
			double latitude, double longitude, String club, String result) {
		return createScoreDetail(-1, scoreId,  shotType,  shotNumber,latitude,  longitude,  club,  result);
	}
	
	
	
	
	public long createScoreDetail(long scoreDetailId,long scoreId, int shotType, int shotNumber,
			double latitude, double longitude, String club, String result) {
		ContentValues scoreDetailValues = new ContentValues();
		if(scoreDetailId!=-1)
			scoreDetailValues.put(ScoreDetail._ID, scoreDetailId);
		scoreDetailValues.put(ScoreDetail.SCORE_ID, scoreId);
		// if (shotType != Integer.MIN_VALUE) {
		// scoreDetailValues.put(ScoreDetail.SHOT_TYPE, shotType);
		// }
		if (club != null && club.length() > 0) {
			scoreDetailValues.put(ScoreDetail.CLUB, club);
		}
		if (result != null && result.length() > 0) {
			scoreDetailValues.put(ScoreDetail.SHOT_RESULT, result);
		}
		scoreDetailValues.put(ScoreDetail.SHOT_NUMBER, shotNumber);
		if (latitude != LATITUDE_NULL && longitude != LONGITUDE_NULL) {
			scoreDetailValues.put(ScoreDetail.GPS_LATITUDE, latitude);
			scoreDetailValues.put(ScoreDetail.GPS_LONGITUDE, longitude);
		}
		scoreDetailValues.put(ScoreDetail.CREATED_DATE, getNow());
		scoreDetailValues.put(ScoreDetail.MODIFIED_DATE, getNow());

		return getWritableDatabase().insert(ScoreDetail.TABLE_NAME, null,
				scoreDetailValues);
	}
	// --------lamtt add-----------
	public long insertRowTempScoreDetail(long scoreDetailId, long scoreId, int shotNumber, double latitude, double longitude, String club, String result) {
		ContentValues scoreDetailValues = new ContentValues();
		scoreDetailValues.put(TempScoreDetail._ID, scoreDetailId);
		scoreDetailValues.put(TempScoreDetail.SCORE_ID, scoreId);
		scoreDetailValues.put(TempScoreDetail.CLUB, club);
		scoreDetailValues.put(TempScoreDetail.SHOT_RESULT, result);
		scoreDetailValues.put(TempScoreDetail.SHOT_NUMBER, shotNumber);
		scoreDetailValues.put(TempScoreDetail.GPS_LATITUDE, latitude);
		scoreDetailValues.put(TempScoreDetail.GPS_LONGITUDE, longitude);
		scoreDetailValues.put(TempScoreDetail.CREATED_DATE, getNow());
		scoreDetailValues.put(TempScoreDetail.MODIFIED_DATE, getNow());
		return getWritableDatabase().insert(TempScoreDetail.TABLE_NAME, null,
				scoreDetailValues);
	}
	//-----end---------
	
	//ngant
	public RoundCursor getRound(String ygolfId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		qb.appendWhere(" and r1." + Round.ROUND_DELETE + " =  "+ 0 );
		String selection = " r1." + Round.YOURGOLF_ID + " = ?";
		String[] selectionArgs = { "" + ygolfId };

		RoundCursor c = (RoundCursor) qb.query(db, null, selection, selectionArgs, null, null, null);
		c.moveToFirst();
		return c;
	}
	
	public void updateRoundPlayDate(String roundId, long playDate) {
		ContentValues values = new ContentValues();		
		values.put(Round.CREATED_DATE, playDate);
		values.put(Round.MODIFIED_DATE, getNow());
		
		String whereClause = Round.YOURGOLF_ID + " = ?";
		String[] whereArgs = new String[] { roundId };

		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
				whereArgs);
	}

    public void updateRoundWeather(String roundId, String weather) {
        ContentValues values = new ContentValues();
        values.put(Round.WEATHER, weather);
        values.put(Round.MODIFIED_DATE, getNow());

        String whereClause = Round.YOURGOLF_ID + " = ?";
        String[] whereArgs = new String[] { roundId };

        getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
                whereArgs);
    }
	
	public void updateRoundMemo(String roundId, String memo) {
		ContentValues values = new ContentValues();		
		values.put(Round.MEMO, memo);
		values.put(Round.MODIFIED_DATE, getNow());
		
		String whereClause = Round.YOURGOLF_ID + " = ?";
		String[] whereArgs = new String[] { roundId };

		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	public void updateHistoryCachePlaydate(String yGolfId, long playDate)
	{
		ContentValues values = new ContentValues();		
		values.put(HistoryCache.PLAYDATE, playDate);
		
		String whereClause = HistoryCache._ID + " = ?";
		String[] whereArgs = new String[] { yGolfId };

		getWritableDatabase().update(HistoryCache.TABLE_NAME, values, whereClause,
				whereArgs);
		
	}
	public void updateHistoryCacheMemo(String yGolfId, String memo)
	{
		ContentValues values = new ContentValues();		
		values.put(HistoryCache.MEMO, memo);
		
		String whereClause = HistoryCache._ID + " = ?";
		String[] whereArgs = new String[] { yGolfId };

		getWritableDatabase().update(HistoryCache.TABLE_NAME, values, whereClause,
				whereArgs);
		
	}
	
	//end ngant
	
	public RoundCursor getRounds() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		qb.appendWhere(" and r1." + Round.ROUND_DELETE + " =   "+ 0 );
		qb.appendWhere(" and r1." + Round.PLAYING + " =   "+ 0 );
		String sortBy = " r1." + Round.CREATED_DATE + " DESC";
		RoundCursor c = (RoundCursor) qb.query(db, null, null, null, null,
				null, sortBy);
		c.moveToFirst();
		return c;
	}
	
	public RoundCursor getRoundsNotPlaying() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		qb.appendWhere(" and r1." + Round.ROUND_DELETE + " =   "+ 0 );
		qb.appendWhere(" and r1." + Round.PLAYING + " =   "+ 0 );
		String sortBy = " r1." + Round.CREATED_DATE + " DESC";
		RoundCursor c = (RoundCursor) qb.query(db, null, null, null, null,
				null, sortBy);
		c.moveToFirst();
		return c;
	}
	public boolean getRoundsNotUpdate() {
		SQLiteDatabase db = getReadableDatabase();
		String sqlStr=" select count(_id) from " +Round.TABLE_NAME +" where "+Round.ROUND_DELETE+"=0 and "+Round.PLAYING+"= 0 and  ("+Round.YOURGOLF_ID +" = '' or "+Round.YOURGOLF_ID +" IS NULL)";
		Cursor c = (Cursor) db.rawQuery(sqlStr, null);
		if(c.moveToFirst())
		{
			if(c.getInt(0)>0)
			{
				c.close();
				return true;
			}
		}
		c.close();
		return false;
	}
	public int getTotalRounds() {
		SQLiteDatabase db = getReadableDatabase();
		String sqlStr=" select count(_id) from " +Round.TABLE_NAME +" where "+Round.ROUND_DELETE +"= 0";
		Cursor c = (Cursor) db.rawQuery(sqlStr, null);
		if(c.moveToFirst())
			return c.getInt(0);
		return 0;
	}
	
	//LAMTT ADD 
	public RoundCursor getRoundsHidtory() {
		SQLiteDatabase db = getReadableDatabase();
		String sqlStr="select distinct r."+Round._ID+" as roundId from ";
		sqlStr +=  Round.TABLE_NAME +" r left join "+Score.TABLE_NAME+" s  on s."+ Score.ROUND_ID+"=r."+Round._ID;
		sqlStr +=" left join "+Player.TABLE_NAME +" l on l._id=s."+Score.PLAYER_ID+"   where s."+Score.HOLE_SCORE +">0 ";
		sqlStr +=" and r." + Round.ROUND_DELETE + " =  0 and l."+Player.OWNNER_FLAG +"=1 and r."+Round.PLAYING +"=0";
		
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		 Cursor score = (Cursor) db.rawQuery(sqlStr, null);
		 if(score.getCount() >0){
			 score.moveToPosition(0);
			qb.appendWhere(" and (r1." + Round._ID+" = "+score.getLong(0));
			for(int i=0;i<score.getCount();i++)
			{
				score.moveToPosition(i);
				qb.appendWhere(" or r1." + Round._ID+" = "+score.getLong(0) +(score.getCount()-i==1?")":""));
			}
			score.close();
			String sortBy = " r1." + Round.CREATED_DATE + " DESC";
			return  (RoundCursor) qb.query(db, null, null, null, null,null, sortBy);
		}
		return null;
		
		
	}
	public RoundCursor getRoundsPlayed(int rowCount) {
		return getRoundsPlayed(0,rowCount);
	}
	public RoundCursor getRoundsPlayed(int start,int rowCount) {
		SQLiteDatabase db = getReadableDatabase();

		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		
		qb.appendWhere(" and r1." + Round.PLAYING + " =  "+ 0 +" and r1." + Round.ROUND_DELETE + " =  "+ 0 );
		
		String sortBy = " r1." + Round.CREATED_DATE +" DESC , r1."+Round.MODIFIED_DATE+ " DESC";
		
		String limit= String.valueOf(start+","+rowCount);
		RoundCursor c = (RoundCursor) qb.query(db, null, null,null, null, null, sortBy,limit);
		
		c.moveToFirst();
		return c;
	}
	
	public RoundCursor getRoundsByPeriod(long ms) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		
		qb.appendWhere(" AND r1." + Round.CREATED_DATE + " >= " + ms);
		qb.appendWhere(" AND r1." + Round.ROUND_DELETE + " = " + 0);
		qb.appendWhere(" AND r1." + Round.PLAYING + " = " + 0);
		String sortBy = " r1." + Round.CREATED_DATE + " DESC";
		
		RoundCursor c = (RoundCursor) qb.query(db, null, null,
				null, null, null, sortBy);
		c.moveToFirst();
		return c;
	}
	public RoundCursor getRoundsHoleByCourseID(long id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		String selection = " r1." + Round.COURSE_ID + " = ?";
		String[] selectionArgs = { "" + id };
		
		RoundCursor c = (RoundCursor) qb.query(db, null, selection,
				selectionArgs, null, null, null);
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getRoundByServerID(String id) {
		
		SQLiteDatabase db = getReadableDatabase();
		String sqlStr="select _id as id, hole as hole, hole_count as hole_count from "+Round.TABLE_NAME +" where "+ Round.ROUND_DELETE + " =  "+ 0 +" and " + Round.YOURGOLF_ID  + " = '"+id.trim()+"'";
		Cursor c = (Cursor) db.rawQuery(sqlStr, null);
		c.moveToFirst();
		return c;
	}
	public RoundCursor getRoundsCusorByServerID(String id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		qb.appendWhere(" and r1." + Round.ROUND_DELETE + " =  "+ 0 );
		String selection = " r1." + Round.YOURGOLF_ID +  " ='"+id.trim()+"'";
		//String[] selectionArgs = { "" + id };
		RoundCursor c = (RoundCursor) qb.query(db, null, selection, null, null, null, null);
		c.moveToFirst();
		return c;
		
	}
	public RoundCursor getRoundsCusorByServerID2(String id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder2();
		qb.appendWhere(" r1." + Round.ROUND_DELETE + " =  "+ 0 );
		String selection = " r1." + Round.YOURGOLF_ID +  " ='"+id.trim()+"'";
		//String[] selectionArgs = { "" + id };
		RoundCursor c = (RoundCursor) qb.query(db, null, selection, null, null, null, null);
		c.moveToFirst();
		return c;
		
	}

//	public TraceCursor getTraces() {
//		SQLiteDatabase db = getReadableDatabase();
//		SQLiteQueryBuilder qb = TraceCursor.getQueryBuilder();
//
//		TraceCursor c = (TraceCursor) qb.query(db, null, null, null, null,
//				null, null);
//		c.moveToFirst();
//		return c;
//	}

	public RoundCursor getRound(long id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		qb.appendWhere(" and r1." + Round.ROUND_DELETE + " =  "+ 0 );
		String selection = " r1." + Round._ID + " = ?";
		String[] selectionArgs = { "" + id };

		RoundCursor c = (RoundCursor) qb.query(db, null, selection, selectionArgs, null, null, null);
		c.moveToFirst();
		return c;
	}

	// LAMTT
	public RoundCursor getRoundByPlaying(boolean playing) {
		RoundCursor c=null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
			qb.appendWhere(" and r1." + Round.ROUND_DELETE + " =  "+ 0 );
			String selection = " r1." + Round.PLAYING + " = ?";
			String[] selectionArgs = { "" + (playing==true?1:0) };
			c = (RoundCursor) qb.query(db, null, selection,selectionArgs, null, null, null);
			
			c.moveToFirst();
			return c;
		} catch (Exception e) {
		}
		return null;
		
	}
	
	// LAMTT
	public RoundCursor getRoundByPlayingNotDelete(boolean playing) {
		RoundCursor c=null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
			String selection = " r1." + Round.PLAYING + " = ?";
			String[] selectionArgs = { "" + (playing==true?1:0) };
			c = (RoundCursor) qb.query(db, null, selection,selectionArgs, null, null, null);
			
			c.moveToFirst();
			return c;
		} catch (Exception e) {
		}
		return null;
		
	}
	
	public boolean findRoundByPlaying(boolean playing) {
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		String selection = "r1." + Round.PLAYING + " = ? ";
		String[] selectionArgs = { ""+(playing==true?1:0) };
		Cursor result = (Cursor) qb.query(db, null, selection,selectionArgs, null, null, null);
		result.moveToFirst();
		if(result.getCount()>0){
			result.close();
			return true;
		}
		result.close();
		return false;
		
	}
	
	public void updateScoreDetail(long id, String selectedClub,
			String selectedShotResult) {
		ContentValues values = new ContentValues();
		values.put(ScoreDetail.CLUB, selectedClub);
		values.put(ScoreDetail.SHOT_RESULT, selectedShotResult);
		values.put(ScoreDetail.MODIFIED_DATE, getNow());

		String whereClause = ScoreDetail._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(ScoreDetail.TABLE_NAME, values,
				whereClause, whereArgs);
	}

	public void updateShotLocation(long id, double lat, double lon) {
		ContentValues values = new ContentValues();
		values.put(ScoreDetail.GPS_LATITUDE, lat);
		values.put(ScoreDetail.GPS_LONGITUDE, lon);
		values.put(ScoreDetail.MODIFIED_DATE, getNow());

		String whereClause = ScoreDetail._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(ScoreDetail.TABLE_NAME, values,
				whereClause, whereArgs);
	}

	public void updateShotNumber(long id, int shotNo) {
		ContentValues values = new ContentValues();
		values.put(ScoreDetail.SHOT_NUMBER, shotNo);
		values.put(ScoreDetail.MODIFIED_DATE, getNow());

		String whereClause = ScoreDetail._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(ScoreDetail.TABLE_NAME, values,
				whereClause, whereArgs);
	}

	
	public void insertAPIPlayer(ArrayList<PlayerObj> playerFromJson,long roundId,long teeID) {
		
		for(int i=0;i<playerFromJson.size();i++)
		{
			long playerId = 0;
			
			PlayerCursor playerCursor = getPlayerByServerId(playerFromJson.get(i).getIdServer());
			if(null != playerCursor && playerCursor.getCount() > 0){
				ContentValues values = new ContentValues();
				values.put(Player.NAME, playerFromJson.get(i).getName());
				values.put(Player.SERVER_ID, playerFromJson.get(i).getIdServer());
				values.put(Player.OWNNER_FLAG, playerFromJson.get(i).getOwnner_flag());
				values.put(Player.CREATED_DATE, getNow());
				values.put(Player.MODIFIED_DATE, getNow());
				
				
				String whereClause = Player. SERVER_ID+ " = ?";
				String[] whereArgs = new String[] { playerFromJson.get(i).getIdServer() };
				
				getWritableDatabase().update(Player.TABLE_NAME, values,
						whereClause, whereArgs);
				
				playerId = playerCursor.getId();
			}
			else if(playerFromJson.get(i).getOwnner_flag() == 1) {
				playerCursor = getOwner();
				
				ContentValues values = new ContentValues();
				values.put(Player.NAME, playerFromJson.get(i).getName());
				values.put(Player.SERVER_ID, playerFromJson.get(i).getIdServer());
				values.put(Player.OWNNER_FLAG, playerFromJson.get(i).getOwnner_flag());
				values.put(Player.CREATED_DATE, getNow());
				values.put(Player.MODIFIED_DATE, getNow());
				
				
				String whereClause = Player._ID + " = " + playerCursor.getId();
				
				getWritableDatabase().update(Player.TABLE_NAME, values,
						whereClause, null);
				
				playerId = playerCursor.getId();
			}
			else {
				
				ContentValues values = new ContentValues();
				values.put(Player.NAME, playerFromJson.get(i).getName());
				values.put(Player.SERVER_ID, playerFromJson.get(i).getIdServer());
				values.put(Player.OWNNER_FLAG, playerFromJson.get(i).getOwnner_flag());
				values.put(Player.CREATED_DATE, getNow());
				values.put(Player.MODIFIED_DATE, getNow());
				values.put(Player.DEL_FLAG, 0);
				
				playerId = getWritableDatabase().insert(Player.TABLE_NAME, null,
						values);
			}
			
			deleteRoundPlayer(playerId, roundId);
			// insert RoundPlayer
			float playerHdcp = Constant.DEFAULT_HDCP_VALUE;
			try {
				playerHdcp = Float.parseFloat(playerFromJson.get(i).getPlayerHdcp());
			} catch (Exception e) {
				playerHdcp = Constant.DEFAULT_HDCP_VALUE;
			}
            int playerGoal;
            try {
                playerGoal = Integer.parseInt(playerFromJson.get(i).getPlayerGoal());
            } catch (Exception e) {
                playerGoal = Constant.DEFAULT_GOAL_VALUE;
            }

            String livePlayerId = "";
            if(playerFromJson.get(i).getLivePlayerId() != null
                    && !playerFromJson.get(i).getLivePlayerId().equals("")
                    && playerFromJson.get(i).getLivePlayerId().length() > 0){
                livePlayerId = playerFromJson.get(i).getLivePlayerId();
            }

			ContentValues values = new ContentValues();
			values.put(RoundPlayer.ROUND_ID, roundId);
			values.put(RoundPlayer.PLAYER_ID, playerId);
			values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
            values.put(RoundPlayer.LIVE_PLAYER_ID, livePlayerId);
			values.put(RoundPlayer.CREATED_DATE, getNow());
			values.put(RoundPlayer.MODIFIED_DATE, getNow());
            values.put(RoundPlayer.PLAYER_GOAL, String.valueOf(playerGoal));
			getWritableDatabase().insert(RoundPlayer.TABLE_NAME, null, values);
			
			ArrayList<ScoreObj> scoreObj= playerFromJson.get(i).getArrScoreObj();
			if(null!=scoreObj)
			{
				for(int s=0;s<scoreObj.size();s++)
				{
					long soreId=createScore(-1,roundId,getHoleByTeeHoleNum(scoreObj.get(s).getHoleNumber(),teeID),
											playerId,
											scoreObj.get(s).getHoleScore(),
											scoreObj.get(s).getGameScore(),
											scoreObj.get(s).getFairway(),
											scoreObj.get(s).getTeeOffClub(),
											scoreObj.get(s).getSandShot() == 1 ? true : false,
											scoreObj.get(s).getWaterHazard(),
											scoreObj.get(s).getOb(),
											scoreObj.get(s).isPuttDisabled());
					
					ArrayList<ScoreDetailObj> scoreDetailObj=scoreObj.get(s).getArrScoreDetailObj();
					if(null!=scoreDetailObj)
					{
						for(int sd=0;sd<scoreDetailObj.size();sd++){
							createScoreDetail(soreId,
									scoreDetailObj.get(sd).getShotNumber(),
									scoreDetailObj.get(sd).getLat(),
									scoreDetailObj.get(sd).getLng(),
									scoreDetailObj.get(sd).getClub(),
									String.valueOf(scoreDetailObj.get(sd).getShotResult()));
							
						}
					}
				}
			}
		}
	}
	
	public void UpdateAPIPlayer(ArrayList<PlayerObj> playerFromJson,long roundId,long teeID) {
		
		ScoreCursor scoreCursor = getScores(roundId);
		if(null != scoreCursor && scoreCursor.getCount() > 0) {
			for (int ii = 0; ii < scoreCursor.getCount(); ii++) {
				scoreCursor.moveToPosition(ii);
				deleteScoreDetails(scoreCursor.getId());
			}
		}
		// Delete score by id
		deleteScoreByRoundID(roundId);
		
		for(int i=0;i<playerFromJson.size();i++)
		{
			long playerId = 0;
			PlayerCursor playerCursor = getPlayerByServerId(playerFromJson.get(i).getIdServer());
			if(null != playerCursor && playerCursor.getCount() > 0){
				ContentValues values = new ContentValues();
				values.put(Player.NAME, playerFromJson.get(i).getName());
				values.put(Player.SERVER_ID, playerFromJson.get(i).getIdServer());
				values.put(Player.OWNNER_FLAG, playerFromJson.get(i).getOwnner_flag());
				values.put(Player.CREATED_DATE, getNow());
				values.put(Player.MODIFIED_DATE, getNow());
				
				
				String whereClause = Player. SERVER_ID+ " = ?";
				String[] whereArgs = new String[] { playerFromJson.get(i).getIdServer() };
				
				getWritableDatabase().update(Player.TABLE_NAME, values,
						whereClause, whereArgs);
				
				playerId = playerCursor.getId();
			}
			else if(playerFromJson.get(i).getOwnner_flag() == 1) {
				playerCursor = getOwner();
				
				ContentValues values = new ContentValues();
				values.put(Player.NAME, playerFromJson.get(i).getName());
				values.put(Player.SERVER_ID, playerFromJson.get(i).getIdServer());
				values.put(Player.OWNNER_FLAG, playerFromJson.get(i).getOwnner_flag());
				values.put(Player.CREATED_DATE, getNow());
				values.put(Player.MODIFIED_DATE, getNow());
				
				
				String whereClause = Player._ID + " = " + playerCursor.getId();
				
				getWritableDatabase().update(Player.TABLE_NAME, values,
						whereClause, null);
				
				playerId = playerCursor.getId();
			}
			else {
				
				ContentValues values = new ContentValues();
				values.put(Player.NAME, playerFromJson.get(i).getName());
				values.put(Player.SERVER_ID, playerFromJson.get(i).getIdServer());
				values.put(Player.OWNNER_FLAG, playerFromJson.get(i).getOwnner_flag());
				values.put(Player.CREATED_DATE, getNow());
				values.put(Player.MODIFIED_DATE, getNow());
				values.put(Player.DEL_FLAG, 0);
				
				playerId = getWritableDatabase().insert(Player.TABLE_NAME, null,
						values);
			}
			
			deleteRoundPlayer(playerId, roundId);
			// insert RoundPlayer
			float playerHdcp;
			try {
				playerHdcp = Float.parseFloat(playerFromJson.get(i).getPlayerHdcp());
			} catch (Exception e) {
				playerHdcp = Constant.DEFAULT_HDCP_VALUE;
			}
            int playerGoal;
            try {
                playerGoal = Integer.parseInt(playerFromJson.get(i).getPlayerGoal());
            } catch (Exception e) {
                playerGoal = Constant.DEFAULT_GOAL_VALUE;
            }
            String livePlayerId = "";
            if(playerFromJson.get(i).getLivePlayerId() != null
                    && !playerFromJson.get(i).getLivePlayerId().equals("")
                    && playerFromJson.get(i).getLivePlayerId().length() > 0){
                livePlayerId = playerFromJson.get(i).getLivePlayerId();
            }
			ContentValues values = new ContentValues();
			values.put(RoundPlayer.ROUND_ID, roundId);
			values.put(RoundPlayer.PLAYER_ID, playerId);
			values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
            values.put(RoundPlayer.LIVE_PLAYER_ID, livePlayerId);
			values.put(RoundPlayer.CREATED_DATE, getNow());
			values.put(RoundPlayer.MODIFIED_DATE, getNow());
            values.put(RoundPlayer.PLAYER_GOAL, String.valueOf(playerGoal));
			getWritableDatabase().insert(RoundPlayer.TABLE_NAME, null, values);
			
			ArrayList<ScoreObj> scoreObj= playerFromJson.get(i).getArrScoreObj();
			if( null != scoreObj && playerId > 0)
			{
				
				for(int s=0;s<scoreObj.size();s++)
				{
					
					
					long soreId = 0;
					
					/******     **********/
					soreId=createScore(-1,roundId,
											getHoleByTeeHoleNum(scoreObj.get(s).getHoleNumber(),teeID),
											playerId,
											scoreObj.get(s).getHoleScore(),
											scoreObj.get(s).getGameScore(),
											scoreObj.get(s).getFairway(),
											scoreObj.get(s).getTeeOffClub(),
											scoreObj.get(s).getSandShot() == 1 ? true : false,
											scoreObj.get(s).getWaterHazard(),
											scoreObj.get(s).getOb(),
											scoreObj.get(s).isPuttDisabled());
					/******     ************/
					
//					int rowScoreEffect = updateScore(roundId,getHoleByTeeHoleNum(scoreObj.get(s).getHoleNumber(),teeID),playerId,scoreObj.get(s).getHoleScore(),scoreObj.get(s).getGameScore());
					
//					if(rowScoreEffect > 0){
//						ScoreCursor scoreCursor = getScore(roundId, getHoleByTeeHoleNum(scoreObj.get(s).getHoleNumber(),teeID), playerId);
//						if(null != scoreCursor && scoreCursor.getCount() > 0){
//							soreId = scoreCursor.getId();
//						}
//					}
					
					ArrayList<ScoreDetailObj> scoreDetailObj=scoreObj.get(s).getArrScoreDetailObj();
					if(null!=scoreDetailObj && soreId > 0)
					{
						for(int sd=0;sd<scoreDetailObj.size();sd++){
							
//							updateScoreDetails(  soreId,
//									scoreDetailObj.get(sd).getShotNumber(),
//									scoreDetailObj.get(sd).getLat(),
//									scoreDetailObj.get(sd).getLng(),
//									scoreDetailObj.get(sd).getClub(),
//									String.valueOf(scoreDetailObj.get(sd).getShotResult()));
							createScoreDetail(soreId,
									scoreDetailObj.get(sd).getShotNumber(),
									scoreDetailObj.get(sd).getLat(),
									scoreDetailObj.get(sd).getLng(),
									scoreDetailObj.get(sd).getClub(),
									String.valueOf(scoreDetailObj.get(sd).getShotResult()));
						}
					}
				}
			}
		}
		// /Log.d(TAG, "ID:" + rowId);
	}
	
	public long insertPlayer(String name) {
		long rowId = 0;
		SQLiteDatabase db = getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(Player.NAME, name);
		values.put(Player.OWNNER_FLAG, FLG_NOT_OWNER);
		values.put(Player.CREATED_DATE, getNow());
		values.put(Player.MODIFIED_DATE, getNow());
		values.put(Player.DEL_FLAG, 0);
		rowId = db.insert(Player.TABLE_NAME, null, values);
		// /Log.d(TAG, "ID:" + rowId);
		return rowId;
	}
	
	public long insertRoundPlayer(long roundId, long playerId, float playerHdcp, String livePlayerId, int playerGoal) {
		long rowId = 0;
		SQLiteDatabase db = getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(RoundPlayer.ROUND_ID, roundId);
		values.put(RoundPlayer.PLAYER_ID, playerId);
		values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
        values.put(RoundPlayer.LIVE_PLAYER_ID, livePlayerId);
		values.put(RoundPlayer.CREATED_DATE, getNow());
		values.put(RoundPlayer.MODIFIED_DATE, getNow());
        values.put(RoundPlayer.PLAYER_GOAL, String.valueOf(playerGoal));
		rowId = db.insert(RoundPlayer.TABLE_NAME, null, values);
		return rowId;
	}

	public long insertRoundPlayer(long roundId, String playerId, float playerHdcp, String livePlayerId, int playerGoal) {
		long rowId = 0;
		SQLiteDatabase db = getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(RoundPlayer.ROUND_ID, roundId);
		values.put(RoundPlayer.PLAYER_ID, playerId);
		values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
        values.put(RoundPlayer.LIVE_PLAYER_ID, livePlayerId);
		values.put(RoundPlayer.CREATED_DATE, getNow());
		values.put(RoundPlayer.MODIFIED_DATE, getNow());
        values.put(RoundPlayer.PLAYER_GOAL, String.valueOf(playerGoal));
		rowId = db.insert(RoundPlayer.TABLE_NAME, null, values);
		return rowId;
	}
	// CanNC:begin
		public boolean isExistedRoundPlayer(long roundId, long playerId) {
			SQLiteDatabase db = getReadableDatabase();
			SQLiteQueryBuilder qb = RoundPlayerCursor.getQueryBuilder();
			qb.appendWhere(RoundPlayer.ROUND_ID + " = " + roundId );
			qb.appendWhere(" AND " + RoundPlayer.PLAYER_ID + " = " + playerId );
			
			RoundPlayerCursor pc = (RoundPlayerCursor) qb.query(db, null, null, null, null,
					null, null);
			pc.moveToFirst();
			if (pc.getCount() > 0) {
				return true;
			}
			return false;
		}
	
	// CanNC:begin
	public boolean isExistedPlayer(String serverId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();
		qb.appendWhere(Player.SERVER_ID + " = '" + serverId.trim() + "'");
		
		PlayerCursor pc = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		pc.moveToFirst();
		if (pc.getCount() > 0) {
			return true;
		}
		return false;
	}
	
	public long getPlayerIDbyServerID(String serverId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();
		qb.appendWhere(Player.SERVER_ID + " = '" + serverId.trim() + "'");
		
		PlayerCursor pc = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		pc.moveToFirst();
		if (pc.getCount() > 0) {
			return pc.getId();
		} else {
			return -1;
		}
	}
	
	public long insertPlayer(String name, String serverId) {
		long rowId = 0;
		SQLiteDatabase db = getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(Player.NAME, name);
		values.put(Player.OWNNER_FLAG, FLG_NOT_OWNER);
		values.put(Player.CREATED_DATE, getNow());
		values.put(Player.MODIFIED_DATE, getNow());
		values.put(Player.DEL_FLAG, 0);
		values.put(Player.SERVER_ID, serverId);
		rowId = db.insert(Player.TABLE_NAME, null, values);
		// /Log.d(TAG, "ID:" + rowId);
		return rowId;
	}
	
	// CanNC:end

	public void deletePlayer(long playerId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Player._ID + " = ?";
		String[] whereArgs = new String[] { "" + playerId };
		db.delete(Player.TABLE_NAME, whereClause, whereArgs);
	}
	
	public void deleteRoundPlayer(long playerId, long roundId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = RoundPlayer.PLAYER_ID + " = ? AND " + RoundPlayer.ROUND_ID + " = ?";
		String[] whereArgs = new String[] { "" + playerId, "" + roundId };
		db.delete(RoundPlayer.TABLE_NAME, whereClause, whereArgs);
	}
	
	public void deleteRoundPlayer(long id) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = RoundPlayer._ID + " = ?";
		String[] whereArgs = new String[] { "" + id};
		db.delete(RoundPlayer.TABLE_NAME, whereClause, whereArgs);
	}
	
	public void deletePlayerHistory(long playerId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Player._ID + " = ?";
		String[] whereArgs = new String[] { "" + playerId };
		ContentValues value=new ContentValues();
		value.put(Player.DEL_FLAG, true);
		db.update(Player.TABLE_NAME,value, whereClause, whereArgs);

	}

	public PlayerCursor getOwner() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();
		qb.appendWhere(Player.OWNNER_FLAG + "='1' or " + Player.OWNNER_FLAG + "=1" );
		PlayerCursor c = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
        if(null != c && c.getCount() > 0){
            return c;
        }
		else {
            c = getOwnerByID();
            return c;
        }
	}
	
	public PlayerCursor getOwnerByID() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();
		qb.appendWhere(Player._ID + "=1");
		PlayerCursor c = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}
	/**
	 * web serviceから取得したコース情報（tee, holeを含む）をDBに取り込む
	 * 
	 * @param course
	 */
	public long createClub(com.asai24.golf.domain.ClubObj club,
			String selectedTeeOobId, com.asai24.golf.domain.Course course) {
		SQLiteDatabase db = getWritableDatabase();
		long clubID;
		long courseId;
		long teeId;
		YgoLog.i(TAG,"createClub....");
		// create club
		clubID= insertClub(club);
		// create course
		
		ContentValues values = new ContentValues();
//		if (club.getExtType().equals(Constant.EXT_TYPE_OOBGOLF)) {
//			values.put(Course.COURSE_OOB_ID, club.getExtId());
//		} else {
//			values.put(Course.COURSE_YOURGOLF_ID, club.getExtId());
//		}
		if (club.getExtType() != null && club.getExtType().equals(Constant.EXT_TYPE_OOBGOLF)) {
			values.put(Course.COURSE_OOB_ID, course.getOobId());
		} else {
			values.put(Course.COURSE_YOURGOLF_ID, course.getYourGolfId());
		}
		
		values.put(Course.CLUB_ID, clubID);
		values.put(Course.CLUB_NAME, club.getClubName());
		values.put(Course.COURSE_NAME, course.getCourseName());
		values.put(Course.MODIFIED_DATE, getNow());
		values.put(Course.CREATED_DATE, getNow());		
		values.put(Course.DEL_FLAG, false);//NAMLH 2012.05.10 put delflag
		courseId = db.insert(Course.TABLE_NAME, null, values);
			
		
		com.asai24.golf.domain.Tee selectedTee = null;
		for (com.asai24.golf.domain.Tee tee : course.getTees()) {
			if (selectedTeeOobId.equals(tee.getOobId())) {
				selectedTee = tee;
				break;
			}
		}

		// create tee
		values = new ContentValues();
		values.put(Tee.TEE_OOB_ID, selectedTee.getOobId());
		values.put(Tee.NAME, selectedTee.getName());
		values.put(Tee.COURSE_ID, courseId);
		values.put(Tee.MODIFIED_DATE, getNow());
		values.put(Tee.CREATED_DATE, getNow());
		teeId = db.insert(Tee.TABLE_NAME, null, values);

		for (com.asai24.golf.domain.Hole hole : selectedTee.getHoles()) {
			// create hole
			values = new ContentValues();
			values.put(Hole.TEE_ID, teeId);
			values.put(Hole.HOLE_NUMBER, hole.getHoleNumber());
			values.put(Hole.PAR, hole.getPar());
			values.put(Hole.WOMEN_PAR, hole.getWomenPar());
			values.put(Hole.YARD, hole.getYard());
			values.put(Hole.HANDICAP, hole.getHandicap());
			values.put(Hole.WOMEN_HANDICAP, hole.getWomenHandicap());
			values.put(Hole.MODIFIED_DATE, getNow());
			values.put(Hole.CREATED_DATE, getNow());
			db.insert(Hole.TABLE_NAME, null, values);
		}

		return courseId;
	}
	//LAMTT
	public long updateApiCourse(com.asai24.golf.domain.Course course,long courseId) {
		
		// create course
		ContentValues values = new ContentValues();
		if (!course.getOobId().equals("") && !course.getOobId().equals("0")) {
			values.put(Course.COURSE_OOB_ID, course.getOobId());
		} else {
			values.put(Course.COURSE_YOURGOLF_ID, course.getYourGolfId());
		}
		//values.put(Course.CLUB_NAME, course.getN);
		values.put(Course.COURSE_NAME, course.getCourseName());
		values.put(Course.MODIFIED_DATE, getNow());
		values.put(Course.CREATED_DATE, getNow());		
		values.put(Course.DEL_FLAG, false);
		String whereClause = Course._ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(courseId) };
		return getWritableDatabase().update(ScoreDetail.TABLE_NAME, values,
				whereClause, whereArgs);
	}
	//LAMTT
	public long insertCourse(com.asai24.golf.domain.Course course,long clubId, String clubName,long create) {
		SQLiteDatabase db = getWritableDatabase();
		long courseId;
		
		// create course
		ContentValues values = new ContentValues();
		if (!course.getOobId().equals("") && !course.getOobId().equals("0")) {
			values.put(Course.COURSE_OOB_ID, course.getOobId());
		} else {
			values.put(Course.COURSE_YOURGOLF_ID, course.getYourGolfId());
		}
		values.put(Course.CLUB_ID, clubId);
		values.put(Course.CLUB_NAME, clubName);
		values.put(Course.COURSE_NAME, course.getCourseName());
		values.put(Course.MODIFIED_DATE, getNow());
		
		values.put(Course.CREATED_DATE, create);		
		values.put(Course.DEL_FLAG, false);//NAMLH 2012.05.10 put delflag
		courseId = db.insert(Course.TABLE_NAME, null, values);
		return courseId;
	}
	/**
	 * web serviceから取得したコース情報（tee, holeを含む）をDBに取り込む
	 * 
	 * @param courseId
	 */
	public long insertTee(com.asai24.golf.domain.Tee tee,long courseId) {
		SQLiteDatabase db = getWritableDatabase();
		long teeId;

		ContentValues values = new ContentValues();
		values = new ContentValues();
		values.put(Tee.TEE_OOB_ID, tee.getOobId());
		values.put(Tee.NAME, tee.getName());
		values.put(Tee.COURSE_ID, courseId);
		values.put(Tee.MODIFIED_DATE, getNow());
		values.put(Tee.CREATED_DATE, getNow());
		teeId = db.insert(Tee.TABLE_NAME, null, values);

//		for (com.asai24.golf.domain.Hole hole : selectedTee.getHoles()) {
//			// create hole
//			values = new ContentValues();
//			values.put(Hole.TEE_ID, teeId);
//			values.put(Hole.HOLE_NUMBER, hole.getHoleNumber());
//			values.put(Hole.PAR, hole.getPar());
//			values.put(Hole.WOMEN_PAR, hole.getWomenPar());
//			values.put(Hole.YARD, hole.getYard());
//			values.put(Hole.HANDICAP, hole.getHandicap());
//			values.put(Hole.WOMEN_HANDICAP, hole.getWomenHandicap());
//			values.put(Hole.MODIFIED_DATE, getNow());
//			values.put(Hole.CREATED_DATE, getNow());
//			db.insert(Hole.TABLE_NAME, null, values);
//		}
		return teeId;
	}
	public int insertHole (ArrayList<com.asai24.golf.domain.Hole> getHoleFromJson,long teeId) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		int i=0;
		for (com.asai24.golf.domain.Hole hole : getHoleFromJson) {
			i++;
			values = new ContentValues();
			values.put(Hole.TEE_ID, teeId);
			values.put(Hole.HOLE_NUMBER, hole.getHoleNumber());
			values.put(Hole.PAR, hole.getPar());
			values.put(Hole.WOMEN_PAR, hole.getWomenPar());
			values.put(Hole.YARD, hole.getYard());
			values.put(Hole.HANDICAP, hole.getHandicap());
			values.put(Hole.WOMEN_HANDICAP, hole.getWomenHandicap());
			values.put(Hole.MODIFIED_DATE, getNow());
			values.put(Hole.CREATED_DATE, getNow());
			db.insert(Hole.TABLE_NAME, null, values);
		}
		return i;
	}
	public long createCourse(com.asai24.golf.domain.Course course,
			String selectedTeeOobId, int mode) {
		SQLiteDatabase db = getWritableDatabase();
		long courseId;
		long teeId;

		// create course
		ContentValues values = new ContentValues();
		if (mode == Constant.OOBGOLF_COURSE) {
			values.put(Course.COURSE_OOB_ID, course.getOobId());
		} else {
			values.put(Course.COURSE_YOURGOLF_ID, course.getYourGolfId());
		}
		values.put(Course.CLUB_NAME, course.getClubName());
		values.put(Course.COURSE_NAME, course.getCourseName());
		values.put(Course.MODIFIED_DATE, getNow());
		values.put(Course.CREATED_DATE, getNow());		
		values.put(Course.DEL_FLAG, false);//NAMLH 2012.05.10 put delflag
		courseId = db.insert(Course.TABLE_NAME, null, values);

		com.asai24.golf.domain.Tee selectedTee = null;
		for (com.asai24.golf.domain.Tee tee : course.getTees()) {
			if (selectedTeeOobId.equals(tee.getOobId())) {
				selectedTee = tee;
				break;
			}
		}

		// create tee
		values = new ContentValues();
		values.put(Tee.TEE_OOB_ID, selectedTee.getOobId());
		values.put(Tee.NAME, selectedTee.getName());
		values.put(Tee.COURSE_ID, courseId);
		values.put(Tee.MODIFIED_DATE, getNow());
		values.put(Tee.CREATED_DATE, getNow());
		teeId = db.insert(Tee.TABLE_NAME, null, values);

		for (com.asai24.golf.domain.Hole hole : selectedTee.getHoles()) {
			// create hole
			values = new ContentValues();
			values.put(Hole.TEE_ID, teeId);
			values.put(Hole.HOLE_NUMBER, hole.getHoleNumber());
			values.put(Hole.PAR, hole.getPar());
			values.put(Hole.WOMEN_PAR, hole.getWomenPar());
			values.put(Hole.YARD, hole.getYard());
			values.put(Hole.HANDICAP, hole.getHandicap());
			values.put(Hole.WOMEN_HANDICAP, hole.getWomenHandicap());
			values.put(Hole.MODIFIED_DATE, getNow());
			values.put(Hole.CREATED_DATE, getNow());
			db.insert(Hole.TABLE_NAME, null, values);
		}

		return courseId;
	}

	public TeeCursor getTeeByCourseIdAndTeeOobId(long courseId, String teeOobId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TeeCursor.getQueryBuilder();
		qb.appendWhere(Tee.TEE_OOB_ID + " = '" + teeOobId + "'");
		qb.appendWhere(" AND " + Tee.COURSE_ID + " = " + courseId);

		TeeCursor c = (TeeCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public TeeCursor getTeeByCourseId(long courseId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TeeCursor.getQueryBuilder();
		qb.appendWhere(Tee.COURSE_ID + " = " + courseId);

		TeeCursor c = (TeeCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public TeeCursor getTeeById(long id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TeeCursor.getQueryBuilder();
		qb.appendWhere(Tee._ID + " = " + id);

		TeeCursor c = (TeeCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}
	
	public TeeCursor getTeeByIdForUploading(long id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TeeCursor.getQueryBuilder2();
		qb.appendWhere(" AND t1." + Tee._ID + " = " + id);

		TeeCursor c = (TeeCursor) qb.query(db, null, null, null, null, null,null);
		c.moveToFirst();
		return c;
	}

	public long insertOrUpdateOwner(String name) {
		long id = -1;
		PlayerCursor p = getOwner();
		if (p.getCount() == 0) {
			id = insertOwner(name);
		} else {
			id = p.getId();
			updatePlayer(id, name);
		}
		p.close();
		return id;
	}

	public void updatePlayerServer(long id, String idServer) {
		ContentValues values = new ContentValues();
		values.put(Player.SERVER_ID, idServer);
		values.put(Player.MODIFIED_DATE, getNow());

		String whereClause = Player._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(Player.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	
	public void updatePlayer(long id, String name) {
		ContentValues values = new ContentValues();
		values.put(Player.NAME, name);
		values.put(Player.MODIFIED_DATE, getNow());

		String whereClause = Player._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(Player.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	
	public void updatePlayer(String serverId, String name) {
		ContentValues values = new ContentValues();
		values.put(Player.NAME, name);
		values.put(Player.MODIFIED_DATE, getNow());

		String whereClause = Player.SERVER_ID + " = ?";
		String[] whereArgs = new String[] { serverId };

		getWritableDatabase().update(Player.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	public void updatePlayer(String serverId, float hdcp) {
		ContentValues values = new ContentValues();
		values.put(Player.PLAYER_HDCP, String.valueOf(hdcp));
		values.put(Player.MODIFIED_DATE, getNow());

		String whereClause = Player.SERVER_ID + " = ?";
		String[] whereArgs = new String[] { serverId };

		getWritableDatabase().update(Player.TABLE_NAME, values, whereClause,
				whereArgs);
	}

    public void updateRoundPlayerGoal(long roundId, long playerId, int playerGoal) {
        ContentValues values = new ContentValues();
        values.put(RoundPlayer.PLAYER_GOAL, String.valueOf(playerGoal));
        values.put(RoundPlayer.MODIFIED_DATE, getNow());

        String whereClause = RoundPlayer.ROUND_ID + " = ? AND " + RoundPlayer.PLAYER_ID + " = ?";
        String[] whereArgs = new String[] { Long.toString(roundId), Long.toString(playerId) };

        getWritableDatabase().update(RoundPlayer.TABLE_NAME, values, whereClause,
                whereArgs);
    }

	public void updateRoundPlayer(long id, float playerHdcp) {
		ContentValues values = new ContentValues();
		values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
		values.put(RoundPlayer.MODIFIED_DATE, getNow());

		String whereClause = RoundPlayer._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(id) };

		getWritableDatabase().update(RoundPlayer.TABLE_NAME, values, whereClause,
				whereArgs);
	}

    public void updateRoundPlayer(long id, float playerHdcp, String livePlayerId) {
        ContentValues values = new ContentValues();
        values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
        values.put(RoundPlayer.LIVE_PLAYER_ID, livePlayerId);
        values.put(RoundPlayer.MODIFIED_DATE, getNow());

        String whereClause = RoundPlayer._ID + " = ?";
        String[] whereArgs = new String[] { Long.toString(id) };

        getWritableDatabase().update(RoundPlayer.TABLE_NAME, values, whereClause,
                whereArgs);
    }
	
	public void updateRoundPlayer(long roundId, long playerId, float playerHdcp) {
		ContentValues values = new ContentValues();
		values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
		values.put(RoundPlayer.MODIFIED_DATE, getNow());

		String whereClause = RoundPlayer.ROUND_ID + " = ? AND " + RoundPlayer.PLAYER_ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(roundId), Long.toString(playerId) };

		getWritableDatabase().update(RoundPlayer.TABLE_NAME, values, whereClause,
				whereArgs);
	}

    public void updateRoundPlayer(long roundId, long playerId, float playerHdcp, String livePlayerId) {
        ContentValues values = new ContentValues();
        values.put(RoundPlayer.PLAYER_HDCP, String.valueOf(playerHdcp));
        values.put(RoundPlayer.LIVE_PLAYER_ID, livePlayerId);
        values.put(RoundPlayer.MODIFIED_DATE, getNow());

        String whereClause = RoundPlayer.ROUND_ID + " = ? AND " + RoundPlayer.PLAYER_ID + " = ?";
        String[] whereArgs = new String[] { Long.toString(roundId), Long.toString(playerId) };

        getWritableDatabase().update(RoundPlayer.TABLE_NAME, values, whereClause,
                whereArgs);
    }

	private long insertOwner(String name) {
		long rowId = 0;
		SQLiteDatabase db = getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(Player.NAME, name);
		values.put(Player.OWNNER_FLAG, FLG_OWNER);
		values.put(Player.CREATED_DATE, getNow());
		values.put(Player.MODIFIED_DATE, getNow());

		rowId = db.insert(Player.TABLE_NAME, null, values);
		return rowId;
	}

	public TotalScoreCursor getTotalScores(long roundId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TotalScoreCursor.getQueryBuilder(roundId);

		String groupBy = "s1.player_id";
		TotalScoreCursor cursor = (TotalScoreCursor) qb.query(db, null, null,
				null, groupBy, null, null);
		cursor.moveToFirst();

		return cursor;
	}
	public int TotalScoreByRoundID(long roundId)
	{
		int result=0;
		SQLiteDatabase db = getReadableDatabase();
		String qslString="select  sum(c."+Score.HOLE_SCORE+") as holescore from "+Score.TABLE_NAME+" c left join "+Player.TABLE_NAME+" l on l._id = c."+Score.PLAYER_ID+" where c."+Score.ROUND_ID +"="+roundId +" and l."+Player.OWNNER_FLAG +"=1" ;
		
		Cursor cursor = db.rawQuery(qslString, null);
		if(cursor.moveToFirst()) {
			result=(int)cursor.getLong(0);
		}
		cursor.close();
		return result;
	}
	public TotalScoreCursor getTotalScores(long roundId, long playerId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TotalScoreCursor.getQueryBuilder(roundId,
				playerId);

		String groupBy = "s1.player_id";
		TotalScoreCursor cursor = (TotalScoreCursor) qb.query(db, null, null,
				null, groupBy, null, null);
		cursor.moveToFirst();

		return cursor;
	}

	private Long getNow() {
		return Long.valueOf(System.currentTimeMillis());
	}

	public long[] getPlayerIds(long roundId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();

		qb.appendWhere(Score.ROUND_ID + "= " + roundId);
		String sorbBy = "" + Score.ROUND_ID + " ASC";

		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, sorbBy);
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < c.getCount(); i++) {
			c.moveToPosition(i);
			if (!ids.contains(c.getPlayerId())) {
				ids.add(c.getPlayerId());
			}
		}
		// /Log.d(TAG, "player id counts:" + ids.size());
		long[] playerIds = new long[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			playerIds[i] = (long) ids.get(i);
		}
		c.close();

        //ticket 6029
		//sort player ids
//		Arrays.sort(playerIds);
		
		return playerIds;
	}

	public ScoreDetailCursor getScoreDetail(long scoreDetailId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreDetailCursor.getQueryBuilder();
		qb.appendWhere(ScoreDetail._ID + " = " + scoreDetailId);
		String sorbBy = "" + ScoreDetail.SHOT_NUMBER;

		ScoreDetailCursor c = (ScoreDetailCursor) qb.query(db, null, null,
				null, null, null, sorbBy);
		c.moveToFirst();
		return c;
	}

	public void updateRound(long roundId, int resultId) {
		ContentValues values = new ContentValues();
		values.put(Round.RESULT_ID, resultId);
		values.put(Score.MODIFIED_DATE, getNow());

		String whereClause = Round._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(roundId) };

		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	
	public void updateRoundPlaying(long roundId, Boolean flag) {
		
		ContentValues values = new ContentValues();
		values.clear();
		values.put (Round.PLAYING, flag);
		String whereClause = Round._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(roundId) };
		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,whereArgs);
	}
	public void deleteLiveInRound(String roundId) {
		ContentValues values = new ContentValues();
		values.put(Round.LIVE_ENTRY_ID, "");
		values.put(Round.LIVE_ID, "");
		values.put(Score.MODIFIED_DATE, getNow());

		String whereClause = Round.YOURGOLF_ID + " = ?";
		String[] whereArgs = new String[] { roundId };

		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	public void updateCachePlayIng(String idServer,Boolean flag)
	{
		ContentValues values = new ContentValues();
		//values.clear();
		values.put (HistoryCache.PLAYING, flag);
		String whereClause = HistoryCache._ID + " = '" +idServer.trim() +"'";
		//String[] whereArgs = new String[] { idServer.trim()};
		getWritableDatabase().update(HistoryCache.TABLE_NAME, values, whereClause,null);
	}
	public void updateRoundDelByRoundServerId(String roundId, Boolean flag) {
		
		ContentValues values = new ContentValues();
		values.clear();
		values.put (Round.ROUND_DELETE, flag);
		String whereClause = Round.YOURGOLF_ID + " = ?";
		String[] whereArgs = new String[] { roundId };
		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,whereArgs);

		
	}
	
	public void updateRoundDelete(long roundId, Boolean flag) {
		
		ContentValues values = new ContentValues();
		values.clear();
		values.put (Round.ROUND_DELETE, flag);
		String whereClause = Round._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(roundId) };
		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,whereArgs);

		
	}
	public void updateYourGolfIdAPI(long roundId, String yourgolfId,String updateAt) {
		ContentValues values = new ContentValues();
		values.put(Round.YOURGOLF_ID, yourgolfId);
		values.put(Round.MODIFIED_DATE, getNow());
		values.put(Round.UPDATE_DATE, updateAt);
		String whereClause = Round._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(roundId) };

		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	public void updateYourGolfId(long roundId, String yourgolfId) {
		ContentValues values = new ContentValues();
		values.put(Round.YOURGOLF_ID, yourgolfId);
		values.put(Round.MODIFIED_DATE, getNow());

		String whereClause = Round._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(roundId) };

		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	public void deleteHistory(long roundId) {
		RoundCursor rc = getRound(roundId);

		// Round Cursorがemptyの場合は処理を行わない。（何かしらのタイミングでEmptyの場合がある。バグ対応）
		if (!rc.moveToFirst()) {
			rc.close();
			return;
		}
		long courseId = rc.getCourseId(); // コースID
		long teeId = rc.getTeeId(); // ティーID

		
		deleteClub(getCourse(courseId).getClubId());
		deleteRound(roundId); // roundテーブルレコード削除
		deleteCourse(courseId); // courseテーブルレコード削除
		deleteTee(teeId); // teeテーブルレコード削除
		deleteHole(teeId); // holeテーブルレコード削除
		
		rc.close();

		ScoreCursor sc = getScores(roundId); // 複数レコード
		PlayerCursor pc;
		long scoreId = 0;
		long playerId = 0;
		ArrayList<Long> playerList = new ArrayList<Long>();

		while (!sc.isAfterLast()) {
			scoreId = sc.getId();
			playerId = sc.getPlayerId();

			deleteScore(scoreId); // scoreテーブルレコード削除
			deleteScoreDetails(scoreId); // score_detailテーブルレコード削除

			if (!playerList.contains(playerId)) {
				playerList.add(playerId);

				pc = getOwner();
				if (playerId != pc.getId()
						&& !checkPlayerExist(playerId, roundId)) {
					// 対象のPlayerIDがOwnerではなく、かつ他テーブルで使用されていない場合
					deletePlayer(playerId); // playerテーブルレコード削除
				}
				pc.close();
			}

			sc.moveToNext();
		}
		sc.close();
	}

	public void deleteRound(long id) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Round._ID + " = ?";
		String[] whereArgs = new String[] { "" + id };
		db.delete(Round.TABLE_NAME, whereClause, whereArgs);
	}

	public void deleteCourse(long courseId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Course._ID + " = ?";
		String[] whereArgs = new String[] { "" + courseId };
		db.delete(Course.TABLE_NAME, whereClause, whereArgs);
	}
	
	/**
	 * NamLH
	 * 2012.05.10
	 * delete course in history
	 * @param courseId
	 */
	public void deleteHistoryCourse(long courseId){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues dataToInsert = new ContentValues();                          
		dataToInsert.put(Course.DEL_FLAG, true);
		String where = Course._ID + "=?";
		String[] whereArgs = {""+courseId};
		db.update(Course.TABLE_NAME, dataToInsert, where, whereArgs);
	}
	/**
	 * NamLH
	 * 2012.05.17
	 * delete Club in history
	 * @param clubExtID
	 */
	public void deleteHistoryClub(String clubExtID){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues dataToInsert = new ContentValues();                          
		dataToInsert.put(Club.CLUB_DELETEED, true);
		String where = Club.CLUB_ID + "=?";
		String[] whereArgs = {""+clubExtID};
		db.update(Club.TABLE_NAME, dataToInsert, where, whereArgs);
	}

	public void deleteTee(long teeId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Tee._ID + " = ?";
		String[] whereArgs = new String[] { "" + teeId };
		db.delete(Tee.TABLE_NAME, whereClause, whereArgs);
	}

	public void deleteHole(long teeId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Hole.TEE_ID + " = ?";
		String[] whereArgs = new String[] { "" + teeId };
		db.delete(Hole.TABLE_NAME, whereClause, whereArgs);
	}

	public void deleteScore(long scoreId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Score._ID + " = ?";
		String[] whereArgs = new String[] { "" + scoreId };
		db.delete(Score.TABLE_NAME, whereClause, whereArgs);
	}
//LAMTT
	public void deleteScoreByRoundID(long roundId) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Score.ROUND_ID + " = ?";
		String[] whereArgs = new String[] { "" + roundId };
		db.delete(Score.TABLE_NAME, whereClause, whereArgs);
	}

	private boolean checkPlayerExist(long playerId, long roundId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.PLAYER_ID + " = " + playerId);
		qb.appendWhere(" AND " + Score.ROUND_ID + " <> " + roundId);

		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);

		boolean result = c.moveToFirst();
		c.close();

		return result;
	}

	// public void deleteTrace() {
	// SQLiteDatabase db = getWritableDatabase();
	// db.delete(Trace.TABLE_NAME, null, null);
	// }

	// public long insertTrace(int activity) {
	// long rowId = 0;
	// SQLiteDatabase db = getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put(Trace.ACTIVITY, activity);
	// values.put(Trace.CREATED_DATE, getNow());
	// values.put(Trace.MODIFIED_DATE, getNow());
	// rowId = db.insert(Trace.TABLE_NAME, null, values);
	// return rowId;
	// }

	// 最新のスコア更新日付取得用
	public LastModifiedScoreCursor getLatestModifedScore(long roundId) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = LastModifiedScoreCursor
				.getQueryBuilder(roundId);

		LastModifiedScoreCursor cursor = (LastModifiedScoreCursor) qb.query(db,
				null, null, null, null, null, null);
		cursor.moveToFirst();

		return cursor;
	}

	// クラブ毎のショット数取得用
	public TotalShotByClubCursor getTotalShotByClub(long roundId,
			long playerId, String club) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TotalShotByClubCursor.getQueryBuilder(roundId,
				playerId, club);

		TotalShotByClubCursor cursor = (TotalShotByClubCursor) qb.query(db,
				null, null, null, null, null, null);
		cursor.moveToFirst();

		return cursor;
	}
	
	//プレイ日の更新
	public void updatePlayDate(long roundId, long playDate,long createDate) {
		ContentValues values = new ContentValues();
		values.put(Round.CREATED_DATE, createDate);
		values.put(Round.MODIFIED_DATE, getNow());

		String whereClause = Round._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(roundId) };

		getWritableDatabase().update(Round.TABLE_NAME, values, whereClause,
				whereArgs);
	}
	

	/**
	 * ラウンドのスコア入力状況を返します
	 * 
	 * @param roundId
	 * @return スコア入力状況
	 * @see Round.COMPLETION_STATUS
	 */
	public int getRoundComepletionStatus(long roundId) {
		SQLiteDatabase db = getReadableDatabase();

		// from
		StringBuffer sqlFrom = new StringBuffer();
		sqlFrom.append(Score.TABLE_NAME + " s1");
		sqlFrom.append("    LEFT JOIN " + Hole.TABLE_NAME + " h1");
		sqlFrom.append("        ON s1." + Score.HOLE_ID + " = h1." + Hole._ID);

		// select
		String[] columns = new String[] { "" };
		StringBuffer retCol = new StringBuffer();
		retCol.append(" CASE");
		retCol.append("     WHEN COUNT(*) = 18 THEN "
				+ COMPLETION_STATUS.ALL_COMPLETED);
		retCol.append("     WHEN COUNT(*) = 9");
		retCol.append("     AND MAX(h1." + Hole.HOLE_NUMBER + ") = 9 THEN "
				+ COMPLETION_STATUS.FRONT_COMPLETED);
		retCol.append("     WHEN COUNT(*) = 9");
		retCol.append("     AND MIN(h1." + Hole.HOLE_NUMBER + ") = 10 THEN "
				+ COMPLETION_STATUS.BACK_COMPLETED);
		retCol.append("     ELSE " + COMPLETION_STATUS.NOT_COMPLETED);
		retCol.append(" END");
		columns[0] = retCol.toString();

		// where
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append("s1." + Score.PLAYER_ID + " = " + Player.OWNER_ID);
		sqlWhere.append(" AND s1." + Score.ROUND_ID + " = ?");
		sqlWhere.append(" AND s1." + Score.HOLE_SCORE + " > 0");

		// where引数
		String[] whereArgs = new String[] { String.valueOf(roundId) };

		// group by
		String groupBy = "s1." + Score.ROUND_ID;

		Cursor c = db.query(sqlFrom.toString(), columns, sqlWhere.toString(),
				whereArgs, groupBy, null, null);
		int ret;
		if (c.moveToFirst()) {
			ret = c.getInt(0);
		} else {
			ret = COMPLETION_STATUS.NOT_COMPLETED;
		}
		c.close();

		return ret;
	}

	public CourseCursor getCoursesModifiedAfter(long modified) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = CourseCursor.getQueryBuilder();
		qb.appendWhere(Course.MODIFIED_DATE + " > " + modified);

		CourseCursor c = (CourseCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	public TeeCursor getTeesModifiedAfter(long modified) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = TeeCursor.getQueryBuilder2();
		qb.appendWhere(" AND t1." + Tee.MODIFIED_DATE + " > " + modified);

		TeeCursor c = (TeeCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public HoleCursor getHolesModifiedAfter(long modified) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = HoleCursor.getQueryBuilder();
		qb.appendWhere(Hole.MODIFIED_DATE + " > " + modified);

		HoleCursor c = (HoleCursor) qb.query(db, null, null, null, null, null,
				null);
		c.moveToFirst();
		return c;
	}

	public RoundCursor getRoundsModifiedAfter(long modified) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = RoundCursor.getQueryBuilder();
		qb.appendWhere(" AND r1." + Round.MODIFIED_DATE + " > " + modified);

		RoundCursor c = (RoundCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	public ScoreCursor getScoresModifiedAfter(long modified) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreCursor.getQueryBuilder();
		qb.appendWhere(Score.MODIFIED_DATE + " > " + modified);

		ScoreCursor c = (ScoreCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	public ScoreDetailCursor getScoreDetailsModifiedAfter(long modified) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ScoreDetailCursor.getQueryBuilder();
		qb.appendWhere(ScoreDetail.MODIFIED_DATE + " > " + modified);

		ScoreDetailCursor c = (ScoreDetailCursor) qb.query(db, null, null,
				null, null, null, null);
		c.moveToFirst();
		return c;
	}

	public PlayerCursor getPlayersModifiedAfter(long modified) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = PlayerCursor.getQueryBuilder();
		qb.appendWhere(Player.MODIFIED_DATE + " > " + modified);

		PlayerCursor c = (PlayerCursor) qb.query(db, null, null, null, null,
				null, null);
		c.moveToFirst();
		return c;
	}

	public Cursor getCoursesDeletedAfter(Long deleted) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(CourseDelete.TABLE_NAME);
		qb.appendWhere(CourseDelete.DELETED_DATE + " > " + deleted);

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();

		return c;
	}
	public CourseCursor getCoursesByClubID(long clubId) { //NAMLH 2012.05.14 fixed
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = CourseCursor.getQueryBuilder();
		qb.setTables(Course.TABLE_NAME);
		qb.appendWhere(Course.CLUB_ID + " = " + clubId);
		
		CourseCursor c =(CourseCursor) qb.query(db, null, null, null, null, null, null);
		
		c.moveToFirst();
		return c;
	}
	public Cursor getTeesDeletedAfter(Long deleted) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TeeDelete.TABLE_NAME);
		qb.appendWhere(TeeDelete.DELETED_DATE + " > " + deleted);

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();

		return c;
	}

	public Cursor getHolesDeletedAfter(Long deleted) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(HoleDelete.TABLE_NAME);
		qb.appendWhere(HoleDelete.DELETED_DATE + " > " + deleted);

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();

		return c;
	}

	public Cursor getRoundsDeletedAfter(Long deleted) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(RoundDelete.TABLE_NAME);
		qb.appendWhere(RoundDelete.DELETED_DATE + " > " + deleted);

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();

		return c;
	}

	public Cursor getScoresDeletedAfter(Long deleted) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ScoreDelete.TABLE_NAME);
		qb.appendWhere(ScoreDelete.DELETED_DATE + " > " + deleted);

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();

		return c;
	}

	public Cursor getScoreDetailsDeletedAfter(Long deleted) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ScoreDetailDelete.TABLE_NAME);
		qb.appendWhere(ScoreDetailDelete.DELETED_DATE + " > " + deleted);

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();

		return c;
	}

	public Cursor getPlayersDeletedAfter(Long deleted) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(PlayerDelete.TABLE_NAME);
		qb.appendWhere(PlayerDelete.DELETED_DATE + " > " + deleted);

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();

		return c;
	}

	/*
	 * Restore
	 */
	public void restore(ArrayList<HashMap<String, String>> courseList,
			ArrayList<HashMap<String, String>> teeList,
			ArrayList<HashMap<String, String>> holeList,
			ArrayList<HashMap<String, String>> roundList,
			ArrayList<HashMap<String, String>> scoreList,
			ArrayList<HashMap<String, String>> scoreDetailList,
			ArrayList<HashMap<String, String>> playerList) {

		SQLiteDatabase db = getWritableDatabase();
		dropDataTables(db);

		createDataTables(db);
		restoreCourseTables(db, courseList);
		restoreTeeTables(db, teeList);
		restoreHoleTables(db, holeList);
		restorePlayerTables(db, playerList);
		restoreRoundTables(db, roundList);
		restoreScoreTables(db, scoreList);
		restoreScoreDetailTables(db, scoreDetailList);
	}

	public void createTempTables() {
		SQLiteDatabase db = getReadableDatabase();
		createTempScoreTables(db);
		createTempScoreDetailTables(db);
	}
	public void dropTempTables() {
		SQLiteDatabase db = getReadableDatabase();
		dropTempScoreDetailTables(db);
		dropTempScoreTables(db);
	}
	private void dropDataTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + Course.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Tee.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Hole.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Player.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Round.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Score.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ScoreDetail.TABLE_NAME);
		
		db.execSQL("DROP TABLE IF EXISTS " + PlayerDelete.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + RoundDelete.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CourseDelete.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TeeDelete.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HoleDelete.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ScoreDelete.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ScoreDetailDelete.TABLE_NAME);
	}

	private void restoreCourseTables(SQLiteDatabase db,
			ArrayList<HashMap<String, String>> dataList) {
		HashMap<String, String> items;
		String type;

		for (int i = 0; i < dataList.size(); i++) {
			items = dataList.get(i);

			ContentValues values = new ContentValues();
			values.put(Course._ID, items.get("id"));
			values.put(Course.CLUB_NAME, items.get("club_name"));
			type = items.get("ext_type");
			if (type.equals("oobgolf")) {
				values.put(Course.COURSE_OOB_ID, items.get("ext_id"));
			} else {
				values.put(Course.COURSE_YOURGOLF_ID, items.get("ext_id"));
			}
			values.put(Course.CLUB_NAME, items.get("club_name"));
			values.put(Course.COURSE_NAME, items.get("course_name"));
			values.put(Course.MODIFIED_DATE, items.get("modified"));
			values.put(Course.CREATED_DATE, items.get("created"));

			db.insert(Course.TABLE_NAME, null, values);
		}
	}

	private void restoreTeeTables(SQLiteDatabase db,
			ArrayList<HashMap<String, String>> dataList) {
		HashMap<String, String> items;
		for (int i = 0; i < dataList.size(); i++) {
			items = dataList.get(i);

			ContentValues values = new ContentValues();
			values.put(Tee._ID, items.get("id"));
			values.put(Tee.NAME, items.get("name"));
			values.put(Tee.TEE_OOB_ID, items.get("ext_id"));
			values.put(Tee.COURSE_ID, items.get("course_id"));
			values.put(Tee.CREATED_DATE, items.get("created"));
			values.put(Tee.MODIFIED_DATE, items.get("modified"));
			db.insert(Tee.TABLE_NAME, null, values);
		}
	}

	private void restoreHoleTables(SQLiteDatabase db,
			ArrayList<HashMap<String, String>> dataList) {
		HashMap<String, String> items;
		for (int i = 0; i < dataList.size(); i++) {
			items = dataList.get(i);

			ContentValues values = new ContentValues();
			values.put(Hole._ID, items.get("id"));
			values.put(Hole.HOLE_NUMBER, items.get("hole_number"));
			values.put(Hole.YARD, items.get("yard"));
			values.put(Hole.PAR, items.get("par"));
			values.put(Hole.WOMEN_PAR, items.get("women_par"));
			values.put(Hole.HANDICAP, items.get("handicap"));
			values.put(Hole.WOMEN_HANDICAP, items.get("women_handicap"));
			values.put(Hole.LATITUDE, items.get("lat"));
			values.put(Hole.LONGITUDE, items.get("lng"));
			values.put(Hole.TEE_ID, items.get("tee_id"));
			values.put(Hole.CREATED_DATE, items.get("created"));
			values.put(Hole.MODIFIED_DATE, items.get("modified"));
			db.insert(Hole.TABLE_NAME, null, values);
		}
	}

	private void restorePlayerTables(SQLiteDatabase db,
			ArrayList<HashMap<String, String>> dataList) {
		HashMap<String, String> items;
		for (int i = 0; i < dataList.size(); i++) {
			items = dataList.get(i);
//			Log.v(TAG, "flg: "+items.get("ownner_flag"));
			ContentValues values = new ContentValues();
			values.put(Player._ID, items.get("id"));
			values.put(Player.NAME, items.get("name"));
			values.put(Player.OWNNER_FLAG, items.get("ownner_flag"));
			values.put(Player.CREATED_DATE, items.get("created"));
			values.put(Player.MODIFIED_DATE, items.get("modified"));
			db.insert(Player.TABLE_NAME, null, values);
		}
	}

	private void restoreRoundTables(SQLiteDatabase db,
			ArrayList<HashMap<String, String>> dataList) {
		HashMap<String, String> items;
		String teeId;
		TeeCursor tc;
		long courseId;

		for (int i = 0; i < dataList.size(); i++) {
			items = dataList.get(i);

			// Tee tableからcouse_idを取得
			teeId = items.get("tee_id");
			tc = getTeeById(Long.valueOf(teeId));
			courseId = tc.getCourseId();
			tc.close();

			ContentValues values = new ContentValues();
			values.put(Round._ID, items.get("id"));
			values.put(Round.TEE_ID, teeId);
			values.put(Round.COURSE_ID, courseId);
			values.put(Round.RESULT_ID, items.get("result_id"));
			if(items.get("yourgolf_id")!=null) {
				if(!items.get("yourgolf_id").equals("null")) {	//バグ対応
					values.put(Round.YOURGOLF_ID, items.get("yourgolf_id"));
				}
			}
			values.put(Round.CREATED_DATE, items.get("created"));
			values.put(Round.MODIFIED_DATE, items.get("modified"));
			db.insert(Round.TABLE_NAME, null, values);
		}
	}

	private void restoreScoreTables(SQLiteDatabase db,
			ArrayList<HashMap<String, String>> dataList) {
		HashMap<String, String> items;
		for (int i = 0; i < dataList.size(); i++) {
			items = dataList.get(i);

			ContentValues values = new ContentValues();
			values.put(Score._ID, items.get("id"));
			values.put(Score.HOLE_SCORE, items.get("hole_score"));
			values.put(Score.ROUND_ID, items.get("round_id"));
			values.put(Score.HOLE_ID, items.get("hole_id"));
			values.put(Score.PLAYER_ID, items.get("player_id"));
			values.put(Score.GAME_SCORE, items.get("game_score"));
			values.put(Score.CREATED_DATE, items.get("created"));
			values.put(Score.MODIFIED_DATE, items.get("modified"));
			db.insert(Score.TABLE_NAME, null, values);
		}
	}

	private void restoreScoreDetailTables(SQLiteDatabase db,
			ArrayList<HashMap<String, String>> dataList) {
		HashMap<String, String> items;
		for (int i = 0; i < dataList.size(); i++) {
			items = dataList.get(i);

			ContentValues values = new ContentValues();
			values.put(ScoreDetail._ID, items.get("id"));
			values.put(ScoreDetail.SHOT_NUMBER, items.get("shot_number"));
			values.put(ScoreDetail.GPS_LATITUDE, items.get("lat"));
			values.put(ScoreDetail.GPS_LONGITUDE, items.get("lng"));
			values.put(ScoreDetail.CLUB, items.get("club"));
			values.put(ScoreDetail.SHOT_RESULT, items.get("shot_result"));
			values.put(ScoreDetail.SCORE_ID, items.get("score_id"));
			values.put(ScoreDetail.CREATED_DATE, items.get("created"));
			values.put(ScoreDetail.MODIFIED_DATE, items.get("modified"));
			db.insert(ScoreDetail.TABLE_NAME, null, values);
		}
	}
	//====LAMTT CLUB =====
	// --------lamtt add-----------
	public long updateApiClub(ClubObj club_ob,long clubId) {
		
		ContentValues clubValues = new ContentValues();
		clubValues.put(Club.CLUB_ID, club_ob.getExtId());
	
		clubValues.put(Club.CLUB_EXT_TYPE, club_ob.getExtType());
		clubValues.put(Club.CLUB_NAME, club_ob.getClubName());
		clubValues.put(Club.CLUB_COUNTRY, club_ob.getCountry());

		clubValues.put(Club.CLUB_CITY, club_ob.getCity());
		clubValues.put(Club.CLUB_ADDRESS, club_ob.getAddress());
		clubValues.put(Club.CLUB_URL, club_ob.getUrl());
		clubValues.put(Club.CLUB_PHONE, club_ob.getPhoneNumber());
		clubValues.put(Club.CLUB_DELETEED, false);
		clubValues.put(Club.CLUB_LAT, club_ob.getLat());
		clubValues.put(Club.CLUB_LNG, club_ob.getLng());
		//clubValues.put(Club.CLUB_CREATED_DATE, getNow());
		clubValues.put(Club.CLUB_MODIFIED_DATE, getNow());
		
		String whereClause = Club._ID + " = ?";
		String[] whereArgs = new String[] { Long.toString(clubId) };
		return getWritableDatabase().update(ScoreDetail.TABLE_NAME, clubValues,
				whereClause, whereArgs);
	}
	public long insertClub(ClubObj club_ob) {
        YgoLog.i(TAG,"insertClub id=" + club_ob.getExtId() +"; ext_type=" + club_ob.getExtType());
		ContentValues clubValues = new ContentValues();
		clubValues.put(Club.CLUB_ID, club_ob.getExtId());
	
		clubValues.put(Club.CLUB_EXT_TYPE, club_ob.getExtType());
		clubValues.put(Club.CLUB_NAME, club_ob.getClubName());
		clubValues.put(Club.CLUB_COUNTRY, club_ob.getCountry());

		clubValues.put(Club.CLUB_CITY, club_ob.getCity());
		clubValues.put(Club.CLUB_ADDRESS, club_ob.getAddress());
		clubValues.put(Club.CLUB_URL, club_ob.getUrl());
		clubValues.put(Club.CLUB_PHONE, club_ob.getPhoneNumber());
		clubValues.put(Club.CLUB_DELETEED, false);
		clubValues.put(Club.CLUB_LAT, club_ob.getLat());
		clubValues.put(Club.CLUB_LNG, club_ob.getLng());
		clubValues.put(Club.CLUB_CREATED_DATE, getNow());
		clubValues.put(Club.CLUB_MODIFIED_DATE, getNow());
		return getWritableDatabase().insert(Club.TABLE_NAME, null,
				clubValues);
	}
	public void deleteClub(long clubId) {
		String whereClause = Club._ID + " = ?";
		String[] whereArgs = new String[] { "" + clubId };
		getWritableDatabase().delete(Club.TABLE_NAME, whereClause,
				whereArgs);
	}
//	public ClubCursor getClubCusorByID(long clubId) {
//		SQLiteDatabase db = getReadableDatabase();
//		SQLiteQueryBuilder qb = ScoreDetailCursor.getQueryBuilder();
//		qb.appendWhere(Club._ID + " = " + clubId);
//		ClubCursor c = (ClubCursor) qb.query(db, null, null,
//				null, null, null, null);
//		c.moveToFirst();
//		return c;
//	}
	//LAMTT update
	public ClubCursor getClubCusorByID(long clubId) {
		SQLiteDatabase db = getReadableDatabase();
		//SQLiteQueryBuilder qb = ScoreDetailCursor.getQueryBuilder();
		SQLiteQueryBuilder qb = ClubCursor.getQueryBuilder();
		qb.appendWhere(Club._ID + " = " + clubId);
		ClubCursor c = (ClubCursor) qb.query(db, null, null,
				null, null, null, null);
		c.moveToFirst();
		return c;
	}
	
	public ClubCursor getAllClub() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ClubCursor.getQueryBuilder();
		String orderBy = " created DESC";
		ClubCursor c = (ClubCursor) qb.query(db, null, null, null, null,
				null, orderBy);
		c.moveToFirst();
		return c;
	}
	public ClubCursor getHistoryClub() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = ClubCursor.getQueryBuilder();
		qb.appendWhere(Club.CLUB_DELETEED + " = " + 0);
		qb.appendWhere(" and "+Club.CLUB_NAME + " not like '%"+ Constant.CLUB_NAME_DEFAULT +"%'" );
		qb.appendWhere(" and "+Club.CLUB_NAME + " not like '%"+ Constant.COURSE_NAME_DEFAULT +"%'" );
		
		//ThoLH 17/05/2012 show newest record for each club
		String groupBy = " ext_id";
		String orderBy = " created DESC";
		ClubCursor c = (ClubCursor) qb.query(db, null, null, null, groupBy,
				null, orderBy);
		c.moveToFirst();
		return c;
	}
	
	@Override
	public synchronized void close(){
		super.close();
	}

}
