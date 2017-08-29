package com.asai24.golf.inputscore;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.asai24.golf.Constant;
import com.asai24.golf.GolfApplication;

import com.asai24.golf.common.Distance;
import com.asai24.golf.db.GolfDatabase;
import com.asai24.golf.db.HoleCursor;
import com.asai24.golf.db.PlayerCursor;
import com.asai24.golf.db.TeeCursor;
import com.asai24.golf.domain.AgencyRequestSummaryObj;
import com.asai24.golf.domain.ClubObj;
import com.asai24.golf.domain.Course;
import com.asai24.golf.domain.GolfDayClub;
import com.asai24.golf.domain.GoraGolfCourseDetailResult;
import com.asai24.golf.domain.LiveInfo;
import com.asai24.golf.domain.PlayerObj;
import com.asai24.golf.domain.Tee;
import com.asai24.golf.object.CommonResources;
import com.asai24.golf.object.ObjectKeyPre;
import com.asai24.golf.utils.CleanUpUtil;
import com.asai24.golf.utils.ContextUtil;
import com.asai24.golf.utils.MyLocation;
import com.asai24.golf.utils.YgoLog;
import com.asai24.golf.view.GolfSelectLayerView_St2;
import com.asai24.golf.view.OnDatePickerDialogKeyListener;
import com.asai24.golf.web.BatchAPI;
import com.asai24.golf.web.GetAgencyRequestSummaryApi;
import com.asai24.golf.web.GetPlayerHandicapApi;
import com.asai24.golf.web.GoraGolfCourseDetailApi;
import com.asai24.golf.web.YourGolfTeeDetailsJsonAPI;
import com.crashlytics.android.Crashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySelectRound extends GolfActivity implements View.OnClickListener, View.OnTouchListener {
    TextView tvClupName,tvAddress,tvNumberPhone,tvTel;
    RelativeLayout mGoraReserveMenu,rlGoflDay;
    private ClubObj mClubOpj = null;
    Button btnBack,btnEdit,rlClubDay,btnAddress;
    private MixpanelAPI mixpanel;
    private ProgressDialog mProgressDialog;
    private MyLocation mMyLocation;
    private double mLat;
    private double mLon;
    private static final int GPS_SETTING = 1;
    private boolean mSearchHistoryFlg = false;
    private Resources mResources;
    private Dialog mDialogList;

/*Group 2*/

    private RelativeLayout rlDatePlay,rlCourse,rlHall,rlTea, btnScoreRequest;
    private TextView tvDatePlay,tvCourse,tvHall,tvTea;
    private Course mCourse;

    private String mCourseId = "";
    private String mTeeSelectId = "";
    List<Tee> tees = null;

    private String mHall = "";
    private String mWeather = "";
    private long mHoleNum = 9;
    private boolean mSelectHole = true;
    private ImageView ivClickCourse;

    private DatePickerDialog mDatePickerDialog;
    private Calendar mPlaydate;
    private HashMap<String, String> weatherHash = new HashMap<String, String>();
    private boolean isClick = false;
    ImageView lv_sunny,lv_cloud,lv_rainy,lv_snow,lv_fog;


    private TextView tvPlayer1,tvPlayer2,tvPlayer3,tvPlayer4,tvPlayer2Handicap,tvPlayer3Handicap,tvPlayer4Handicap,tvCurPlayerHandicap;
    private LinearLayout btnClearPlayer2,btnClearPlayer3,btnClearPlayer4;

    private String userName;
    private GolfDatabase mDb;
    private List<Long> mPlayerIds = new ArrayList<Long>();

    Resources r;
    private static final int MAX_LENGTH = 20;

    public InputMethodManager imm;

    private String mPlayerId2 = "";
    private String mPlayerId3 = "";
    private String mPlayerId4 = "";
    private GolfSelectLayerView_St2 view;

    private int FLAG_PLAYER_POSITION = 2; // value of {2,3,4}
    private int FLAG_PARENT = 1000;

    private long mTeeId;
    //	private String mDatePlay;


//    private GolfApplication mApplication;

    private static final int REQUEST_CODE_HANDICAP = 100;
    private Object[] list1ShownDhcp, list2ShownDhcp;
    //    private HashMap<String, String> shownDhcpHash;
//    private HashMap<String, String> shownDhcpRevertHash;
    private ListView lstStrokeHandicap1,lstStrokeHandicap2;
    private CustomBaseAdapter baseAdapter1,baseAdapter2;
    private int selectedIndex1,selectedIndex2,selectedId = -1;
    private View selectedView, strokeHandicapLayout;

    private String mName2,mPlayerHadicap2,mName3,mPlayerHadicap3,mName4,mPlayerHadicap4,defaultHDCP = "";
    private boolean isPuma = false;
    RelativeLayout rlStart;

    // TayPVS -
    private int rowWidth,rowHeight;
    private static ScrollView scrollview;
    boolean isFromAgency = false;
    boolean isCheckStartFromLive = false;
    LinearLayout lnGroup1,lnGroup2, lnGroup3, lnGroup4,lnRequestAgency, lnStepBar;
    /*GROUP 4*/
    private  boolean isEnter, isUsePoint, isStableMode,isMeasure;
    private String measureValue;
    private static Preference club;
    private static CheckBoxPreference measure, userPoint, stableFord, enterCompetitor;
    private static Context m_context;
    private LinearLayout lnMoreGroup1;
    private Button btnMoreGroup3, btnMoreGroup4, btnMoreGroup1;
    public static String NEED_PROFILE_TO_RELOAD = "needProfileToReload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_context = this;
        setContentView(R.layout.activity_search_round_new);
        isFromAgency = getIntent().getBooleanExtra(Constant.KEY_FROM_SCORE_AGENCY,false);
        isCheckStartFromLive = getIntent().getBooleanExtra(Constant.CHECK_FOR_START_LIVE,false);
        mResources = getResources();
        mClubOpj = (ClubObj) getIntent().getExtras().getSerializable(mResources.getString(R.string.intent_club));
        // Reset memo data
        resetMemoData();
        mixpanel = MixpanelAPI.getInstance(this, Constant.MIX_PANEL_TOKEN);
        initView();

        if (null != savedInstanceState) {
            FLAG_PLAYER_POSITION = savedInstanceState.getInt("FLAG_PLAYER_POSITION", 2);
            selectedIndex1 = savedInstanceState.getInt("selectedIndex1", -1);
            selectedIndex2 = savedInstanceState.getInt("selectedIndex2", -1);
            selectedId = savedInstanceState.getInt("selectedId", -1);

            mName2 = savedInstanceState.getString("name2");
            mName3 = savedInstanceState.getString("name3");
            mName4 = savedInstanceState.getString("name4");

            mPlayerId2 = savedInstanceState.getString("id2");
            mPlayerId3 = savedInstanceState.getString("id3");
            mPlayerId4 = savedInstanceState.getString("id4");

            mPlayerHadicap2 = savedInstanceState.getString("playerHadicap2");
            mPlayerHadicap3 = savedInstanceState.getString("playerHadicap3");
            mPlayerHadicap4 = savedInstanceState.getString("playerHadicap4");

            restoreDataAfterSkillAct();
        }

       loadData();
    }
    private void loadData(){
        if(!isCheckStartFromLive) {
            initRoundFist();
        }
        initGroup2();
        if(!isFromAgency) {
            initGroup3AddPlayer();
            initGroup4Setting();
        }
        showLayout();
    }
    private void initView(){
        scrollview = (ScrollView) findViewById(R.id.scrollView2);
        lnGroup1 = (LinearLayout) findViewById(R.id.group1);
        lnMoreGroup1 = (LinearLayout)findViewById(R.id.ln_new_round_hide_1);
        btnMoreGroup1 = (Button) findViewById(R.id.btn_round_new_more_group1);
        btnMoreGroup1.setOnClickListener(this);
        lnGroup2 = (LinearLayout) findViewById(R.id.group2);
        lnGroup3 = (LinearLayout) findViewById(R.id.group3);
        btnMoreGroup3 = (Button) findViewById(R.id.btn_new_round_more_group3);
        btnMoreGroup3.setOnClickListener(this);
        lnGroup4 = (LinearLayout) findViewById(R.id.group4);
        btnMoreGroup4 = (Button) findViewById(R.id.btn_new_round_more_group4);
        btnMoreGroup4.setOnClickListener(this);
        lnRequestAgency = (LinearLayout) findViewById(R.id.lnRequestAgency);
        lnStepBar = (LinearLayout) findViewById(R.id.step_bar_layout_new);
        btnBack = (Button) findViewById(R.id.top_back_btn);
        btnEdit = (Button) findViewById(R.id.top_edit);
        Distance.SetHeader(this, true, true, getString(R.string.search_title));
//        btnEdit.setText(getResources().getString(R.string.btn_next));
//        btnEdit.setEnabled(false);
//        btnEdit.setVisibility(View.VISIBLE);
//        btnEdit.setOnClickListener(this);
        btnEdit.setVisibility(View.INVISIBLE);

        //view 1
        btnAddress = (Button)findViewById(R.id.btn_address);
        tvClupName = (TextView)findViewById(R.id.tv_clup_name);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvNumberPhone = (TextView) findViewById(R.id.tv_phone_dial);
        mGoraReserveMenu = (RelativeLayout)findViewById(R.id.gora_reserve_menu_new);
        mGoraReserveMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoraReservePage(mClubOpj.getRakutenId());
            }
        });

        tvClupName = (TextView) findViewById(R.id.tv_clup_name);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvTel = (TextView) findViewById(R.id.tv_phone_dial);
        rlClubDay = (Button) findViewById(R.id.btn_club_name);
        rlGoflDay = (RelativeLayout) findViewById(R.id.rv_golf_day_link);

        btnAddress.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvTel.setClickable(true);
        tvTel.setOnClickListener(this);
        rlClubDay.setOnClickListener(this);
        rlGoflDay.setOnClickListener(this);
        findViewById(R.id.btn_phone_dial).setClickable(true);
        findViewById(R.id.btn_phone_dial).setOnClickListener(this);
        //view 2
        rlStart = (RelativeLayout)findViewById(R.id.play_hole);
        mClubOpj = (ClubObj) getIntent().getExtras().getSerializable(mResources.getString(R.string.intent_club));
        rlDatePlay = (RelativeLayout) findViewById(R.id.dateItem);
        rlCourse = (RelativeLayout) findViewById(R.id.courseItem);
        rlHall = (RelativeLayout) findViewById(R.id.hallItem);
        rlTea = (RelativeLayout) findViewById(R.id.teaItem);
        ivClickCourse = (ImageView) findViewById(R.id.clickCourse);
        btnScoreRequest = (RelativeLayout) findViewById(R.id.score_Request);
        btnScoreRequest.setEnabled(false);
        btnScoreRequest.setFocusable(false);
        btnScoreRequest.setOnClickListener(this);
        lv_sunny= (ImageView) findViewById(R.id.ln_sunny);
        lv_cloud = (ImageView) findViewById(R.id.ln_cloud);
        lv_rainy = (ImageView) findViewById(R.id.ln_umbrella);
        lv_snow = (ImageView) findViewById(R.id.ln_snow);
        lv_fog = (ImageView) findViewById(R.id.ln_rainny);
        btnBack.setOnClickListener(this);
        rlDatePlay.setOnClickListener(this);
        if (Distance.isSmallDevice(this)) {
            rlDatePlay.requestFocus();
        }

        lv_fog.setOnClickListener(this);
        lv_sunny.setOnClickListener(this);
        lv_snow.setOnClickListener(this);
        lv_rainy.setOnClickListener(this);
        lv_cloud.setOnClickListener(this);
        rlCourse.setOnClickListener(this);
        rlHall.setOnClickListener(this);
        rlTea.setOnClickListener(this);


        rlStart.setOnClickListener(this);
        rlStart.setEnabled(false);
        rlStart.setFocusable(false);
        // KinND add button add partner
        tvDatePlay = (TextView) findViewById(R.id.datePlay);

        tvCourse = (TextView) findViewById(R.id.tv_course);
        tvHall = (TextView) findViewById(R.id.tv_hall);
        tvTea = (TextView) findViewById(R.id.tv_tea);
        //view 3
        ((RelativeLayout) findViewById(R.id.curHandicap1Layout)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.handicap2Layout)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.handicap3Layout)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.handicap4Layout)).setOnClickListener(this);

        ((RelativeLayout) findViewById(R.id.player2Layout)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.player3Layout)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.player4Layout)).setOnClickListener(this);

        ((Button) findViewById(R.id.btnCancelHandicap)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnStoreHandicap)).setOnClickListener(this);
        tvCurPlayerHandicap = (TextView) findViewById(R.id.tvCurPlayerHandicap);
        strokeHandicapLayout = (RelativeLayout) findViewById(R.id.stroke_handicap_edit_layout);
        strokeHandicapLayout.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels / 2;
        strokeHandicapLayout.requestLayout();

        lstStrokeHandicap1 = (ListView) findViewById(R.id.stroke_handicap_lst1);
        lstStrokeHandicap1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lstStrokeHandicap2 = (ListView) findViewById(R.id.stroke_handicap_lst2);
        lstStrokeHandicap2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        tvPlayer1 = (TextView) findViewById(R.id.player1);
        tvPlayer2 = (TextView) findViewById(R.id.player2);
        tvPlayer3 = (TextView) findViewById(R.id.player3);
        tvPlayer4 = (TextView) findViewById(R.id.player4);
        tvPlayer2Handicap = (TextView) findViewById(R.id.tvPlayer2Handicap);
        tvPlayer3Handicap = (TextView) findViewById(R.id.tvPlayer3Handicap);
        tvPlayer4Handicap = (TextView) findViewById(R.id.tvPlayer4Handicap);
        btnClearPlayer2 = (LinearLayout) findViewById(R.id.clear_player2);
        btnClearPlayer3 = (LinearLayout) findViewById(R.id.clear_player3);
        btnClearPlayer4 = (LinearLayout) findViewById(R.id.clear_player4);
        Button btn_clear2 = (Button) findViewById(R.id.clear_player2_btn);
        Button btn_clear3 = (Button) findViewById(R.id.clear_player3_btn);
        Button btn_clear4 = (Button) findViewById(R.id.clear_player4_btn);

        btnClearPlayer2.setVisibility(View.GONE);
        btnClearPlayer3.setVisibility(View.GONE);
        btnClearPlayer4.setVisibility(View.GONE);

        btnClearPlayer2.setOnClickListener(this);
        btnClearPlayer3.setOnClickListener(this);
        btnClearPlayer4.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
        btn_clear3.setOnClickListener(this);
        btn_clear4.setOnClickListener(this);
        hideView();
    }

    private void hideView(){
        lnMoreGroup1.setVisibility(View.GONE);
        btnMoreGroup1.setVisibility(View.VISIBLE);
        rlClubDay.setVisibility(View.GONE);
        btnMoreGroup3.setVisibility(View.VISIBLE);
        btnMoreGroup4.setVisibility(View.VISIBLE);
        lnGroup2.setVisibility(View.VISIBLE);
        lnGroup3.setVisibility(View.GONE);
        lnGroup4.setVisibility(View.GONE);
    }
    private void showLayout(){
        if(isFromAgency){
            lnGroup3.setVisibility(View.GONE);
            lnGroup4.setVisibility(View.GONE);
            lnGroup2.setPadding(0,0,0,10);
            rlStart.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
        if(isCheckStartFromLive){
            lnRequestAgency.setVisibility(View.GONE);
            lnGroup1.setVisibility(View.GONE);
            lnStepBar.setVisibility(View.GONE);
        }
    }
    /*Round First*/

    private void initRoundFist(){
        mSearchHistoryFlg = getIntent().getExtras().getBoolean(getString(R.string.intent_search_history_mode), false);
        /*ThuNA 2013/05/23 ADD-E*/
        tvClupName.setText(mClubOpj.getClubName());
        tvAddress.setText(mClubOpj.getAddress());
        // TayPVS
        SpannableString spanString = new SpannableString(mClubOpj.getPhoneNumber());
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        tvTel.setText(mClubOpj.getPhoneNumber());

        //ThoLH - #2192 [And-Task] Don't show ">" if don't have URL or TEL - 2012-06-28
        if (mClubOpj.getPhoneNumber() == null || mClubOpj.getPhoneNumber().length() == 0) {
//            findViewById(R.id.call).setVisibility(View.INVISIBLE);
        }
        if (mClubOpj.getUrl() == null || mClubOpj.getUrl().length() == 0) {
        }
        boolean isVisibleLeft = false;
        boolean isVisibleRight = false;
        if (mClubOpj.getRakutenId() == null) {
            //CanNC - Support Puma - 2013-12-21
            if (GolfApplication.isPuma() == true) {
//                findViewById(R.id.temp_line).setVisibility(View.INVISIBLE);
                mGoraReserveMenu.setVisibility(View.INVISIBLE);
            } else {
                mGoraReserveMenu.setVisibility(View.INVISIBLE);
            }
            isVisibleRight = true;
        }
        if (Distance.isSmallDevice(this)) {
            // #14994 - Don't show golf day item if it's small device
            rlGoflDay.setVisibility(View.INVISIBLE);
            isVisibleLeft = true;
        } else if (mClubOpj.getGolfDayClubs() == null) {
            isVisibleLeft = true;
            rlGoflDay.setVisibility(View.INVISIBLE);
        } else {
            if (mClubOpj.getGolfDayClubs().size() <= 0) {
                isVisibleLeft = true;
                rlGoflDay.setVisibility(View.INVISIBLE);
            }
        }
        if(isVisibleLeft && isVisibleRight){
            rlGoflDay.setVisibility(View.GONE);
            mGoraReserveMenu.setVisibility(View.GONE);
        }



    }

    protected void openGoraReservePage(final String rakutenId) {
        (new GetGoraGolfCourseDetailTask(ActivitySelectRound.this, rakutenId)).execute();
    }
    public static final class GetGoraGolfCourseDetailTask extends AsyncTask<Void, Void, GoraGolfCourseDetailResult> {
        private final String rakutenId;
        private GoraGolfCourseDetailApi api;
        private ProgressDialog dialog;
        private WeakReference<ActivitySelectRound> context;

        private GetGoraGolfCourseDetailTask(ActivitySelectRound context, String rakutenId) {
            this.context = new WeakReference<ActivitySelectRound>(context);
            this.rakutenId = rakutenId;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(context.get());
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage(context.get().getString(R.string.msg_now_loading));
            dialog.show();

            api = new GoraGolfCourseDetailApi();
        }

        @Override
        protected GoraGolfCourseDetailResult doInBackground(Void... params) {
            GoraGolfCourseDetailResult result = api.get(rakutenId);
            return result;
        }

        protected void onPostExecute(GoraGolfCourseDetailResult result) {
            dialog.dismiss();

            if (result.isSuccess()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getReserveCalUrl()));
                context.get().startActivity(intent);
            } else {
                Toast.makeText(context.get().getApplicationContext(), R.string.gora_error, Toast.LENGTH_LONG).show();
            }

        }
    }
    private void resetMemoData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.SAVE_MEMO_TEXT_KEY, "");
        editor.commit();
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            View view = getCurrentFocus();
            if (view != null && view == tvAddress) {
                showConfirmOpenAddressMap();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    private boolean checkGps() {
        LocationManager myLocManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (!(myLocManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) &&
                !(myLocManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            showDialog(GPS_SETTING);
            return false;
        }
        return true;
    }
    private void showConfirmOpenAddressMap() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setMessage(R.string.message_call_google_map)
                .setPositiveButton(R.string.upload_request_button_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
//							callDrawLineMap();
                                if (!isNetworkAvailable()) {
                                    showDialog(NETWORK_UNAVAILABLE);
                                } else {
                                    /**
                                     * NguyenTT
                                     * Don't check location setting is on/off, Lets Google map handle it.
                                     */
                                    launchNavigationWithoutLocation();
                                }
                            }
                        }
                )
                .setNegativeButton(R.string.upload_request_button_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create().show();
    }
    /**
     * NguyenTT
     * Don't need current location when start google map, Lets Google map handle itself
     */
    private void launchNavigationWithoutLocation() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        if (mClubOpj.getLat() != 0 || mClubOpj.getLng() != 0) {    //緯度、経度がある場合はそれを使用
            intent.setData(Uri.parse("http://maps.google.com/maps?daddr=" + mClubOpj.getLat() + "," + mClubOpj.getLng() + "&dirflg=d"));
        } else if (mClubOpj.getAddress() != null && !mClubOpj.getAddress().equals("")) {    //住所を使用
            intent.setData(Uri.parse("http://maps.google.com/maps?daddr=" + mClubOpj.getAddress() + "&dirflg=d"));
        } else {    //クラブ名を使用
            intent.setData(Uri.parse("http://maps.google.com/maps?daddr=" + mClubOpj.getClubName() + "&dirflg=d"));
        }

        try {
            startActivity(intent);
            mLat = 0;
            mLon = 0;
        } catch (android.content.ActivityNotFoundException ex) {
            this.notifyMessage(getString(R.string.google_map_not_found));
        }
    }
    private void launchNavigation() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        if (mClubOpj.getLat() != 0 || mClubOpj.getLng() != 0) {    //緯度、経度がある場合はそれを使用
            intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + mLat + "," + mLon
                    + "&daddr=" + mClubOpj.getLat() + "," + mClubOpj.getLng() + "&dirflg=d"));
        } else if (mClubOpj.getAddress() != null && !mClubOpj.getAddress().equals("")) {    //住所を使用
            intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + mLat + "," + mLon
                    + "&daddr=" + mClubOpj.getAddress() + "&dirflg=d"));
        } else {    //クラブ名を使用
            intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + mLat + "," + mLon
                    + "&daddr=" + mClubOpj.getClubName() + "&dirflg=d"));
        }

        try {
            startActivity(intent);
            mLat = 0;
            mLon = 0;
        } catch (android.content.ActivityNotFoundException ex) {
            this.notifyMessage(getString(R.string.google_map_not_found));
        }
    }
    /*
    * 現在地取得
    */
    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(final Location location) {

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            if (location != null) {
                mLat = location.getLatitude();
                mLon = location.getLongitude();

                launchNavigation();
            } else {
                ActivitySelectRound.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ActivitySelectRound.this.notifyMessage(R.string.location_undetermined_message);
                    }
                });

            }
        }
    };
    /**
     * Author LAMTT
     *
     * @param callNum
     */
    private void openDial(final String callNum) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + callNum.replace("-", "").replace(".", "").replace("+", "")));
        startActivity(callIntent);
    }
    private class GolfDayCampaignDownloadTask extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progressDialog = new ProgressDialog(ActivitySelectRound.this);
            progressDialog.setMessage(ActivitySelectRound.this.getResources().getString(R.string.loading));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
