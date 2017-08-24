package com.asai24.golf.inputscore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.asai24.golf.Constant;
import com.asai24.golf.utils.YgoLog;

import io.repro.android.Repro;

public class GolfBrowserAct extends GolfActivity implements View.OnClickListener {

    private static final String TAG = GolfBrowserAct.class.getSimpleName();

    public static final String URL_KEY = "browser_url";
    private static final String CARD_USER_AGENT_STRING = "GNP GolfNetworkPlus";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private LinearLayout mBack;
    private LinearLayout mForward;

    private MyChromeClient mWebChromeClient = null;
    private View mCustomView;
    private RelativeLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    private boolean mIsShowCouponList = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.browser_topscreen);

        initialFooter();

        mWebView = (WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        configWebView();
        if(getIntent().getBooleanExtra(Constant.URL_IMPORTANT_INFORMATION, false)){ // check for start from GolfTop first time to show submenu in golftop
            SharedPreferences prefs = getSharedPreferences(Constant.ACCEPT_NEW_TERMS, 0);
            prefs.edit().putBoolean(Constant.DISPLAY_SUBMENU_FIRST_TIME, true).commit();
        }

        String url = getIntent().getStringExtra(URL_KEY);
        if (!TextUtils.isEmpty(url)) {
            if (url.contains(getString(R.string.host_coupon_url))) {
                mIsShowCouponList = true;
            } else {
                mIsShowCouponList = false;
            }
            mWebView.loadUrl(url);
        } else {
            finish();
        }
    }

    private void initialFooter() {

        mBack = (LinearLayout) findViewById(R.id.bot_back);
        mBack.setOnClickListener(this);
        mBack.setEnabled(false);
        mBack.setFocusable(false);
//
//        mForward = (LinearLayout) findViewById(R.id.bot_forward);
//        mForward.setOnClickListener(this);
//        mForward.setEnabled(false);
//        mForward.setFocusable(false);
//
//        View reloadView = findViewById(R.id.bot_reload);
//        reloadView.setOnClickListener(this);
//        reloadView.setNextFocusLeftId(R.id.bot_reload);
//
//        findViewById(R.id.bot_close).setOnClickListener(this);
//
//        View webView = findViewById(R.id.webview);
//        webView.setNextFocusLeftId(R.id.bot_reload);
//        webView.requestFocus();
    }

    private void configWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(mWebView.getSettings().getUserAgentString() + " " + CARD_USER_AGENT_STRING);
        YgoLog.e("CanNC", "OpenCard user agent: " + mWebView.getSettings().getUserAgentString());
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
            settings.setDatabasePath(databasePath);
        }

        mWebChromeClient = new MyChromeClient();
        mWebView.setWebChromeClient(mWebChromeClient);

        mWebView.setWebViewClient(new WebViewClient() {
            String loginCookie = "";

            @Override
            public void onLoadResource(WebView wv, String url) {
                CookieManager cMgr = CookieManager.getInstance();
                loginCookie = cMgr.getCookie(url);
            }

            @Override
            public void onPageFinished(WebView wv, String url) {
                CookieManager cMgr = CookieManager.getInstance();
                cMgr.setCookie(url, loginCookie);

                setEnableButtons();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                if (mIsShowCouponList) {
                    handler.proceed(Constant.PP_COUPON_USERNAME, Constant.PP_COUPON_PASSWORD);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                if (failingUrl.contains(Constant.PARAM_AUTH_TOKEN)) {
                    view.loadUrl("javascript:document.open();document.close();");
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                YgoLog.e(TAG, "Web view url: " + url);
                if (url.contains(Constant.URL_TV_DOMAIN) || url.contains(Constant.URL_TV_DOMAIN_APP)) {
                    moveTVScreen(url);

                    return true;
                }else if(url.startsWith("ygo://top/")){
                    finish();
                    return true;
                }else if (url.startsWith("ygo://webview_16109")){
                    Intent newApp = new Intent(Intent.ACTION_VIEW,  Uri.parse("ygo://webview_16109"));
                    startActivity(newApp);
                    finish();
                    return true;
                }else if(url.startsWith("ygo://reserve")){
                    Intent newApp = new Intent(Intent.ACTION_VIEW,  Uri.parse("ygo://reserve"));
                    startActivity(newApp);
                    finish();
                    return true;
                }else if(url.startsWith(getString(R.string.app_host))){
                    Intent newApp = new Intent(Intent.ACTION_VIEW,  Uri.parse(url));
                    startActivity(newApp);
                    finish();
                    return true;
                }else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });
    }

    private void setEnableButtons() {

        //check forward
        boolean canForward = mWebView.canGoForward();
        mForward.setEnabled(canForward);
        mForward.setFocusable(canForward);
        //check back
        boolean canBack = mWebView.canGoBack();
        mBack.setEnabled(canBack);
        mBack.setFocusable(canBack);

//        if (canBack) {
//            findViewById(R.id.webview).setNextFocusLeftId(R.id.bot_back);
//            findViewById(R.id.bot_reload).setNextFocusLeftId(R.id.bot_back);
//        } else if (canForward) {
//            findViewById(R.id.webview).setNextFocusLeftId(R.id.bot_forward);
//            findViewById(R.id.bot_reload).setNextFocusLeftId(R.id.bot_forward);
//        } else {
//            findViewById(R.id.webview).setNextFocusLeftId(R.id.bot_reload);
//            findViewById(R.id.bot_reload).setNextFocusLeftId(R.id.bot_reload);
//        }
    }

    private void moveTVScreen(String url) {

//        Intent tvIntent = new Intent(GolfBrowserAct.this, GolfSUVTActivity.class);
//        tvIntent.putExtra(Constant.URL_TV_DOMAIN_INTENT, url.replace(Constant.URL_TV_DOMAIN, Constant.URL_TV_DOMAIN_APP));
//        startActivity(tvIntent);
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            mWebView.onPause();
        } catch (Exception e) {
            YgoLog.e(TAG, "Error while destroying web view...", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("repro-notification-id")) {
            Repro.trackNotificationOpened(extras.getString("repro-notification-id"));
            extras.clear();
        }

        try {
            mWebView.onResume();
        } catch (Exception e) {
            YgoLog.e(TAG, "Error while resuming web view...", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
        } catch (Exception e) {
            YgoLog.e(TAG, "Error while destroying web view...", e);
        }

        try {
            System.gc();
        } catch (Exception e) {}
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.bot_close:
//                if(!getIntent().getStringExtra(URL_KEY).isEmpty() && getIntent().getStringExtra(URL_KEY).equals(Constant.URL_SPECIAL_PAGE_WEB_VIEW_3)){
//                    onBackPressed();
//                }else {
//                    if(isTaskRoot()){
//                        Intent intent = new Intent(this, GolfTop.class);
//                        startActivity(intent);
//                    }
//                    finish();
//                }
//                break;
//            case R.id.bot_back:
//                if (mWebView.canGoBack()) {
//                    mWebView.goBack();
//                }
//                setEnableButtons();
//                break;
//            case R.id.bot_forward:
//                if (mWebView.canGoForward()) {
//                    mWebView.goForward();
//                }
//                setEnableButtons();
//                break;
//            case R.id.bot_reload:
//                mWebView.reload();
//                break;
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            if (mCustomViewContainer != null) {
                mWebChromeClient.onHideCustomView();
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                if(isTaskRoot()){
//                    Intent intent = new Intent(this, GolfTop.class);
//                    startActivity(intent);
                }
                finish();

            }
    }

    private class MyChromeClient extends WebChromeClient {

        FrameLayout.LayoutParams layoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        public void onProgressChanged(WebView view, int progress) {
            if (progress >= 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                YgoLog.e("CanNC", "onShowCustomView: custom view is not null");
                callback.onCustomViewHidden();
                return;
            }
            YgoLog.e("CanNC", "onShowCustomView: execute show");
            mContentView = (RelativeLayout) findViewById(R.id.webViewRoot);
            mContentView.setVisibility(View.GONE);
            mCustomViewContainer = new FrameLayout(GolfBrowserAct.this);
            mCustomViewContainer.setLayoutParams(layoutParameters);
            mCustomViewContainer.setBackgroundResource(android.R.color.black);
            view.setLayoutParams(layoutParameters);
            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
            setContentView(mCustomViewContainer);

        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                YgoLog.e("CanNC", "onHideCustomView: custom view is null");
                return;
            } else {
                YgoLog.e("CanNC", "onHideCustomView: execute hide");
                // Hide the custom view.
                mCustomView.setVisibility(View.GONE);
                // Remove the custom view from its container.
                mCustomViewContainer.removeView(mCustomView);
                mCustomView = null;
                mCustomViewContainer.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewContainer = null;
                // Show the content view.
                mContentView.setVisibility(View.VISIBLE);
                setContentView(mContentView);
            }
        }
    }
}

