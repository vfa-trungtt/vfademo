package vfa.vfdemo.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


public class ScreenUtils {
	
	public static int width = 0;
	public static int height = 0;
	public static int realWidth = 0;
	public static int realHeight = 0;
	public static int statusBarHeight = 0;

	public static String getDensityName(Context context) {

        float density = context.getResources().getDisplayMetrics().density;
		if (density >= 4.0) {
			return "xxxhdpi";
		} else if (density >= 3.0) {
			return "xxhdpi";
		} else if (density >= 2.0) {
			return "xhdpi";
		} else if (density >= 1.5) {
			return "hdpi";
		} else if (density >= 1.0) {
			return "mdpi";
		} else {
			return "ldpi";
		}
	}

	public static String getScreenSize(Context context) {
		int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		String size = "";
		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			size = "large";
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			size = "normal";
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			size = "small";
			break;
		default:
			size = "unknown";
		}
		return size;
	}
	
	
	public static int getStatusBarHeight(Context context) {
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}

	public static int convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    int px = (int) (dp * (metrics.densityDpi / 160f));
	    return px;
	}

	public static int convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    int dp = (int) (px / (metrics.densityDpi / 160f));
	    return dp;
	}
	
	public static void getDimensionScreen(Context context) {

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
        width   = size.x;
		height  = size.y;
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

//        LogUtils.debug("=== SCREEN INFO ===");
//        LogUtils.info("Width:"+width + ",Height:"+height);
//        LogUtils.info("Desity dpi:"+metrics.densityDpi + ",density:" + metrics.density +
//                ",Scale desisty:"+metrics.scaledDensity);
//
//        LogUtils.debug("=== SCREEN INFO ===");
	}
}