//            try {
//                String result = "";
//                // Golf Day action
//                GolfDayCampaignAPI golfDayAPI = new GolfDayCampaignAPI();
//                result = golfDayAPI.getGolfDayCampaign();
//
//                return result;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            if (!TextUtils.isEmpty(result)) {
                if (Distance.isDonNotShowGolfDayMessage(ActivitySelectRound.this)) {
                    golfDayItemAction(false, result);
                } else {
                    golfDayItemAction(true, result);
                }

            }
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
    private void golfDayItemAction(final boolean isShowDialog, final String isCampaign) {

        if (mClubOpj.getGolfDayClubs().size() == 1) {
            openGolfDay(isShowDialog, isCampaign, mClubOpj.getGolfDayClubs().get(0).getClubId(), null, null, Constant.CALL_FROM.CLUB_DETAIL);
        } else {
            mDialogList = new Dialog(ActivitySelectRound.this);
            ListView lsvTee = new ListView(ActivitySelectRound.this);

            GolfDayAdapter mCourseAdapter = new GolfDayAdapter(ActivitySelectRound.this, mClubOpj.getGolfDayClubs());
            lsvTee.setAdapter(mCourseAdapter);
            mDialogList.setContentView(lsvTee);
            lsvTee.setBackgroundResource(R.drawable.login_background_repeat);
            lsvTee.setCacheColorHint(Color.TRANSPARENT);
            mDialogList.setTitle(getString(R.string.title_golf_day_select));
            YgoLog.d("TayPVS", "TayPVS - country - " + mClubOpj.getCountry());
            lsvTee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    openGolfDay(isShowDialog, isCampaign, mClubOpj.getGolfDayClubs().get(position).getClubId(), null, null, Constant.CALL_FROM.CLUB_DETAIL);
                    mDialogList.dismiss();
                }
            });
            mDialogList.show();
        }
    }
    /**
     * @author LAMTT custom dialog for COurse
     */
    private class GolfDayAdapter extends ArrayAdapter<GolfDayClub> {
        private final LayoutInflater mInflater;

        public GolfDayAdapter(Context context, List<GolfDayClub> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            GolfDayListViewHolder holder;
            if (view == null) {
                holder = new GolfDayListViewHolder();
                view = mInflater.inflate(R.layout.golf_day_list, parent,
                        false);
                holder.tvGolfNameView = (TextView) view
                        .findViewById(R.id.golf_day_club_name_item);
                view.setTag(holder);
            } else {
                holder = (GolfDayListViewHolder) view.getTag();
            }

            final GolfDayClub golfDayClub = getItem(position);
            final String clubName = golfDayClub.getClubName();
            if (clubName != null)
                holder.tvGolfNameView.setText(clubName);

            return view;
        }
    }
    /**
     * @author LAMTT COntrol of layout Course list
     */
    static class GolfDayListViewHolder {
        TextView tvGolfNameView;
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                // Check Permissions Granted or not
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showConfirmOpenAddressMap();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*ROUND FIRST END*/
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_address:
//                showConfirmOpenAddressMap();
//                break;
//            case R.id.call:
//            case R.id.btn_phone_dial:
//                String callNum = tvTel.getText().toString();
//                if (!callNum.equals("")) {
//                    openDial(callNum);
//                }
//
//                break;
//            case R.id.top_back_btn:
//                onBackPressed();
//                break;
//            case R.id.btn_club_name:
//                String urlClub = mClubOpj.getUrl();
//                if (!urlClub.equals("")) {
//                    openStandardBrowser(urlClub);
//                }
//                break;
//
//            case R.id.rv_golf_day_link:
//                new GolfDayCampaignDownloadTask().execute();
//                break;
//            case R.id.dateItem:
//                if (null != mDatePickerDialog && mDatePickerDialog.isShowing())
//                    return;
//                mDatePickerDialog.show();
//                break;
//            case R.id.hallItem:
//                // CongVC 2012-05-31 check is showing dialog
//                if (null != mDialogList && mDialogList.isShowing())
//                    return;
//                CustomDialog(this, R.id.hallItem);
//                break;
//            case R.id.courseItem:
//                // CongVC 2012-05-31 check is showing dialog
//                if (null != mDialogList && mDialogList.isShowing())
//                    return;
//                if (!isNetworkAvailable())
//                    AlertMessage(getString(R.string.warning),
//                            getString(R.string.network_erro_or_not_connet));
//                else
//                    CustomDialog(this, R.id.courseItem);
//                break;
//            case R.id.teaItem:
//                // CongVC 2012-05-31 check is showing dialog
//                if (null != mDialogList && mDialogList.isShowing())
//                    return;
//                // ThoLH - ticket 2319 ユーザとして、詳細情報がないゴルフ場を選択してしまったら、どうすべきか教えて欲しい -
//                // 2012-07-06
//                if (tees == null || tees.size() == 0) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySelectRound.this);
//                    LinearLayout messggeLayout = new LinearLayout(
//                            ActivitySelectRound.this);
//                    TextView txtMessage = new TextView(ActivitySelectRound.this);
//                    txtMessage.setText(getString(R.string.no_tees_message));
//                    txtMessage.setPadding(5, 5, 5, 5);
//                    txtMessage.setTextSize(14);
//                    txtMessage.setGravity(Gravity.CENTER);
//                    messggeLayout.addView(txtMessage);
//                    builder.setView(messggeLayout);
//                    builder.setNegativeButton(getString(R.string.ok),
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                }
//                            }).show();
//                } else {
//                    CustomDialog(this, R.id.teaItem);
//                }
//
//                break;
//            //TaiNN-score_input_request
//            case R.id.score_Request:
//
//                    if (!isNetworkAvailable()) {
//                        AlertMessage(getString(R.string.warning), getString(R.string.network_erro_or_not_connet));
//                    } else {
//
//                        // check for Guest user
//                        if(GuestUser.isUserGuest()){
//                            showDialogRequestSign();
//                        }else {
//                            // track when tap on Agency Request button
//                            mixpanel.track(Constant.Gtrack.PAGE_PLAY_PREPARE_AGENCY, null);
//
//                            isClick = true;
//                            // Check if user requested agency score is limit
//                            new CheckAgencyRequestLimitTask().execute();
//                        }
//                    }
//
//                break;
//            case R.id.ln_sunny:
//                changeBackGroundWeather(1);
//                break;
//            case R.id.ln_cloud:
//                changeBackGroundWeather(2);
//                break;
//            case R.id.ln_umbrella:
//                changeBackGroundWeather(3);
//                break;
//            case R.id.ln_snow:
//                changeBackGroundWeather(4);
//                break;
//            case R.id.ln_rainny:
//                changeBackGroundWeather(5);
//                break;
//            /*Group 3*/
//            case R.id.player1Layout:
//                FLAG_PLAYER_POSITION = 1;
//
//                break;
//            case R.id.player2Layout:
//                FLAG_PLAYER_POSITION = 2;
//                callSelectHistoryAct_St2();
//                break;
//            case R.id.player3Layout:
//                FLAG_PLAYER_POSITION = 3;
//                callSelectHistoryAct_St2();
//                break;
//            case R.id.player4Layout:
//                FLAG_PLAYER_POSITION = 4;
//                callSelectHistoryAct_St2();
//                break;
//            case R.id.curHandicap1Layout:
//                selectedId = 1;
//                showStrokeHandicapLayout(tvCurPlayerHandicap.getText().toString());
//                break;
//            case R.id.handicap2Layout:
//                if (!tvPlayer2.getText().toString().equals("")) {
//                    selectedId = 2;
//                    showStrokeHandicapLayout(tvPlayer2Handicap.getText().toString());
//                }
//                break;
//            case R.id.handicap3Layout:
//                if (!tvPlayer3.getText().toString().equals("")) {
//                    selectedId = 3;
//                    showStrokeHandicapLayout(tvPlayer3Handicap.getText().toString());
//                }
//                break;
//            case R.id.handicap4Layout:
//                if (!tvPlayer4.getText().toString().equals("")) {
//                    selectedId = 4;
//                    showStrokeHandicapLayout(tvPlayer4Handicap.getText().toString());
//                }
//                break;
//
//            case R.id.top_edit:
//            case R.id.play_hole:
//                if (isClick)
//                    return;
//                mixpanel.track(Constant.Gtrack.EVENT_TAP_PREPARE_ROUND_SCORE_INPUT,null);
//                Repro.track(Constant.Gtrack.EVENT_TAP_PREPARE_ROUND_SCORE_INPUT);
//                isClick = true;
//                String requestStr = getIntent().getExtras().getString(Constant.REQUEST_GOLF_FROM_LIVE);
//                if (requestStr != null && requestStr.equals(Constant.REQUEST_GOLF_FROM_LIVE)) {
//                    new StartBatchApiTask().execute();
//                } else {
//
//                    (new StartScoreEntryTask()).execute();
//                }
//
//                break;
//            case R.id.btnCancelHandicap:
//                finishEditHandicap(RESULT_CANCELED);
//                break;
//            case R.id.btnStoreHandicap:
//                finishEditHandicap(RESULT_OK);
//                break;
//            case R.id.clear_player2:
//            case R.id.clear_player2_btn:
//                if (tvPlayer2.getVisibility() == View.VISIBLE) {
//                    tvPlayer2.setText("");
//                    tvPlayer2.setHint(getResources().getString(R.string.select_player_name));
//                    btnClearPlayer2.setVisibility(View.INVISIBLE);
//                    mPlayerId2 = "";
//                    mName2 = "";
//                    mPlayerHadicap2 = "";
//                    tvPlayer2Handicap.setText(defaultHDCP);
//                    if (isPuma) {
//                        //((TextView) findViewById(R.id.tvDHCP2_hint)).setVisibility(View.VISIBLE);
//                    } else {
//                        View player = findViewById(R.id.player2Layout);
//                        player.setNextFocusRightId(R.id.handicap2Layout);
//                        findViewById(R.id.handicap2Layout).setNextFocusLeftId(R.id.player2Layout);
//
//                        player.requestFocus();
//                    }
//                }
//                break;
//
//            case R.id.clear_player3:
//            case R.id.clear_player3_btn:
//                if (tvPlayer3.getVisibility() == View.VISIBLE) {
//                    tvPlayer3.setHint(getResources().getString(R.string.select_player_name));
//                    tvPlayer3.setText("");
//                    btnClearPlayer3.setVisibility(View.INVISIBLE);
//                    mPlayerId3 = "";
//                    mName3 = "";
//                    mPlayerHadicap3 = "";
//                    tvPlayer3Handicap.setText(defaultHDCP);
//                    if (isPuma) {
//                        //((TextView) findViewById(R.id.tvDHCP3_hint)).setVisibility(View.VISIBLE);
//                    } else {
//                        View player = findViewById(R.id.player3Layout);
//                        player.setNextFocusRightId(R.id.handicap3Layout);
//                        findViewById(R.id.handicap3Layout).setNextFocusLeftId(R.id.player3Layout);
//
//                        player.requestFocus();
//                    }
//                }
//                break;
//            case R.id.clear_player4:
//            case R.id.clear_player4_btn:
//                if (tvPlayer4.getVisibility() == View.VISIBLE) {
//                    tvPlayer4.setHint(getResources().getString(R.string.select_player_name));
//                    tvPlayer4.setText("");
//                    btnClearPlayer4.setVisibility(View.INVISIBLE);
//                    mPlayerId4 = "";
//                    mName4 = "";
//                    mPlayerHadicap4 = "";
//                    tvPlayer4Handicap.setText(defaultHDCP);
//                    if (isPuma) {
//                        //((TextView) findViewById(R.id.tvDHCP4_hint)).setVisibility(View.VISIBLE);
//                    } else {
//                        View player = findViewById(R.id.player4Layout);
//                        player.setNextFocusRightId(R.id.handicap4Layout);
//                        findViewById(R.id.handicap4Layout).setNextFocusLeftId(R.id.player4Layout);
//
//                        player.requestFocus();
//                    }
//                }
//                break;
//            case R.id.btn_new_round_more_group3:
//                if(GuestUser.isUserGuest()){
//                    showDialogRequestSign();
//                }else {
//                    btnMoreGroup3.setVisibility(View.GONE);
//                    lnGroup3.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.btn_new_round_more_group4:
//                if(GuestUser.isUserGuest()){
//                    showDialogRequestSign();
//                }else {
//                    btnMoreGroup4.setVisibility(View.GONE);
//                    lnGroup4.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.btn_round_new_more_group1:
//                    rlClubDay.setVisibility(View.VISIBLE);
//                    btnMoreGroup1.setVisibility(View.GONE);
//                    lnMoreGroup1.setVisibility(View.VISIBLE);
//                break;
//            default:
//                break;
//        }
    }
    private void showDialogRequestSign(){
//        new DialogCustomRequestSignIn(this, new DialogCustomRequestSignIn.ListenerRequestDialog() {
//            @Override
//            public void signInRequest() {
//                Intent intent = DialogCustomRequestSignIn.createNewIntentStartLoginScreen(ActivitySelectRound.this);
//                startActivity(intent);
//            }
//        }).showDialog();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
/*GROUP 2*/

    private void initGroup2(){

        final Bundle extras = getIntent().getExtras();
        mResources = getResources();
        mSearchHistoryFlg = extras.getBoolean(
                getString(R.string.intent_search_history_mode), false);

        mPlaydate = Calendar.getInstance();
        mDatePickerDialog = createDatePickerDialog();

        getDefaultRound();

        if (extras.getBoolean(Constant.KEY_FROM_SCORE_AGENCY, false)) {

            // Set next focus
            if (!GolfApplication.isPuma() && Distance.isSmallDevice(this)) {
                rlTea.setNextFocusDownId(R.id.score_Request);
                rlTea.setNextFocusRightId(R.id.score_Request);
            }
        } else {
            // Set next focus
            if(!GolfApplication.isPuma() && Distance.isSmallDevice(this)) {

                rlTea.setNextFocusDownId(R.id.score_Request);
                btnScoreRequest.setNextFocusDownId(R.id.top_edit);
                btnScoreRequest.setNextFocusRightId(R.id.top_edit);
            }

        }

        // Initialize the weather hashmap
        weatherHash.put(Constant.SUNNY, getString(R.string.sunny));
        weatherHash.put(Constant.CLOUDY, getString(R.string.cloudy));
        weatherHash.put(Constant.RAINY, getString(R.string.rainy));
        weatherHash.put(Constant.SNOWING, getString(R.string.snowing));
        weatherHash.put(Constant.FOGGY, getString(R.string.foggy));
        String requestStr = extras.getString(Constant.REQUEST_GOLF_FROM_LIVE);
        if (requestStr != null && requestStr.equals(Constant.REQUEST_GOLF_FROM_LIVE)) {

            LiveInfo liveInfo = (LiveInfo) extras.getSerializable(Constant.LIVE_INFO);
            if (liveInfo != null) {
                // Parse date time format
                String newFormat = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                Date testDate = null;
                try {
                    testDate = sdf.parse(liveInfo.getPlayDate());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (testDate != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.date_format1));
                    newFormat = formatter.format(testDate);
                    // Update view
                    tvDatePlay.setText(newFormat);
                    mPlaydate.setTime(testDate);
                }

                // Update weather
                mWeather = liveInfo.getWeather().toLowerCase();
                switch (mWeather){
                    case Constant.SUNNY:
                        changeBackGroundWeather(1);
                    break;
                    case Constant.CLOUDY:
                        changeBackGroundWeather(2);
                        break;
                    case Constant.RAINY:
                        changeBackGroundWeather(3);
                        break;
                    case Constant.SNOWING:
                        changeBackGroundWeather(4);
                        break;
                    case Constant.FOGGY:
                        changeBackGroundWeather(5);
                        break;
                }
                // tvSunny.setText(weatherHash.get(mWeather));
            }
        }

    }

    private void changeBackGroundWeather(int num){
        lv_sunny.setBackgroundResource(0);
        lv_sunny.setImageResource(R.drawable.round_detail_wether_sunny_not_select);
        lv_cloud.setBackgroundResource(0);
        lv_cloud.setImageResource(R.drawable.round_detail_wether_cloud_not_select);
        lv_rainy.setBackgroundResource(0);
        lv_rainy.setImageResource(R.drawable.round_detail_wether_rainny_not_select);
        lv_snow.setBackgroundResource(0);
        lv_snow.setImageResource(R.drawable.round_detail_wether_snowing_not_select);
        lv_fog.setBackgroundResource(0);
        lv_fog.setImageResource(R.drawable.round_detail_wether_foggy_not_select);
        switch (num){
            case 1:
                lv_sunny.setBackgroundResource(R.drawable.btn_round_detail_weather_selector);
                lv_sunny.setImageResource(R.drawable.round_detail_wether_sunny);
                mWeather = Constant.SUNNY;
                break;
            case 2:
                lv_cloud.setBackgroundResource(R.drawable.btn_round_detail_weather_selector);
                lv_cloud.setImageResource(R.drawable.round_detail_wether_cloud);
                mWeather = Constant.CLOUDY;
                break;
            case 3:
                lv_rainy.setBackgroundResource(R.drawable.btn_round_detail_weather_selector);
                lv_rainy.setImageResource(R.drawable.round_detail_wether_rainny);
                mWeather = Constant.RAINY;
                break;
            case 4:
                lv_snow.setBackgroundResource(R.drawable.btn_round_detail_weather_selector);
                lv_snow.setImageResource(R.drawable.round_detail_wether_snowing);
                mWeather = Constant.SNOWING;
                break;
            case 5:
                lv_fog.setBackgroundResource(R.drawable.btn_round_detail_weather_selector);
                lv_fog.setImageResource(R.drawable.round_detail_wether_foggy);
                mWeather = Constant.FOGGY;
                break;
        }
    }
    @SuppressLint("SimpleDateFormat")
    private DatePickerDialog createDatePickerDialog() {
        final SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format1));

        int year = mPlaydate.get(Calendar.YEAR);
        int monthOfYear = mPlaydate.get(Calendar.MONTH);
        int dayOfMonth = mPlaydate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mPlaydate.set(year, monthOfYear, dayOfMonth);
                tvDatePlay.setText(format.format(mPlaydate.getTime()));
            }
        };

        final DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, year, monthOfYear, dayOfMonth);
        dialog.setTitle(R.string.title_tee_playdate);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                int year = mPlaydate.get(Calendar.YEAR);
                int monthOfYear = mPlaydate.get(Calendar.MONTH);
                int dayOfMonth = mPlaydate.get(Calendar.DAY_OF_MONTH);
                dialog.updateDate(year, monthOfYear, dayOfMonth);
            }
        });
        dialog.setOnKeyListener(new OnDatePickerDialogKeyListener());

        return dialog;
    }
    private void getDefaultRound() {
        // Tee
        tvCourse.setText(getString(R.string.msg_choose_course));
        tvCourse.setTextColor(getResources().getColorStateList(R.color.preparation_no_data_text_color_selector));
        if (null != mClubOpj && null != mClubOpj.getCourses()) {
            if (mClubOpj.getCourses().size() == 1) {
                rlCourse.setEnabled(false);
                rlCourse.setFocusable(false);

                if (mClubOpj.getExtType() != null && mClubOpj.getExtType().equals(Constant.EXT_TYPE_OOBGOLF)) {
                    mCourseId = mClubOpj.getCourses().get(0).getOobId();
                } else {
                    mCourseId = mClubOpj.getCourses().get(0).getYourGolfId();
                }
			    /*ThuNA 2013/05/24 ADD-E*/
                mCourse = mClubOpj.getCourses().get(0);
                if (!mSearchHistoryFlg) {
                    if (!isNetworkAvailable())
                        AlertMessage(getString(R.string.warning),
                                getString(R.string.network_erro_or_not_connet));
                    else {
                        (new GetTeeDefaultTask()).execute();
                    }

                }
                mHoleNum = mCourse.getHoles();
                if (mHoleNum == 9)
                    mSelectHole = false;
                SetCourseName(mClubOpj.getCourses().get(0).getCourseName());
            } else {
                rlCourse.setEnabled(true);
                rlCourse.setFocusable(true);
            }
        }

        if (mSearchHistoryFlg && null != mCourse) {

            tees = mCourse.getTees();
            if (null != tees && tees.size() == 1) {
                tvTea.setText(tees.get(0).getName());
                tvTea.setTextColor(getResources().getColorStateList(R.color.preparation_text_color_selector));
                mTeeSelectId = tees.get(0).getOobId();

            }
            lv_sunny.setBackgroundResource(R.drawable.btn_round_detail_weather_selector);
            //tvSunny.setText(getString(R.string.sunny));

            SetCourseName(mCourse.getCourseName());
            mHoleNum = (long) tees.get(0).getHoles().size();
            if (mHoleNum == 9) {
                mSelectHole = false;
            }

            btnScoreRequest.setAlpha(1);
            btnScoreRequest.setEnabled(true);
            btnScoreRequest.setFocusable(true);

            rlStart.setAlpha(1);
            rlStart.setEnabled(true);
            rlStart.setFocusable(true);
            btnEdit.setEnabled(true);
            btnEdit.setFocusable(true);

        } else {
           // tvSunny.setText(getString(R.string.sunny));
           lv_sunny.setBackgroundResource(R.drawable.btn_round_detail_weather_selector);
            tvTea.setText(getString(R.string.msg_choose_tee));
            tvTea.setTextColor(getResources().getColorStateList(R.color.preparation_no_data_text_color_selector));
        }
        tvHall.setText(getString(R.string.hole_one));
        mHall = Constant.HOLE_ONE;

        mWeather = Constant.SUNNY;
        tvDatePlay.setText(getString(R.string.today));
    }

    private class GetTeeDefaultTask extends AsyncTask<String, Integer, Long> {
        private ProgressDialog mDialog;

        @Override
        protected Long doInBackground(String... params) {

            getListTee(mCourseId);
            return (long) 1;
        }

        @Override
        protected void onPreExecute() {

           /* mDialog = new ProgressDialog(SearchRoundNew.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.msg_now_loading));
            if(mDialog != null)
                mDialog.show();*/
        }

        @Override
        protected void onPostExecute(Long courseId) {

            try {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            } catch (Exception e) {
            }
        }
    }
    private void SetCourseName(String courseName) {
        if (courseName.equals("")) {
            tvCourse.setText("-");
            ivClickCourse.setVisibility(View.GONE);
        } else {
            tvCourse.setText(courseName);
            ivClickCourse.setVisibility(View.INVISIBLE);
        }
        if (getString(R.string.msg_choose_course).equals(courseName)) {
            tvCourse.setTextColor(getResources().getColorStateList(R.color.preparation_no_data_text_color_selector));
        } else {
            tvCourse.setTextColor(getResources().getColorStateList(R.color.preparation_text_color_selector));
        }
    }
    public void getListTee(String courseId) {

        if (!mSearchHistoryFlg) {
            if (mClubOpj.getExtType() != null && mClubOpj.getExtType().equals(Constant.EXT_TYPE_OOBGOLF)) {
//                OobTeeDetailsAPI api = new OobTeeDetailsAPI(
//                        ActivitySelectRound.this);
//                tees = api.getTees(courseId);
            } else {

                HashMap<String, String> mParams = new HashMap<String, String>();
                mParams.put(YourGolfTeeDetailsJsonAPI.KEY_PARAMS_AUTH_TOKEN,
                        Distance.getAuthTokenLogin(this));
                mParams.put(Constant.KEY_APP,
                        Constant.KEY_API_APP_VERSION.replace(Constant.KEY_VERSION_NAME, GolfApplication.getVersionName()));
                YourGolfTeeDetailsJsonAPI apiJson = new YourGolfTeeDetailsJsonAPI();
                tees = apiJson.getSearchResult(mParams, courseId);

            }
            mCourse.setTees(tees);
        }
    }
    public void CustomDialog(Context context, int id) {

//        switch (id) {
//            case R.id.sunnyItem:
//               // CallWeatherDialog(m_context);
//                break;
//            case R.id.hallItem:
//                if (mSelectHole == true)
//                    CallHallDialog(context);
//                break;
//            case R.id.courseItem:
//                CallCourseDialog(context);
//                break;
//            case R.id.teaItem:
//                if (mCourseId.equals("") && !mSearchHistoryFlg) {
//                    AlertMessage(getString(R.string.warning),
//                            getString(R.string.tee_alert_submit));
//                } else {
//                    if (!mSearchHistoryFlg) {
//
//                        CallTeeDialog(this);
//                    }
//                }
//                break;
//        }
    }

    private void CallHallDialog(Context context) {
        // final Dialog dialog = new Dialog(GolfSearchTeaAct.this);
        // CongVC 2012-05-31 change dialog
        mDialogList = new Dialog(ActivitySelectRound.this);
        final String[] hallArr = {getString(R.string.hole_one),
                getString(R.string.hole_ten)};
        final ListView lsvHall = new ListView(context);
        lsvHall.setBackgroundResource(R.drawable.login_background_repeat);
        lsvHall.setCacheColorHint(Color.TRANSPARENT);

        HallAdapter hall = new HallAdapter(context, hallArr);
        lsvHall.setAdapter(hall);
        mDialogList.setContentView(lsvHall);
        mDialogList.setTitle(getString(R.string.title_tee_start));
        lsvHall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                tvHall.setText(hallArr[arg2]);
                mHall = (arg2 == 0 ? Constant.HOLE_ONE : Constant.HOLE_TEN);
                mDialogList.dismiss();
            }
        });
        mDialogList.show();
    }
    private void CallCourseDialog(Context context) {
        // final Dialog dialog = new Dialog(GolfSearchTeaAct.this);
        // CongVC 2012-05-31 change dialog
        mDialogList = new Dialog(ActivitySelectRound.this);
        ListView lsvCourse = new ListView(context);
        lsvCourse.setBackgroundResource(R.drawable.login_background_repeat);
        lsvCourse.setCacheColorHint(Color.TRANSPARENT);
        final List<Course> lstCourse = mClubOpj.getCourses();
        if (lstCourse != null && lstCourse.size() > 1) {
            CourseAdapter mCourseAdapter = new CourseAdapter(context, lstCourse);
            lsvCourse.setAdapter(mCourseAdapter);
            mDialogList.setContentView(lsvCourse);

            mDialogList.setTitle(getString(R.string.title_tee_course));
            lsvCourse
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            tvCourse.setText(lstCourse.get(arg2).getCourseName());
                            tvCourse.setTextColor(getResources().getColorStateList(R.color.preparation_text_color_selector));
                            mCourse = lstCourse.get(arg2);
                            if (mClubOpj.getExtType() != null && mClubOpj.getExtType().equals(
                                    Constant.EXT_TYPE_OOBGOLF)) {
                                mCourseId = lstCourse.get(arg2).getOobId();
                            } else {
                                mCourseId = lstCourse.get(arg2).getYourGolfId();
                            }
                            if (!isNetworkAvailable()) {
                                mDialogList.dismiss();
                                AlertMessage(
                                        getString(R.string.warning),
                                        getString(R.string.network_erro_or_not_connet));

                            } else {
                                CallTeeDialogTask callteeAnsy = new CallTeeDialogTask(
                                        mDialogList);
                                callteeAnsy.execute();
                            }
                            btnEdit.setEnabled(true);
                            btnEdit.setFocusable(true);

                        }
                    });
            mDialogList.show();
        }

    }
    protected class CheckAgencyRequestLimitTask extends AsyncTask<Void, Object, AgencyRequestSummaryObj> {
        private ProgressDialog mProgressDialog;
        private GetAgencyRequestSummaryApi api;

        public CheckAgencyRequestLimitTask() {
            mProgressDialog = new ProgressDialog(ActivitySelectRound.this);
        }

        @Override
        protected AgencyRequestSummaryObj doInBackground(Void... params) {
            api = new GetAgencyRequestSummaryApi();
            AgencyRequestSummaryObj summaryObj = api.get(Distance.getAuthTokenLogin(ActivitySelectRound.this));
            return summaryObj;
        }

        @Override
        protected void onPreExecute() {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage(getString(R.string.msg_now_loading));
                mProgressDialog.show();
            }
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(AgencyRequestSummaryObj result) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (result != null) {
                // show agency request state
                int availableCnt = result.getMax() - (result.getRequestedNumber() - result.getCanceledNumber());
                Bundle extras = getIntent().getExtras();
                if (availableCnt > 0) {

                    if ((!mCourseId.equals("") || mSearchHistoryFlg) && !mTeeSelectId.equals("")) {
//                        Intent intent = new Intent(ActivitySelectRound.this, GolfScoreRequestAct.class);
//                        intent.putExtra(Constant.CLUB_OPJ_SEARCH, mClubOpj);
//                        intent.putExtra(Constant.COURSE_SEARCH, mCourse);
//                        intent.putExtra(Constant.TEE_SEARCH, mTeeSelectId);
//                        intent.putExtra(getString(R.string.intent_search_history_mode), mSearchHistoryFlg);
//                        intent.putExtra(Constant.HALL_SEARCH, mHall);
//                        intent.putExtra(Constant.WEATHER_SEARCH, mWeather);
//                        intent.putExtra(Constant.DATE_PLAY_SEARCH, mPlaydate.getTimeInMillis());
//                        intent.putExtra(Constant.HOLE_COUNT, mHoleNum);
//                        String requestStr = extras.getString(Constant.REQUEST_GOLF_FROM_LIVE);
//                        if (requestStr != null) {
//                            intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, requestStr);
//                            LiveInfo liveInfo = (LiveInfo) getIntent().getExtras().getSerializable(Constant.LIVE_INFO);
//                            intent.putExtra(Constant.LIVE_INFO, liveInfo);
//                        }
//                        startActivityForResult(intent, 0);
                    }
                } else {
                    int isChargeUser = extras.getInt(Constant.KEY_IS_WEB_CHARGE_USER, 0);
                    if (isChargeUser == 0) {
                        // checking campaign user experience
//                        Intent intent = new Intent(ActivitySelectRound.this, CampaignCodeEnterAct.class);
//                        intent.putExtra(Constant.CLUB_OPJ_SEARCH, mClubOpj);
//                        intent.putExtra(Constant.COURSE_SEARCH, mCourse);
//                        intent.putExtra(Constant.TEE_SEARCH, mTeeSelectId);
//                        intent.putExtra(getString(R.string.intent_search_history_mode), mSearchHistoryFlg);
//                        intent.putExtra(Constant.HALL_SEARCH, mHall);
//                        intent.putExtra(Constant.WEATHER_SEARCH, mWeather);
//                        intent.putExtra(Constant.DATE_PLAY_SEARCH, mPlaydate.getTimeInMillis());
//                        intent.putExtra(Constant.HOLE_COUNT, mHoleNum);
//                        String requestStr = extras.getString(Constant.REQUEST_GOLF_FROM_LIVE);
//                        if (requestStr != null) {
//                            intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, requestStr);
//                            LiveInfo liveInfo = (LiveInfo) getIntent().getExtras().getSerializable(Constant.LIVE_INFO);
//                            intent.putExtra(Constant.LIVE_INFO, liveInfo);
//                        }
//                        if(!result.getCampaignUserExperience()){
//
//                            intent.putExtra(Constant.CAMPAIGN_USE_EXPERIENCE,false);
//                        }else{
//
//                            intent.putExtra(Constant.CAMPAIGN_USE_EXPERIENCE,true);
//                        }
//
//                        startActivityForResult(intent, 0);
                    } else {
                        AlertMessage(getString(R.string.warning), getString(R.string.yourgolf_request_score_e0161));
                    }
                }
            } else if (api.getmResult() == Constant.ErrorServer.ERROR_SOCKET_TIMEOUT ||
                    api.getmResult() == Constant.ErrorServer.ERROR_CONNECT_TIMEOUT
                    || api.getmResult() == Constant.ErrorServer.ERROR_GENERAL) {

                AlertMessage(getString(R.string.warning), getString(R.string.network_erro_or_not_connet));
            } else if (api.getmResult() == Constant.ErrorServer.ERROR_E0105) {
                // code:401 The authentication token is invalid.
                AlertMessage(getString(R.string.warning), getString(R.string.yourgolf_account_update_e0105));
            }

            isClick = false;
            api = null;
            super.onPostExecute(result);
        }
    }
    private void CallTeeDialog(Context context) {
        if (tees == null || tees.size() == 0) {
            tvTea.setText(getString(R.string.msg_choose_tee));
            tvTea.setTextColor(getResources().getColorStateList(R.color.preparation_no_data_text_color_selector));
            // ThoLH - ticket 2319 ユーザとして、詳細情報がないゴルフ場を選択してしまったら、どうすべきか教えて欲しい -
            // 2012-07-06
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySelectRound.this);
            LinearLayout messggeLayout = new LinearLayout(ActivitySelectRound.this);
            TextView txtMessage = new TextView(ActivitySelectRound.this);
            txtMessage.setText(getString(R.string.no_tees_message));
            txtMessage.setPadding(5, 5, 5, 5);
            txtMessage.setTextSize(14);
            txtMessage.setGravity(Gravity.CENTER);
            messggeLayout.addView(txtMessage);
            builder.setView(messggeLayout);
            builder.setNegativeButton(getString(R.string.ok),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            mTeeSelectId = "";
            return;
        }
        // final Dialog dialog = new Dialog(GolfSearchTeaAct.this);
        // CongVC 2012-05-31 change dialog
        mDialogList = new Dialog(ActivitySelectRound.this);
        ListView lsvTee = new ListView(ActivitySelectRound.this);

        TeeAdapter mCourseAdapter = new TeeAdapter(ActivitySelectRound.this, tees);
        lsvTee.setAdapter(mCourseAdapter);
        mDialogList.setContentView(lsvTee);
        lsvTee.setBackgroundResource(R.drawable.login_background_repeat);
        lsvTee.setCacheColorHint(Color.TRANSPARENT);
        mDialogList.setTitle(getString(R.string.title_tee_select));
        lsvTee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mCourse.setTees(tees);
                mTeeSelectId = tees.get(arg2).getOobId();
                tvTea.setText(tees.get(arg2).getName());
                tvTea.setTextColor(getResources().getColorStateList(R.color.preparation_text_color_selector));
                mDialogList.dismiss();
                btnScoreRequest.setAlpha(1);
                btnScoreRequest.setEnabled(true);
                btnScoreRequest.setFocusable(true);

                rlStart.setAlpha(1);
                rlStart.setEnabled(true);
                rlStart.setFocusable(true);
                btnEdit.setEnabled(true);
                btnEdit.setFocusable(true);
            }
        });
        mDialogList.show();

    }

    private class TeeAdapter extends ArrayAdapter<Tee> {
        private final LayoutInflater mInflater;

        public TeeAdapter(Context context, List<Tee> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            TeeListViewHolder holder;
            if (view == null) {
                holder = new TeeListViewHolder();
                view = mInflater.inflate(R.layout.search_course_list, parent,
                        false);
                holder.tvTeeNameView = (TextView) view
                        .findViewById(R.id.course_name_item);
                view.setTag(holder);
            } else {
                holder = (TeeListViewHolder) view.getTag();
            }

            final Tee tee = getItem(position);
            final String courseName = tee.getName();

            holder.tvTeeNameView.setText(courseName);

            return view;
        }
    }

    /**
     * @author LAMTT COntrol of layout Course list
     */
    static class TeeListViewHolder {
        TextView tvTeeNameView;
    }
    private class HallAdapter extends ArrayAdapter<String> {
        private final LayoutInflater mInflater;

        public HallAdapter(Context context, String[] weather) {
            super(context, 0, weather);
            mInflater = LayoutInflater.from(context);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            HallViewHolder holder;
            if (view == null) {
                holder = new HallViewHolder();
                view = mInflater
                        .inflate(R.layout.select_weather, parent, false);
                holder.tvHallNameView = (TextView) view
                        .findViewById(R.id.tvWeatherName);
                view.setTag(holder);
            } else {
                holder = (HallViewHolder) view.getTag();
            }
            String name = getItem(position);
            holder.tvHallNameView.setText(name);

            return view;
        }
    }

    /**
     * @author LAMTT COntrol of layout Weather list
     */
    static class HallViewHolder {
        TextView tvHallNameView;
    }
    private class CourseAdapter extends ArrayAdapter<Course> {
        private final LayoutInflater mInflater;

        public CourseAdapter(Context context, List<Course> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            CourseListViewHolder holder;
            if (view == null) {
                holder = new CourseListViewHolder();
                view = mInflater.inflate(R.layout.search_course_list, parent,
                        false);
                holder.courseNameView = (TextView) view
                        .findViewById(R.id.course_name_item);
                view.setTag(holder);
            } else {
                holder = (CourseListViewHolder) view.getTag();
            }

            final Course course = getItem(position);
            final String courseName = course.getCourseName();

            holder.courseNameView.setText(courseName);
            return view;
        }
    }

    /**
     * @author LAMTT COntrol of layout Course list
     */
    static class CourseListViewHolder {
        TextView courseNameView;

    }
    private  void setBackgroundForLoad(ListView lsvCourse){
        lsvCourse.setBackgroundResource(R.drawable.login_background_repeat);
        lsvCourse.setCacheColorHint(Color.TRANSPARENT);
    }
    private class CallTeeDialogTask extends AsyncTask<String, Integer, Long> {
        private ProgressDialog mDialog;
        Dialog dialogPopub;

        public CallTeeDialogTask(Dialog dialog) {
            dialogPopub = dialog;
        }

        ListView lsvCourse = new ListView(ActivitySelectRound.this);

        @Override
        protected Long doInBackground(String... params) {
            if (mSearchHistoryFlg) {
                return (long) 0;
            }
            setBackgroundForLoad(lsvCourse);

            getListTee(mCourseId);
            return (long) 1;
        }

        @Override
        protected void onPreExecute() {

            mDialog = new ProgressDialog(ActivitySelectRound.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.msg_now_loading));
            if (!isFinishing())
                mDialog.show();
        }

        @Override
        protected void onPostExecute(Long courseId) {

            if (courseId == 1) {
                dialogPopub.dismiss();
            }
            mHoleNum = mCourse.getHoles();
            if (mHoleNum == 9) {
                mSelectHole = false;

                tvHall.setText(getString(R.string.hole_one));
                mHall = Constant.HOLE_ONE;
            } else {
                mSelectHole = true;
            }
            tvTea.setText(getString(R.string.msg_choose_tee));
            tvTea.setTextColor(getResources().getColorStateList(R.color.preparation_no_data_text_color_selector));
            mTeeSelectId = "";
            mDialog.dismiss();
        }
    }
    private void AlertMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        }).show();
    }

