package com.asai24.golf.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class Golf {
	public static final String AUTHORITY_BASE = "com.example.Golf";

	public static final class Player implements BaseColumns {
		public static final String TABLE_NAME = "players";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.player";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.player";

		public static final String NAME = "name";
		public static final String SERVER_ID = "server_id";
		public static final String OWNNER_FLAG = "ownner_flag";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		public static final String DEL_FLAG = "del_flag";
		public static final String PLAYER_HDCP = "player_hdcp";

		public static final String DEFAULT_SORT_ORDER = NAME;

		public static final long OWNER_ID = 1;
	}

	public static final class Round implements BaseColumns {
		public static final String TABLE_NAME = "rounds";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.round";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.round";

		public static final String COURSE_ID = "course_id";
		public static final String TEE_ID = "tee_id";
		public static final String RESULT_ID = "result_id";
		public static final String YOURGOLF_ID = "yourgolf_id";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		public static final String UPDATE_DATE = "update_date";
		public static final String PLAYING = "playing";
		public static final String HALL = "hole";
		public static final String HALL_COUNT = "hole_count";
		public static final String WEATHER = "weather";
		public static final String ROUND_DELETE = "round_del";
		public static final String DEFAULT_SORT_ORDER = CREATED_DATE;
		/*ThuNA 2013/04/09 ADD-S*/
		public static final String MEMO = "memo";
        public static final String LIVE_ENTRY_ID = "live_entry_id";
		public static final String LIVE_ID = "live_id";
		/*ThuNA 2013/04/09 ADD-E*/
		
		public static final class COMPLETION_STATUS {
			public static final int ALL_COMPLETED = 0; // 1-18ホールのスコア入力済み
			public static final int FRONT_COMPLETED = 1; // 1-9ホールのスコア入力済み
			public static final int BACK_COMPLETED = 2; // 10-18ホールのスコア入力済み
			public static final int NOT_COMPLETED = 3; // 上記以外
		}
	}
	
	public static final class RoundPlayer implements BaseColumns {
		public static final String TABLE_NAME = "round_player";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.round_player";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.round_player";

		public static final String _ID = "_id";
		public static final String ROUND_ID = "round_id";
		public static final String PLAYER_ID = "player_id";
		public static final String PLAYER_HDCP = "player_hdcp";
        public static final String LIVE_PLAYER_ID = "live_player_id";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
        public static final String PLAYER_GOAL = "player_goal";
		
		public static final String DEFAULT_SORT_ORDER = ROUND_ID;

	}

	public static final class Course implements BaseColumns {
		public static final String TABLE_NAME = "courses";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.course";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.course";
		
		public static final String _ID = "_id";
		public static final String COURSE_NAME = "course_name";
		public static final String CLUB_ID = "club_id";
		public static final String CLUB_NAME = "club_name";
		public static final String COURSE_OOB_ID = "course_oob_id";
		public static final String COURSE_YOURGOLF_ID = "course_yourgolf_id";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		//NAMLH del_flag
		public static final String DEL_FLAG = "del_flag";

		public static final String DEFAULT_SORT_ORDER = COURSE_NAME;

		public enum ExtType {
			OobGolf("oobgolf"), YourGolf("yourgolf"), YourGolf2("yourgolf2");
			private String name;

			private ExtType(String name) {
				this.name = name;
			}

			public String toString() {
				return name;
			}
		}
	}
// LAMTT  add clup class
	public static final class Club implements BaseColumns {
		public static final String TABLE_NAME = "club";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.club.dir/vnd.example.club";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.club.item/vnd.example.club";

		public static final String CLUB_URL = "url";
		public static final String CLUB_ID = "ext_id";
		public static final String _ID = "_id";
		public static final String CLUB_NAME = "name";
		public static final String CLUB_ADDRESS = "address";
		public static final String CLUB_CITY = "city";

		public static final String CLUB_COUNTRY = "country";
		public static final String CLUB_PHONE = "phone";
		
		
		public static final String CLUB_EXT_TYPE = "ext_type";
		public static final String CLUB_LAT = "lat";
		public static final String CLUB_LNG = "lng";	
		public static final String CLUB_CREATED_DATE = "created";
		public static final String CLUB_MODIFIED_DATE = "modified";
		public static final String CLUB_DELETEED = "del_flag";
		
		public static final String DEFAULT_SORT_ORDER = CLUB_NAME;

		
		
		
	}
	
	
	public static final class HistoryCache implements BaseColumns {
		public static final String TABLE_NAME = "historycache";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String _ID = "_id";
		public static final String CLUB_NAME = "club_name";
		public static final String TOTAL_SHOT = "total_shot";
		public static final String TOTAL_PUTT = "total_putt";
		public static final String PLAYDATE = "playdate";
		public static final String PLAYING = "playing";
		/*ThuNA 2013/03/06 ADD-S*/
		public static final String GORA_SCORE_ID = "gora_score_id";
		public static final String MEMO = "memo";
		/*ThuNA 2013/03/06 ADD-E*/
	}
	
	public static final class Tee implements BaseColumns {
		public static final String TABLE_NAME = "tees";
		public static final String NAME = "name";
		public static final String COURSE_ID = "course_id";
		public static final String TEE_OOB_ID = "tee_oob_id";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
	}

	public static final class Hole implements BaseColumns {
		public static final String TABLE_NAME = "holes";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.hole";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.hole";

		public static final String TEE_ID = "tee_id";
		public static final String HOLE_NUMBER = "hole_number";
		public static final String PAR = "par";
		public static final String WOMEN_PAR = "women_par";
		public static final String YARD = "yard";
		public static final String HANDICAP = "handicap";
		public static final String WOMEN_HANDICAP = "women_handicap";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";

		public static final String DEFAULT_SORT_ORDER = HOLE_NUMBER;
	}

	public static final class Score implements BaseColumns {
		
		public static final String TABLE_NAME = "scores";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.score";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.score";

		public static final String PLAYER_ID = "player_id";
		public static final String ROUND_ID = "round_id";
		public static final String HOLE_ID = "hole_id";
		public static final String HOLE_SCORE = "hole_score";
		public static final String GAME_SCORE = "game_score";
		public static final String FAIRWAY = "fairway";
		public static final String TEE_OFF_CLUB = "tee_off_club";
		public static final String SAND_SHOT = "sand_shot";
		public static final String WATER_HAZARD = "water_hazard";
		public static final String OB = "ob";
		public static final String PUTT_DISABLED = "putt_disabled";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		public static final String DEFAULT_SORT_ORDER = CREATED_DATE;
	}

	public static final class ScoreDetail implements BaseColumns {
		public static final String TABLE_NAME = "score_details";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.score_detail";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.score_detail";

		public static final String SCORE_ID = "score_id";
		// public static final String SHOT_TYPE = "shot_type";
		public static final String SHOT_NUMBER = "shot_number";
		public static final String GPS_LATITUDE = "gps_latitude";
		public static final String GPS_LONGITUDE = "gps_longitude";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		// TODO clubテーブル作る？
		public static final String CLUB = "club";
		// TODO shot_resultテーブル作る？
		public static final String SHOT_RESULT = "shot_result";

		public static final String DEFAULT_SORT_ORDER = CREATED_DATE;
	}
	// LAMTT ADD------
	public static final class TempScore implements BaseColumns {
		public static final String TABLE_NAME = "temp_scores";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.score";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.score";

		public static final String PLAYER_ID = "player_id";
		public static final String ROUND_ID = "round_id";
		public static final String HOLE_ID = "hole_id";
		public static final String HOLE_SCORE = "hole_score";
		public static final String GAME_SCORE = "game_score";
		public static final String CREATED_DATE = "created";
		
		public static final String MODIFIED_DATE = "modified";
		public static final String FAIRWAY = "fairway";
		public static final String TEE_OFF_CLUB = "tee_off_club";
		public static final String SAND_SHOT = "sand_shot";
		public static final String WATER_HAZARD = "water_hazard";
		public static final String OB = "ob";
		public static final String PUTT_DISABLED = "putt_disabled";
		public static final String DEFAULT_SORT_ORDER = CREATED_DATE;
	}

	public static final class TempScoreDetail implements BaseColumns {
		public static final String TABLE_NAME = "temp_score_details";
		public static final String AUTHORITY = AUTHORITY_BASE + "."
				+ TABLE_NAME;

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.score_detail";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.score_detail";

		public static final String SCORE_ID = "score_id";
		// public static final String SHOT_TYPE = "shot_type";
		public static final String SHOT_NUMBER = "shot_number";
		public static final String GPS_LATITUDE = "gps_latitude";
		public static final String GPS_LONGITUDE = "gps_longitude";
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		// TODO clubテーブル作る？
		public static final String CLUB = "club";
		// TODO shot_resultテーブル作る？
		public static final String SHOT_RESULT = "shot_result";

		public static final String DEFAULT_SORT_ORDER = CREATED_DATE;
	}
	
	//------------

//	public static final class Trace implements BaseColumns {
//		public static final String TABLE_NAME = "traces";
//		public static final String AUTHORITY = AUTHORITY_BASE + "."
//				+ TABLE_NAME;
//
//		public static final Uri CONTENT_URI = Uri.parse("content://"
//				+ AUTHORITY);
//		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.trace";
//		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.trace";
//
//		public static final String ACTIVITY = "activity";
//		public static final String CREATED_DATE = "created";
//		public static final String MODIFIED_DATE = "modified";
//
//		public static final String LOG = "log";
//		public static final String TIME = "time";
//		public static final String ID = "id";
//		// 場所はここでいい？
//		public static final int ON_CREATE = 1;
//		public static final int PLAY_GOLF = 2;
//		public static final int HISTORIES = 3;
//		public static final int SEARCH_COURSE = 4;
//		public static final int PLAY = 5;
//		public static final int HOLE_MAP = 6;
//		public static final int SCORE_CARD = 7;
//	}

	private interface DeleteTableColumns {
		public static final String DELETED_ID = "deleted_id";
		public static final String DELETED_DATE = "deleted_date";
	}

	public static final class PlayerDelete implements DeleteTableColumns {
		public static final String TABLE_NAME = "player_delete";
	}

	public static final class RoundDelete implements DeleteTableColumns {
		public static final String TABLE_NAME = "round_delete";
	}

	public static final class CourseDelete implements DeleteTableColumns {
		public static final String TABLE_NAME = "course_delete";
	}

	public static final class TeeDelete implements DeleteTableColumns {
		public static final String TABLE_NAME = "tee_delete";
	}

	public static final class HoleDelete implements DeleteTableColumns {
		public static final String TABLE_NAME = "hole_delete";
	}

	public static final class ScoreDelete implements DeleteTableColumns {
		public static final String TABLE_NAME = "score_delete";
	}

	public static final class ScoreDetailDelete implements DeleteTableColumns {
		public static final String TABLE_NAME = "score_detail_delete";
	}
	

}
