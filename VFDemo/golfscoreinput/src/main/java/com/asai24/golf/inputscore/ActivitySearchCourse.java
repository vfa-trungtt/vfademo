package com.asai24.golf.inputscore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asai24.golf.Constant;
import com.asai24.golf.Constant.ErrorServer;

import com.asai24.golf.GolfApplication;

import com.asai24.golf.common.Distance;
import com.asai24.golf.db.Golf.Course.ExtType;
import com.asai24.golf.db.GolfDatabase;
import com.asai24.golf.domain.ClubDownloadResult;
import com.asai24.golf.domain.ClubObj;
import com.asai24.golf.domain.Course;
import com.asai24.golf.domain.Hole;
import com.asai24.golf.domain.Tee;
import com.asai24.golf.object.GuestUser;
import com.asai24.golf.utils.CleanUpUtil;
import com.asai24.golf.utils.ContextUtil;
import com.asai24.golf.utils.YgoLog;
import com.asai24.golf.view.BalloonItemizedOverlay;
import com.asai24.golf.view.OnSingleTapListener;
import com.asai24.golf.view.SimpleItemizedOverlay;
import com.asai24.golf.view.TapControlledMapView;
import com.asai24.golf.web.ClubAPI;
import com.asai24.golf.web.CourseSearchJsonAPI;
import com.asai24.golf.web.CourseSearchResult;
import com.asai24.golf.web.DeleteHistoryCLubAPI;
import com.asai24.golf.web.HistoryClubAPI;
import com.asai24.golf.web.HistoryCourseAPI;
import com.crashlytics.android.Crashlytics;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActivitySearchCourse extends GolfActivity implements OnClickListener, OnKeyListener {

    private static final String TAG = ActivitySearchCourse.class.getSimpleName();

    private static final String DATA_FOOTER_MORE    = "MORE_";
    private static final String DATA_FOOTER_REQUEST = "REQUEST";

    private boolean mIsEditHistory = false;

    private static final int NEW_SEARCH = 0;
    private static final int NEW_SEARCH_NEARBY = 1;
    private static final int LOAD_MORE = 2;

    private static final int FLAG_TO_LOGIN = 56;
    private static final int FLAG_TO_NEW_LIVE = 57;

    private static final String SEARCH_NEARBY_RADIUS = "30";

    private GolfDatabase mDb;
    private Resources r;

    private EditText mKeywordEditText;
    private Button mBtnHistory;
    private Button mBtnSearch;
    private Button mBtnSearchKey;
    private Button mBtnNearby;
    private Button mBtnEdit;

    private ListView mSearchResultListViewHistory;
    private ListView mSearchResultListViewNeighbor;
    private TapControlledMapView mSearchResultMapViewNeighbor;
    private List<Overlay> mapOverlays;
    private Drawable drawable;
    private SimpleItemizedOverlay itemizedOverlay;
    private ListView mSearchResultListViewNew;
    private View mFooterView;
    private View mFooterNeighView;
    private View mFooterHistoryView;
    private View mFooterMoreHistoryView;
    //NamLH 2012.05.10
    private View mNoResultView;
    private View mNoResultNeighView;
    // CongVC 2012-04-16
    private View mRequestMailView;
    private View mRequestNeighMailView;

    private ClubAdapter mClubNeighAdapter;
    private ClubAdapter mClubAdapter;
    private ClubAdapter mClubHistoryAdapter;

    // コース検索getパラメータ
    private HashMap<String, String> mParams;
    private CourseSearchResult courseSearchResult;

    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private Location mLocation;
    private Timer locationTimer;
    private long time;
    private boolean mLocSearchFlg;
    private ProgressDialog mProgressDialog;

    //	private Course mSelectedCourse;
    private ClubObj mSelectedClub;
    private static ClubObj mSelectedClubForLive;
    private int mCourseMode;
    private boolean mFinishFlg;
    private int mSearchTapType = 0; //0 history,1 neighbor,2 new search
    private HistoryClubAPI mClubAPI;
    private List<TextView> mLstBtnEdit = new ArrayList<TextView>();
    private Context mContext;
    private Button mBtnMore;
    private Button mBtnNeighMore;
    private Button mBtnHistoryMore;

    private int mIdtemSelect = 0;
    private int mSelectionHistory = 0;

    private boolean otherAppFlg;
    private boolean flagSearchNavi = false;

    private final static int ZOOM_LEVEL = 16;
    private GeoPoint trackCenterGeoPoint;
    private int mCurrentZoomLevel = 16;

    private MyCLocationOverlay myOverlay;

    private int mKeyWordCurrentPage = 0;
    private int mMapCurrentPage = 0;
    private int mHistoryCurrentPage = 0;

    /**
     * On createyourgolf_keyword
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        YgoLog.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        mSelectedClubForLive = null;
        String requestFromLive = getIntent().getStringExtra(Constant.REQUEST_GOLF_FROM_LIVE);

        //Go to top page when user have already been playing
        if (mDb == null)
            mDb = GolfDatabase.getInstance(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.golf_search);
        mCourseMode = Constant.HISTORY_COURSE;

        final Bundle extras = getIntent().getExtras();
        otherAppFlg = (extras != null && extras.getString(Constant.APP_NAME) != null);
        mContext = ActivitySearchCourse.this;

        mDb = GolfDatabase.getInstance(this);
        r = getResources();
        mFinishFlg = false;

        mClubAPI = new HistoryClubAPI();

        mBtnHistory = (Button) findViewById(R.id.btn_history);
        mBtnNearby = (Button) findViewById(R.id.btn_nearby);
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mBtnSearchKey = (Button) findViewById(R.id.yourgolf_search);
        mBtnSearchKey.setOnClickListener(this);
        mBtnEdit = (Button) findViewById(R.id.top_edit);
        mBtnHistory.setOnClickListener(this);
        mBtnNearby.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
        mBtnEdit.setOnClickListener(this);
        ((Button) findViewById(R.id.top_back_btn)).setOnClickListener(this);


        //ThoLH - set title and button history background - 2012-06-22
        Distance.SetHeader(this, true, true, getString(R.string.search_title));

        if (requestFromLive != null && requestFromLive.equals(Constant.REQUEST_GOLF_FROM_LIVE)) {
            Distance.SetHeaderTitle(this, getString(R.string.live_golfsearch_title));
            findViewById(R.id.step_bar_layout).setVisibility(View.GONE);
            findViewById(R.id.imageLogo).setBackgroundResource(R.drawable.ygo_live_banner);
        }

        mSearchResultListViewHistory = (ListView) findViewById(R.id.yourgolf_courses_history);
        mSearchResultListViewNeighbor = (ListView) findViewById(R.id.yourgolf_courses_neighbor_list);
//        mSearchResultMapViewNeighbor = (TapControlledMapView) findViewById(R.id.yourgolf_courses_neighbor_map);
        // dismiss balloon upon single tap of MapView (iOS behavior)
        mSearchResultMapViewNeighbor.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public boolean onSingleTap(MotionEvent e) {
                itemizedOverlay.hideAllBalloons();
                return true;
            }
        });
        mapOverlays = mSearchResultMapViewNeighbor.getOverlays();
        // Overlay
        drawable = getResources().getDrawable(R.drawable.a_map_pin);
        itemizedOverlay = new SimpleItemizedOverlay(this, drawable, mSearchResultMapViewNeighbor);
        itemizedOverlay.setShowClose(false);
        itemizedOverlay.setShowDisclosure(true);
        itemizedOverlay.setSnapToCenter(false);

        // CanNC-2014/04/21
        Drawable myLocationDrawble = getResources().getDrawable(R.drawable.mylocation);
        myOverlay = new MyCLocationOverlay(this, myLocationDrawble, mSearchResultMapViewNeighbor);
        myOverlay.setShowClose(false);
        myOverlay.setShowDisclosure(true);
        myOverlay.setSnapToCenter(false);

        mSearchResultListViewNew = (ListView) findViewById(R.id.yourgolf_courses_search_new);
        //NAMLH 2012.08.05 hide keyboard
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mClubAdapter = new ClubAdapter(this, new ArrayList<ClubObj>());
		mClubNeighAdapter=new ClubAdapter(this, new ArrayList<ClubObj>());
        mClubHistoryAdapter = new ClubAdapter(this, new ArrayList<ClubObj>());
        initFooterView();

        if (otherAppFlg) {
            selectSearchTab();

            mCourseMode = Constant.OOBGOLF_COURSE;
            mSearchTapType = 2;
        }

        //mFooterView.setVisibility(View.VISIBLE);
        // コンテント表示設定
        mKeywordEditText = (EditText) findViewById(R.id.yourgolf_keyword);
        mKeywordEditText.setOnClickListener(this);
        mKeywordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    selectSearchTab();

                    //mIsEditHistory = false;
                    mSearchTapType = 2;
                    mCourseMode = Constant.OOBGOLF_COURSE;
                    GolfSearch();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        if (otherAppFlg) {
          //  getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final int mode = extras.getInt(Constant.MODE);
            if (mode == 0) {
                mKeywordEditText.setText(extras.getString(Constant.CLUB_NAME));

            } else {
                final Course course = new Course();
                course.setYourGolfId(String.valueOf(extras.getLong("yourgolfId")));
                course.setClubName(extras.getString("clubName"));
                course.setCourseName(extras.getString("courseName"));
                course.setAddress(extras.getString("address"));
                course.setCountry(extras.getString("country"));
                course.setUrl(extras.getString("url"));
                course.setPhoneNumber(extras.getString("phoneNumber"));

//				mKeywordEditText.setText(course.getName());
                mKeywordEditText.setText(course.getClubName());
            }
            if (!Distance.checkLoginYourgolfAccount(this)) {
                otherAppFlg = false;
//                Intent intent = new Intent(ActivitySearchCourse.this, GolfLoginAct.class);
//                intent.putExtra("BackSearchNavi", true);
//                startActivityForResult(intent, FLAG_TO_LOGIN);

            } else {
                otherAppFlg = false;
                GolfSearch();
                setLayout();
            }
        } else {
           // getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        if (!GolfApplication.isPuma()) {
            mKeywordEditText.requestFocus();
        }
        openSearchTab();
        LinearLayout linearLayout = (LinearLayout)mNoResultView.findViewById(R.id.ln_text_result);
        if(Locale.getDefault().getLanguage().toString().equals("ja")){

            linearLayout.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.GONE);
        }
        if(GuestUser.isUserGuest()) {
            showDialogGuestUser();
        }
    }
    private void showDialogGuestUser(){
//        new DialogCustomRequestSignInSearchCourse(this, new DialogCustomRequestSignInSearchCourse.ListenerRequestDialogRound() {
//            @Override
//            public void signInRequest() {
//                Intent intent = DialogCustomRequestSignInSearchCourse.createNewIntentStartLoginScreen(ActivitySearchCourse.this);
//                startActivity(intent);
//            }
//        }).showDialog();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                // Check Permissions Granted or not
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapTab();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    /**
     * Init footer view
     */
    private void initFooterView() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterView = inflater.inflate(R.layout.more_result_item, mSearchResultListViewNew, false);
        mBtnMore = (Button) mFooterView.findViewById(R.id.btnMoreResult);

        mRequestMailView = inflater.inflate(R.layout.mail_request_search, mSearchResultListViewNew, false);
        ((Button) mRequestMailView.findViewById(R.id.btnRequestMail)).setOnClickListener(this);

        mNoResultView = inflater.inflate(R.layout.no_result_search_course, mSearchResultListViewNew, false);
        TextView noresult = (TextView) mNoResultView.findViewById(R.id.tv_input_key);
        noresult.setTextColor(r.getColor(R.color.black));
        noresult.setTypeface(null, Typeface.BOLD);
        noresult.setText(getResources().getString(R.string.search_input_keywords));
        noresult.setGravity(Gravity.CENTER);
        noresult.setTextSize(18);


        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterNeighView = inflater2.inflate(R.layout.more_result_item, mSearchResultListViewNeighbor, false);
        mBtnNeighMore = (Button) mFooterNeighView.findViewById(R.id.btnMoreResult);

        mRequestNeighMailView = inflater.inflate(R.layout.mail_request_search, mSearchResultListViewNeighbor, false);
        ((Button) mRequestNeighMailView.findViewById(R.id.btnRequestMail)).setOnClickListener(this);

        mNoResultNeighView = inflater.inflate(android.R.layout.simple_list_item_1, mSearchResultListViewNeighbor, false);

        LayoutInflater inflater3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterHistoryView = inflater3.inflate(android.R.layout.simple_list_item_1, mSearchResultListViewHistory, false);
        TextView noresult3 = (TextView) mFooterHistoryView.findViewById(android.R.id.text1);
        noresult3.setTextColor(r.getColor(R.color.black));
        noresult3.setText(getResources().getString(R.string.search_no_result));
        noresult3.setGravity(Gravity.CENTER);

        mFooterMoreHistoryView = inflater3.inflate(R.layout.more_result_item, mSearchResultListViewHistory, false);
        mBtnHistoryMore = (Button) mFooterMoreHistoryView.findViewById(R.id.btnMoreResult);

        mBtnHistoryMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    notifyMessage(R.string.network_erro_or_not_connet);
                } else {
                    int currentPage = mClubAPI.getPage();
                    mClubAPI = new HistoryClubAPI();
                    new LoadClubHistoryTask(currentPage + 1).execute();
                }
            }
        });

    }

    /**
     * add some flag for load footer*
     */
    private static final int HISTORY_NO_RESULT = 1;
    private static final int SEARCH_SHOW_MORE = 2;
    private static final int SEARCH_LAST_PAGE = 3;
    private static final int SEARCH_NO_RESULT = 4;
    private int mFooterType = -1;
    private int mFooterTypeNeighbor = -1;

    private boolean mIsHistoryInitialized = false;

    private void RemoveFooterViewIfExit(ListView listViewResult, View footerView, View requestMailView, View noResultView, int type) {
        try {
            switch (mSearchTapType == 1 ? mFooterTypeNeighbor : mFooterType) {
                case HISTORY_NO_RESULT:
                    listViewResult.removeFooterView(noResultView);
                    break;
                case SEARCH_LAST_PAGE:
                    listViewResult.removeFooterView(requestMailView);
                    break;
                case SEARCH_NO_RESULT:
                    listViewResult.removeFooterView(noResultView);
                    listViewResult.removeFooterView(requestMailView);
                    break;
                case SEARCH_SHOW_MORE:
                    listViewResult.removeFooterView(footerView);
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    private void SetFooterViewIfNotExit(ListView listViewResult, View footerView, View requestMailView, View noResultView, int type, int currentPage) {
        switch (type) {
            case HISTORY_NO_RESULT:
                listViewResult.addFooterView(noResultView, null, false);
                break;
            case SEARCH_LAST_PAGE:
                listViewResult.addFooterView(requestMailView, DATA_FOOTER_REQUEST, true);
                break;
            case SEARCH_NO_RESULT:
                listViewResult.addFooterView(noResultView, null, false);
                listViewResult.addFooterView(requestMailView, DATA_FOOTER_REQUEST, true);
                break;
            case SEARCH_SHOW_MORE:
                listViewResult.addFooterView(footerView, DATA_FOOTER_MORE + currentPage, true);
                if (listViewResult == mSearchResultListViewNeighbor) {
                    mMapCurrentPage = currentPage;
                } else if (listViewResult == mSearchResultListViewNew) {
                    mKeyWordCurrentPage = currentPage;
                }
            default:
                break;
        }
    }

    /**
     * Load footer in listview
     *
     * @param type
     */
    private void loadListViewFooter(int type, int currentPage) {
        switch (mSearchTapType) {
            case 1:
			    if (mSearchResultListViewNeighbor.getFooterViewsCount() >0){
				    RemoveFooterViewIfExit(mSearchResultListViewNeighbor,mFooterNeighView,mRequestNeighMailView,mNoResultNeighView,type);
			    }
			    SetFooterViewIfNotExit(mSearchResultListViewNeighbor,mFooterNeighView,mRequestNeighMailView,mNoResultNeighView,type, currentPage);
			    mSearchResultListViewNeighbor.setFooterDividersEnabled(false);
                break;
            case 2:
                if (mSearchResultListViewNew.getFooterViewsCount() > 0) {
                    RemoveFooterViewIfExit(mSearchResultListViewNew, mFooterView, mRequestMailView, mNoResultView, type);
                }
                SetFooterViewIfNotExit(mSearchResultListViewNew, mFooterView, mRequestMailView, mNoResultView, type, currentPage);
                mSearchResultListViewNew.setFooterDividersEnabled(false);
                break;
        }
        if (mSearchTapType == 1)
            mFooterTypeNeighbor = type;
        else
            mFooterType = type;
    }

    private void loadHistory() {

        mClubAPI = new HistoryClubAPI();
        new LoadClubHistoryTask().execute();

    }


    protected class LoadClubHistoryTask extends
            AsyncTask<String, Object, ErrorServer> {

        private ArrayList<ClubObj> arrCLubObject = new ArrayList<ClubObj>();
        private ProgressDialog mDialog;
        private int page;

        public LoadClubHistoryTask() {
            this.page = 1;
            mSelectionHistory = 0;
            mClubHistoryAdapter.clear();

            mSearchResultListViewHistory.removeFooterView(mFooterMoreHistoryView);

            mLstBtnEdit.clear();

        }

        public LoadClubHistoryTask(int page) {
            this.page = page;
            mSelectionHistory = mSearchResultListViewHistory.getCount();
        }

        @Override
        protected ErrorServer doInBackground(String... params) {
            // sendLog();
            ErrorServer status = ErrorServer.NONE;
            try {

                String mToken = Distance.getAuthTokenLogin(mContext);

                if (isNetworkAvailable() && !mToken.equals("")) {
                    HashMap<String, Object> mParams = new HashMap<String, Object>();
                    mParams.put(HistoryClubAPI.KEY_PARAMS_AUTH_TOKEN, mToken);
                    mParams.put(Constant.KEY_APP, Constant.KEY_API_APP_VERSION.replace(
                            Constant.KEY_VERSION_NAME, GolfApplication.getVersionName()));
					/*ThuNA 2013/05/27 ADD-S*/
                    // Exclude the oob
                    String requestFromLive = getIntent().getStringExtra(Constant.REQUEST_GOLF_FROM_LIVE);
                    if (requestFromLive != null) {
                        mParams.put(HistoryClubAPI.KEY_ONLY_YOURGOLF, 1); // Search only your golf
                    }
                    mParams.put(HistoryClubAPI.KEY_PARAMS_PAGE, this.page);
                    arrCLubObject = mClubAPI.getSearchResult(mParams);
                    status = mClubAPI.getmResult();

                } else {
                    status = ErrorServer.ERROR_CONNECT_TIMEOUT;
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = ErrorServer.ERROR_GENERAL;
            }
            return status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(ActivitySearchCourse.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(r.getString(R.string.msg_now_loading));
            //if(!isFinishing()) mDialog.show();
            if (!mDialog.isShowing()) mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ErrorServer result) {
            super.onPostExecute(result);

            try {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            } catch (Exception e) {
            }

            if (result == ErrorServer.NONE) {
                if (null != arrCLubObject && arrCLubObject.size() > 0) {
                    for (ClubObj clubObj : arrCLubObject) {
                        mClubHistoryAdapter.add(clubObj);
                    }
                }
                mClubHistoryAdapter.notifyDataSetChanged();

                mSearchResultListViewHistory.removeFooterView(mFooterMoreHistoryView);
                mSearchResultListViewHistory.removeFooterView(mFooterHistoryView);
                if (mClubHistoryAdapter.getCount() == 0) {
                    mSearchResultListViewHistory.addFooterView(mFooterHistoryView);
                } else {
                    int currentPage = mClubAPI.getPage();
                    if (currentPage < mClubAPI.getTotal()) {
                        mSearchResultListViewHistory.addFooterView(mFooterMoreHistoryView, DATA_FOOTER_MORE + currentPage, true);
                        mHistoryCurrentPage = currentPage;
                    }
                }

                mSearchResultListViewHistory.setAdapter(mClubHistoryAdapter);
                mSearchResultListViewHistory.setSelection(mSelectionHistory);
                mSearchResultListViewHistory.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        YgoLog.e(TAG, "tap on item history...");
                        mSelectedClub = (ClubObj) parent.getItemAtPosition(position);
                        if (mSelectedClub.getExtType().equals(Constant.EXT_TYPE_YOURGOLF)) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_e0138), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (mSelectedClub != null && !mIsEditHistory) {
                            if (mSelectedClub.getExtType().equals(ExtType.YourGolf2.toString())) {
                                new GetClubTask(ActivitySearchCourse.this).getAsync(mSelectedClub.getExtId());
                            } else {
                                setSearchHistoty();
                            }
                        }
                    }
                });

                mIsHistoryInitialized = true;
            } else if (result == ErrorServer.ERROR_CONNECT_TIMEOUT
                    || result == ErrorServer.ERROR_SOCKET_TIMEOUT) {
                //|| result == ErrorServer.ERROR_GENERAL){
                mSearchResultListViewHistory.addFooterView(mFooterHistoryView);
                mSearchResultListViewHistory.setAdapter(new ClubAdapter(ActivitySearchCourse.this, new ArrayList<ClubObj>()));
                AlertMessage(getString(R.string.warning), getString(R.string.network_erro_or_not_connet));

                //notifyMessage(R.string.network_erro_or_not_connet);
            }
        }
    }

    /**
     * Set layout and its content
     */
    private void setLayout() {
        mBtnEdit.setVisibility(View.GONE);

        switch (mSearchTapType) {
            case 0:
                mBtnEdit.setVisibility(View.VISIBLE);
                mSearchResultListViewNew.setVisibility(View.GONE);
                mSearchResultListViewHistory.setVisibility(View.VISIBLE);
                mKeywordEditText.requestFocus();
                mSearchResultMapViewNeighbor.setVisibility(View.GONE);
                //CanNC - Support Puma - 2013-12-20
                try {
                    if (GolfApplication.isPuma() == true) {
                        findViewById(R.id.yourgolf_keyword_search_linearlayout).setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.yourgolf_courses_neighbor).setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                }
                hideKeyboard();
                break;
            case 1:
                if(mSearchResultListViewNeighbor.getCount()==0){
                    mSearchResultListViewNeighbor.setAdapter(new ClubAdapter(this, new ArrayList<ClubObj>()));
                }
                mSearchResultListViewNew.setVisibility(View.GONE);
                mSearchResultListViewHistory.setVisibility(View.GONE);
                mSearchResultMapViewNeighbor.setVisibility(View.VISIBLE);
                //CanNC - Support Puma - 2013-12-20
                try {
                    if (GolfApplication.isPuma() == true) {
                        findViewById(R.id.yourgolf_keyword_search_linearlayout).setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.yourgolf_courses_neighbor).setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                }
                hideKeyboard();
                break;
            case 2:
                if (mSearchResultListViewNew.getCount() == 0) {
                    loadListViewFooter(HISTORY_NO_RESULT, 0);
                    mSearchResultListViewNew.setAdapter(new ClubAdapter(this, new ArrayList<ClubObj>()));
                    setLayout();
                }
                mSearchResultListViewNew.setVisibility(View.VISIBLE);
                mSearchResultListViewHistory.setVisibility(View.GONE);
                mSearchResultMapViewNeighbor.setVisibility(View.GONE);

                //CanNC - Support Puma - 2013-12-20
                try {
                    if (GolfApplication.isPuma() == true) {
                        findViewById(R.id.yourgolf_keyword_search_linearlayout).setVisibility(View.VISIBLE);

                    } else {
                        findViewById(R.id.yourgolf_courses_neighbor).setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                }
                showKeyboard();
                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        YgoLog.i(TAG, "onResume flagSearchNavi:" + flagSearchNavi);
        if (flagSearchNavi) {
            flagSearchNavi = false;
            otherAppFlg = false;
            GolfSearch();
        }
        setLayout();
        if (trackCenterGeoPoint != null) {
            mSearchResultMapViewNeighbor.getController().setZoom(mCurrentZoomLevel);
            mSearchResultMapViewNeighbor.getController().setCenter(trackCenterGeoPoint);
        } else {
            mSearchResultMapViewNeighbor.getController().setZoom(ZOOM_LEVEL);
        }
        mSearchResultMapViewNeighbor.setSatellite(false);
        YgoLog.i(TAG, "onResume END");
    }

    @Override
    protected void onPause() {
        stopLocationService();
        hideKeyboard();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        //現在地検索中は処理しない
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

//        switch (v.getId()) {
//            case R.id.yourgolf_search: {
//                if (!mKeywordEditText.getText().toString().equals("")) {
//                    selectSearchTab();
//
//                    //mIsEditHistory = false;
//                    mSearchTapType = 2;
//                    mCourseMode = Constant.OOBGOLF_COURSE;
//                    setLayout();
//                    GolfSearch();
//                    hideKeyboard();
//                }
//                break;
//            }
//            //New layout
//            case R.id.yourgolf_keyword: {
//
//                selectSearchTab();
//
//                mSearchTapType = 2;
//                //mIsEditHistory = false;
//                mCourseMode = Constant.OOBGOLF_COURSE;
//                setLayout();
//                break;
//            }
//            case R.id.top_edit:
//                if (mClubHistoryAdapter.getCount() == 0)
//                    return;
//                mIsEditHistory = !mIsEditHistory;
//                if (mIsEditHistory) {
//                    mBtnEdit.setText(getResources().getString(R.string.search_done_button));
//                    mSearchResultListViewHistory.setItemsCanFocus(true);
//                    //ThoLH - fixed bug 2167 - 2012-06-27
//                    mSearchResultListViewHistory.setOnItemClickListener(null);
//                } else {
//                    mBtnEdit.setText(getResources().getString(R.string.search_edit_button));
//                    mSearchResultListViewHistory.setItemsCanFocus(false);
//                    //ThoLH - fixed bug 2167 - 2012-06-27
//                    mSearchResultListViewHistory.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            mSelectedClub = (ClubObj) parent.getItemAtPosition(position);
//
//                            if (mSelectedClub != null) {
//                                //showDialog(DIALOG_HISTORY_CLICK);
//                                setSearchHistoty();
//
//                            }
//                        }
//                    });
//                }
//
//                for (TextView tv : mLstBtnEdit) {
//                    if (mIsEditHistory)
//                        tv.setVisibility(View.VISIBLE);
//                    else
//                        tv.setVisibility(View.GONE);
//                }
////			loadHistory();
//
//                break;
//            case R.id.btn_history:
//                openHistoryTab();
//
//                break;
//            case R.id.btn_search:
//                openSearchTab();
//                break;
//            case R.id.btn_nearby:
//
//               if(!checkPermissionRequirement(PERMISSION_LOCATION))
//                   onMapTab();
//
//                break;
//            case R.id.top_back_btn:
//                onBackPressed();
//                break;
//            //end new layout
//            // CongVC 2012-04-16 Process send email request search
//            case R.id.btnRequestMail:
//                sendMailRequestSearch();
//                break;
//        }
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    private void onMapTab(){
        selectMapTab();

        //mIsEditHistory = false;
        mSearchTapType = 1;
        mCourseMode = Constant.OOBGOLF_COURSE;
        setLayout();

        if (!isNetworkAvailable()) {
            showDialog(NETWORK_UNAVAILABLE);
        } else if (mLocation == null) {
            if (checkLocationService()) {

                mLocSearchFlg = true;
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage(getString(R.string.msg_now_get_location));
                mProgressDialog.show();
//                    android.util.Log.i("cannc", "vo start location");
                startLocationService();
            }
        } else {
            mSearchResultListViewNeighbor.setAdapter(new ClubAdapter(this, new ArrayList<ClubObj>()));
            GolfSearchActTask task = new GolfSearchActTask();
            task.setLatitude(mLocation.getLatitude());
            task.setLongitude(mLocation.getLongitude());
            task.execute(NEW_SEARCH_NEARBY);
        }
    }

    private void openHistoryTab() {
        if (!mIsHistoryInitialized) {
            loadHistory();
        }
        selectHistoryTab();

        mCourseMode = Constant.HISTORY_COURSE;
        mSearchTapType = 0;
        setLayout();
    }

    private void openSearchTab() {
        selectSearchTab();

        mCourseMode = Constant.OOBGOLF_COURSE;
        mSearchTapType = 2;
        setLayout();
    }

    private void selectSearchTab() {

        mBtnSearch.setBackgroundResource(R.drawable.tab_b_selected_selector);
        mBtnSearch.setTextColor(getResources().getColor(R.color.black));

        mBtnNearby.setBackgroundResource(R.drawable.tab_selected);
        mBtnNearby.setTextColor(getResources().getColor(R.color.white));

        mBtnHistory.setBackgroundResource(R.drawable.tab_selected);
        mBtnHistory.setTextColor(getResources().getColor(R.color.white));
        findViewById(R.id.yourgolf_keyword_search_linearlayout).setVisibility(View.VISIBLE);
        mKeywordEditText.requestFocus();

    }

    private void selectMapTab() {

        mBtnNearby.setBackgroundResource(R.drawable.tab_b_selected_selector);
        mBtnNearby.setTextColor(getResources().getColor(R.color.black));

        mBtnHistory.setBackgroundResource(R.drawable.tab_selected);
        mBtnHistory.setTextColor(getResources().getColor(R.color.white));

        mBtnSearch.setBackgroundResource(R.drawable.tab_selected);
        mBtnSearch.setTextColor(getResources().getColor(R.color.white));
        findViewById(R.id.yourgolf_keyword_search_linearlayout).setVisibility(View.GONE);
        hideKeyboard();

    }

    private void selectHistoryTab() {

        mBtnHistory.setBackgroundResource(R.drawable.tab_b_selected_selector);
        mBtnHistory.setTextColor(getResources().getColor(R.color.black));

        mBtnNearby.setBackgroundResource(R.drawable.tab_selected);
        mBtnNearby.setTextColor(getResources().getColor(R.color.white));

        mBtnSearch.setBackgroundResource(R.drawable.tab_selected);
        mBtnSearch.setTextColor(getResources().getColor(R.color.white));
        findViewById(R.id.yourgolf_keyword_search_linearlayout).setVisibility(View.GONE);
        hideKeyboard();
    }

    private void setSearchHistoty() {
        new DetailHistoryTask(mSelectedClub.getIdServer(), mSelectedClub.getExtType()).execute();

    }

    protected class DetailHistoryTask extends
            AsyncTask<String, Object, ErrorServer> {

        private ProgressDialog mDialog;
        ArrayList<Course> courseResult = new ArrayList<Course>();
        String clubID = "";
        String extType = "";

        public DetailHistoryTask(String clubID, String extType) {
            this.clubID = clubID;
            this.extType = extType;
        }

        @Override
        protected ErrorServer doInBackground(String... params) {

            ErrorServer status = ErrorServer.NONE;
            try {

                String mToken = Distance.getAuthTokenLogin(mContext);

                if (isNetworkAvailable()) {

                    HashMap<String, String> mParams = new HashMap<String, String>();
                    mParams.put(HistoryCourseAPI.KEY_PARAMS_AUTH_TOKEN, mToken);
                    mParams.put(Constant.KEY_APP, Constant.KEY_API_APP_VERSION.replace(
                            Constant.KEY_VERSION_NAME, GolfApplication.getVersionName()));

                    mParams.put(HistoryCourseAPI.KEY_PARAMS_CLUB_ID, clubID);
                    mParams.put(HistoryCourseAPI.KEY_PARAMS_EXT_TYPE, extType);

                    HistoryCourseAPI courseApi = new HistoryCourseAPI();
                    courseResult = courseApi.getSearchResult(mParams);

                    status = courseApi.getmResult();

                }


            } catch (Exception e) {
                e.printStackTrace();
                status = ErrorServer.ERROR_GENERAL;
            }
            return status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(ActivitySearchCourse.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(r.getString(R.string.msg_now_loading));
            if (!isFinishing()) mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ErrorServer result) {
            super.onPostExecute(result);

            mDialog.dismiss();

            if (result == ErrorServer.NONE && null != courseResult
                    && courseResult.size() > 0) {
                mSelectedClub.setCourses(courseResult);
                mSelectedClubForLive = mSelectedClub;

                int flagStart = 0;
                 Intent intent = null;
                String requestFromLive = getIntent().getStringExtra(Constant.REQUEST_GOLF_FROM_LIVE);
                if (requestFromLive != null) {
//                    intent = new Intent(ActivitySearchCourse.this, GolfDetailsAct.class);
//                    flagStart = FLAG_TO_NEW_LIVE;
//                    intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, requestFromLive);
                } else {
					/*ThuNA 2013/05/22 ADD-E*/
                    intent = new Intent(ActivitySearchCourse.this, ActivitySelectRound.class);
                }
                boolean isFromScoreAgencyButton = getIntent().getBooleanExtra(Constant.KEY_FROM_SCORE_AGENCY, false);
                intent.putExtra(Constant.KEY_FROM_SCORE_AGENCY, isFromScoreAgencyButton);
                int webUserType = getIntent().getIntExtra(Constant.KEY_IS_WEB_CHARGE_USER, 0);
                intent.putExtra(Constant.KEY_IS_WEB_CHARGE_USER, webUserType);

                intent.putExtra(r.getString(R.string.intent_club), mSelectedClub);
                intent.putExtra(r.getString(R.string.intent_search_history_mode), true);
                startActivityForResult(intent, flagStart);

            } else if (result == ErrorServer.ERROR_CONNECT_TIMEOUT
                    || result == ErrorServer.ERROR_SOCKET_TIMEOUT
                    || result == ErrorServer.ERROR_GENERAL) {

                notifyMessage(R.string.network_erro_or_not_connet);
            }
        }
    }


    /**
     * Process send mail request search
     */
    private void sendMailRequestSearch() {
        String account, version;
        try {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType(getString(R.string.email_request_search_attached_type));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_request_search_subject));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.mail_support_your_golf)});
            /////////////////////// ticket 7225
            ContextUtil contextUtil = new ContextUtil(ActivitySearchCourse.this);
            intent.putExtra(Intent.EXTRA_TEXT, contextUtil.getContentMail(mKeywordEditText.getText().toString()));
            ///////////////////////////
            /////////////
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") ||
                        info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
            if (best != null)
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GolfSearch() {
        if (mKeywordEditText != null) {
            final String text = mKeywordEditText.getText().toString();
            if (text != null && !text.trim().equals("")) {

                if (!isNetworkAvailable()) {
                    showDialog(NETWORK_UNAVAILABLE);
                } else {
                    GolfSearchActTask task = new GolfSearchActTask();
                    task.execute(NEW_SEARCH);
                }
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class ClubAdapter extends ArrayAdapter<ClubObj> {
        private final LayoutInflater mInflater;

        public ClubAdapter(Context context, List<ClubObj> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            CourseListViewHolder holder;
//			TextView btnEdit ;
            if (view == null) {    //view==nullの場合のみinflateし、holderセットをする。既にある場合は再利用を行う。
                holder = new CourseListViewHolder();
                view = mInflater.inflate(R.layout.search_course_history_item, parent, false);
                holder.courseNameView = (TextView) view.findViewById(R.id.history_course_name);
                holder.courseEtcView = (TextView) view.findViewById(R.id.history_course_downloaded_date);
                holder.courceDeleteButton = (Button) view.findViewById(R.id.btn_delete);
                view.setTag(holder);
            } else {
                holder = (CourseListViewHolder) view.getTag();
            }

            //ListView内のテキストセット
            final ClubObj club = getItem(position);
            final String courseName = club.getClubName();

            String courseEtc = "";
            if (!club.getAddress().toString().equals("")) {
                if (!club.getCountry().toString().equals(""))
                    courseEtc = club.getAddress() + ", " + club.getCountry();
                else
                    courseEtc = club.getAddress();
            } else if (!club.getCountry().toString().equals(""))
                courseEtc = club.getCountry();

            holder.courseNameView.setText(courseName);
            holder.courseEtcView.setText(courseEtc);
            if (!mIsEditHistory || mSearchTapType != 0) {
                holder.courceDeleteButton.setVisibility(View.GONE);
            }
            holder.courceDeleteButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    new DeleteHistoryTask(getItem(position).getIdServer()).execute();
                }
            });
            mLstBtnEdit.add(holder.courceDeleteButton);
            return view;
        }
    }

    static class CourseListViewHolder {
        TextView courseNameView;
        TextView courseEtcView;
        Button courceDeleteButton;
    }

    protected class DeleteHistoryTask extends
            AsyncTask<String, Object, ErrorServer> {

        private ProgressDialog mDialog;
        private boolean blnResult;
        String clubID = "";

        public DeleteHistoryTask(String clubID) {
            this.clubID = clubID;
            blnResult = false;
        }

        @Override
        protected ErrorServer doInBackground(String... params) {

            ErrorServer status = ErrorServer.NONE;
            try {

                String mToken = Distance.getAuthTokenLogin(mContext);

                if (isNetworkAvailable()) {

                    HashMap<String, String> mParams = new HashMap<String, String>();
                    mParams.put(HistoryCourseAPI.KEY_PARAMS_AUTH_TOKEN, mToken);
                    mParams.put(Constant.KEY_APP, Constant.KEY_API_APP_VERSION.replace(
                            Constant.KEY_VERSION_NAME, GolfApplication.getVersionName()));
                    mParams.put(HistoryCourseAPI.KEY_PARAMS_CLUB_ID, clubID);

                    DeleteHistoryCLubAPI deleteApi = new DeleteHistoryCLubAPI();
                    blnResult = deleteApi.getSearchResult(mParams);

                    status = deleteApi.getmResult();
                } else {
                    status = ErrorServer.ERROR_GENERAL;
                }


            } catch (Exception e) {
                e.printStackTrace();
                status = ErrorServer.ERROR_GENERAL;
            }
            return status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(ActivitySearchCourse.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(r.getString(R.string.msg_now_loading));
            if (!isFinishing()) mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ErrorServer result) {
            super.onPostExecute(result);

            mDialog.dismiss();

            if (result == ErrorServer.NONE && blnResult) {
                loadHistory();
            } else if (result == ErrorServer.ERROR_CONNECT_TIMEOUT
                    || result == ErrorServer.ERROR_SOCKET_TIMEOUT
                    || result == ErrorServer.ERROR_GENERAL) {

                notifyMessage(R.string.network_erro_or_not_connet);
            }
        }
    }

    private void AlertMessage(String title, String message) {
        try {
            if (((Activity) this).isFinishing() == false) {
                new AlertDialog.Builder(ActivitySearchCourse.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .show();
            }
        } catch (Exception e) {
        }
    }

    private class GolfSearchActTask extends
            AsyncTask<Integer, Integer, CourseSearchResult> {
        private int searchType;
        private ErrorServer errorServer = ErrorServer.NONE;
        private ProgressDialog mDialog;
        private double latitude;
        private double longitude;

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        @Override
        protected CourseSearchResult doInBackground(Integer... params) {
            searchType = (int) params[0];

            switch (searchType) {
                case NEW_SEARCH:
                    mClubAdapter = new ClubAdapter(mContext, new ArrayList<ClubObj>());
                    mParams = new HashMap<String, String>();
                    mParams.put("keywords", mKeywordEditText.getText().toString().trim());
                    break;
                case NEW_SEARCH_NEARBY:
                    mClubNeighAdapter = new ClubAdapter(mContext, new ArrayList<ClubObj>());
                    mParams = new HashMap<String, String>();
                    //mParams.put("radius", SEARCH_NEARBY_RADIUS);
                    mParams.put("lat", latitude + "");
                    mParams.put("lng", longitude + "");
                    break;
                case LOAD_MORE:
                    if (mSearchTapType == 1) {
                        mParams.put("lat", latitude + "");
                        mParams.put("lng", longitude + "");
                    }
                    int page = (int) params[1];
                    mParams.put("page", "" + page);
                    break;
            }
            clearCourseSearchResult();
//
            mParams.put(CourseSearchJsonAPI.KEY_PARAMS_AUTH_TOKEN, Distance.getAuthTokenLogin(ActivitySearchCourse.this));
            mParams.put(Constant.KEY_APP,
                    Constant.KEY_API_APP_VERSION.replace(Constant.KEY_VERSION_NAME, GolfApplication.getVersionName()));
			/*ThuNA 2013/05/27 ADD-S*/
            // Exclude the oob
            String requestFromLive = getIntent().getStringExtra(Constant.REQUEST_GOLF_FROM_LIVE);
            if (requestFromLive != null) {
                mParams.put(CourseSearchJsonAPI.KEY_ONLY_YOURGOLF, "1");
            }
            CourseSearchJsonAPI searchJsonApi = new CourseSearchJsonAPI();
            courseSearchResult = searchJsonApi.getSearchResult(mParams);
            errorServer = searchJsonApi.getmResult();

            return courseSearchResult;
        }

        @Override
        protected void onPreExecute() {

            mDialog = new ProgressDialog(ActivitySearchCourse.this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(r.getString(R.string.msg_now_loading));
            if (!isFinishing()) mDialog.show();

        }

        @Override
        protected void onPostExecute(CourseSearchResult searchResult) {
            if (mFinishFlg) return;
            final List<ClubObj> clubs = searchResult.getClubs();
            final int currentPage = searchResult.getCurrentPage();

            if (errorServer.equals(ErrorServer.ERROR_SOCKET_TIMEOUT) || errorServer.equals(ErrorServer.ERROR_CONNECT_TIMEOUT)) {
                try {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    this.cancel(true);
                    if (((Activity) ActivitySearchCourse.this).isFinishing() == false) {
                        AlertMessage(getString(R.string.warning), getString(R.string.network_erro_or_not_connet));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return;
            } else {
                switch (mSearchTapType) {
                    case 1:
                        if (searchType == NEW_SEARCH || searchType == NEW_SEARCH_NEARBY) {
						    mClubNeighAdapter.clear();
                            itemizedOverlay.clearClubs();
                        }
                        for (ClubObj club : clubs) {
                            mClubNeighAdapter.add(club);
                            itemizedOverlay.addClubObj(club);
                        }
                        YgoLog.i(TAG, "mSearchTapType = 1 and searchType = " + searchType);
                        mSearchResultMapViewNeighbor.getController().animateTo(new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6)));
                        updateMapViewOverlay();
                        SetListViewResult(mSearchResultListViewNeighbor, mClubNeighAdapter,clubs.size(),currentPage);
                        break;
                    case 2:
                        if (searchType == NEW_SEARCH || searchType == NEW_SEARCH_NEARBY) {
                            mClubAdapter.clear();
                        }
                        for (ClubObj club : clubs) {
                            mClubAdapter.add(club);
                        }
                        SetListViewResult(mSearchResultListViewNew, mClubAdapter, clubs.size(), currentPage);
                        TextView noresult = (TextView) mNoResultView.findViewById(R.id.tv_input_key);
                        noresult.setText(getResources().getString(R.string.search_no_result));
                        noresult.setTypeface(null, Typeface.NORMAL);
                        LinearLayout layout = (LinearLayout)mNoResultView.findViewById(R.id.ln_text_result);
                        layout.setVisibility(View.GONE);
                        break;
                }
                mDialog.dismiss();
                this.cancel(true);
            }
        }
    }

    /**
     * @author Akira Sosa
     */
    private static class GetClubTask extends AsyncTask<Void, Void, ClubDownloadResult> {
        private ClubAPI api;
        private WeakReference<ActivitySearchCourse> context;
        private ContextUtil contextUtil;
        private String clubId;
        private ProgressDialog dialog;

        public GetClubTask(ActivitySearchCourse context) {
            this.context = new WeakReference<ActivitySearchCourse>(context);
        }

        public void getAsync(String clubId) {
            this.clubId = clubId;
            execute();
        }

        @Override
        protected ClubDownloadResult doInBackground(Void... params) {
            ClubDownloadResult result = new ClubDownloadResult();
            try {
                result = api.get(clubId);
            } catch (Exception e) {
                Crashlytics.logException(e);
                result.setErrorStatus(ErrorServer.ERROR_GENERAL);
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            contextUtil = new ContextUtil(context.get().getApplicationContext());

            dialog = new ProgressDialog(context.get());
            dialog.setIndeterminate(true);
            dialog.setMessage(context.get().getString(R.string.msg_now_loading));
            dialog.show();

            Map<String, String> baseParams = new HashMap<String, String>();
            baseParams.put("auth_token", contextUtil.getAuthToken());
            baseParams.put("app", "android" + GolfApplication.getVersionName());
            api = new ClubAPI(baseParams);
        }

        @Override
        protected void onPostExecute(ClubDownloadResult result) {
            dialog.dismiss();

            if (result.getErrorStatus() == null) {
                mSelectedClubForLive = result.getClub();

				/*ThuNA 2013/05/22 ADD-S*/
                Intent intent = null;
                int flagStart = 0;
                String requestFromLive = context.get().getIntent().getStringExtra(Constant.REQUEST_GOLF_FROM_LIVE);
                if (requestFromLive != null) {
//                     intent = new Intent(context.get(), GolfDetailsAct.class);
//                    intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, requestFromLive);
//                    flagStart = FLAG_TO_NEW_LIVE;
//                    YgoLog.d("TEST_START","1");
                } else {
					/*ThuNA 2013/05/22 ADD-E*/
                     intent = new Intent(context.get(), ActivitySelectRound.class);
                    YgoLog.d("TEST_START","2");
                }
                intent.putExtra(context.get().getString(R.string.intent_club), result.getClub());
                // CanNC: #11085 - 5/1/2015
                boolean isFromScoreAgencyButton = context.get().getIntent().getBooleanExtra(Constant.KEY_FROM_SCORE_AGENCY, false);
                intent.putExtra(Constant.KEY_FROM_SCORE_AGENCY, isFromScoreAgencyButton);
                int webUserType = context.get().getIntent().getIntExtra(Constant.KEY_IS_WEB_CHARGE_USER, 0);
                intent.putExtra(Constant.KEY_IS_WEB_CHARGE_USER, webUserType);
                context.get().startActivityForResult(intent, flagStart);
            } else {
                contextUtil.handleErrorStatus(result.getErrorStatus());
            }
        }
    }


    private void SetListViewResult(ListView listViewResult, ClubAdapter mClubAdapter, int size, final int currentPage) {
        YgoLog.d("TayPVs - ", "TayPVS - Search Course , SetListViewResult ");
        loadListViewFooter(SEARCH_SHOW_MORE, currentPage);
        if (mClubAdapter.getCount() == 0) {
            listViewResult.setAdapter(mClubAdapter);
            loadListViewFooter(SEARCH_NO_RESULT, currentPage);
        } else if (size == 0) {
            loadListViewFooter(SEARCH_LAST_PAGE, currentPage);
        } else {
            listViewResult.setAdapter(mClubAdapter);
            loadListViewFooter(SEARCH_SHOW_MORE, currentPage);
            listViewResult.setSelection(mIdtemSelect);
            mIdtemSelect = 0;
            listViewResult.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ClubObj clubObj = (ClubObj) parent.getItemAtPosition(position);
                    if (clubObj == null) {
                        view.performClick();
                    } else {
//                        ScoreInputAct.livingActivities.push(ActivitySearchCourse.this);
//                        new GetClubTask(ActivitySearchCourse.this).getAsync(clubObj.getExtId());
                    }
                }
            });
            switch (mSearchTapType) {
                case 1:
					mBtnNeighMore.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							CallMore(currentPage);
						}
					});
                    break;
                case 2:
                    mBtnMore.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CallMore(currentPage);
                        }
                    });
                    break;
            }

        }
        mClubAdapter.notifyDataSetChanged();

    }

    private void CallMore(int currentPage) {
        if (!isNetworkAvailable())
            AlertMessage(getString(R.string.warning), getString(R.string.network_erro_or_not_connet));
        else {
            GolfSearchActTask task = new GolfSearchActTask();
            if (mSearchTapType == 2) {
                mIdtemSelect = mClubAdapter.getCount();
            } else if (mSearchTapType == 1) {
                task.setLatitude((double) mSearchResultMapViewNeighbor.getMapCenter().getLatitudeE6() / 1e6);
                task.setLongitude((double) mSearchResultMapViewNeighbor.getMapCenter().getLongitudeE6() / 1e6);
            }
            task.execute(LOAD_MORE, currentPage + 1);
        }
    }

    /*
     * (non-Javadoc) 戻るボタン対策
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FLAG_TO_LOGIN) {
                flagSearchNavi = true;
                onResume();
            } else if (requestCode == FLAG_TO_NEW_LIVE) {
                Intent intent = new Intent();
                if (mSelectedClubForLive != null) {
                    intent.putExtra(getString(R.string.intent_club), mSelectedClubForLive);
                }
                setResult(RESULT_OK, intent);
                finish();
            } else {
                setResult(RESULT_OK);
                finish();
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == FLAG_TO_LOGIN) {
            otherAppFlg = false;
            flagSearchNavi = false;
            onBackPressed();
            onDestroy();
        } else if (resultCode == RESULT_CANCELED && requestCode == FLAG_TO_NEW_LIVE) {
            // Do nothing on here
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        YgoLog.d(TAG, "onDestroy");

        clearCourseSearchResult();
        if (mClubAdapter != null) {
            mClubAdapter.clear();
        }
        CleanUpUtil.cleanupView(findViewById(R.id.search_course_yourgolf));
        super.onDestroy();
        System.gc();
    }

    private void clearCourseSearchResult() {
        if (courseSearchResult != null) {
            List<Course> courses = courseSearchResult.getCourses();
            if (courses != null) {
                List<Tee> tees = null;
                List<Hole> holes = null;
                for (int i = 0; i < courses.size(); i++) {
                    tees = courses.get(i).getTees();
                    if (tees != null) {
                        for (int j = 0; j < tees.size(); j++) {
                            holes = tees.get(j).getHoles();
                            if (holes != null) holes.clear();
                            holes = null;
                        }
                        tees.clear();
                        tees = null;
                    }
                }
                courses.clear();
                courses = null;
            }
            courseSearchResult = null;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            View view = getCurrentFocus();
            if (view != null) {
                int viewId = view.getId();
                YgoLog.e(TAG, "Current focus: " + viewId + "; " + view);
//                switch (viewId) {
//                    case R.id.yourgolf_keyword:
//
//                        callSearchFromKeyEvent(view);
//                        return true;
//
//                    case R.id.yourgolf_courses_search_new:
//
//                        Object selectedItem1 = ((ListView) view).getSelectedItem();
//                        YgoLog.e(TAG, "---> 1.SelectedItem: " + selectedItem1);
//                        if (selectedItem1 != null && selectedItem1 instanceof String) {
//
//                            String data = (String) selectedItem1;
//                            if (data.startsWith(DATA_FOOTER_MORE)) {
//
//                                CallMore(mKeyWordCurrentPage);
//                                return true;
//                            } else if (data.equals(DATA_FOOTER_REQUEST)) {
//
//                                sendMailRequestSearch();
//                                return true;
//                            }
//                        }
//                        break;
//                    case R.id.yourgolf_courses_history:
//
//                        Object selectedItem2 = ((ListView) view).getSelectedItem();
//                        YgoLog.e(TAG, "---> 2.SelectedItem: " + selectedItem2);
//                        if (selectedItem2 != null) {
//                            if (selectedItem2 instanceof String) {
//
//                                String data = (String) selectedItem2;
//                                if (data.startsWith(DATA_FOOTER_MORE)) {
//
//                                    CallMore(mHistoryCurrentPage);
//                                    return true;
//                                } else if (data.equals(DATA_FOOTER_REQUEST)) {
//
//                                    sendMailRequestSearch();
//                                    return true;
//                                }
////                            } else if (selectedItem2 instanceof ClubObj) {
////                                if (mIsEditHistory) {
////                                    new DeleteHistoryTask(((ClubObj) selectedItem2).getIdServer()).execute();
////                                    return true;
////                                }
//                            }
//                        }
//                        break;
//                    case R.id.yourgolf_courses_neighbor_list:
//
//                        Object selectedItem3 = ((ListView) view).getSelectedItem();
//                        YgoLog.e(TAG, "---> 3.SelectedItem: " + selectedItem3);
//                        if (selectedItem3 != null && selectedItem3 instanceof String) {
//
//                            String data = (String) selectedItem3;
//                            if (data.startsWith(DATA_FOOTER_MORE)) {
//
//                                CallMore(mMapCurrentPage);
//                                return true;
//                            } else if (data.equals(DATA_FOOTER_REQUEST)) {
//
//                                sendMailRequestSearch();
//                                return true;
//                            }
//                        }
//                        break;
//                }
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP
                && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)) {

            callSearchFromKeyEvent(v);

            return true;
        }
        return false;
    }

    private void callSearchFromKeyEvent(View view) {

        //Hide Keybord
        final InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        //NAMLH 2012.05.10 Search Course
        mIsEditHistory = false;
        mCourseMode = Constant.OOBGOLF_COURSE;

        //Switch to tab search - Task #2189
        selectSearchTab();
        setLayout();
        GolfSearch();
    }

    /*
     * 位置情報取得処理
     */
    private void startLocationService() {
        stopLocationService();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 位置情報機能非搭載端末の場合
        if (mLocationManager == null) {
            // 何も行いません
            return;
        }

        String provider = "";
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            provider = LocationManager.NETWORK_PROVIDER;
        else
            provider = LocationManager.GPS_PROVIDER;

        // 最後に取得できた位置情報が3分以内のものであれば有効とします。
        final Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);

        if (lastKnownLocation != null && (new Date().getTime() - lastKnownLocation.getTime()) <= (3 * 60 * 1000L)) {

            onLocationDetected(lastKnownLocation);
            return;
        }

        // Toast の表示と LocationListener の生存時間を決定するタイマーを起動します。
        locationTimer = new Timer(true);
        time = 0L;
        final Handler handler = new Handler();
        locationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public boolean cancel() {

                return super.cancel();
            }

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (time >= (180 * 100L)) {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.cancel();
                                mProgressDialog.dismiss();

                                mProgressDialog = null;
                            }
                            stopLocationService();
                            if (mLocSearchFlg) {
                                ActivitySearchCourse.this.notifyMessage(getString(R.string.location_undetermined_message));
                                cancel();
                                return;
                            }
                            mLocSearchFlg = false;
                        }
                        time = time + 1000L;

                    }
                });
            }
        }, 0L, 1000L);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //mLocationManager.getLastKnownLocation(provider);
                onLocationDetected(location);

            }

            @Override
            public void onProviderDisabled(final String provider) {
            }

            @Override
            public void onProviderEnabled(final String provider) {
            }

            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {
            }
        };
        mLocationManager.requestLocationUpdates(provider, 10000, 0, mLocationListener);


    }

    private void stopLocationService() {
        mLocation = null;
        if (locationTimer != null) {
            locationTimer.cancel();
            locationTimer.purge();
            locationTimer = null;
        }
        if (mLocationManager != null) {
            if (mLocationListener != null) {
                mLocationManager.removeUpdates(mLocationListener);
                mLocationListener = null;
            }
            mLocationManager = null;
        }
    }

    /**
     * 現在地が特定されたら呼ばれる
     *
     * @param location
     */
    private void onLocationDetected(final Location location) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        stopLocationService();
        mLocation = location;

        if (mLocSearchFlg) {
            GolfSearchActTask task = new GolfSearchActTask();
            task.setLatitude(mLocation.getLatitude());
            task.setLongitude(mLocation.getLongitude());
            task.execute(NEW_SEARCH_NEARBY);
        }
        mLocSearchFlg = false;
    }

    private void hideKeyboard() {
         new CountDownTimer(10,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mKeywordEditText.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mKeywordEditText.getWindowToken(), 0);
            }
        }.start();

    }

    private void showKeyboard() {
            mKeywordEditText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(mKeywordEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void performOverlayClick(ClubObj clubObj) {
//        ScoreInputAct.livingActivities.push(ActivitySearchCourse.this);
        mSelectedClubForLive = clubObj;
        itemizedOverlay.hideAllBalloons();
        // Store the current center
        trackCenterGeoPoint = mSearchResultMapViewNeighbor.getMapCenter();
        mCurrentZoomLevel = mSearchResultMapViewNeighbor.getZoomLevel();

		/*ThuNA 2013/05/22 ADD-S*/
        Intent intents = null;
        int flagStart = 0;
        String requestFromLive = getIntent().getStringExtra(Constant.REQUEST_GOLF_FROM_LIVE);
        if (requestFromLive != null) {
//            intents = new Intent(ActivitySearchCourse.this, GolfDetailsAct.class);
//            flagStart = FLAG_TO_NEW_LIVE;
//            intents.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, requestFromLive);

            YgoLog.d("Start_activity","1");
        } else {
			/*ThuNA 2013/05/22 ADD-E*/
            intents = new Intent(ActivitySearchCourse.this, ActivitySelectRound.class);
            YgoLog.d("Start_activity","2");
        }

        intents.putExtra(r.getString(R.string.intent_club), clubObj);
        // CanNC: #11085 - 5/1/2015
        boolean isFromScoreAgencyButton = getIntent().getBooleanExtra(Constant.KEY_FROM_SCORE_AGENCY, false);
        intents.putExtra(Constant.KEY_FROM_SCORE_AGENCY, isFromScoreAgencyButton);
        int webUserType = getIntent().getIntExtra(Constant.KEY_IS_WEB_CHARGE_USER, 0);
        intents.putExtra(Constant.KEY_IS_WEB_CHARGE_USER, webUserType);
        startActivityForResult(intents, flagStart);

    }

    private void updateMapViewOverlay() {
        YgoLog.i(TAG, "updateMapViewOverlay...");
        itemizedOverlay.clearOverLay();
        if (itemizedOverlay.getInitialSize() > 0) {
            for (ClubObj clubObj : itemizedOverlay.getClubs()) {
                // Try to test
                GeoPoint point = new GeoPoint((int) (clubObj.getLat() * 1e6), (int) (clubObj.getLng() * 1e6));
                OverlayItem overlayItem = new OverlayItem(point, clubObj.getClubName(), "");
                itemizedOverlay.addOverlay(overlayItem);
            }
            if (!mapOverlays.contains(itemizedOverlay)) {
                mapOverlays.add(itemizedOverlay);
            }
        }

        // CanNC-2014/04/21
        if (mLocation != null) {
            GeoPoint mMyLocation = new GeoPoint((int) (mLocation.getLatitude() * 1E6), (int) (mLocation.getLongitude() * 1E6));
            if (mMyLocation != null) {
                myOverlay.clearOverLay();
                OverlayItem item = new OverlayItem(mMyLocation, "Current Location", "");
                myOverlay.addOverlay(item);

                if (!mapOverlays.contains(myOverlay)) {
                    mapOverlays.add(myOverlay);
                }
            }
        }
    }

    public void updateSearchClub() {
        itemizedOverlay.hideAllBalloons();
        GolfSearchActTask task = new GolfSearchActTask();
        task.setLatitude((double) mSearchResultMapViewNeighbor.getMapCenter().getLatitudeE6() / 1e6);
        task.setLongitude((double) mSearchResultMapViewNeighbor.getMapCenter().getLongitudeE6() / 1e6);
        task.execute(NEW_SEARCH_NEARBY);
    }

    private class MyCLocationOverlay extends BalloonItemizedOverlay<OverlayItem> {

        private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
        private Context context;

        public MyCLocationOverlay(Context curContext, Drawable defaultMarker, MapView mapView) {
            super(boundCenter(defaultMarker), mapView);
            this.context = curContext;
        }

        public void addOverlay(OverlayItem overlay) {
            m_overlays.add(overlay);
            populate();
        }

        public void clearOverLay() {
            m_overlays.clear();
        }

        @Override
        protected OverlayItem createItem(int i) {
            return m_overlays.get(i);
        }

        @Override
        public int size() {
            return m_overlays.size();
        }

        @Override
        protected boolean onBalloonTap(int index, OverlayItem item) {
            this.hideBalloon();
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(mSelectedClubForLive!=null)
            savedInstanceState.putSerializable(getString(R.string.save_bundle_club),mSelectedClubForLive);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSelectedClubForLive = (ClubObj) savedInstanceState.getSerializable(getString(R.string.save_bundle_club));
    }
}
