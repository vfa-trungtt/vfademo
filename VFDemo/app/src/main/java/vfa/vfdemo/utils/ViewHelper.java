package vfa.vfdemo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHelper {

    public static View getView(Context context, int layoutId){
        LayoutInflater inflator = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflator.inflate(layoutId, null);
    }

    public static ViewGroup getViewGroup(Context context, int layoutId){
        LayoutInflater inflator = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return (ViewGroup) inflator.inflate(layoutId, null);
    }

    public static float convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

//    public static float convertPixelsToDp(float px, Context context) {
//        Resources resources = context.getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float dp = px / (metrics.densityDpi / 160f);
//        return dp;
//    }
}