/*Group 3*/

    private void initGroup3AddPlayer(){
        isPuma = GolfApplication.isPuma();
        collectDhcpData();

        baseAdapter1 = new CustomBaseAdapter(0);
        lstStrokeHandicap1.setAdapter(baseAdapter1);
        lstStrokeHandicap1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                selectedIndex1 = arg2;
                baseAdapter1.notifyDataSetChanged();
                if (arg2 == 7) {
                    selectedIndex2 = 0;
                    baseAdapter2.notifyDataSetChanged();
                } else if (selectedIndex2 == 0 || selectedIndex2 == -1) {
                    selectedIndex2 = 1;
                    baseAdapter2.notifyDataSetChanged();
                }
                // scroll to visible
                scrollForVisible(lstStrokeHandicap2, selectedIndex2);
            }
        });
        baseAdapter2 = new CustomBaseAdapter(1);
        lstStrokeHandicap2.setAdapter(baseAdapter2);
        lstStrokeHandicap2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                selectedIndex2 = arg2;
                baseAdapter2.notifyDataSetChanged();
                if (arg2 == 0) {
                    selectedIndex1 = 7;
                    baseAdapter1.notifyDataSetChanged();
                } else if (selectedIndex1 == 7 || selectedIndex1 == -1) {
                    selectedIndex1 = 6;
                    baseAdapter1.notifyDataSetChanged();
                }
                // scroll to visible
                scrollForVisible(lstStrokeHandicap1, selectedIndex1);
            }
        });

        mDb = GolfDatabase.getInstance(this);

        PlayerCursor playerCursor = mDb.getOwner();

        if (null == playerCursor || playerCursor.getCount() <= 0) {
            playerCursor = mDb.getOwnerByID();
        }

        if (null != playerCursor && playerCursor.getCount() > 0) {
//            userName = GolfTop.getProfileName(this, playerCursor.getId(), playerCursor.getName());
        } else {
            userName = "me";
        }

        tvPlayer1.setText(userName);

        mPlayerIds = new ArrayList<>();
        mPlayerIds.add(playerCursor.getId());
        playerCursor.close();

        r = getResources();

      //  Bundle extras = getIntent().getExtras();

       /* mClubOpj = (ClubObj) extras.getSerializable(Constant.CLUB_OPJ_SEARCH);
        mCourse = (Course) extras.getSerializable(Constant.COURSE_SEARCH);
        mTeeSelectId = extras.getString(Constant.TEE_SEARCH);
        mHall = extras.getString(Constant.HALL_SEARCH);
        mLongDatePlay = extras.getLong(Constant.DATE_PLAY_SEARCH);
        mWeather = extras.getString(Constant.WEATHER_SEARCH);
        mHoleCount = extras.getLong(Constant.HOLE_COUNT);*/

