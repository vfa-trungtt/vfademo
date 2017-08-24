package com.asai24.golf;

import java.io.Serializable;

public class Constant {

    // Base URLs
    public static final String URL_YOURGOLF;
    public static final String URL_GOLFDB;
//    public static final String URL_S3_BASE;
    public static final String URL_YGO_LIVE_BASE;
//    public static final String URL_S3_AMAZONAWS;
    public static final String URL_SUVT_BASE;
    public static final String URL_GOLF_DAY_BASE;
    public static final String URL_GET_MOVIE_THUMB;
    public static final String API_KEY_MOVIE;
    public static final String REPRO_APP_TOKEN;
    public static final String URL_IMPORTANT_INFORMATION;
    public static final String URL_BASE_NEWS_TAB;

    // Base URLs END
//
//    public static final String URL_FORGOT_PASSWORD;
//    public static final String BASE_URL_TOP_AD;
//    public static final String URL_BANNER_ADMOD;
//    public static final String URL_YOURGOLF_PROFILE;
//    public static final String URL_GALLERY_JSON_URL;
//    public static final String URL_BANNER_SCORECARD;
//    public static final String URL_IMAGE_SHARE;
    public static final String IN_APP_BILLING_ITEM;
    public static final String SUBSCRIPTION_ITEM_SCORE = "com.gnp.production.purchase.gn002";
    public static final String SUBSCRIPTION_ITEM_TV = "com.gnp.production.purchase.gn999";
    public static final String SUBSCRIPTION_ITEM_TV_ANNUAL = "com.gnp.production.purchase.gn005";
    public static final String URL_LIVE_LEADERBOARD;
    public static final String FB_APP_ID;
    public static final String GOOGLE_SENDER_ID;  // Google project id
//    public static final String APP_TYPE;

    public static final String URL_CROP_IMAGE ="lIVE_CROP_IMAGE_PATH";
    public static final String CHECK_EDIT_PHOTO ="CHECK_EDIT_PHOTO_DETAIL";

    public static final String URL_LOAD_IMAGE;
    public static final String URL_LOAD_DETAIL_PAGE_MOVIE;

    public static final String URL_SPECIAL_PAGE;
    public static final String APP_VADOR_KEY;
    public static final String URL_IMPORTANT_INFORMATION_TAG = "/public/appstart_redirect.html";
    public static  String URL_GORA_CLUB_ID;
    public static String URL_PREFIX_TV_DRILL_YGO;
    public static String URL_PREFIX_TV_DRILL_MOVIES;
    public static String URL_PREFIX_IMAGE_DRILL;
    public static String DRILL_TRIAL_CODE_IN_SCHEME;
    public static String DISPLAY_SUBMENU_FIRST_TIME = "DISPLAY_SUBMENU_FIRST_TIME";
    public static String IS_UPDATE_VIEW = "UPDATE_VIEW";

    public static String URL_PREFIX_PUBLIC_DRILL_TAG;
    public static final String IS_TAP ="is_tap_in_golf_top";
//    public static final String URL_SAVING_BILLING = URL_GOLFDB + "/v2/android/receipts.json";


    public static final String PARAM_ID = "PARAM_ID";

    //Crashlytics Custom Key Environment
    public static final String CRASHLYTICS_ENIRONMENT;

    // アプリケーションID
    public final static String YAHOO_CLIEN_ID;
    public static String URL_DRILL_SHARE;
    public static String APP_VADOR_KEY_ROUND_RERSER;
    public static String APP_VADOR_KEY_PLAY_HISTORY;
    public static String APP_VADOR_KEY_CAMPAIN;
    public static String URL_CAMPAIN_FRAGMENT;
    public static String URL_VIDEO_PLAY;
    public static String URL_VIDEO_LIVE;
    public static String URL_PREFIX_PLAY_VIDE0;
    public static String APP_VADOR_KEY_NEWS;

    public static boolean isDebug = true;


