package vfa.vfdemo.fragments.hdiapp;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import vfa.vfdemo.R;

import vn.hdisoft.hdilib.fragments.VFFragment;

/**
 * Created by trungtt on 8/3/17.
 */

public class FragHost extends VFFragment {

    WebView webView;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_webview;
    }

    @Override
    public void onViewLoaded() {
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("http://192.168.5.97");
    }

    public void loadUrl(String url){
        webView.loadUrl(url);
    }
}
