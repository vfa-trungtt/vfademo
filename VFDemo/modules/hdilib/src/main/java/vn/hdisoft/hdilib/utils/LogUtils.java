package vn.hdisoft.hdilib.utils;

import android.util.Log;

import java.util.ArrayList;

public class LogUtils {
	
	public static String DEFAULT_TAG = "hdilib";
	public static boolean IS_DEBUG = true;
	
	public static void debug(String msg){
		if(IS_DEBUG){
			Log.d(DEFAULT_TAG,msg);
		}
	}
	public static void info(String msg){
		if(IS_DEBUG){
			Log.i(DEFAULT_TAG, msg);
		}
	}
	
	public static void error(String msg){
		if(IS_DEBUG){
			Log.e(DEFAULT_TAG, msg);
		}
	}

	public static void error(Exception e){
		if(IS_DEBUG){
			Log.e(DEFAULT_TAG, e.toString());
		}
	}
	
	public static void printArrayInfo(ArrayList<Object> arr){
		if(!IS_DEBUG) return;
		for(Object ob : arr){
			info(ob.toString());
		}
	}
	public static void printArrayStringInfo(ArrayList<String> arr){
		if(!IS_DEBUG) return;
		info("ARRAY:");
		for(String ob : arr){
			info(ob);
		}
	}
	
	public static void printArrayInfo(Object[] arr){
		if(!IS_DEBUG) return;
		for(Object ob : arr){
			info(ob.toString());
		}
	}

	public static long startTime;
	public static void startTrackingTime(){
		startTime = System.currentTimeMillis();
	}

	public static void endTrackingTime(String msg){
		if(IS_DEBUG){
			long current = System.currentTimeMillis();
			long period = current - startTime;
			Log.e(DEFAULT_TAG, msg +":"+ period +"ms" +" - "+ period/1000 + " seconds." );
		}
	}
}