    static {
        if (isDebug) {
            URL_YOURGOLF = "https://golfuser-stage.yourgolf-online.com";
            URL_GOLFDB = "https://api-stage.yourgolf-online.com";
            URL_YGO_LIVE_BASE = "https://live-stage.golfnetwork.co.jp";
            IN_APP_BILLING_ITEM = "test.service";
            YAHOO_CLIEN_ID = "dj0zaiZpPUkyckd0anZhMHA0ViZzPWNvbnN1bWVyc2VjcmV0Jng9Y2E-";
            FB_APP_ID = "499486623474805";
            GOOGLE_SENDER_ID = "695222133464";

            URL_SUVT_BASE = "http://gn-stg-apfront.suvt.tv";
            URL_SUVT_BUY = "https://gn-stg-apfront.suvt.tv/buy";
            URL_SUVT_FAILURE = "gn-stg-apfront.suvt.tv/failure";
            URL_SUVT_XVIE_AUTH = "gn-stg-apfront.suvt.tv/xvie_auth";
            GNTV_DOMAIN = "gn-stg-apfront.suvt.tv";

            XVIE_APP_SIGNATURE = "269344BD37F6A6EBB6CF87944386E8D1D1D07939";

          //  URL_NOTIFICATION_MESSAGE = "https://s3.amazonaws.com/ygo-staging/public/notifications.json";
            URL_TOP_BANNER_REDIRECT = "https://s3.amazonaws.com/ygo-staging/public/redirect.html?url=";

            URL_GOLF_DAY_BASE = "http://ygo-staging.s3-website-us-east-1.amazonaws.com";
            URL_TV_DOMAIN = "gn-stg-front.suvt.tv";
            URL_TV_DOMAIN_APP = "gn-stg-apfront.suvt.tv";

            CRASHLYTICS_ENIRONMENT = "STAGING";
            PP_APP_KEY = "a3dfe9da-4219-4c8b-b5d3-5924a363382d";
            URL_GET_MOVIE_THUMB ="http://gn-stg-api.suvt.tv";
            API_KEY_MOVIE ="stg7e5e8bfff4f22";
            URL_LOAD_IMAGE = "http://gn-stg-front.suvt.tv";
            URL_LOAD_DETAIL_PAGE_MOVIE ="http://gn-stg-front.suvt.tv/p/";
            REPRO_APP_TOKEN ="106a72a3-ef1e-4069-ae4a-b508701d0fa0";
            URL_SPECIAL_PAGE ="http://ygo-staging.s3-website-us-east-1.amazonaws.com/";
            URL_IMPORTANT_INFORMATION ="http://ygo-staging.s3-website-us-east-1.amazonaws.com"+ URL_IMPORTANT_INFORMATION_TAG;
            URL_GORA_CLUB_ID = URL_GOLFDB+"/v1/histories/gora_club_ids.json";
            APP_VADOR_KEY ="0ba6f60dcddb6018a845e070e31e4640";
            URL_PREFIX_TV_DRILL_YGO ="https://gn-stg-api.suvt.tv/";
            URL_PREFIX_TV_DRILL_MOVIES ="https://gn-stg-movieserver.yourgolf-online.com/";
            URL_PREFIX_IMAGE_DRILL ="https://gn-stg-front.suvt.tv";
            URL_PREFIX_PUBLIC_DRILL_TAG ="https://s3.amazonaws.com/ygo-staging/public/";
            DRILL_TRIAL_CODE_IN_SCHEME ="32307342";
            URL_DRILL_SHARE = "https://score-staging.golfnetwork.co.jp/drills/";
            APP_VADOR_KEY_ROUND_RERSER = "214bb666b3414fa7538a15474798fbe7";
            APP_VADOR_KEY_PLAY_HISTORY = "f29ad8cbe0206888bd4fe7677a74ac8c";
            APP_VADOR_KEY_CAMPAIN="0ba6f60dcddb6018a845e070e31e4640";
            URL_CAMPAIN_FRAGMENT ="http://ygo-staging.s3-website-us-east-1.amazonaws.com/public/campaign_redirect.html";
            URL_VIDEO_PLAY =" http://gn-stg-apfront.suvt.tv/apptop_player";
            URL_VIDEO_LIVE =" http://gn-stg-apfront.suvt.tv/applive_player";
            URL_PREFIX_PLAY_VIDE0 ="http://ygo-staging.s3.amazonaws.com";
            APP_VADOR_KEY_NEWS = "07ba62e217771c1b72c120a4721b82cd";
            URL_BASE_NEWS_TAB = "https://gw-staging.yourgolf-online.com";
        } else {
            URL_YOURGOLF = "https://golfuser.yourgolf-online.com";
            URL_GOLFDB = "https://api.yourgolf-online.com";
            URL_YGO_LIVE_BASE = "https://live.golfnetwork.co.jp";
            IN_APP_BILLING_ITEM = "com.yourgolf_online.yourgolf.production.dps";
            YAHOO_CLIEN_ID = "dj0zaiZpPTUyeksyd0dBUktLYyZzPWNvbnN1bWVyc2VjcmV0Jng9YjE-";
            FB_APP_ID = "111773938992736";
            GOOGLE_SENDER_ID = "429133680001";

            URL_SUVT_BASE = "http://tv-apfront.golfnetwork.co.jp";
            URL_SUVT_BUY = "https://tv-apfront.golfnetwork.co.jp/buy";
            GNTV_DOMAIN = "tv-apfront.golfnetwork.co.jp";
            URL_SUVT_FAILURE = "tv-apfront.golfnetwork.co.jp/failure";
            URL_SUVT_XVIE_AUTH = "tv-apfront.golfnetwork.co.jp/xvie_auth";

            XVIE_APP_SIGNATURE = "331D579DD3C51D1E41A101DBDBFEA60ADF665A83";

           // URL_NOTIFICATION_MESSAGE = "https://s3.amazonaws.com/ygo-production/public/notifications.json";
            URL_TOP_BANNER_REDIRECT = "https://s3.amazonaws.com/ygo-production/public/redirect.html?url=";
            URL_GOLF_DAY_BASE = "http://ygo-production.s3-website-us-east-1.amazonaws.com";
            URL_TV_DOMAIN = "tv.golfnetwork.co.jp";
            URL_TV_DOMAIN_APP = "tv-apfront.golfnetwork.co.jp";

            CRASHLYTICS_ENIRONMENT = "PRODUCTION";
            PP_APP_KEY = "a1a9ce53-b4a8-4dcd-a6e1-f1a68d32f9e1";
            URL_GET_MOVIE_THUMB ="http://tv-api.golfnetwork.co.jp";
            API_KEY_MOVIE ="pro0cad7c10b68bb";
            URL_LOAD_IMAGE = "https://tv-apfront.golfnetwork.co.jp";
            URL_LOAD_DETAIL_PAGE_MOVIE = "https://tv-apfront.golfnetwork.co.jp/p/";
            REPRO_APP_TOKEN ="a1a97d9b-530d-479b-a885-387e26647449";
            URL_SPECIAL_PAGE ="http://ygo-production.s3-website-us-east-1.amazonaws.com/";
            URL_IMPORTANT_INFORMATION ="http://ygo-production.s3-website-us-east-1.amazonaws.com"+ URL_IMPORTANT_INFORMATION_TAG;
            URL_GORA_CLUB_ID = URL_GOLFDB+"/v1/histories/gora_club_ids.json";

            APP_VADOR_KEY ="771c657f04816082dd6750247b9479f2";
            URL_PREFIX_TV_DRILL_YGO ="https://tv-api.golfnetwork.co.jp/";
            URL_PREFIX_TV_DRILL_MOVIES ="https://gn-movieserver.yourgolf-online.com/";
            URL_PREFIX_IMAGE_DRILL ="https://tv.golfnetwork.co.jp";
            URL_PREFIX_PUBLIC_DRILL_TAG ="https://s3.amazonaws.com/ygo-production/public/";
            DRILL_TRIAL_CODE_IN_SCHEME ="20316122";
            URL_DRILL_SHARE = "https://score.golfnetwork.co.jp/drills/";
            APP_VADOR_KEY_ROUND_RERSER = "64d2c66be59f3ae67ba8454ea9c4b99b";
            APP_VADOR_KEY_PLAY_HISTORY = "8819cf6e235556cef8a9c6f5856d5c4b";
            APP_VADOR_KEY_CAMPAIN="771c657f04816082dd6750247b9479f2";
            URL_CAMPAIN_FRAGMENT ="http://ygo-production.s3-website-us-east-1.amazonaws.com/public/campaign_redirect.html";
            URL_VIDEO_PLAY ="http://tv-apfront.golfnetwork.co.jp/apptop_player";
            URL_VIDEO_LIVE ="http://tv-apfront.golfnetwork.co.jp/applive_player";
            URL_PREFIX_PLAY_VIDE0 ="http://ygo-production.s3.amazonaws.com";
            APP_VADOR_KEY_NEWS = "9c2de4f2ceb64e15b224481d57e5acf3";
            URL_BASE_NEWS_TAB = "https://gw.yourgolf-online.com";
        }

//        if (GolfApplication.isPremium()) {
//            URL_S3_BASE = "https://ygo-premium.s3.amazonaws.com";
//            URL_S3_AMAZONAWS = "http://ygo-premium.s3-website-us-east-1.amazonaws.com";
//        } else if (GolfApplication.isDebug()) {
//            URL_S3_BASE = "https://ygo-staging.s3.amazonaws.com";
//            URL_S3_AMAZONAWS = "http://ygo-staging.s3-website-us-east-1.amazonaws.com";
//        } else if (GolfApplication.isPuma()) {
//            URL_S3_BASE = "https://ygo-puma.s3.amazonaws.com";
//            URL_S3_AMAZONAWS = "http://ygo-puma.s3-website-us-east-1.amazonaws.com";
//        } else {
//            URL_S3_BASE = "https://ygo-production.s3.amazonaws.com";
//            URL_S3_AMAZONAWS = "http://ygo-production.s3-website-us-east-1.amazonaws.com";
//        }
//
//        if (GolfApplication.isPuma())
//            APP_TYPE = "puma";
//        else if (GolfApplication.isPremium())
//            APP_TYPE = "paid";
//        else
//            APP_TYPE = "free";

//        BASE_URL_TOP_AD = URL_S3_BASE + "/public/banners/";
//        URL_BANNER_ADMOD = BASE_URL_TOP_AD + "banner01.json";
//        URL_GALLERY_JSON_URL = URL_S3_BASE + "/public/gallery/gallery.json";
//        URL_IMAGE_SHARE = URL_S3_BASE + "/public/score_sharing/score_sharing.json";
//        URL_BANNER_SCORECARD = URL_S3_BASE + "/public/score_card_banner/score_card_banner.json";
//
//        if (GolfApplication.isPuma()) {
//            URL_FORGOT_PASSWORD = URL_YOURGOLF + "/users/password/new";
//            URL_YOURGOLF_PROFILE = URL_YOURGOLF + "/settings/account";
//        } else {
//            URL_FORGOT_PASSWORD = URL_YOURGOLF + "/account/forgot_password";
//            URL_YOURGOLF_PROFILE = URL_YOURGOLF + "/setting/profile";
//        }
        // This part maybe have to remove

        // Live LeaderBoard URL
        URL_LIVE_LEADERBOARD = URL_YGO_LIVE_BASE + "/lives/:live_id/leader_board";


    }
    //Change term of use config
    public static final boolean TERM_OF_USE_CHANGED = false;
    ///////////////////////////
    public static final int DEFAULT_LIMIT = 20;

    // ////////////////////////

