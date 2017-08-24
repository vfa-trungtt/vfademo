package com.asai24.golf.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.asai24.golf.inputscore.GolfBrowserAct;


/**
 * Created by TayPVS on 6/17/15.
 */
public class AppCommonUtil {

    /**
     * Open Webview
     *
     * @param context
     * @param url     : web url
     */
    public static void openBrowserWebview(Context context, String url) {
        if (url.contains("http://") || url.contains("https://")) {
            try {
                Intent intent = new Intent(context, GolfBrowserAct.class);
                intent.putExtra(GolfBrowserAct.URL_KEY, url).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Open Play Store from package, if no Play store, open browswer
     *
     * @param mContext
     * @param packName : Package of Application
     */
    public static void installAppFromGooglePlay(Context mContext,
                                                String packName) {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("market://details?id=" + packName)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + packName)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

}
