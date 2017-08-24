package com.asai24.golf.inputscore;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asai24.golf.Constant;

import com.asai24.golf.common.Distance;
import com.asai24.golf.db.GolfDatabase;
import com.asai24.golf.db.RoundCursor;
import com.asai24.golf.listener.ServiceListener;
import com.asai24.golf.utils.AppCommonUtil;
import com.asai24.golf.utils.ConvertUtils;
import com.asai24.golf.utils.YgoLog;
import com.asai24.golf.view.ResizableImageView;
import com.google.android.maps.MapActivity;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.List;
import java.util.Locale;

import io.repro.android.Repro;

abstract public class GolfActivity extends MapActivity {

    private String TAG = "==================" + GolfActivity.class.getSimpleName() + "=======================";
    protected static final int NETWORK_UNAVAILABLE = 99;
    private static final int LOCATION_SERVICE_SETTING = 100;


    protected Toast mToast;
    private int REQUEST_APP_SETTINGS = 1000;
    public static final int PERMISSION_CONTACT = 123;
    public static final int PERMISSION_LOCATION = 124;
    public static final int PERMISSION_SDCARD = 125;
    private SharedPreferences pre,appPreferences;
    private String nameSharePre = "SHARE_FOR_PERMISSION";
    public  static ProgressDialog progressDialog;
    private LoadingView mLoadingView;

//    public APIInterfaceImpl service;


    public static void createProgressDialog(Context context){
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Now in progress");

        }
    }
    public static  void showProgressDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        progressDialog.show();
    }
    public static void dismissProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public void cancelProgressDialog(){
        progressDialog.cancel();
    }
    public void setNullProgressDialog(){
        progressDialog = null;
    }

    public void openNewWebView(String url) {

        if (!TextUtils.isEmpty(url)) {
            try {
                Intent intent = new Intent(this,GolfBrowserAct.class);
                intent.putExtra(GolfBrowserAct.URL_KEY, url);
                startActivity(intent);

            } catch (Exception e) {
                YgoLog.e(TAG, "Exception when load web view...", e);
            }
        }
    }

    /**
     * Invoked when the user wants to open Web site.
     *
     * @param url the Web site's url
     */
    public void openWebView(String url) {

        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra(Constant.EXTRA_URL, url);
        startActivity(intent);

    }

    public void openStandardBrowser(String url) {
        YgoLog.i(TAG, "Url: " + url);

        if (url.contains("http://") || url.contains("https://")) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                YgoLog.e(TAG, "Exception when open browser...", e);
            }
        }
    }

    protected void sendMail(String mailTo) {

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", mailTo, null));
            startActivity(intent);
        } catch (Exception e) {
            YgoLog.e(TAG, "Exception when call send mail...", e);
        }
    }

    public void notifyMessage(int msgId) {
        notifyMessage(getString(msgId));
    }

    public void notifyMessage(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(this, null, Toast.LENGTH_LONG);
        }