    ////////
//    public static final String URL_NEWS = URL_S3_AMAZONAWS + "/public/news.html";
//    public static final String URL_USAGE = URL_S3_AMAZONAWS + "/public/usage.html";
//    public static final String URL_VESION = URL_S3_AMAZONAWS + "/public/version-android.html";
//    public static final String URL_RECOMMENT = URL_S3_AMAZONAWS + "/public/news2.html";
//    public static final String URL_AGENCY_GUIDE_PAID = URL_S3_AMAZONAWS + "/public/agency_guide_paid.html";
//    public static final String URL_AGENCY_GUIDE_FREE = URL_S3_AMAZONAWS + "/public/agency_guide_free.html";
//    public static final String URL_PURCHASE_SCORE_DETAIL = URL_S3_AMAZONAWS + "/public/purchase_score.html";
//    public static final String URL_PURCHASE_TV_DETAIL = URL_S3_AMAZONAWS + "/public/purchase_tv.html";
//    public static final String URL_PURCHASE_TV_YEARLY_DETAIL = URL_S3_AMAZONAWS + "/public/purchase_annual.html";
//
//    public static final String URL_CAMPAIGN_PAGE = URL_S3_AMAZONAWS + "/public/campaign_redirect.html";
    //////////
    /**
     * ****** Url sourse search *********
     */
    public static final String URL_COURSE_SEARCH = URL_GOLFDB + "/v1/clubs/search.json";
    public static final String URL_TEE_SEARCH = URL_GOLFDB + "/v1/courses/courseID/yardage.json";
    public static final String URL_GET_CLUB_INFO = URL_GOLFDB + "/v1/clubs/:id.json";
    /**
     * ****** Url Round download - upload *********
     */
    public static final String URL_SCORE_UPLOAD = URL_GOLFDB + "/v1/rounds/score_upload.json";
    // public static final String URL_SCORE_DOWNLOAD = URL_GOLFDB +
    // "/v1/rounds/id_download/score_download.json";
    public static final String URL_SCORE_DOWNLOAD = URL_GOLFDB
            + "/v2/rounds/id_download/score_download.json";
    /**
     * ****** Url Round history *********
     */
    public static final String URL_HISTORY_LIST_HISTORY_JSON = URL_GOLFDB
            + "/v1/histories/list_history.json";
    public static final String URL_HISTORY_TOTAL_HISTORY = URL_GOLFDB
            + "/v1/histories/total_play_history.json";
    public static final String URL_HISTORY_DELETE_LIST_HISTORY_JSON = URL_GOLFDB
            + "/v1/rounds/roundid.json";
    public static final String URL_ROUND_DATE = URL_GOLFDB + "/v1/rounds/NEWID.json";
    public static final String URL_ROUND_DATE_UPDATE_ST2 = URL_GOLFDB + "/v2/rounds/NEWID.json";
    /**
     * ****** Url Club history *********
     */
    public static final String URL_CLUBS_LIST_HISTORY = URL_GOLFDB + "/v1/club_histories.json";
    public static final String URL_CLUBS_COURSE_HISTORY = URL_GOLFDB
            + "/v1/club_histories/clubID/course_detail.json";
    public static final String URL_CLUBS_DELETE_HISTORY = URL_GOLFDB
            + "/v1/club_histories/clubID.json";
    /**
     * ****** Url Players *********
     */
    public static final String URL_SELECT_PLAYER_HISTORY_LIST_JSON = URL_GOLFDB + "/v1/players.json";
    public static final String URL_NEW_PLAYER_UPLOAD = URL_GOLFDB + "/v2/players.json";
    public static final String URL_EDIT_PLAYER_NAME = URL_GOLFDB + "/v1/players/playerID.json";
    public static final String URL_SELECT_PLAYER_HANDICAP = URL_GOLFDB + "/v1/players/:id.json";
    /**
     * ****** Url Analysis *********
     */
    public static final String URL_SCORE_ANALYSIS = URL_GOLFDB + "/v1/analysis/score_analysis.json";
    public static final String URL_SCORE_ANALYSIS_V2 = URL_GOLFDB + "/v2/analysis/score_analysis.json";
    /**
     * ****** Url Gift *********
     */
    public static final String URL_HISTORY_GIFTS_SCORE_CARD = URL_GOLFDB
            + "/v1/gifts/score_card.json";
    public static final String URL_GIFT_GET_PLAYERS = URL_GOLFDB + "/v1/gifts/get_players.json";
    public static final String URL_GIFT_SEND_GIFT = URL_GOLFDB + "/v1/gifts/send_gift.json";
    public static final String URL_GIFT_LIST_SENDERS = URL_GOLFDB + "/v1/gifts/list_senders.json";
    public static final String URL_REMOVE_GIFT_SENDER = URL_GOLFDB + "/v1/gifts/dismiss.json";
    public static final String URL_GIFT_LIST_GIFTS = URL_GOLFDB + "/v1/gifts/list_gifts.json";
    public static final String URL_GIFT_RECEIVE = URL_GOLFDB + "/v1/gifts/receive.json";
    public static final String URL_ADD_FRIENDS_UPLOAD = URL_GOLFDB + "/v1/gifts/add_friends.json";
    public static final String URL_UPDATE_PLAYER_UPLOAD = URL_GOLFDB
            + "/v1/gifts/update_player.json";
    public static final String URL_LIST_RECEIVE_PLAYERS = URL_GOLFDB
            + "/v1/gifts/list_received_player.json";
    public static final String URL_V1_REVIEW_STATUS = URL_GOLFDB + "/v1/review_status.json";
    public static final String URL_V1_CLUBS = URL_GOLFDB + "/v1/clubs";
    public static final String URL_V1_SCORE_CARDS = URL_GOLFDB + "/v1/score_cards";
    public static final String URL_V1_AUTH = URL_GOLFDB + "/v1/auth";
    public static final String URL_V1_AUTH_EX = URL_GOLFDB + "/v1/external/auth";
    /**
     * ******* Url Live ************
     */
    public static final String URL_LIVE_LIST = URL_GOLFDB + "/v1/lives/participating.json";
    public static final String URL_LIVE_JOIN = URL_GOLFDB + "/v1/lives/join.json";

    public static final String URL_LIVE_PLAYER_HANDICAP = URL_GOLFDB + "/v1/live_players/:id.json";

    public static final String URL_LIVE_ENTRY_HANDICAP = URL_GOLFDB + "/v1/live_entries/:id.json";
    public static final String URL_LIVE_SCORES = URL_GOLFDB
            + "/v1/live_entries/:id/live_scores.json";
    public static final String URL_LIVE_BATCH = URL_GOLFDB + "/batch";
    public static final String URL_LIVE_URL_PLAYER = "/v1/live_players.json";
    public static final String URL_LIVE_COURSE_HOLE = URL_GOLFDB + "/v1/clubs/:id/courses.json";

    // DatPV
    public static final String URL_LOGIN = URL_GOLFDB
            + "/v1/rakuten/account/sign_in.json?auth_token=";
    public static final String URL_CHECK_LOGIN = URL_GOLFDB
            + "/v1/rakuten/account/check.json?auth_token=";
    public static final String URL_LOGOUT = URL_GOLFDB
            + "/v1/rakuten/account/sign_out.json?auth_token=";
    public static final String URL_BROWSER_REGISTER = "https://booking.gora.golf.rakuten.co.jp/?menu=id&act=login&query=";
    public static final String SAVING_PHOTO_FILENAME = "/%s.jpg";
    public static final String SAVING_PHOTO_FOLDER = "/yourgolf";
    public static final String SAVING_DEFAULT_PHOTO_FOLDER = "/yourgolf/default";
    public static final String SHARED_PHOTO_ASSETS_FOLDER = "default_shared_images/";
    public static final String URL_SHARE_SCORE_JA = "http://bit.ly/11jY9GH";
    public static final String URL_SHARE_SCORE_EN = "http://bit.ly/11I2tPQ";
    public static final String TOTAL_ROUNDS = "TotalRounds";
    public static final String BEST_SCORE = "BestScore";
    public static final String AVERAGE = "Average";
    public static final String LABEL_SCORE = "LabelScore";
    public static final String DECIMAL = "decimal";
    public static final String LAST_3_MONTHS = "Last 3 months";
    public static final String LAST_6_MONTHS = "Last 6 months";
    public static final String ENTIRE_PERIOD = "entire period";
    public static final String LAST_3_MONTHS_JA = "直近３ヶ月";
    public static final String LAST_6_MONTHS_JA = "直近６ヶ月";
    public static final String ENTIRE_PERIOD_JA = "全期間";
    public static final String JAPAN_LANGUAGE = "日本語 (日本)";
    public static final String ITEM_CHECK = "ItemCheck";
    public static final String FLG_API_REQUEST_HISTORY = "FlgApiRequestHistory";
    public static final String DATE_MODIFIED = "Last-Modified";
    public static final String KEY_LAST_MODIFIED_DATE = "LastModifiedDate";
    public static final String KEY_UPDATE_GALLERY = "UpdateGallery";
    public static final String EAGLE = "eagle";
    public static final String BIRDIE = "birdie";
    public static final String PAR = "par";
    public static final String BOGEY = "bogey";
    public static final String DOUBLE_BOGEY = "double_bogey";
    public static final String OTHERS = "others";
    public static final String FILE_STRING = "file:///sdcard" + SAVING_PHOTO_FOLDER;
    // End
    // ThuNA
    public static final String URL_PUT_GET_SCORE_CARD = URL_GOLFDB
            + "/v1/rounds/" + PARAM_ID + "/score_card.json";
    public static final String URL_GET_PENDING_PLAYER = URL_GOLFDB + "/v1/gifts/pending.json";
    public static final String URL_GET_PENDING_PLAYER_FROM_GIFT = URL_GOLFDB + "/v1/gifts/id.json";
    public static final String URL_PUT_RESEND_GIFT = URL_GOLFDB + "/v1/gifts/:id/resend.json";
    public static final String URL_POST_NEW_LIVE = URL_GOLFDB + "/v1/lives.json";
    public static final String URL_GET_LIVE_INFO = URL_GOLFDB + "/v1/lives/:id.json";
    public static final String URL_PUT_LIVE_UPDATE = URL_GOLFDB + "/v1/lives/:id.json";
    public static final String URL_LIVE_DELETE_MEMBER = URL_GOLFDB + "/v1/live_entries/id.json";
    public static final String URL_SAVING_BILLING = URL_GOLFDB + "/v2/android/receipts.json";
    public static final String URL_SUMMARY_JSON = "/summary.json";
    public static final String SAVE_MEMO_TEXT_KEY = "save_memo_text_key";