//        mApplication = (GolfApplication) getApplication();

        // Call api to get handicap of current user
        new GetPlayerHandicapTask(this).execute();

        mixpanel = MixpanelAPI.getInstance(this, Constant.MIX_PANEL_TOKEN);
        mixpanel.track(Constant.Gtrack.PAGE_PREPARE_PLAYER, null);


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    if(!isFromAgency) {
            outState.putInt("FLAG_PLAYER_POSITION", FLAG_PLAYER_POSITION);
            outState.putInt("selectedIndex1", selectedIndex1);
            outState.putInt("selectedIndex2", selectedIndex2);
            outState.putInt("selectedId", selectedId);

            outState.putString("name2", mName2);
            outState.putString("name3", mName3);
            outState.putString("name4", mName4);

            outState.putString("id2", mPlayerId2);
            outState.putString("id3", mPlayerId3);
            outState.putString("id4", mPlayerId4);

            outState.putString("playerHadicap2", mPlayerHadicap2);
            outState.putString("playerHadicap3", mPlayerHadicap3);
            outState.putString("playerHadicap4", mPlayerHadicap4);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (null != savedInstanceState) {
            FLAG_PLAYER_POSITION = savedInstanceState.getInt("FLAG_PLAYER_POSITION", 2);
            selectedIndex1 = savedInstanceState.getInt("selectedIndex1", -1);
            selectedIndex2 = savedInstanceState.getInt("selectedIndex2", -1);
            selectedId = savedInstanceState.getInt("selectedId", -1);

            mName2 = savedInstanceState.getString("name2");
            mName3 = savedInstanceState.getString("name3");
            mName4 = savedInstanceState.getString("name4");

            mPlayerId2 = savedInstanceState.getString("id2");
            mPlayerId3 = savedInstanceState.getString("id3");
            mPlayerId4 = savedInstanceState.getString("id4");

            mPlayerHadicap2 = savedInstanceState.getString("playerHadicap2");
            mPlayerHadicap3 = savedInstanceState.getString("playerHadicap3");
            mPlayerHadicap4 = savedInstanceState.getString("playerHadicap4");
        }
    }
    @Override
    protected void onDestroy() {

        mixpanel.flush();
        if(!isFromAgency)
            CleanUpUtil.cleanupView(findViewById(R.id.selectplayers_edit_st2));
        if (view != null) {
            view.destroy();
        }
        super.onDestroy();
        System.gc();
    }
    @Override
    protected void onResume() {
        super.onResume();
        isClick = false;
        if(!isFromAgency)
            resizeLayoutHDCPLayout();

    }
    // TayPVS
    private void resizeLayoutHDCPLayout(){
        if(rowWidth!=0&&rowHeight!=0) {
            RelativeLayout.LayoutParams rowParams = new RelativeLayout.LayoutParams(rowWidth, rowHeight);
            rowParams.height = rowHeight;
            rowParams.width = rowWidth;
            rowParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ((RelativeLayout) findViewById(R.id.curHandicap1Layout)).setLayoutParams(rowParams);
            ((RelativeLayout) findViewById(R.id.handicap2Layout)).setLayoutParams(rowParams);
            ((RelativeLayout) findViewById(R.id.handicap3Layout)).setLayoutParams(rowParams);
            ((RelativeLayout) findViewById(R.id.handicap4Layout)).setLayoutParams(rowParams);
        }
    }

    private class CustomBaseAdapter extends BaseAdapter {

        private int type = 0;

        /**
         * If type == 0 this is a decimal (for first list view). Other wise, this is for second list view.
         * @param type
         */
        public CustomBaseAdapter(int type) {
            this.type = type;
        }
        @Override
        public int getCount() {
            if (type == 0) {
                return list1ShownDhcp.length;
            } else {
                return list2ShownDhcp.length;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.hdcp_item, null);

            TextView tvHdcp = (TextView) retval.findViewById(R.id.hdcp);
            if (type == 0) {
                tvHdcp.setGravity(Gravity.CENTER | Gravity.RIGHT);
                tvHdcp.setText(list1ShownDhcp[position].toString());
                if(selectedIndex1 != -1 && selectedIndex1 == position){
                    retval.setBackgroundResource(R.drawable.gradation_lightyellow);
                }else{
                    retval.setBackgroundResource(R.drawable.hdcp_list_item_bg);
                }
            } else {
                tvHdcp.setGravity(Gravity.CENTER | Gravity.LEFT);
                tvHdcp.setText(list2ShownDhcp[position].toString());
                if(selectedIndex2 != -1 && selectedIndex2 == position){
                    retval.setBackgroundResource(R.drawable.gradation_lightyellow);
                }else{
                    retval.setBackgroundResource(R.drawable.hdcp_list_item_bg);
                }
            }
            return retval;
        }
    }
    private void collectDhcpData() {

        // Initialize the dhcp default value
        ArrayList<String> list1 = new ArrayList<>();
        for(int index = -6; index <= 40; index ++){
            if(index < 0){
                String item = "+" + Math.abs(index); // +6, +5,.....
                list1.add(item);
            }else{
                if(index == 1){
                    list1.add("-");
                }
                list1.add(String.valueOf(index));
            }
        }
        list1ShownDhcp = list1.toArray();

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("-");
        for (int i = 0; i < 10; i++) {
            list2.add("." + i);
        }
        list2ShownDhcp = list2.toArray();

    }
    private class GetPlayerHandicapTask extends AsyncTask<Integer, Integer, PlayerObj> {
        private GetPlayerHandicapApi api;
        private ProgressDialog dialog;
        private ContextUtil contextUtil;
        private Context context;

        public GetPlayerHandicapTask(Context context) {
            this.context = context;
        }


        @Override
        protected PlayerObj doInBackground(Integer... params) {
            PlayerObj result = new PlayerObj();

            try {
                result = api.get();
            } catch (Exception e) {
                YgoLog.d("CALLAPI","error: "+e.getMessage());
                Crashlytics.logException(e);
                result.setErrorStatus(Constant.ErrorServer.ERROR_GENERAL);
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            contextUtil = new ContextUtil(context);

            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage(context.getString(R.string.msg_now_loading));
            dialog.show();

            String idServer = Distance.GetProfileItem(ActivitySelectRound.this, getString(R.string.pref_profile_id_server));
            if (null == idServer || idServer.trim().equals("")) {
                PlayerCursor pc = mDb.getOwner();
                if (null != pc && pc.getCount() > 0) {
                    idServer = pc.getServerId();
                }
            }

            Map<String, String> baseParams = new HashMap<String, String>();
            baseParams.put("auth_token", Distance.getAuthTokenLogin(ActivitySelectRound.this));
            baseParams.put("id", String.valueOf(idServer));
            api = new GetPlayerHandicapApi(baseParams);
        }

        @Override
        protected void onPostExecute(PlayerObj result) {
            dialog.dismiss();
            if (result.getErrorStatus() == null) {
                // Udpate UI
                updateView(result);
            } else {
                contextUtil.handleErrorStatus(result.getErrorStatus());
            }
        }


    }

    private void restoreDataAfterSkillAct() {

        // Get handicap for player 2
        String player2Hdcp = mPlayerHadicap2;
        if (player2Hdcp != null && player2Hdcp.length() > 0 && !player2Hdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
            tvPlayer2Handicap.setText(parseToHdcpDisplay(player2Hdcp));
            btnClearPlayer2.setVisibility(View.VISIBLE);
        } else {
            btnClearPlayer2.setVisibility(View.GONE);
            tvPlayer2.setHint(getResources().getString(R.string.select_player_name));
        }
        // Get handicap for player 3
        String player3Hdcp = mPlayerHadicap3;
        if (player3Hdcp != null && player3Hdcp.length() > 0 && !player3Hdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
            tvPlayer3Handicap.setText(parseToHdcpDisplay(player3Hdcp));
            btnClearPlayer3.setVisibility(View.VISIBLE);
        } else {
            btnClearPlayer3.setVisibility(View.GONE);
            tvPlayer3.setHint(getResources().getString(R.string.select_player_name));
        }
        // Get handicap for player 4
        String player4Hdcp = mPlayerHadicap4;
        if (player4Hdcp != null && player4Hdcp.length() > 0 && !player4Hdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
            tvPlayer4Handicap.setText(parseToHdcpDisplay(player4Hdcp));
            btnClearPlayer4.setVisibility(View.VISIBLE);
        } else {
            btnClearPlayer4.setVisibility(View.GONE);
            tvPlayer4.setHint(getResources().getString(R.string.select_player_name));

        }

        if (!mName2.equals("")) {
            tvPlayer2.setText(mName2);
            tvPlayer2.setVisibility(View.VISIBLE);

            btnClearPlayer2.setVisibility(View.VISIBLE);
            if (!isPuma) {
                findViewById(R.id.player2Layout).setNextFocusRightId(R.id.clear_player2);
                findViewById(R.id.handicap2Layout).setNextFocusLeftId(R.id.clear_player2);
            }

            // Add the hdcp to player
            if (mPlayerHadicap2 != null && mPlayerHadicap2.length() > 0) {
                if (!mPlayerHadicap2.equals("null") && !mPlayerHadicap2.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                   tvPlayer2Handicap.setText(parseToHdcpDisplay(mPlayerHadicap2));
                   btnClearPlayer2.setVisibility(View.VISIBLE);
                } else {
                    tvPlayer2.setHint(getResources().getString(R.string.select_player_name));
                   btnClearPlayer2.setVisibility(View.GONE);
                }

            } else {
                tvPlayer2.setHint(getResources().getString(R.string.select_player_name));
                btnClearPlayer2.setVisibility(View.GONE);
            }
            /*ThuNA 2013/05/09 ADD-E*/
        }
        if (!mName3.equals("")) {
            tvPlayer3.setText(mName3);
            tvPlayer3.setVisibility(View.VISIBLE);
            btnClearPlayer3.setVisibility(View.VISIBLE);

            if (!isPuma) {
                findViewById(R.id.player3Layout).setNextFocusRightId(R.id.clear_player3);
                findViewById(R.id.handicap3Layout).setNextFocusLeftId(R.id.clear_player3);
            }

            // Add the hdcp to player
            if (mPlayerHadicap3 != null && mPlayerHadicap3.length() > 0) {
                if (!mPlayerHadicap3.equals("null") && !mPlayerHadicap3.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                    tvPlayer3Handicap.setText(parseToHdcpDisplay(mPlayerHadicap3));
                    btnClearPlayer3.setVisibility(View.VISIBLE);
                } else {
                    tvPlayer3.setHint(getResources().getString(R.string.select_player_name));
                    btnClearPlayer3.setVisibility(View.GONE);
                }

            } else {
               tvPlayer3.setHint(getResources().getString(R.string.select_player_name));
                btnClearPlayer3.setVisibility(View.GONE);
            }
            /*ThuNA 2013/05/09 ADD-E*/
        }
        if (!mName4.equals("")) {
            tvPlayer4.setText(mName4);
            tvPlayer4.setVisibility(View.VISIBLE);
            btnClearPlayer4.setVisibility(View.VISIBLE);

            if (!isPuma) {
                findViewById(R.id.player4Layout).setNextFocusRightId(R.id.clear_player4);
                findViewById(R.id.handicap4Layout).setNextFocusLeftId(R.id.clear_player4);
            }

            // Add the hdcp to player
            if (mPlayerHadicap4 != null && mPlayerHadicap4.length() > 0) {
                if (!mPlayerHadicap4.equals("null") && !mPlayerHadicap4.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                    tvPlayer4Handicap.setText(parseToHdcpDisplay(mPlayerHadicap4));
                    btnClearPlayer4.setVisibility(View.VISIBLE);
                } else {
                    tvPlayer4.setHint(getResources().getString(R.string.select_player_name));
                    btnClearPlayer4.setVisibility(View.GONE);
                }
            } else {
                tvPlayer4.setHint(getResources().getString(R.string.select_player_name));
                btnClearPlayer4.setVisibility(View.GONE);
            }
        }
    }
    private void updateView(PlayerObj playerObj) {
        if (playerObj != null) {
            String curHdcp = playerObj.getPlayerHdcp();
            if (curHdcp != null && curHdcp.length() > 0) {
                YgoLog.i("TASK", "updateView ++++++++ " + curHdcp);
                if (!curHdcp.equals("null") && !curHdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                   tvCurPlayerHandicap.setText(parseToHdcpDisplay(curHdcp));
                    tvCurPlayerHandicap.setVisibility(View.VISIBLE);
                } else {
                    tvCurPlayerHandicap.setVisibility(View.INVISIBLE);
                }

            } else {
                tvCurPlayerHandicap.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void showStrokeHandicapLayout(String hdcp) {
        switch (selectedId) {
            case 1:
                selectedView = findViewById(R.id.curHandicap1Layout);
                break;
            case 2:
                selectedView = findViewById(R.id.handicap2Layout);
                break;
            case 3:
                selectedView = findViewById(R.id.handicap3Layout);
                break;
            case 4:
                selectedView = findViewById(R.id.handicap4Layout);
                break;
        }

        if (hdcp == null || hdcp.length() == 0) {
            hdcp = "-";
        }
        if (hdcp.equals("-")) {
            // Find the selected index
            for(int index = 0; index < list1ShownDhcp.length; index++){
                if(hdcp.equals(list1ShownDhcp[index].toString())){
                    selectedIndex1 = index;
                    break;
                }
            }
            for (int index = 0; index < list2ShownDhcp.length; index++) {
                if(hdcp.equals(list2ShownDhcp[index].toString())){
                    selectedIndex2 = index;
                    break;
                }
            }
        } else {
            String[] element = hdcp.split("\\.");
            YgoLog.e("CanNC", "HDCP: " + hdcp + " => length:" + element.length);
            // Find the selected index
            for(int index = 0; index < list1ShownDhcp.length; index++){
                if(element[0].equals(list1ShownDhcp[index].toString())){
                    selectedIndex1 = index;
                    break;
                }
            }
            String second = "-";
            if (element.length > 1) {
                second = "." + element[1];
            }
            for (int index = 0; index < list2ShownDhcp.length; index++) {
                if(second.equals(list2ShownDhcp[index].toString())){
                    selectedIndex2 = index;
                    break;
                }
            }
        }
        if(strokeHandicapLayout.getVisibility() != View.VISIBLE){
            // Show stroke layout with animation
            strokeHandicapLayout.setAnimation(outToTopAnimation());
            strokeHandicapLayout.setVisibility(View.VISIBLE);

            if (Distance.isSmallDevice(this)) {
                findViewById(R.id.btnCancelHandicap).requestFocus();
            }
        }

        if(selectedIndex1 != -1){
            baseAdapter1.notifyDataSetChanged();
            lstStrokeHandicap1.setSelection(selectedIndex1);
        }
        if(selectedIndex2 != -1){
            baseAdapter2.notifyDataSetChanged();
            lstStrokeHandicap2.setSelection(selectedIndex2);
        }
    }
    private void callSelectHistoryAct_St2() {
        if (isNetworkAvailable()) {
            try {
                List<String> lstPlayerIds = new ArrayList<String>();
                if (!mPlayerId2.equals(""))
                    lstPlayerIds.add(mPlayerId2);
                if (!mPlayerId3.equals(""))
                    lstPlayerIds.add(mPlayerId3);
                if (!mPlayerId4.equals(""))
                    lstPlayerIds.add(mPlayerId4);
                String[] playerIds = new String[lstPlayerIds.size()];
                for (int i = 0; i < playerIds.length; i++) {
                    playerIds[i] = lstPlayerIds.get(i);
                }
//                Intent intent = new Intent(this, SelectPlayerHistoryAtc_St2.class);
//                intent.putExtra(Constant.PLAYER_IDS, playerIds);
//                startActivityForResult(intent, FLAG_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            notifyMessage(R.string.network_erro_or_not_connet);
        }
    }
    private String getTextCurPlayerHandicap(){
        return tvCurPlayerHandicap.getText().toString();
    }
    private String getTextPlayer2(){
        return tvPlayer2.getText().toString().trim();
    }
    private String getTextPlayer3(){
        return tvPlayer3.getText().toString().trim();
    }
    private String getTextPlayer4(){
        return tvPlayer4.getText().toString().trim();
    }
    private class StartBatchApiTask extends AsyncTask<String, Integer, Constant.ErrorServer> {
        private ProgressDialog mDialog;
        private HashMap<String, String> arrResult;
        private String jsonRequest = "";
        private String jsonResponse = "";

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(ActivitySelectRound.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(R.string.msg_now_loading));

            if (!isFinishing()) {
                mDialog.show();
            }

        }

        @Override
        protected Constant.ErrorServer doInBackground(String... strings) {

            ArrayList<String> arrPlayerIds = new ArrayList<String>();

            String idServerOwner = Distance.GetProfileItem(ActivitySelectRound.this, getString(R.string.pref_profile_id_server));
            if (null == idServerOwner || idServerOwner.trim().equals("")) {
                PlayerCursor pc = mDb.getOwner();
                if (null != pc && pc.getCount() > 0) {
                    idServerOwner = pc.getServerId();
                }
            }

            arrPlayerIds.add(idServerOwner);
            if (!getTextPlayer2().equals("")) {
                arrPlayerIds.add(mPlayerId2);
            }
            if (!getTextPlayer3().equals("")) {
                arrPlayerIds.add(mPlayerId3);
            }
            if (!getTextPlayer4().equals("")) {
                arrPlayerIds.add(mPlayerId4);
            }

            String[] arrPlayer = new String[arrPlayerIds.size()];
            for (int i = 0; i < arrPlayerIds.size(); i++) {
                arrPlayer[i] = arrPlayerIds.get(i);
            }

            HashMap<String, String> hashParam = new HashMap<String, String>();
            hashParam.put(BatchAPI.KEY_PARAMS_AUTH_TOKEN, Distance.getAuthTokenLogin(ActivitySelectRound.this));

            String liveEntryId = "";
            LiveInfo liveInfo = (LiveInfo) getIntent().getExtras().getSerializable(Constant.LIVE_INFO);
            if (liveInfo != null) {
                liveEntryId = liveInfo.getEntryId();
            }

            hashParam.put(BatchAPI.KEY_LIVE_ENTRY_ID, liveEntryId);

            String courseId = "";
            if (mCourse.getOobId() != null && !mCourse.getOobId().equals("")) {
                courseId = mCourse.getOobId();
            } else {
                courseId = mCourse.getYourGolfId();
            }
            hashParam.put(BatchAPI.KEY_COURSE_ID, courseId);

            BatchAPI batchApi = new BatchAPI();

            arrResult = batchApi.execBatch(hashParam, arrPlayer);

            jsonRequest = batchApi.getJsonRequest();
            jsonResponse = batchApi.getJsonResponse();

            return batchApi.getmResult();
        }

        @Override
        protected void onPostExecute(Constant.ErrorServer errorServer) {
            if (errorServer == Constant.ErrorServer.NONE && arrResult != null && !arrResult.isEmpty()) {
                new StartScoreEntryTask(mDialog, arrResult).execute();
            } else {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                    mDialog = null;
                }

                throw new BatchException(getString(R.string.msg_error_send_batch_api)
                        + "\n\n" + getString(R.string.msg_error_send_batch_request_api) + "\n" + jsonRequest
                        + "\n\n" + getString(R.string.msg_error_send_batch_response_api) + "\n" + jsonResponse
                        + "\n");


            }
        }
    }
    private class StartScoreEntryTask extends AsyncTask<String, Integer, Long> {
        private ProgressDialog mDialog;
        private long roundId;
        private long holeId;
        private HashMap<String, String> arrLivePlayerId;

        public StartScoreEntryTask() {
            mDialog = null;
            arrLivePlayerId = null;

        }

        public StartScoreEntryTask(ProgressDialog mDialog, HashMap<String, String> arrLivePlayerId) {
            this.mDialog = mDialog;
            this.arrLivePlayerId = arrLivePlayerId;

        }

        @Override
        protected Long doInBackground(String... params) {

            mCourseId = String.valueOf(mDb.createClub(mClubOpj, mTeeSelectId, mCourse));
            TeeCursor c = mDb.getTeeByCourseIdAndTeeOobId(Long.parseLong(mCourseId),
                    mTeeSelectId);
            mTeeId = c.getId();
            c.close();
            for (int i = 2; i <= 4; i++) {
                switch (i) {
                    case 2:
                        if (!getTextPlayer2().equals("")) {
                            long id = mDb.getPlayerIDbyServerID(mPlayerId2);
                            if (id > 0) {
                                mDb.updatePlayer(id, getTextPlayer2());
                                mPlayerIds.add(id);
                            } else {
                                mPlayerIds.add(mDb.insertPlayer(getTextPlayer2(), mPlayerId2));
                            }
                        }
                        break;
                    case 3:
                        if (!getTextPlayer3().equals("")) {
                            long id = mDb.getPlayerIDbyServerID(mPlayerId3);
                            if (id > 0) {
                                mDb.updatePlayer(id, getTextPlayer3());
                                mPlayerIds.add(id);
                            } else {
                                mPlayerIds.add(mDb.insertPlayer(getTextPlayer3(), mPlayerId3));
                            }
                        }
                        break;
                    case 4:
                        if (!getTextPlayer4().equals("")) {
                            long id = mDb.getPlayerIDbyServerID(mPlayerId4);
                            if (id > 0) {
                                mDb.updatePlayer(id, getTextPlayer4());
                                mPlayerIds.add(id);
                            } else {
                                mPlayerIds.add(mDb.insertPlayer(getTextPlayer4(), mPlayerId4));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            String liveEntryId = "", liveId = "";
            LiveInfo liveInfo = (LiveInfo) getIntent().getExtras().getSerializable(Constant.LIVE_INFO);
            if (liveInfo != null) {
                liveEntryId = liveInfo.getEntryId();
                liveId = liveInfo.getId();
            }

            roundId = mDb.createRound(Long.parseLong(mCourseId), mTeeId, mPlayerIds, mHall,
                    mWeather, mPlaydate.getTimeInMillis() , mHoleNum, Long.valueOf(System.currentTimeMillis()), liveEntryId, liveId);

            mDb.updateRoundPlaying(roundId, true);
            mDb.updateRoundDelete(roundId, false);
            SharedPreferences pref = PreferenceManager
                    .getDefaultSharedPreferences(ActivitySelectRound.this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(getString(R.string.pref_score_detail_pause), (mHall.equals(Constant.HOLE_ONE) ? 1 : 10));
            editor.commit();

            HoleCursor hc = mDb.getTeeHoles(mTeeId,
                    (mHall.equals(Constant.HOLE_ONE) ? 1 : 10));
            holeId = hc.getId();
            hc.close();
			/*ThuNA 2013/05/10 ADD-S*/
            // Insert hdcp information to round_player table
            String idServerOwner = Distance.GetProfileItem(ActivitySelectRound.this, getString(R.string.pref_profile_id_server));
            if (null == idServerOwner || idServerOwner.trim().equals("")) {
                PlayerCursor pc = mDb.getOwner();
                if (null != pc && pc.getCount() > 0) {
                    idServerOwner = pc.getServerId();
                }
            }

            String livePlayer1Id = arrLivePlayerId == null ? "" : arrLivePlayerId.get(idServerOwner);
            if (getTextCurPlayerHandicap().length() == 0) {
                mDb.insertRoundPlayer(roundId, mDb.getOwner().getId(), Constant.DEFAULT_HDCP_VALUE, livePlayer1Id, Constant.DEFAULT_GOAL_VALUE);
            } else {
                mDb.insertRoundPlayer(roundId, mDb.getOwner().getId(),
                        parseFromHdcpDisplay(getTextCurPlayerHandicap()), livePlayer1Id, Constant.DEFAULT_GOAL_VALUE);
            }
            if (!getTextPlayer2().equals("")) {
                String livePlayer2Id = arrLivePlayerId == null ? "" : arrLivePlayerId.get(mPlayerId2);
                ;
                long id = mDb.getPlayerIDbyServerID(mPlayerId2);
                if(getPlayer2Handicap().length() == 0) {
                    mDb.insertRoundPlayer(roundId, id, Constant.DEFAULT_HDCP_VALUE, livePlayer2Id, Constant.DEFAULT_GOAL_VALUE);
                } else {
                    mDb.insertRoundPlayer(roundId, id, parseFromHdcpDisplay(getPlayer2Handicap()), livePlayer2Id, Constant.DEFAULT_GOAL_VALUE);
                }
            }
            if (!getTextPlayer3().equals("")) {
                String livePlayer3Id = arrLivePlayerId == null ? "" : arrLivePlayerId.get(mPlayerId3);
                ;
                long id = mDb.getPlayerIDbyServerID(mPlayerId3);
                if (getPlayer3Handicap().length() == 0) {
                    mDb.insertRoundPlayer(roundId, id, Constant.DEFAULT_HDCP_VALUE, livePlayer3Id, Constant.DEFAULT_GOAL_VALUE);
                } else {
                    mDb.insertRoundPlayer(roundId, id, parseFromHdcpDisplay(getPlayer3Handicap()), livePlayer3Id, Constant.DEFAULT_GOAL_VALUE);
                }
            }
            if (!getTextPlayer4().equals("")) {
                String livePlayer4Id = arrLivePlayerId == null ? "" : arrLivePlayerId.get(mPlayerId4);
                long id = mDb.getPlayerIDbyServerID(mPlayerId4);
                if (getPlayer4Handicap().length() == 0) {
                    mDb.insertRoundPlayer(roundId, id, Constant.DEFAULT_HDCP_VALUE, livePlayer4Id, Constant.DEFAULT_GOAL_VALUE);
                } else {
                    mDb.insertRoundPlayer(roundId, id, parseFromHdcpDisplay(getPlayer4Handicap()), livePlayer4Id, Constant.DEFAULT_GOAL_VALUE);
                }
            }
            mixpanel.track(Constant.Gtrack.PAGE_SCORE_ENTRY, null);

            return Long.parseLong(mCourseId);

        }

        @Override
        protected void onPreExecute() {
            if (null == mDialog) {
                mDialog = new ProgressDialog(ActivitySelectRound.this);
                mDialog.setIndeterminate(true);
                mDialog.setMessage(getString(R.string.msg_now_loading));

                if (!isFinishing()) {
                    mDialog.show();
                }
            }

        }

        @Override
        protected void onPostExecute(Long courseId) {

            try {
                SharedPreferences pref = PreferenceManager
                        .getDefaultSharedPreferences(ActivitySelectRound.this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong(getString(R.string.pref_hole_num), mHoleNum);
                editor.putInt(getString(R.string.pref_hole_start),
                        (mHall.equals(Constant.HOLE_ONE) ? 1 : 10));
                editor.commit();


//                Intent intent = new Intent(ActivitySelectRound.this,ActivityScoreInput.class);
//
//                intent.putExtra(Constant.PLAYING_COURSE_ID, Long.parseLong(mCourseId));
//                intent.putExtra(Constant.PLAYING_TEE_ID, mTeeId);
//                intent.putExtra(Constant.PLAYING_ROUND_ID, roundId);
//                intent.putExtra(Constant.PLAYING_HOLE_ID, holeId);
//                intent.putExtra(Constant.EXT_TYPE_YOURGOLF, mClubOpj.getExtType());
//                intent.putExtra(Constant.ROUND_ID, mClubOpj.getExtId());
//
//
//                String requestStr = getIntent().getExtras().getString(Constant.REQUEST_GOLF_FROM_LIVE);
//                if (requestStr != null && requestStr.equals(Constant.REQUEST_GOLF_FROM_LIVE)) {
//                    intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, requestStr);
//                }
//
//                long[] playerIds = new long[mPlayerIds.size()];
//                for (int i = 0; i < mPlayerIds.size(); i++) {
//                    playerIds[i] = (long) mPlayerIds.get(i);
//                }
//
//                intent.putExtra(Constant.PLAYER_IDS, playerIds);
//
//                Distance.SetPreferHistory(ActivitySelectRound.this, false);
//                startActivity(intent);
            } catch (Exception e) {
                YgoLog.e("TASK", "StartScoreEntryTask done,  Error: " + e.getMessage());
            }

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
            isClick = false;
            setResult(RESULT_OK);
            finish();
        }
    }
    private String getPlayer2Handicap(){
        return tvPlayer2Handicap.getText().toString();
    }
    private String getPlayer3Handicap(){
        return tvPlayer3Handicap.getText().toString();
    }
    private String getPlayer4Handicap(){
        return tvPlayer4Handicap.getText().toString();
    }

    private void finishEditHandicap(int resultCode) {
        if (resultCode == RESULT_OK) {
            if (selectedIndex1 != -1 && selectedIndex2 != -1 && selectedId != -1) {
                String selectedHandicap = list1ShownDhcp[selectedIndex1].toString();
                if (!selectedHandicap.equals("-")) {
                    selectedHandicap += list2ShownDhcp[selectedIndex2];
                }
                String playerId = null;
                switch (selectedId) {
                    case 1:
                        if (!selectedHandicap.equals("-")) {
                            ((TextView) findViewById(R.id.tvCurPlayerHandicap)).setText(selectedHandicap);
                            ((TextView) findViewById(R.id.tvCurPlayerHandicap)).setVisibility(View.VISIBLE);
                        } else {
                            ((TextView) findViewById(R.id.tvCurPlayerHandicap)).setVisibility(View.INVISIBLE);
                            ((TextView) findViewById(R.id.tvCurPlayerHandicap)).setText(defaultHDCP);
                        }
//                        findViewById(R.id.curHandicap1Layout).setBackgroundResource(R.drawable.white_shape_no_droder_bg);

                        playerId = Distance.GetProfileItem(ActivitySelectRound.this, getString(R.string.pref_profile_id_server));
                        if (null == playerId || playerId.trim().equals("")) {
                            PlayerCursor pc = mDb.getOwner();
                            if (null != pc && pc.getCount() > 0) {
                                playerId = pc.getServerId();
                            }
                        }
                        break;
                    case 2:
                        if (!selectedHandicap.equals("-")) {
                            tvPlayer2Handicap.setText(selectedHandicap);
                            tvPlayer2Handicap.setVisibility(View.VISIBLE);
                        } else {

                            tvPlayer2Handicap.setText(defaultHDCP);
                        }
                        playerId = mPlayerId2;
                        break;
                    case 3:
                        if (!selectedHandicap.equals("-")) {
                            tvPlayer3Handicap.setText(selectedHandicap);
                            tvPlayer3Handicap.setVisibility(View.VISIBLE);
                        } else {
                            tvPlayer3Handicap.setText(defaultHDCP);
                        }
                        playerId = mPlayerId3;
                        break;
                    case 4:
                        if (!selectedHandicap.equals("-")) {
                            tvPlayer4Handicap.setText(selectedHandicap);
                        } else {
                            tvPlayer4Handicap.setText(defaultHDCP);
                        }
                        playerId = mPlayerId4;
                        break;
                    default:
                        break;
                }
                if (playerId != null) {
                    // Update database for HDCP
                    mDb.updatePlayer(playerId, parseFromHdcpDisplay(selectedHandicap));
                    // Update to server
                    new UpdateHandicapToServer(this, playerId, parseFromHdcpDisplay(selectedHandicap)).execute();
                }
            }
        }
        // Reset value
        selectedId = -1;
        selectedIndex1 = -1;
        selectedIndex2 = -1;

        // Hide animation
        strokeHandicapLayout.setAnimation(outToBottomAnimation());
        strokeHandicapLayout.setVisibility(View.GONE);

        if (Distance.isSmallDevice(this) && selectedView != null) {
            selectedView.requestFocus();
        }
    }

    private class BatchException extends RuntimeException {
        public BatchException(String message) {

            super(message);

        }
    }
    private Animation outToTopAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        outtoRight.setDuration(400);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
    private Animation outToBottomAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        outtoRight.setDuration(400);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
    private class UpdateHandicapToServer extends AsyncTask<Integer, Void, Constant.UPLOAD_STATUS_CODE> {
        private ProgressDialog mDialog;
        String playerId;
        float hdcp;

        public UpdateHandicapToServer(Context context, String playerId, float hdcp) {
            this.playerId = playerId;
            this.hdcp = hdcp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(ActivitySelectRound.this);
            mDialog.setIndeterminate(true);

            mDialog.setMessage(ActivitySelectRound.this
                    .getString(R.string.msg_now_loading));
            if (!isFinishing())
                mDialog.show();
        }

        @Override
        protected Constant.UPLOAD_STATUS_CODE doInBackground(Integer... params) {

//            PutStrokeHandicapAPI handicapApi = new PutStrokeHandicapAPI(ActivitySelectRound.this, Constant.URL_SELECT_PLAYER_HANDICAP);
//            Constant.UPLOAD_STATUS_CODE status = handicapApi.updateHandicap(playerId, hdcp);
//
//            if (status == Constant.UPLOAD_STATUS_CODE.YOURGOLF_SUCCESS) {
//                YgoLog.d("TASK", "update to server done");
//            }
//
//            return status;

            return Constant.UPLOAD_STATUS_CODE.YOURGOLF_SUCCESS;
        }

        @Override
        protected void onPostExecute(Constant.UPLOAD_STATUS_CODE status) {
            super.onPostExecute(status);

            if (status == Constant.UPLOAD_STATUS_CODE.YOURGOLF_SUCCESS) {
                mDialog.dismiss();

            } else {
                mDialog.dismiss();
                if (status == Constant.UPLOAD_STATUS_CODE.YOURGOLF_INVALID_TOKEN) {
                    notifyMessage(R.string.yourgolf_account_auth_token_key);
                } else if (status == Constant.UPLOAD_STATUS_CODE.YOURGOLF_ID_NOTFOUND) {
                    notifyMessage(R.string.yourgolf_round_id_not_found);
                } else if (status == Constant.UPLOAD_STATUS_CODE.YOURGOLF_DONT_PERMISSION) {
                    notifyMessage(R.string.yourgolf_round_id_dont_permission);
                } else if (status == Constant.UPLOAD_STATUS_CODE.YOURGOLF_ERROR) {
                    notifyMessage(R.string.status_send_error);
                } else {
                    notifyMessage(R.string.network_erro_or_not_connet);
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == REQUEST_CODE_HANDICAP) {
            if (resultCode == RESULT_OK && data != null) {
                // Get handicap for current player
                String curHdcp = data.getStringExtra(Constant.CUR_PLAYER_HADICAP);
                YgoLog.i("TASK", "onActivityResult: hdcp=" + curHdcp);
                if (curHdcp != null && curHdcp.length() > 0 && !curHdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                    ((TextView) findViewById(R.id.tvCurPlayerHandicap)).setText(parseToHdcpDisplay(curHdcp));
                } else {
                    ((TextView) findViewById(R.id.tvCurPlayerHandicap)).setText(defaultHDCP);
                }
                // Get handicap for player 2
                String player2Hdcp = data.getStringExtra(Constant.PLAYER2_HADICAP);
                mPlayerHadicap2 = player2Hdcp;
                YgoLog.i("TASK", "onActivityResult 22222++++" + player2Hdcp);
                if (player2Hdcp != null && player2Hdcp.length() > 0 && !player2Hdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                    tvPlayer2Handicap.setText(parseToHdcpDisplay(player2Hdcp));
                } else {
                    tvPlayer2Handicap.setText(defaultHDCP);
                }
                // Get handicap for player 3
                String player3Hdcp = data.getStringExtra(Constant.PLAYER3_HADICAP);
                mPlayerHadicap3 = player3Hdcp;
                if (player3Hdcp != null && player3Hdcp.length() > 0 && !player3Hdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                    tvPlayer3Handicap.setText(parseToHdcpDisplay(player3Hdcp));
                } else {
                    tvPlayer3Handicap.setText(defaultHDCP);
                }
                // Get handicap for player 4
                String player4Hdcp = data.getStringExtra(Constant.PLAYER4_HADICAP);
                mPlayerHadicap4 = player4Hdcp;
                if (player4Hdcp != null && player4Hdcp.length() > 0 && !player4Hdcp.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                    tvPlayer4Handicap.setText(parseToHdcpDisplay(player4Hdcp));
                } else {
                    tvPlayer4Handicap.setText(defaultHDCP);
                 }
            }
        } else {
            if (resultCode == RESULT_OK && null != data) {
                String name = data.getStringExtra(Constant.PLAYER_NAME);
                String id = data.getStringExtra(Constant.PLAYER_ID);
				/*ThuNA 2013/05/09 ADD-S*/
                String playerHadicap = data.getStringExtra(Constant.PLAYER_HADICAP);
				/*ThuNA 2013/05/09 ADD-E*/
                Boolean maxFlg = false;
                if (name.length() > MAX_LENGTH) {
                    name = name.substring(0, MAX_LENGTH);
                    maxFlg = true;
                }

                if (maxFlg) {
                    Toast.makeText(ActivitySelectRound.this,
                            r.getString(R.string.max_length_warning),
                            Toast.LENGTH_LONG).show();
                }

                if (FLAG_PLAYER_POSITION == 2) {
                    tvPlayer2.setText(name);
                    tvPlayer2.setVisibility(View.VISIBLE);
                    btnClearPlayer2.setVisibility(View.VISIBLE);
                    mPlayerId2 = id;
                    mName2 = name;
                    mPlayerHadicap2 = playerHadicap;

                    if (!isPuma) {
                        findViewById(R.id.player2Layout).setNextFocusRightId(R.id.clear_player2);
                        findViewById(R.id.handicap2Layout).setNextFocusLeftId(R.id.clear_player2);
                    }
					/*ThuNA 2013/05/09 ADD-S*/
                    // Add the hdcp to player
                    if (playerHadicap != null && playerHadicap.length() > 0) {
                        if (!playerHadicap.equals("null") && !playerHadicap.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                            tvPlayer2Handicap.setText(parseToHdcpDisplay(playerHadicap));
                        } else {
                            tvPlayer2Handicap.setText(defaultHDCP);
                        }

                    } else {
                        tvPlayer2Handicap.setText(defaultHDCP);
                    }
					/*ThuNA 2013/05/09 ADD-E*/
                } else if (FLAG_PLAYER_POSITION == 3) {
                    tvPlayer3.setText(name);
                    tvPlayer3.setVisibility(View.VISIBLE);
                    btnClearPlayer3.setVisibility(View.VISIBLE);
                    mPlayerId3 = id;
                    mName3 = name;
                    mPlayerHadicap3 = playerHadicap;

                    if (!isPuma) {
                        findViewById(R.id.player3Layout).setNextFocusRightId(R.id.clear_player3);
                        findViewById(R.id.handicap3Layout).setNextFocusLeftId(R.id.clear_player3);
                    }
					/*ThuNA 2013/05/09 ADD-S*/
                    // Add the hdcp to player
                    if (playerHadicap != null && playerHadicap.length() > 0) {
                        if (!playerHadicap.equals("null") && !playerHadicap.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                            tvPlayer3Handicap.setText(parseToHdcpDisplay(playerHadicap));
                        } else {
                            tvPlayer3Handicap.setText(defaultHDCP);
                        }

                    } else {
                        tvPlayer3Handicap.setText(defaultHDCP);
                    }
					/*ThuNA 2013/05/09 ADD-E*/
                } else if (FLAG_PLAYER_POSITION == 4) {
                    tvPlayer4.setText(name);
                    tvPlayer4.setVisibility(View.VISIBLE);
                    btnClearPlayer4.setVisibility(View.VISIBLE);
                    mPlayerId4 = id;
                    mName4 = name;
                    mPlayerHadicap4 = playerHadicap;

                    if (!isPuma) {
                        findViewById(R.id.player4Layout).setNextFocusRightId(R.id.clear_player4);
                        findViewById(R.id.handicap4Layout).setNextFocusLeftId(R.id.clear_player4);
                    }
					/*ThuNA 2013/05/09 ADD-S*/
                    // Add the hdcp to player
                    if (playerHadicap != null && playerHadicap.length() > 0) {
                        if (!playerHadicap.equals("null") && !playerHadicap.equals(String.valueOf(Constant.DEFAULT_HDCP_VALUE))) {
                            tvPlayer4Handicap.setText(parseToHdcpDisplay(playerHadicap));
                        } else {
                            tvPlayer4Handicap.setText(defaultHDCP);
                        }

                    } else {
                        tvPlayer4Handicap.setText(defaultHDCP);
                    }
					/*ThuNA 2013/05/09 ADD-E*/
                }
            }
        }
    }
 /*GROUP 4*/

    private void initGroup4Setting(){

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        PrefsFragment mPrefsFragment = new PrefsFragment();

        mFragmentTransaction.replace(R.id.lst_list, mPrefsFragment);
        mFragmentTransaction.commit();
        getPreferenceInit();

        YgoLog.d("RUN_ENBAL","RUN_ENBAL *****");
    }
    private static void scrollTop(){
        scrollview.requestFocus(View.FOCUS_UP);
        scrollview.scrollTo(0,0);
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.setting_new_round);

             club = (Preference) findPreference(getResources().getString(R.string.key_owner_club));
             measure = (CheckBoxPreference)findPreference(getResources().getString(R.string.setting_category_app_setting_measurement_unit_key));
            userPoint = (CheckBoxPreference)findPreference(getResources().getString(R.string.setting_category_app_setting_use_game_point_key));
            stableFord = (CheckBoxPreference)findPreference(getResources().getString(R.string.key_owner_stableford));
            enterCompetitor = (CheckBoxPreference)findPreference(getResources().getString(R.string.input_putt_for_friend_key));
            setChecked(measure,false);
            club.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent = new Intent(m_context, ClubSettingAct.class);
//                    intent.putExtra(getResources().getString(R.string.key_owner_club), true);
//                    startActivity(intent);
                    return false;
                }
            });
            measure.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CheckBoxPreference checkPref = (CheckBoxPreference) preference;

//                    SharedPreferences pref = GolfApplication.getPreferences();
//
//                    SharedPreferences.Editor editor = pref.edit();
//
//                    if (checkPref.isChecked()) {
//                        editor.putString(getString(R.string.key_owner_measure_unit), "yard");
//                        checkPref.setChecked(false);
//                    } else {
//                        editor.putString(getString(R.string.key_owner_measure_unit), "meter");
//                        checkPref.setChecked(true);
//                    }
//                    editor.commit();
                    return false;
                }
            });

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            ListView lv = (ListView) getView().findViewById(android.R.id.list);
            lv.setFocusable(false);
            lv.setScrollbarFadingEnabled(false);
            lv.setVerticalScrollBarEnabled(false);
            scrollTop();
            YgoLog.d("RUN_ENBAL","RUN_ENBAL");

        }

        private void setChecked(CheckBoxPreference p, boolean defaultFlg) {
//            SharedPreferences pref = GolfApplication.getPreferences();
//            p.setChecked(pref.getBoolean(p.getKey(), defaultFlg));
//            if (p.getKey().equals(getString(R.string.setting_category_app_setting_measurement_unit_key))) {
//                String measureValue = pref.getString(getString(R.string.key_owner_measure_unit), "yard");
//                p.setChecked((measureValue.equals("meter")) ? true : false);
//            }
        }
    }

    private  void getPreferenceInit(){
       SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
       isEnter = pref.getBoolean(getResources().getString(R.string.input_putt_for_friend_key),false);
       isUsePoint = pref.getBoolean(getResources().getString(R.string.setting_category_app_setting_use_game_point_key),false);
       isMeasure = pref.getBoolean(getResources().getString(R.string.setting_category_app_setting_measurement_unit_key), false);
       isStableMode = pref.getBoolean(getResources().getString(R.string.key_owner_stableford),false);
         measureValue = pref.getString(getString(R.string.key_owner_measure_unit), "yard");
   }
    private void setBackPreference(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.key_owner_measure_unit),measureValue);
        editor.putBoolean(getString(R.string.input_putt_for_friend_key), isEnter);
        editor.putBoolean(getString(R.string.setting_category_app_setting_use_game_point_key), isUsePoint);
        editor.putBoolean(getString(R.string.key_owner_stableford), isStableMode);
        editor.putBoolean(getString(R.string.setting_category_app_setting_measurement_unit_key),isMeasure);
        if(CommonResources.lstCubNewRound != null && CommonResources.lstCubNewRound.size()>0){
            for(ObjectKeyPre ob: CommonResources.lstCubNewRound){
                editor.putBoolean(ob.getKey(), Boolean.parseBoolean(ob.getmValue()));
            }
        }
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isFromAgency) {
            setBackPreference();
            if (strokeHandicapLayout.getVisibility() != View.VISIBLE) {
                finish();
            } else {
                finishEditHandicap(RESULT_CANCELED);
            }
        }
        finish();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case GPS_SETTING:
                return new AlertDialog.Builder(ActivitySelectRound.this).setIcon(
                        R.drawable.ic_dialog_menu_generic).setTitle(
                        R.string.check_gps_title).setMessage(
                        R.string.check_gps_message).setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                Intent gpsIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                gpsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(gpsIntent);
                            }
                        }
                ).setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        }
                ).create();
        }
        return null;
    }
}
