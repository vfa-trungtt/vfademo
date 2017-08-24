package com.asai24.golf.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by huynq on 9/15/16.
 */
public class ConvertUtils {
    public static int convertDpToPx(Context context, int dp) {
        try {
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion <= 16) {
                // Do something for 14 and above versions
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            }
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
        }catch(Exception e){
            return 0;
        }
    }
    public static float convertPxToDp(Context context, int px) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= 16) {
            // Do something for 14 and above versions
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
        }
        return (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,   px,context.getResources().getDisplayMetrics());
    }

}