    // End
    public static final String URL_STATISTICS_HISTORY2 = URL_GOLFDB + "/v1/analysis/short_analysis.json";
    public static final String URL_NOTIFICATION = URL_GOLFDB + "/v1/devices.json";

    public static final String FILE_CACHE_TOTAL_HISTORY = "total_history.dat";
    public static final String URL_SPECIAL_PAGE_WEB_VIEW = URL_SPECIAL_PAGE+"public/pgatourlive_free.html";
    public static final String URL_SPECIAL_PAGE_WEB_VIEW_3 = URL_SPECIAL_PAGE+"public/pgatourlive_paid.html";
    //TaiNN
    public static final String URL_CREATE_AGENCY_REQUEST = URL_GOLFDB + "/v1/agency_requests.json";
    public static final String URL_UPDATE_STT_AGENCY_REQUEST = URL_GOLFDB + "/v1/agency_requests/%s.json";
    public static final String URL_SEND_ASAT_ADVERTISING = URL_GOLFDB + "/v1/asat/ad_ids.json";
    ///////////////////////////// notification
    public static final String GOOGLE_REGISTER = "com.google.android.c2dm.intent.REGISTER";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_ACTION = "action";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_START_FROM_PUSH = "start_from_push";
    public static final String NOTIFICATE_REGISTER_ACTION = "Register_Notification";
    public static final String NOTIFICATE_ACTION_ADVERTISE = "ADVERTISE";
    public static final String NOTIFICATE_ACTION_AGENCY_REQUEST_DONE = "AGENCY_REQUEST_DONE";
    public static final String NOTIFICATE_ACTION_BROWSER = "OPEN_BROWSER";
    public static  final String CHECKREVICER_REPRO = "PUSH_RE_PRO_TRUE";
    public static final String REPRO_CHECK_SEND_APP_MESSAGE = "REPRO_CHECK_SEND_APP_MESSAGE";
    public static final String NOTIFICATE_ACTION_DRILL = "PRACTICE_DRILL";
    ///////////////////////////////////////

    public static final String EXT_TYPE_YOURGOLF = "yourgolf";
    public static final String EXT_TYPE_OOBGOLF = "oobgolf";
    public static final String TYPE_YOURGOLE = "yourgolf2";

    public static final Boolean GOOGLE_ANALYTICS_TEST_FLG = false;

    public static final String PLAYER_IDS = "player_ids";
    public static final String PLAYING_COURSE_ID = "playing_course_id";
    public static final String PLAYING_ROUND_ID = "playing_round_id";
    public static final String PLAYING_TEE_ID = "playing_tee_id";
    public static final String PLAYING_HOLE_ID = "playing_hole_id";
    public static final String PLAYING_PAR = "playing_par";
    public static final String PLAYING_HOLE_NUMBER = "playing_hole_number";
    public static final String PLAYING_HOLE_YARDS = "playing_hole_yards";
    public static final String PLAYING_HOLE_HCP = "playing_hole_hcp";
    public static final String SCORE_DETAIL_ID = "score_detail_id";
    public static final String PLAYING_PLAYER_ID = "playing_player_id";
    public static final String PLAYING_COURSE_MAP = "playing_course_map";
    public static final String SCORE_ID = "score_id";
    public static final String PLAY_NINE = "play_nine";
    public static final String REQUEST_SAVE_MEMO = "request_save_memo";
    public static final String ROUND_ID = "round_id";
    public static final String FLAG_HDCP = "flag_hdcp";
    public static final String BIT_MAP_IMAGE = "BIT_MAP_IMAGE";

    public static final String DRILL_DEFAULT_THUMBNAIL ="/imagecache/LGblBagcBwN7pmb1ZmbvY3ImpP9go3IhqP9coJSaMF90nUIgLz5unJjiL2SwnTHiGx9WGHSUEF9ipzqsp2y6MF5dpTpvB2x6ZGgmBwV5BvVinJ1uM2IwLJAbMF85BGxiZP8jYmNiZP8jYmRiZPV7sD==";
    public static final int STATUS_SEND_SUCCESS = 0;// 成功
    public static final int STATUS_SEND_WITH_ZERO_SCORE = 1;// スコアに0あり
    public static final int STATUS_SEND_NO_SETTINGS = 2;// セッション取得不可
    public static final int STATUS_SEND_INVALID_SESSION = 3;//
    public static final int STATUS_SEND_NO_PERMISSION = 4;//
    public static final int STATUS_SEND_ERROR = 9;// 予期せぬエラー（通信など）
    public static final String PLAYING_18_HOLES = "0";
    public static final String PLAYING_9_HOLES = "1";
    public static final int COURSE_SEARCH_PER_PAGE = 20;

    public static final String SCORE_TAG = "score";
    public static final String ERROR_TAG = "error";
    public static final String ID_TAG = "id";
    public static final String BASE_64_ENCODE_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApglG8UgSlTbQiKt2ppDV7gcd2lNJqWYO+zFIeCPoCeTaKJskr02lI2xLoz9bfgOqOm88gAZrSuaoVSgxNmce5DZ4EAJyjIU+dkBVp1lcXwd38cOMTgnu5yo4/bgvAK1Riaf58Zmor0nqMoen+UE2iTzHgZTADSWEIKetWVEnStjalb3A10enPjGA3MQo8rli/5CIKGLRLOfqQiW1/lJfbWhFe7n+rg7X1KrsCdiAVMagKBWszUfOsE52ZqRx7SkzT9PhMiAfOILWbT7kdbU5RjzJwVOIB9PaC6P9KySbhoNV+svjVOjlWIivv0YemDEtM4qGEdPof10sDGI3lVszVQIDAQAB";
    public static final String PURCHASE_INFO = "Purchase_info";
    public static final String RECEIPT_ID = "Receipt_id";
    /*
     * public static final String DRIVER="dr"; public static final String WOOD_3="3w"; public static
     * final String WOOD_4="4w"; public static final String WOOD_5="5w"; public static final String
     * WOOD_7="7w"; public static final String HYBRID="hy"; public static final String IRON_2="2i";
     * public static final String IRON_3="3i"; public static final String IRON_4="4i"; public static
     * final String IRON_5="5i"; public static final String IRON_6="6i"; public static final String
     * IRON_7="7i"; public static final String IRON_8="8i"; public static final String IRON_9="9i";
     * public static final String PITCHING_WEDGE="pw"; public static final String GAP_WEDGE="gw";
     * public static final String SAND_WEDGE="sw"; public static final String LOB_WEDGE="lw";
     */
    public static final String PUTT_SAVE = "Putter";
    public static final String PUTT = "pt";// dont use as value of "hX_tee_club"
    public static final String PUTT_NO_COUNT = "ptt";

    public static final int FAIRWAY = 0;
    public static final int ROUGH = 1;
    public static final int GREEN = 2;
    public static final int BUNKER = 3;
    public static final int HOLE = 4;
    public static final int WATER = 5;

    public static final int TEMP_YARD = 0;
    public static final int TEMP_PAR = 4;


    // CanNC:begin
    public static final String FAIRWAY_LEFT = "left";
    public static final String FAIRWAY_CENTER = "center";
    public static final String FAIRWAY_RIGHT = "right";
    // CanNC:end

    public static final int DIALOG_YES_NO_MESSAGE = 1;


//    public static final int[] FAVICONS = {R.drawable.oob_favicon, R.drawable.asai_favicon,};
//    public static final int[] SITE_NAMES = {R.string.oobgolf_title, R.string.asai_title,};

    // デフォルトロケーション（東京タワー）
    public static final Double DEFAULT_LOCATION_LATITUDE = 35.658671;
    public static final Double DEFAULT_LOCATION_LONGITUDE = 139.7454;

    public static final int MAX_SOCRE = 15; // スコア入力ショット数最大値

