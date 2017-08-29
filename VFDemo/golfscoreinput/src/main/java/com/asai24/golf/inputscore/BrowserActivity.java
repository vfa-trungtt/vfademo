package com.asai24.golf.inputscore;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asai24.golf.Constant;
import com.asai24.golf.utils.YgoLog;

public class BrowserActivity extends GolfActivity implements View.OnClickListener {

    private static final String TAG = BrowserActivity.class.getSimpleName();

    public static final String BUNDLE_SHOW_TV_ACTIVATION = "BUNDLE_SHOW_TV_ACTIVATION";

	private WebView mWebView;
	private ProgressBar mProgressBar;
    private boolean mIsShowCouponList = false;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		// v3.3.5 タイトルバー非表示
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        Bundle bundle = getIntent().getExtras();
        final String url = bundle.getString(Constant.EXTRA_URL);
        if (!TextUtils.isEmpty(url) && url.contains(getString(R.string.host_coupon_url))) {
            mIsShowCouponList = true;
        } else {
            mIsShowCouponList = false;
        }
        if (!bundle.getBoolean(BUNDLE_SHOW_TV_ACTIVATION, false)) {
            if (!mIsShowCouponList) {
                setContentView(R.layout.browser);
                findViewById(R.id.top_title).setVisibility(View.GONE);
            } else {
                // Show Coupon
                setContentView(R.layout.browser_tv_activation);

                TextView textView = (TextView) findViewById(R.id.top_title);
                textView.setText(getString(R.string.setting_profile_passport_title));
                textView.setVisibility(View.VISIBLE);
            }
        } else {
            // Show Android TV activation
            setContentView(R.layout.browser_tv_activation);

            TextView textView = (TextView) findViewById(R.id.top_title);
            textView.setText(getString(R.string.setting_yourgolf_tv_activation));
            textView.setVisibility(View.VISIBLE);
        }
//        findViewById(R.id.top_close).setOnClickListener(this);
//        findViewById(R.id.top_refresh).setOnClickListener(this);

		mWebView = (WebView)findViewById(R.id.webview);
		mProgressBar = (ProgressBar)findViewById(R.id.progress);
		

        YgoLog.e("BrowserActivity", "URL: " + url);
		if (url==null || url.equals("")) {
			finish();
		} else {
			mWebView.loadUrl(url);
            WebSettings settings = mWebView.getSettings();
			settings.setBuiltInZoomControls(true);
			settings.setUseWideViewPort(true);
            settings.setJavaScriptEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
                settings.setDatabasePath(databasePath);
            }
			
			mWebView.setWebChromeClient(new WebChromeClient() {
	        	public void onProgressChanged(WebView view, int progress) {
	        		if(progress >= 100) {
	        			mProgressBar.setVisibility(View.GONE);
	        		}else {
	        			mProgressBar.setVisibility(View.VISIBLE);
	        		}
	        	}
	        });
			
			mWebView.setWebViewClient(new WebViewClient() {
	        	String loginCookie = "";
	        	@Override
	        	public void onLoadResource(WebView wv, String url){
	        		CookieManager cMgr = CookieManager.getInstance();
	        		loginCookie = cMgr.getCookie(url);
	        	}
	        	@Override
	        	public void onPageFinished(WebView wv, String url){
	        		CookieManager cMgr = CookieManager.getInstance();
	        		cMgr.setCookie(url, loginCookie);
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
                    YgoLog.e(TAG, "shouldOverrideUrlLoading: " + url);

                    if (url.contains(Constant.URL_TV_DOMAIN) || url.contains(Constant.URL_TV_DOMAIN_APP)) {
                        moveTVScreen(url);

                        return true;
                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                }
            });
	    }
	}

    private void moveTVScreen(String url) {

//        Intent tvIntent = new Intent(BrowserActivity.this, GolfSUVTActivity.class);
//        tvIntent.putExtra(Constant.URL_TV_DOMAIN_INTENT, url.replace(Constant.URL_TV_DOMAIN, Constant.URL_TV_DOMAIN_APP));
//        startActivity(tvIntent);
    }

	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                	if(mWebView.canGoBack()) {
                		mWebView.goBack();
                		return true;
                	}
                	break;
                default:
                	break;
            }
        }
        return super.dispatchKeyEvent(event);
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
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.top_close:
//                finish();
//                break;
//            case R.id.top_refresh:
//                mWebView.reload();
//                break;
//        }

    }
}
