package vn.hdisoft.hdilib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PrefAppSettings {

    private static final String DEFAULT_NAME = "VFLib";

    public static void setString(Context context, String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        if (TextUtils.isEmpty(value)) {
            edit.remove(key);
        } else {
            edit.putString(key, value);
        }
        edit.apply();
    }
    public static boolean checkKey (Context context, String key){
    	SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
    	return pref.contains(key);
    }

    public static String getString(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }


    public static String getString(Context context, String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }


    public static void setBoolean(Context context, String key, boolean bool) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putBoolean(key, bool);
        edit.apply();
    }


    public static boolean getBoolean(Context context, String key) {
    	return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (TextUtils.isEmpty(key) || context == null) {
            return false;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    public static void setLong(Context context, String key, long l) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putLong(key, l);
        edit.apply();
    }

    public static long getLong(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return 0L;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getLong(key, 0L);
    }


    public static void setInt(Context context, String key, int integer) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putInt(key, integer);
        edit.apply();
    }


    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    public static int getInt(Context context, String key, int defaultvalue) {
        if (TextUtils.isEmpty(key)) {
            return defaultvalue;
        }

        SharedPreferences pref = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultvalue);
    }

    public static boolean deleteAll(Context context){
    	SharedPreferences settings = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);

    	boolean result =  settings.edit().clear().commit();
    	Editor edit = settings.edit();
        edit.apply();
    	return result;
    }

    public static long getAppFirstInstallTime(Context context){
	    PackageInfo packageInfo;
	    try {
	    if(Build.VERSION.SDK_INT>8/*Build.VERSION_CODES.FROYO*/ ){
	        packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.firstInstallTime;
	    }else{
	        ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
	        String sAppFile = appInfo.sourceDir;
	        return new File(sAppFile).lastModified();
	    }
	    } catch (NameNotFoundException e) {
	    //should never happen
	    return 0;
	    }
	}


    public static void saveMap(Context context, String key, Map<String,String> inputMap){
        SharedPreferences mSharedPref = ((FragmentActivity)context).getPreferences(Context.MODE_PRIVATE);
        if (mSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Editor editor = mSharedPref.edit();
            editor.remove(key).commit();
            editor.putString(key, jsonString);
            editor.commit();
        }
    }

    public static Map<String,String> loadMap(Context context, String key){
        Map<String,String> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = ((FragmentActivity)context).getPreferences(Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(key, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String k = keysItr.next();
                    String v = (String) jsonObject.get(k);
                    outputMap.put(k,v);
                }
            }
        }catch(Exception e){
        }
        return outputMap;
    }

}