    public static final double RADIUS_OF_EARTH_WGS84_METER = 6378137.0;

    // コース検索元識別用
    public static final int OOBGOLF_COURSE = 1; // oobgolfモード
    public static final int YOURGOLF_COURSE = 2; // 日本国内モード
    public static final int HISTORY_COURSE = 3; // 履歴検索

    // v3.3.2 st saikami 20111012
    // 純広告用のバナー掲載位置コード
    public static final int TOP_TITLE = 10; // トップ画面のタイトル画像
    public static final int TOP_TOP = 11; // トップ画面の上部
    public static final int TOP_BOTTOM = 12; // トップ画面の下部
    public static final int SCORE_INPUT_GROUP = 41; // スコア入力：グループ
    public static final int SCORE_INPUT_SINGLE = 42; // スコア入力：シングル

    public static final class IntentKey {

        public static final String SHARE_TYPE = "share_type";
    }

    // v3.3.2 ed saikami 20111012

    // Google Analytics用
    public static final class Gtrack {
        public static final String UA_CODE = "UA-12579430-1";

        public static final String PAGE_SIGN_UP = "Login_New_Id";
        public static final String PAGE_NEW_PROFILE = "Login_New_Profile";
        public static final String EVENT_TOP_GALLERY_SELECTED = "Top_Gallery";
        public static final String PARAMS_SELECTED_PHOTO_URL = "Selected_photo_url";
        public static final String PAGE_TOP = "Top";
        public static final String PAGE_SEARCH = "Play_prepare_club";
        public static final String PAGE_PREPARE_ROUND = "Play_prepare_round";
        public static final String PAGE_PREPARE_PLAYER = "Play_prepare_player";
        public static final String PAGE_PREPARE_NEW_PLAYER = "Play_prepare_newplayer";
        public static final String PAGE_SCORE_ENTRY = "Play_score_input";
        public static final String EVENT_SCORE_FACEBOOK = "Play_score_fb";
        public static final String EVENT_SCORE_TWITTER = "Play_score_tw";
        public static final String PAGE_SCORE_CARD = "Play_score_card";
        public static final String PAGE_SCORE_NOTE = "Play_score_note";
        public static final String PAGE_HISTORY = "History_top";
        public static final String PAGE_HISTORY_DETAIL = "History_detail";
        public static final String PAGE_TOP_GIFT = "Gift_top";
        public static final String PAGE_GIFT_SEND_MAIL = "Gift_send_mail";
        public static final String PAGE_GIFT_ADD_FRIEND = "Gift_add_friend";
        public static final String PAGE_GIFT_SENT_PAGE = "Gift_sent_Page";
        public static final String PAGE_TOTAL_ANALYSIS = "Analysis_all";
        public static final String PAGE_OLD_TOTAL_ANALYSIS = "Analysis_old";
        public static final String PAGE_LIVE = "Live_top";
        public static final String PAGE_TOP_MENU_TV = "Top_Menu_TV";
        public static final String PAGE_PLAY_PREPARE_AGENCY = "Play_prepare_Agency";
        public static final String EVENT_SHOW_NOTIFICATION = "Top_Notice_Message";
        public static final String EVENT_OPEN_GOLF_DAY_FROM_CLUB = "golfnav_open_club";
        public static final String EVENT_OPEN_GOLF_DAY_FROM_HOLE = "golfnav_open_hole";
        public static final String EVENT_OPEN_GNP_FROM_SCHEME = "golfnav_open_scoreinput";
        public static final String EVENT_OPEN_GOLF_DAY_PAGE = "golfnav_store_page";
        public static final String EVENT_OPEN_GOLF_DAY_FROM_HISTORY = "golfnav_open_history";
        public static final String EVENT_HISTORY_DETAIL_SCORE_SHARE = "History_detail_Scoreshare";
        public static final String EVENT_SCORE_CARD = "Score_Card";
        public static final String EVENT_SCORE_SHARE_CARD = "Score_Share_Card";
        public static final String EVENT_SCORE_SHARE_CARD_SHARE = "Score_Share_Card_Share";
        public static final String EVENT_PURCHASE_SCORE_DISPLAY = "Purchase_Score_Display";
        public static final String EVENT_PURCHASE_SCORE_COMPLETED = "Purchase_Score_Completed";
        public static final String EVENT_PURCHASE_TV_DISPLAY = "Purchase_TV_Display";
        public static final String EVENT_PURCHASE_TV_ANNUAL_DISPLAY = "Purchase_Annual_Display";
        public static final String EVENT_PURCHASE_TV_COMPLETED = "Purchase_TV_Completed";
        public static final String EVENT_PURCHASE_TV_ANNUAL_COMPLETED = "Purchase_Annual_Completed";
        public static final String PAGE_TOP_COUPON = "Coupon_Btn_Top";
        public static final String PAGE_REGISTRY_MEMBER_SUCCESS = "Regist_Member";
        public static final String PAGE_TV_BUY_PAGE_DISPLAY = "TV_Buypage_Disp";
        public static final String PAGE_PUSH_YGO_APP_START = "Push_Ygo_AppStart";
        public static final String PAGE_PUSH_PP_APP_START = "Push_PP_AppStart";
        public static final String EVENT_AGENCY_TRIAL_CAMPAIGN_REGIST = "Agency_Trial_Campaign_Regist";
        public static final String EVENT_AGENCY_REQUEST = "Agency_Request";
        public static final String EVENT_OPEN_CAMPAIGN_REGISTRATION_PAGE_NO_USE_EXPERIENCE = "Display_Free_Trial";
        public static final String EVENT_OPEN_CAMPAIGN_REGISTRATION_PAGE_YES_USE_EXPERIENCE = "Display_Used_Trial";
        public static final String EVENT_TAP_FREE_TRIAL_NOW_IN_CAMPAIGN_PAGE = "Tap_Free Trial";
        public static final String EVENT_SCORE_PURCHASE = "ScorePurchase";
        public static final String EVENT_TV_PURCHASE = "TVPurchase";
        public static final String EVENT_TV_ANNUAL_PURCHASE = "AnnualPurchase";
        public static  final String EVENT_PUSH_REPRO_APP_START ="Push_Repro_AppStart";
        public static final String EVENT_SCORE_UPLOAD_COMPLETE = "Score_Upload_complete";

        public static final String EVENT_PUSH_NOTIFICATION_OFF ="Push_Switch_Off";
        public static final String EVENT_PUSH_GOLF_MOVIE_BUTTON ="History_Movie_Btn_Tap";

        public static final String EVENT_TV_TRIAL_CAMPAIGN_REGIST = "TV_Trial_Campaign_Regist";
        public static final String EVENT_TV_TRIAL_CAMPAIGN_DISPLAY = "Display_TV_Trial";

        public static final String EVENT_SCORE_TRIAL_CAMPAIGN_REGIST = "Score500_Trial_Campaign_Regist";
        public static final String EVENT_SCORE_TRIAL_CAMPAIGN_DISPLAY = "Display_Score500_Trial";
        public static final String EVENT_PLAN_SELECT_DATE ="Tap_select_date";
        public static final String EVENT_PLAN_GORA_SEARCH ="Tap_GORA_search";
        public static final String EVENT_PLAN_ONE_PLAN ="Tap_one_plan";
        public static final String EVENT_PLAN_MORE_PLAN ="Tap_and_more_plan";
        public static final String EVENT_PLAN_ROUND_RESERVE ="Tap_round_reserve";

