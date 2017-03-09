package vfa.vfdemo.fragments.book;

import android.webkit.WebView;

import vfa.vfdemo.R;
import vfa.vflib.fragments.VFFragment;

/**
 * Created by Vitalify on 3/9/17.
 */

public class FragBookPage extends VFFragment {
    WebView webView;
    String bookChapterUrl = "http://sstruyen.com/doc-truyen/sac-hiep/dinh-cap-luu-manh/chuong-38-tinh-dich/113003.html";
    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_book_page;
    }

    @Override
    public void onViewLoaded() {
        webView = (WebView) rootView.findViewById(R.id.webView);
        loadBookChapter();
    }

    public void loadBookChapter(){
        webView.loadUrl(bookChapterUrl);
    }
}
