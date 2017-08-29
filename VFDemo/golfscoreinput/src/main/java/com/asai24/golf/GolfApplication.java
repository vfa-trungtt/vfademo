package com.asai24.golf;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by trungtt on 8/17/17.
 */

public class GolfApplication {
    public static boolean isPuma() {
        return false;
    }
    public static String getVersionName() {
        PackageInfo packageInfo = null;
//        try {
//            packageInfo = mInstance.getPackageManager().getPackageInfo(mInstance.getPackageName(), PackageManager.GET_CONFIGURATIONS);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return packageInfo.versionName;
        return "";
    }
}