        public static final String EVENT_LIVE_HISTORY = "Tap_LiveScore_On_History";
        public static final String EVENT_TOTAL_ANALYSIS_HISTORY = "Tap_Analysis_On_History";
        public static final String EVENT_GOLF_MOVIE_TOUCH ="Tap_Golf_Movie";
        public static final String EVENT_PHOTOSCORE_SHARE_SYMBOL     = "PhotoScore_Share_Symbol";
        public static final String EVENT_PHOTOSCORE_SHARE_NUMERIC    = "PhotoScore_Share_Numeric";
        public static final String EVENT_PHOTOSCORE_SHARE_PAPER      = "PhotoScore_Share_Paper";
        public static final String EVENT_PHOTOSCORE_SHARE_FASHION    = "PhotoScore_Share_fashion";
        public static final String EVENT_PHOTOSCORE_SHARE_BONCHAN    = "PhotoScore_Share_BonChan";
        public static final String EVENT_PHOTOSCORE_SHARE_JOYBON     = "PhotoScore_Share_enjoyBon";
        // Drill
        public static final String EVENT_TAP_PRACTICE_DRILL ="Tap_PracticeDrill";
        public static final String EVENT_DISPLAY_PRACTICE_DRILL_DETAIL = "Display_Practice_Drill_Detail";
        public static final String EVENT_PLAY_MOVIE_PRATICE_DRILL = "Play_Practice_Drill";
        public static final String EVENT_PRACTICE_DRILL_CAM_REG ="Practice_Drill_Cam_Reg";
        public static final String EVENT_PRACTICE_DRILL_CAM_END="Practice_Drill_Cam_End";
        // Special page
        public static final String EVENT_DISPLAY_SPECIAL_PAGE_FREEE ="Display_pgalive_freepage";
        public static final String EVENT_TAP_SPECIAL_PAGE_BUTTON = "Tap_pgalive_watch";
        public static final String EVENT_DISPLAY_SPECIAL_PAGE_PAID = "Display_pgalive_paidpage";
        // guest
        public static final String Tap_AppStart_RegistMember  = "Tap_AppStart_RegistMember";
        public static final String Tap_AppStart_Login ="Tap_AppStart_Login";
        public static final String Tap_AppStart_Trial = "Tap_AppStart_Trial";
        public static final String Tap_LoginPopUp_Login = "Tap_LoginPopUp_Login";
        public static final String Tap_LoginPopUp_Cancel = "Tap_LoginPopUp_Cancel";
        public static final String Display_PurchaseSelect_Screen = "Display_PurchaseSelect_Screen";
        public static final String Tap_PurchaseSelect_Score = "Tap_PurchaseSelect_Score";
        public static final String Tap_PurchaseSelect_TV = "Tap_PurchaseSelect_TV";
        public static final String Tap_PurchaseSelect_Annual = "Tap_PurchaseSelect_Annual";
        public static final String Trial ="Trial";
        public static final String PS001 ="PS001";
        public static final String Tap_RoundPopUp_Login = "Tap_RoundPopUp_Login";
        public static final String Tap_RoundPopUp_Cancel = "Tap_RoundPopUp_Cancel";
        public static final String EVENT_TAP_PREPARE_ROUND_SCORE_INPUT = "Tap_PrepareRound_ScoreInput";
        public static final String EVENT_TAP_HOLE_SCORE_SAVE = "Tap_Hole_ScoreSave";
        public static final String EVENT_TAP_HOLE_MENU_SCORE_SAVE ="Tap_HoleMenu_ScoreSave";

        public static final String Display_AppTop_Major          = "Display_AppTop_Major";
        public static final String Play_GolfMovie_Video          = "Play_GolfMovie_Video";
        public static final String Play_Major_Video              = "Play_Major_Video";
        public static final String Play_GolfMovie2_Video         = "Play_GolfMovie2_Video";
        public static final String Play_GolfMovie3_Video         = "Play_GolfMovie3_Video";
        public static final String Tap_News_Content              = "Tap_News_Content";


    }

    // YourGolf Account Management API
    public static final class YourGolfAccountApi {
        public static final int REGISTER = 4;
        public static final int REGISTER_NEW = 0;
        public static final int PROFILE = 5;
        public static final int GET_API = 6;
        public static final int UPDATE = 1;
        public static final int LOST_PASSWORD = 2;
    }

    /**
     * Enum error API server
     *
     * @author CongVC
     */
    public static enum ErrorServer implements Serializable {
        NONE, ERROR_GENERAL, ERROR_SOCKET_TIMEOUT, ERROR_CONNECT_TIMEOUT,ERROR_CONNECT, ERROR_E0105, ERROR_E0156,

        ERROR_FILE_NOT_FOUND_INTERNAL, ERROR_E0111, ERROR_E0114, ERROR_E0116, ERROR_E0117, ERROR_E0112,

        ERROR_E0119, ERROR_E0120, ERROR_E0121, ERROR_E0122, ERROR_E0123, ERROR_E0124, ERROR_E0126, ERROR_E0127, ERROR_E0128, ERROR_E0153,

        ERROR_E0133, ERROR_E0136, ERROR_E0001, ERROR_E0138, ERROR_E0141, ERROR_E0142, ERROR_E0143, ERROR_E0144, ERROR_E0145, ERROR_E0146, ERROR_E0103,

        //TaiNN
        ERROR_E0158, ERROR_E0160, ERROR_E0161, ERROR_E0162, ERROR_E0149, ERROR_E0163, ERROR_E0164, ERROR_E0165, ERROR_E0166, ERROR_RESTORE_PURCHASE,

        ERROR_E0139, ERROR_E0140, ERROR_INTERNAL_SERVER,

        GORA_NO_EXIST, GORA_ACCOUNT_OK, GORA_ACCOUNT_EXPIRED, ERROR_UNEXPECTED,

        UPLOAD_IMAGE_SUCCESS, UPLOAD_IMAGE_FAIL,ERROR_404,ERROR_400,ERROR_500,

        ERROR_E0173, ERROR_E0174, ERROR_E0176,ERROR_TOKEN,NULL
    }

    // oobgolf, yourgolf upload result status
    public static enum UPLOAD_STATUS_CODE {
        OOB_SUCCESS, // 成功
        OOB_NO_SETTINGS, // アカウント設定が入力されていなかった
        OOB_INVALID_SESSION, // ID/PWの組合せが不正だった（ログインできなかった）
        OOB_ZERO_SCORE, // スコア未入力のホールがあった
        OOB_NO_PERMISSION, // 編集できない記録を編集しようとした
        OOB_ERROR, // 予期せぬエラー
        YOURGOLF_SUCCESS, // 成功
        YOURGOLF_NO_SETTINGS, // アカウント未設定(auth tokenを保持しているかどうかで判定）
        YOURGOLF_INVALID_TOKEN, // auth tokenが不正
        YOURGOLF_UNEXPECTED_ERROR, // サーバ側の不明エラー
        YOURGOLF_ERROR, // 予期せぬエラー
        YOURGOLF_ID_NOTFOUND, YOURGOLF_DUPLICATE_NAME, YOURGOLF_SCORE_CARD_EMPTY, YOURGOLF_DONT_PERMISSION, NONE,

        GORA_EXPIRED
    }

    public static enum BANNER_ACTION {
        ADVERTISE, SEND_GIFT, SEARCH_CLUB, HISTORY, ANALYSIS, SETTING
    }

    public static enum CALL_FROM {
        CLUB_DETAIL,
        ROUND_DETAIL,
        HOLE_SCREEN
    }

    public static enum SHARE_TYPE implements Serializable {
        SYMBOL,
        NUMERIC,
        PAPER,
        FASHION,
        BONCHAN,
        JOYBON
    }

    public static final class TAB_INDEX {
        public static final int PICK_UP        = 0;
        public static final int NEWS           = 1;
        public static final int HISTORY        = 2;
        public static final int RESERVE        = 3;
        public static final int LIVE           = 4;
        public static final int MAJOR_MOVIE    = 5;
        public static final int GOLF_MOVIE     = 6;
        public static final int GOLF_MOVIE_2   = 7;
        public static final int GOLF_MOVIE_3   = 8;
        public static final int DRILL          = 9;
    }

    // 全国ゴルフ場ナビ用
    // public static String GOLF_NAVI = "com.asai24.golf_navi";
    public static final String APP_NAME = "APP_NAME";
    public static final String COURSE_MODE = "COURSE_MODE";
    public static final String MODE = "MODE";
    public static final String CLUB_NAME = "CLUB_NAME";

    // v3.3.5 st saikami
    // パーゴルフ記事通知用
    public static final String JA_PARGOLF_ACTION = "JA_PARGOLF_ACTION";
    // LAMTT
    public static final String KEY_CALL_PROFILE = "KEY_CALL_PROFILE";
    public static final String CLUB_NAME_DEFAULT = "Your Golf Club(Sample)";
    public static final String COURSE_NAME_DEFAULT = "Your Golf Course(Sample)";
    public static final String CLUB_OPJ_SEARCH = "club_search";
    public static final String COURSE_SEARCH = "course_search";
    public static final String TEE_SEARCH = "tee_search";

    public static final String PLAYER_NAME = "player_name";
    public static final String PLAYER_ID = "player_id";
    public static final String PLAYER_EMAIL = "player_email";
    public static final String PLAYER_DISPLAY_AS_FRIEND = "player_display_as_friend";
    public static final String PLAYER_DHCP = "player_hdcp";