//		mToast.cancel();
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infoset = mConnectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : infoset) {
            if (info.isAvailable() && info.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /*
     * GPS&Wifi設定確認
     */
    protected boolean checkLocationService() {
        LocationManager myLocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!(myLocManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))) {
            showDialog(LOCATION_SERVICE_SETTING);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {

        Configuration configuration = getResources().getConfiguration();
        if (configuration.fontScale > 1.0f) {
            YgoLog.e(TAG, "onResume-fontScale: " + configuration.fontScale);
            configuration.fontScale = 1.0f;

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(configuration, metrics);
        }
        super.onResume();

        Repro.showInAppMessage(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Configuration configuration = getResources().getConfiguration();
        if (configuration.fontScale > 1.0f) {
            YgoLog.e(TAG, "onCreate-fontScale: " + configuration.fontScale);
            configuration.fontScale = 1.0f;

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(configuration, metrics);
        }

//        GolfApplication.setPumaTheme(this);

        super.onCreate(savedInstanceState);
        YgoLog.i(TAG, "class name: " + this.getClass().getSimpleName());

//        ActivityHistoryManager.addNewActivity(this);
//        if (this.getClass().isAssignableFrom(ScoreInputAct.class)) {
//
//            ActivityHistoryManager.shutdownFromActivityHistory(RoundDetailAct.class);
//            ActivityHistoryManager.shutdownFromActivityHistory(RoundDetailAct_St4.class);
//        }
//        pre = getApplicationContext().getSharedPreferences(nameSharePre,MODE_PRIVATE);
//        appPreferences = ((GolfApplication) getApplication()).getAppPreferences();
//        service = ((GolfApplication) getApplication()).getService();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // v3.3.2 st saikami 20111003
    @Override
    protected void onDestroy() {

        try {
            super.onDestroy();
//            ActivityHistoryManager.removeFromActivityHistory(this);
//            YgoLog.i(TAG, "onDestroy size activity: " + ActivityHistoryManager.ActivityHistory.size());
        } catch (Exception e) {
            YgoLog.i(TAG, "onDestroy error: " + e.getMessage());
        }
    }

    // v3.3.2 ed saikami
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case NETWORK_UNAVAILABLE:
                return new AlertDialog.Builder(this).setIcon(
                        R.drawable.alert_dialog_icon).setTitle(
                        R.string.warning).setMessage(
                        R.string.network_erro_or_not_connet).setPositiveButton(
                        R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        }).create();
            case LOCATION_SERVICE_SETTING:
                return new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.check_gps_title)
                        .setMessage(R.string.check_gps_message)
                        .setPositiveButton(R.string.setting,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(gpsIntent);
                                    }
                                }).setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).create();
        }
        return null;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    protected String parseToHdcpDisplay(float hdcp) {

        if (hdcp == Constant.DEFAULT_HDCP_VALUE) {
            return "-";
        } else if (hdcp < 0) {
            return "+" + Math.abs(hdcp); // +6, +5,.....
        } else {
            return String.valueOf(hdcp);
        }
    }

    protected String parseToHdcpDisplay(String hdcpStr) {

        try {
            float hdcp = Float.valueOf(hdcpStr);
            if (hdcp == Constant.DEFAULT_HDCP_VALUE) {
                return "-";
            } else if (hdcp < 0) {
                return "+" + Math.abs(hdcp); // +6, +5,.....
            } else {
                return String.valueOf(hdcp);
            }
        } catch (Exception e) {
            return "-";
        }
    }

    protected float parseFromHdcpDisplay(String hdcp) {

        if (!TextUtils.isEmpty(hdcp)) {
            try {
                hdcp = hdcp.replace("+", "-");
                float result = Float.parseFloat(hdcp);
                if (result > -7 && result < 50) {
                    return result;
                }
            } catch (Exception e) {
            }
        }
        return Constant.DEFAULT_HDCP_VALUE;
    }

    protected void scrollForVisible(ListView listView, int selectedIndex) {

        if (listView == null || selectedIndex < 0) {
            return;
        }
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        if (selectedIndex < first || selectedIndex > last) {
            listView.setSelection(selectedIndex);
        }
    }

    protected class CallPlayingTask extends AsyncTask<String, Object, Integer> {
        private ProgressDialog mpPrDialog;
        private Context ctx;
        private GolfDatabase database;
        private RoundCursor roundCursor;

        public CallPlayingTask(GolfDatabase mDb, Context context) {

            ctx = context;
            database = mDb;
            mpPrDialog = new ProgressDialog(ctx);
        }

        @Override
        protected Integer doInBackground(String... params) {
            roundCursor = database.getRoundByPlaying(true);
            startPlaying(database, roundCursor.getId(), -1);
            return 1;
        }

        @Override
        protected void onPreExecute() {
            mpPrDialog.setIndeterminate(true);
            mpPrDialog.setMessage(getString(R.string.msg_now_loading));
            mpPrDialog.show();
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            try {
                if (mpPrDialog.isShowing()) {
                    mpPrDialog.dismiss();
                }
            } catch (Exception e) {
            }
        }
    }

    protected int startPlaying(GolfDatabase database, long roundID, int holeNumber) {

        YgoLog.i(TAG, "Start playing roundId=" + roundID + "; holeNumber=" + holeNumber);
//        String extType;
//        try {
//            RoundCursor rc = database.getRound(roundID);
//            if (null == rc || rc.getCount() <= 0) {
//                rc = database.getRoundByPlaying(true);
//                if (null == rc || rc.getCount() <= 0) {
//                    rc = database.getRoundByPlayingNotDelete(true);
//                }
//            }
//
//            long courseId = rc.getCourseId();
//            long teeId = rc.getTeeId();
//            int pauseHoleNumber;
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//            if (holeNumber > 0) {
//                pauseHoleNumber = holeNumber;
//
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putInt(getString(R.string.pref_score_detail_pause), holeNumber);
//                editor.commit();
//            } else {
//                pauseHoleNumber = prefs.getInt(getString(R.string.pref_score_detail_pause), 1);
//            }
//
//            HoleCursor hc = database.getTeeHoles(teeId, pauseHoleNumber);
//            long holeId = hc.getId();
//            long[] playerIds = database.getPlayerIds(roundID);
//
//            extType = rc.getColClubExtType();
//            Intent intent = new Intent(this, ScoreInputAct.class);
//            if (ActivityHistoryManager.checkActivityExist(ScoreInputAct.class)) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            }
//
//            String liveEntryId = rc.getLiveEntryId();
//            String liveId = rc.getLiveId();
//            if (!TextUtils.isEmpty(liveEntryId) && !"null".equals(liveEntryId)) {
//                intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, Constant.REQUEST_GOLF_FROM_LIVE);
//            } else if (!TextUtils.isEmpty(liveId) && !"null".equals(liveId)) {
//                intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, Constant.REQUEST_GOLF_FROM_LIVE);
//            }
//
//            RoundPlayerCursor roundPlayerCursor = database.getRoundPlayerByRoundIdAndPlayerId(roundID, playerIds[0]);
//            if (roundPlayerCursor != null && roundPlayerCursor.getCount() > 0) {
//                intent.putExtra(Constant.CUR_PLAYER_HADICAP, roundPlayerCursor.getPlayerHdcp());
//            } else {
//                intent.putExtra(Constant.CUR_PLAYER_HADICAP, Constant.DEFAULT_HDCP_VALUE);
//            }
//            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//            boolean live = pref.getBoolean(Constant.LIVE_PLAYING, false);
//            if (live) {
//                intent.putExtra(Constant.REQUEST_GOLF_FROM_LIVE, Constant.REQUEST_GOLF_FROM_LIVE);
//                YgoLog.i(TAG, "Live....");
//            }
//            intent.putExtra(Constant.PLAYING_COURSE_ID, courseId);
//            intent.putExtra(Constant.PLAYING_TEE_ID, teeId);
//            intent.putExtra(Constant.PLAYING_ROUND_ID, roundID);
//            intent.putExtra(Constant.PLAYING_HOLE_ID, holeId);
//            intent.putExtra(Constant.PLAYER_IDS, playerIds);// 2009-05-27
//            intent.putExtra(Constant.ROUND_ID, rc.getColClubExtId());
//            intent.putExtra(Constant.EXT_TYPE_YOURGOLF, extType);
//
//
//            rc.close();
//            hc.close();
//
//            startActivity(intent);
//            return 1;
//        } catch (Exception e) {
//            YgoLog.e(TAG, "startPlaying error:" + e.getMessage());
//            return 2;
//        }
        return 1;
    }

    /**
     * Show message before open GolfDay
     *
     * @param isCampaign
     * @param golfCourseId
     * @param courseId
     * @param hole
     * @return
     */
    public void openGolfDay(boolean isShowMessage, final String isCampaign, final String golfCourseId, final String courseId, final String hole, final Constant.CALL_FROM isCallFrom) {

        if (isShowMessage) {
            final Dialog golfDayDialog = new Dialog(this, android.R.style.Theme_Translucent);
            golfDayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // here we set layout of  dialog
            golfDayDialog.setContentView(R.layout.dialog_golf_day_confirm);
            golfDayDialog.setCancelable(true);
            if (!Locale.getDefault().getLanguage().equals("ja")) {
                golfDayDialog.findViewById(R.id.tvGolfDayMessage).setVisibility(View.GONE);
            }
            final CheckBox dialogCb = (CheckBox) golfDayDialog.findViewById(R.id.golf_day_show_dialog_cb);
            if (isCampaign.equals("true")) {
                ((ResizableImageView) golfDayDialog.findViewById(R.id.golf_day_campaign_img)).setImageResource(R.drawable.golfnavi_message_cmpgn);
            } else {
                ((ResizableImageView) golfDayDialog.findViewById(R.id.golf_day_campaign_img)).setImageResource(R.drawable.golfnavi_message_normal);
            }
            golfDayDialog.findViewById(R.id.golf_day_confirm_course_map_bt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialogCb.isChecked()) {
                        Distance.markDonNotShowGolfDayMessage(GolfActivity.this);
                    }
                    openGolfDay(golfCourseId, courseId, hole, isCampaign, isCallFrom);
                    golfDayDialog.dismiss();
                }
            });
            golfDayDialog.findViewById(R.id.golf_day_confirm_cancel_bt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    golfDayDialog.dismiss();
                }
            });
            golfDayDialog.show();
        } else {
            openGolfDay(golfCourseId, courseId, hole, isCampaign, isCallFrom);
        }
    }

    /**
     * Call open Golf Day
     *
     * @param golfCourseId
     * @param course
     * @param hole
     */
    private void openGolfDay(String golfCourseId, String course, String hole, String isCampaign, Constant.CALL_FROM isCallFrom) {
        if (!TextUtils.isEmpty(golfCourseId)) {

            PackageManager pack_manager = getPackageManager();
            boolean resolved = false;
            List<ApplicationInfo> list = pack_manager.getInstalledApplications(0);
            // Check if Line is installed
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).packageName.equals(Constant.GOLF_DAY_PACKAGE)) {
                    // do what you want
                    resolved = true;
                    break;
                }
            }
            // Track event
            MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, Constant.MIX_PANEL_TOKEN);
            int mGolfCourseId = 0;
            int mCourse = 0;
            int mHole = 0;
            try {
                mGolfCourseId = Integer.parseInt(golfCourseId);
                mCourse = Integer.parseInt(course);
                mHole = Integer.parseInt(hole);
            } catch (Exception e) {
                YgoLog.e(TAG, "Exception when parse course info...", e);
            }
            // check application installed
            if (resolved) {
                int userId;
                if (Distance.getMemberStatus(this) > 0
                        || isCampaign.equals("true")) {
                    userId = Constant.GOLF_DAY_PARAM_USER_PREMIUM;
                } else {
                    userId = Constant.GOLF_DAY_PARAM_USER_NORMAL;
                }
                YgoLog.i(TAG, "OpenGolfDay: golfCourseId = " + golfCourseId + " ; course = " + course + " ; hole = " + hole + " ; userId = " + userId);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName(Constant.GOLF_DAY_PACKAGE, Constant.GOLF_DAY_DEFAULT_CLASS);
                intent.putExtra("user", userId);
                intent.putExtra("golf_course", mGolfCourseId);

                // Call from hole screen
                if (!TextUtils.isEmpty(course) && !TextUtils.isEmpty(hole)) {
                    mixpanel.track(Constant.Gtrack.EVENT_OPEN_GOLF_DAY_FROM_HOLE, null);

                    intent.putExtra("m", Constant.GOLF_DAY_PARAM_HOLE_MAP);
                    intent.putExtra("course", mCourse);
                    intent.putExtra("hole", mHole);
                } else { // Call from club detail or history
                    if (isCallFrom == Constant.CALL_FROM.CLUB_DETAIL) {
                        mixpanel.track(Constant.Gtrack.EVENT_OPEN_GOLF_DAY_FROM_CLUB, null);
                    } else {
                        mixpanel.track(Constant.Gtrack.EVENT_OPEN_GOLF_DAY_FROM_HISTORY, null);
                    }

                    intent.putExtra("m", Constant.GOLF_DAY_PARAM_COURSE_MENU);
                }
                startActivity(intent);
            } else {
                mixpanel.track(Constant.Gtrack.EVENT_OPEN_GOLF_DAY_PAGE, null);
                // If not installed - Open PlayStore
                AppCommonUtil.installAppFromGooglePlay(getBaseContext(), Constant.GOLF_DAY_PACKAGE);
            }
            mixpanel.flush();
        } else {
            YgoLog.i(TAG, "ToGolfDay: golf course id is empty");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                switch (requestCode) {
                    case PERMISSION_CONTACT:
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                            pre.edit().putBoolean(String.valueOf(PERMISSION_CONTACT), true).commit();

                        }else{
                            pre.edit().putBoolean(String.valueOf(PERMISSION_CONTACT), false).commit();
                        }
                        break;
                    case PERMISSION_LOCATION:
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) || !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            pre.edit().putBoolean(String.valueOf(PERMISSION_LOCATION), true).commit();
                            YgoLog.d("PERMISSION_TAG", "location_save : "+ "true");
                        }else{
                            pre.edit().putBoolean(String.valueOf(PERMISSION_LOCATION), false).commit();
                            YgoLog.d("PERMISSION_TAG", "location_save : "+ "false");
                        }
                        break;
                    case PERMISSION_SDCARD:
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) || !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            pre.edit().putBoolean(String.valueOf(PERMISSION_SDCARD), true).commit();
                            YgoLog.d("PERMISSION_TAG", "sdcard_save : "+ "true");
                        }else{
                            pre.edit().putBoolean(String.valueOf(PERMISSION_SDCARD), false).commit();
                            YgoLog.d("PERMISSION_TAG", "sdcard_save : "+ "false");
                        }
                        break;
                }

            }
        }

    }
    public boolean checkPermissionRequirement(int m_permission){
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {
            switch (m_permission) {
                case PERMISSION_CONTACT:
                    int isReadContact = checkSelfPermission(Manifest.permission.READ_CONTACTS);

                    if ( isReadContact != PackageManager.PERMISSION_GRANTED ) {
                        if (pre.getBoolean(String.valueOf(PERMISSION_CONTACT),false)) {
                           // showDialogRequirmentPermission(PERMISSION_CONTACT);
                        }else
                            checkPermission(PERMISSION_CONTACT);
                        return true;
                    }
                    return false;

                case PERMISSION_LOCATION:
                    int isLocationAccessPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                    int isLocationFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

                    if (isLocationAccessPermission != PackageManager.PERMISSION_GRANTED || isLocationFinePermission != PackageManager.PERMISSION_GRANTED) {
                        YgoLog.d("PERMISSION_TAG", String.valueOf(pre.getBoolean(String.valueOf(PERMISSION_LOCATION),false)));
                        if (pre.getBoolean(String.valueOf(PERMISSION_LOCATION),false)) {
                            //showDialogRequirmentPermission(PERMISSION_LOCATION);
                        }else
                            checkPermission(PERMISSION_LOCATION);
                        return true;
                    }
                    return false;

                case PERMISSION_SDCARD:
                    int isReadSdCard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    int isWriteSdCard = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (isReadSdCard != PackageManager.PERMISSION_GRANTED || isWriteSdCard != PackageManager.PERMISSION_GRANTED) {
                        YgoLog.d("PERMISSION_TAG", "sdcard_read : "+ String.valueOf(pre.getBoolean(String.valueOf(PERMISSION_SDCARD),false)));
                        if (pre.getBoolean(String.valueOf(PERMISSION_SDCARD),false)) {
                           // showDialogRequirmentPermission(PERMISSION_SDCARD);
                        }else
                            checkPermission(PERMISSION_SDCARD);
                        return true;
                    }
                    return false;

            }


        }
        return false;
    }
    private String getTitleDialog(int m_permission){
        switch (m_permission){

            case PERMISSION_CONTACT:
                return getResources().getString(R.string.permission_contacts);

            case PERMISSION_LOCATION:
                return getResources().getString(R.string.permission_location);
            case PERMISSION_SDCARD:
                return getResources().getString(R.string.permission_sdcard);
        }
        return "";
    }
    public void showDialogRequirmentPermission(final int m_permission) {
        final int permissionRequirement = m_permission;
        AlertDialog.Builder dialog =new AlertDialog.Builder(this);
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1)
            dialog=new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        dialog.setTitle(getResources().getString(R.string.requirement_permission_title));
        dialog.setMessage(getTitleDialog(m_permission));
        dialog.setPositiveButton("Go to App Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // checkPermission(permissionRequirement);
                dialog.dismiss();
                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);

            }
        });
        dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void checkPermission(int permission){
        switch (permission){
            case PERMISSION_CONTACT:
                if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                            permission);
                }
                break;
            case PERMISSION_LOCATION:
                if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            permission);
                }
                break;
            case PERMISSION_SDCARD:
                if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            permission);
                }
                break;
            default:
                break;
        }
    }

    public void showLoadingView(Context context, boolean isShowLoading, ViewGroup viewContain, boolean sizeFull) {
        if(isShowLoading) {
            View viewLoading = LayoutInflater.from(context).inflate(R.layout.loading_request, null);
            LinearLayout.LayoutParams layoutParams;
            if(sizeFull){
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }else{
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.convertDpToPx(context,200));
            }
            viewLoading.setLayoutParams(layoutParams);
            viewContain.removeAllViews();
            viewContain.addView(viewLoading);
        }
    }

    public void hideLoadingView(ViewGroup viewContain, ViewGroup viewChild) {
        viewContain.removeAllViews();
        viewContain.addView(viewChild);
    }
    public  void showErrorLoading(Context context, ViewGroup viewContain, Constant.ErrorServer errorServer, final ServiceListener listener){
        View viewLoadingError = (ViewGroup) viewContain.getChildAt(0);
        String message ="";
        if(errorServer == Constant.ErrorServer.ERROR_CONNECT){
            message = context.getString(R.string.no_internet_connectivity_msg);
        }
        else if(errorServer == Constant.ErrorServer.ERROR_CONNECT_TIMEOUT){
            message = context.getString(R.string.error_connect_timeout);

        }else if(errorServer == Constant.ErrorServer.ERROR_SOCKET_TIMEOUT) {
            message = context.getString(R.string.error_socket_timeout);

        }else if(errorServer == Constant.ErrorServer.ERROR_404){
            message = context.getString(R.string.network_erro_or_not_connet);
        }else if(errorServer == Constant.ErrorServer.ERROR_400){
            message = context.getString(R.string.error_400_request);
        }else if(errorServer == Constant.ErrorServer.ERROR_INTERNAL_SERVER){
            message = context.getString(R.string.error_internal_server);
        }else
            message = context.getString(R.string.error_general);

        ((TextView) viewLoadingError.findViewById(R.id.tv_loading_error)).setText(message);
        ((TextView) viewLoadingError.findViewById(R.id.tv_loading_error)).setVisibility(View.VISIBLE);
        ((ProgressBar) viewLoadingError.findViewById(R.id.prg_loading)).setVisibility(View.GONE);

        viewContain.removeAllViews();
        viewContain.addView(viewLoadingError);
    }
    public void showErrorViewLoading(ViewGroup viewContain, String message){
        View viewLoadingError = (ViewGroup) viewContain.getChildAt(0);
        ((TextView) viewLoadingError.findViewById(R.id.tv_loading_error)).setText(message);
        ((TextView) viewLoadingError.findViewById(R.id.tv_loading_error)).setVisibility(View.VISIBLE);
        ((ProgressBar) viewLoadingError.findViewById(R.id.prg_loading)).setVisibility(View.GONE);
    }

    /** Inner class */
    public static class LoadingView extends RelativeLayout {


        public LoadingView(Context context, boolean isShowLoading) {
            super(context);
            try {
                init(context, isShowLoading);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void init(Context context, boolean isShowLoading) throws Exception {
            try {
                View rootView = LayoutInflater.from(context).inflate(R.layout.loading_request,null);
                ProgressBar pgbLoading = (ProgressBar) rootView.findViewById(R.id.prg_loading);
                if (isShowLoading) {
                    pgbLoading.setVisibility(View.VISIBLE);
                } else {
                    pgbLoading.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                throw new Exception(e);
            }
        }

    }
    public AlertDialog DialogNoNetWork(Context context){
        AlertDialog dialogNetWork =  new AlertDialog.Builder(context).setIcon(
                R.drawable.alert_dialog_icon).setTitle(
                R.string.warning).setMessage(
                R.string.network_erro_or_not_connet).setPositiveButton(
                R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        finish();
                        dialog.dismiss();
                    }
                }).create();
        return dialogNetWork;
    }
}
