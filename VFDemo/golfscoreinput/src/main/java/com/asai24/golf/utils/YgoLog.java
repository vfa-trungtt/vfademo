package com.asai24.golf.utils;

import android.util.Log;


public class YgoLog {
	public static void d(String tag, String msg) {
//		if (GolfApplication.isDebug()) {
//			Log.d(tag + "-YGO", msg);
//		}
        Log.d(tag + "-YGO", msg);
	}

	public static void i(String tag, String msg) {
//		if (GolfApplication.isDebug()) {
//			Log.i(tag + "-YGO", msg);
//		}
        Log.i(tag + "-YGO", msg);
	}

	public static void e(String tag, String msg) {
//		if (GolfApplication.isDebug()) {
//			Log.e(tag + "-YGO", msg);
//		}
        Log.e(tag + "-YGO", msg);
	}

	public static void e(String tag, String msg, Exception e) {
//		if (GolfApplication.isDebug()) {
//			Log.e(tag + "-YGO", msg, e);
//		}
        Log.e(tag + "-YGO", msg, e);
	}

}