    public static final String HALL_SEARCH = "hall_search";
    public static final String DATE_PLAY_SEARCH = "date_play_search";
    public static final String HOLE_COUNT = "hole_count";
    public static final String WEATHER_SEARCH = "weather_search";

    public static final String SUNNY = "sunny";
    public static final String CLOUDY = "cloudy";
    public static final String RAINY = "rainy";
    public static final String SNOWING = "snowing";
    public static final String FOGGY = "foggy";

    public static final String HOLE_ONE = "hole_one";
    public static final String HOLE_TEN = "hole_ten";

    public static final String KEY_VERSION_NAME = "VERSION_NAME";
    public static final String KEY_API_APP_VERSION = "android" + KEY_VERSION_NAME;
    public static final String KEY_APP = "app";
    public static final String KEY_GORA = "gora";

    //ticket 7227
    public static final String URL_GORA_GOLF_COURSE_DETAIL = "https://app.rakuten.co.jp/services/api/Gora/GoraGolfCourseDetail/20131113?format=json&applicationId=1083670463465254180&affiliateId=0db0b791.d6e84c38.0db0b792.a2f620e8&golfCourseId=";

    public static final String GIFT_ID = "gift_id";
    public static final String EMAIL = "email";
    public static final String MESSAGE_POST = "message_post";
    public static final String BITMAP_POST = "bitmap_post";
    public static final String BITMAP_NAME = "bitmap_name";
    /* THUNA 2013/05/08 ADD-S */
    public static final String PLAYER_HADICAP = "player_hadicap";
    public static final String CUR_PLAYER_NAME = "cur_player_name";
    public static final String CUR_PLAYER_HADICAP = "cur_player_hadicap";
    public static final String PLAYER2_NAME = "player2_name";
    public static final String PLAYER2_HADICAP = "player2_hadicap";
    public static final String PLAYER3_NAME = "player3_name";
    public static final String PLAYER3_HADICAP = "player3_hadicap";
    public static final String PLAYER4_NAME = "player4_name";
    public static final String PLAYER4_HADICAP = "player4_hadicap";
    public static final String PLAYER_HADICAP_NEW = "player_hadicap_new";
    public static final String SCORE_INPUT_LIVE_ID = "score_input_live_id";
    /* THUNA 2013/05/08 ADD-E */

    /* THUNA 2013/05/22 ADD-S */
    public static final String LIVE_INFO = "live_info";
    public static final String REQUEST_GOLF_FROM_LIVE = "request_golf_from_live";
    public static final String CLUB_ID = "club_id";
    public static final String LIVE_ENTRY_ID = "live_entry_id";
    public static final String TOP_IMAGE_URL = "top_image_url";
    public static final String CHECK_FOR_START_LIVE ="check_for_start_live";
    /* THUNA 2013/05/22 ADD-E */
    public static final int DEFAULT_HDCP_VALUE = 99;
    public static final int DEFAULT_GOAL_VALUE = 99;

    public static final String SCOPE_GOOGLE = "oauth2:https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

    public static final int RESULT_YAHOO = 33;
    public final static int RESULT_GOOGLE = 44;
    public static final int CONNECT_TIMEOUT = 6000;

    //yahoo jp
    public static final String YAHOO_NONCE = "44Ki44Kk44OH44Oz44OG44Kj44OG44Kj44Gu5rW344Gv5bqD5aSn44Gg44KP";
    public static final String YAHOO_STATE = "5LiW55WM44GMWeODkOOBhCEh";
    public final static String YCONNECT_PREFERENCE_NAME = "yconnect";
    public final static int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    public final static int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    public static final String LIVE_PLAYING = "LIVE_PLAYING";
    public static final String ACCEPT_NEW_TERMS = "ACCEPT_NEW_TERMS_1";
    public static final String CLICK_TERM = "CLICK_TERM";
    public static final String ACCEPT_NEW_TERMS_APP_UPDATE = "ACCEPT_NEW_TERMS_APP_UPDATE";


    public static final String PP_APP_KEY;//= "a3dfe9da-4219-4c8b-b5d3-5924a363382d";
//    public static final String PP_APP_KEY = "JjOn8o1zL+kISvCbb2aQDrIMFfEYuc5qHJejW/2NcyA=";//old key
    //    public static final String PP_APP_KEY = "JjOn8o1zL+kISvCbb2aQDrIMFfEYuc5q97uxMUjoVz+JGXZezsusbQ=="; // Test coupon
    public static final String PP_COUPON_USERNAME = "yourgolfonline";
    public static final String PP_COUPON_PASSWORD = "FptGofpN";

    public static final String MIX_PANEL_TOKEN = "4dd07e6ef49481a8ff1df122d49d425d";

    public static final String KEY_FROM_SCORE_AGENCY = "KEY_FROM_SCORE_AGENCY";
    public static final String KEY_IS_WEB_CHARGE_USER = "KEY_IS_WEB_CHARGE_USER";
    public static final String KEY_FROM_SETTING = "KEY_FROM_SETTING";

    public static final String URL_AGENCY_REQUEST_DETAIL = URL_GOLFDB + "/v1/agency_requests/" + PARAM_ID + ".json";
    public static final String URL_AGENCY_REQUEST_SUMMARY = URL_GOLFDB + "/v1/agency_requests/summary.json";

    public static final String URL_SUVT_BUY;
    public static final String URL_SUVT_LIVE = URL_SUVT_BASE + "/live";
    public static final String URL_SUVT_SEARCH = URL_SUVT_BASE + "/search";
    public static final String URL_SUVT_VOD = URL_SUVT_BASE + "/vod";
    public static final String URL_SUVT_RECOMMEND = URL_SUVT_BASE + "/recommend";
    public static final String URL_SUVT_GEAR = URL_SUVT_BASE + "/gear";
    public static final String URL_SUVT_FAILURE;
    public static final String URL_SUVT_XVIE_AUTH;
    public static final String URL_SUVT_PURCHASE = "/tv_charge";
    public static final String URL_SCORE_PURCHASE = "/score_charge";
    public static final String URL_PURCHASE = "/charge";
    public static final String URL_TV_ANNUAL = "/tv_annual_charge";

    public static final String USER_AGENT_STRING = "GN_APP";
    public static final String GNTV_DOMAIN;
    public static final String GNTV_DOMAIN_2 = "stg-spod-connect-elb-196198329.ap-northeast-1.elb.amazonaws.com";
    public static final String GNTV_DOMAIN_3 = "connect.skyperfectv.co.jp";
    public static final String FLAG_OPEN_WALKTHROUGH = "flag_open_walkthrough";

    public static final String URL_PLAYED_COURSES = URL_GOLFDB + "/v1/courses/played.json";
    public static final String PARAM_AUTH_TOKEN = "auth_token";
    public static final String PARAM_YEAR = "year";
    public static final String PARAM_LAST_MONTH = "last_month";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_ONLY_YOURGOLF2 = "only_yourgolf2";
    public static final String PARAM_APP = "app";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_COURSE_EXT_ID = "course_ext_id";
    public static final String PARAM_COURSE_ID = "course_id";
    public static final String PARAM_ONLY_PLAYED_18_HOLES = "only_played_18_holes";
    public static final String PARAM_ONLY_PLAYED_ALL_HOLES = "only_played_all_holes";
    public static final String PARAM_PLATFORM = "platform";
    public static final String URL_TOP_NEWS_JSON = "https://ookami-proxy.yourgolf-online.com/v1/news/public?sports_id=7&referral=gnplus";
//    public static final String URL_TOP_ADVERTISING_JSON = URL_S3_AMAZONAWS + "/public/gallery/top.json";

    public static final String URL_TV_USER_STATUS = URL_GOLFDB + "/v1/payment/available_for_purchase.json";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_ACCESS_FROM_JAPAN = "access_from_japan";

    public static final String SAVE_PERIOD_LAST_MONTH = "SAVE_PERIOD_LAST_MONTH";
    public static final String SAVE_PERIOD_YEAR = "SAVE_PERIOD_YEAR";
    public static final String SAVE_COURSE_COURSE_ID = "SAVE_COURSE_COURSE_ID";
    public static final String SAVE_COURSE_COURSE_NAME = "SAVE_COURSE_COURSE_NAME";
    public static final String SAVE_COURSE_CLUB_NAME = "SAVE_COURSE_CLUB_NAME";

    public static final String URL_RAKUTEN_ACCOUNT = URL_GOLFDB + "/v1/rakuten/account.json";

    public static final String XVIE_APP_SIGNATURE;

    public static final String LAST_TIME_SHOW_NOTIFICATION_MESSAGE = "LAST_TIME_SHOW_NOTIFICATION_MESSAGE";
    public static final String LAST_MESSAGE_URL = "LAST_MESSAGE_URL";
    //public static final String URL_NOTIFICATION_MESSAGE;
    public static final String KEY_NOTIFICATIONS = "notifications";
    public static final String KEY_NOTIFICATION_TARGET = "target";
    public static final String KEY_NOTIFICATION_PLATFORM = "platform";
    public static final String KEY_NOTIFICATION_PLATFORM_VALUE = "android";
    public static final String KEY_NOTIFICATION_APP_TYPE = "app_type";
    public static final String KEY_NOTIFICATION_LANG = "lang";
    public static final String KEY_NOTIFICATION_LANG_EN = "en";
    public static final String KEY_NOTIFICATION_LANG_JA = "ja";
    public static final String KEY_NOTIFICATION_LANG_OTHERS = "others";
    public static final String KEY_NOTIFICATION_TITLE = "title";
    public static final String KEY_NOTIFICATION_MESSAGE = "message";
    public static final String KEY_NOTIFICATION_URL = "url";

    public static final String URL_TOP_BANNER_REDIRECT;
    public static final String URL_TV_DOMAIN;
    public static final String URL_TV_DOMAIN_APP;
    public static final String URL_TV_DOMAIN_INTENT = "URL_TV_DOMAIN_INTENT";
    public static final String TOP_ACTIVITY_INTENT = "TOP_ACTIVITY_INTENT";
    public static final String APP_NAME_PACKAGE = "com.asai24.golf";
    public static final String GOLF_DAY_PACKAGE = "jp.mappleon.golfnavisu";
    public static final String GOLF_DAY_DEFAULT_CLASS = "jp.mappleon.golfnavisu.SplashActivity";
    public static final String GOLF_DAY_PARAM_COURSE_MENU = "course_menu";
    public static final String GOLF_DAY_PARAM_HOLE_MAP = "hole_map";
    public static final int GOLF_DAY_PARAM_USER_NORMAL = 11021;
    public static final int GOLF_DAY_PARAM_USER_PREMIUM = 11022;

    public static final String KEY_BUNDLE_PERIOD_ITEM = "KEY_BUNDLE_PERIOD_ITEM";
    public static final String KEY_BUNDLE_COURSE_ITEM = "KEY_BUNDLE_COURSE_ITEM";

    public static final String URL_GET_COURSE_MAP = URL_GOLFDB + "/v1/courses/" + PARAM_ID + ".json";
//    public static final String URL_GOLF_DAY_CAMPAIGN = URL_S3_AMAZONAWS + "/public/gnp_settings.json";

//    public static final String URL_COUNTER_100_HELP = URL_S3_AMAZONAWS + "/public/100count_help.html";

    public static final String URL_TV_ACTIVATION = URL_GOLFDB + "/v1/activations";

    public static final String JCOM_USER_AGENT_STRING = "GN_APP_JCOM";

    public static final String URL_GET_SHARE_IMAGE_S3_INFO = URL_GOLFDB + "/v1/photo_score/presign.json";

    public static final String URL_REGISTER_CAMPAIGN_CODE = URL_GOLFDB + "/v1/campaign_entries.json";
    public static final String URL_POST_REGISTER_TV_TRIAL_CAMPAIGN_CODE = URL_GOLFDB + "/v1/free_tv_campaign_entries.json";
    public static final String URL_POST_REGISTER_SCORE_TRIAL_CAMPAIGN_CODE = URL_GOLFDB + "/v1/free_score500_campaign_entries.json";
    public static final String URL_GET_REGISTER_TV_TRIAL_CAMPAIGN_CODE = URL_GOLFDB + "/v1/free_tv_campaign_entry.json";
    public static final String URL_GET_REGISTER_SCORE_TRIAL_CAMPAIGN_CODE = URL_GOLFDB + "/v1/free_score500_campaign_entry.json";
//    public static final String URL_AGENCY_TRIAL_CAMPAIGN_CODE = URL_S3_AMAZONAWS + "/public/agency_trial_campaigncode.html";

    public static final String CAMPAIGN_USE_EXPERIENCE ="campaign_use_experience";
    public static final String CAMPAIGN_CODE_TRIAL = "00000000";
    // Get movies
    public static final String URL_GET_MOVIES_END = URL_GET_MOVIE_THUMB + "/android_tv_recommend_list";
    public static final String PARAM_API_KEY ="api_key";
    public static final String PARAM_SPEC ="spec";
    public static final String PARAM_AUTHENTICATION_TOKEN ="authentication_token";
    public static final String PARAM_NUM ="num";
    public static final String MESS_ERROR ="MESS_ERROR";

    public static  boolean isScoreCard = false;
    public static final String PURCHASE_STATUS_NULL ="PS001";

    // Scheme for open special page
    public static final String SCHEME_OPEN_SPCIAL_PAGE = "SCHEME_OPEN_SPCIAL_PAGE";
    public static final String SCHEME_OPEN_ROUND_RESERVE = "SCHEME_OPEN_ROUND_RESERVE";
    public static final String SCHEME_SPCIAL_PAGE = "ygo";
    public static final String HOST_SPCIAL_PAGE = "webview_16109";

    public static final boolean isSupportTvTrialCampaign = true;
    public static final boolean isSupportScoreTrialCampaign = true;
    // is true when open tab Drill from sheme ygo://drill_top to hide view history and recomand in tab drill. others is false
    public static boolean isHideViewFragmentDrill =false;

    // API DRill
    public static final int MAX_TIME_FOR_RETRY_REQUEST = 3;
    public static final String APP_PREF_FILE = "ygo-https.xml";
    public static final String URL_DRILL_GROUP = "drill_display_group";
    public static final String URL_DRILL_GROUP_DETAIL ="drill_display_group_detail";
    public static final String URL_DRILL_TV_VIEWING_HISTORY =  "drill_tv_viewing_history";
    public static final String URL_DRILL_TV_RECOMMEND_LIST =  "drill_tv_recommend_list";
    public static final String URL_DRILL_TV_RECOMMEND_CORRESPOND ="drill_tv_recommend_correspond";
    public static final String URL_DRILL_TV_PROGRAM = "drill_tv_program";
    public static final String URL_DRILL_TV_PROGRAM_GROUP = "drill_tv_program_group";
    public static final String URL_USER_TYPE ="v1/accounts/me.json";
    public static final String URL_DRILL_EVLUATIONS_LEFT = "/v1/drill_evaluation/recommendation/{drill_id}.json";
    public static final String URL_DRILL_EVLUATIONS_CENTER = "/v1/drill_evaluation/indeed/{drill_id}.json";
    public static final String URL_DRILL_EVLUATIONS_RIGHT = "/v1/drill_evaluation/valuable/{drill_id}.json";
    public static final String URL_DRILL_MEMO = "/v1/drill_memo/{drill_id}.json";
    // Drill home parameter send intent
    public static final String DRILL_ID_PROGRAME_CODE ="DRILL_ID_PROGRAME_CODE";
    public static final String DRILL_ID_CATEGORY_CODE ="PARA_ID_CATEGORY_CODE";
    public static final String DRILL_LINKER_CLICK_FROM_HISTORY ="DRILL_LINKER_CLICK_FROM_HISTORY";
    public static final String DRILL_TITLE_CATEGORY_NAME ="DRILL_TITLE_CATEGORY_NAME";

    public static final String URL_MAJOR_GROUP_DETAIL ="apptop_player_display_group_detail";
    public static final String URL_LIVE_GROUP_DETAIL ="apptop_player_epg_parts";

    public static final String URL_MOVIE_TAB     = "/public/golf_movie_tab/custom_movie_tab.json";
    public static final String URL_MOVIE2_TAB    = "/public/golf_movie_tab/custom_movie2_tab.json";
    public static final String URL_MOVIE3_TAB    = "/public/golf_movie_tab/custom_movie3_tab.json";
    public static final String URL_MAJOR_TAB     = "/public/golf_movie_tab/custom_major_tab.json";
    public static final String URL_HOME_TAB      = "home_url";
    public static final String URL_NEWS_TAB      = "/home/news.json";
    public static final String URL_DRILL_TAB     = "/home/drills.json";
    public static final String URL_LIVE_TAB      = "/public/golf_movie_tab/custom_live_tab.json";
    public static String FIVE_APP_ID = "154429";
    public static String FIVE_SLOT_ID = "904490";
}
