package com.asai24.golf.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.asai24.golf.Constant.ErrorServer;


import com.asai24.golf.GolfApplication;
import com.asai24.golf.db.GolfDatabase;
import com.asai24.golf.inputscore.R;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Akira Sosa
 * Utility methods related context
 */
public class ContextUtil {
	private Context context;

	public ContextUtil(Context context) {
		this.context = context.getApplicationContext();
	}

	public String getAuthToken() {
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		return pref.getString(context.getString(R.string.yourgolf_account_auth_token_key), "").trim();
	}

	public void handleErrorStatus(ErrorServer errorStatus) {
        int resId = 0;
        if(errorStatus == ErrorServer.ERROR_E0103){
            resId = R.string.yourgolf_account_auth_token_e0103;
        }
        else if(errorStatus == ErrorServer.ERROR_E0153){
            resId = R.string.Login_Error_E0153;
        }
        else {
            String resIdStr = errorStatus.toString().toLowerCase();
            resId = context.getResources().getIdentifier(resIdStr, "string", context.getPackageName());
        }

		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}
	
	public Intent createIntentForSendDbfileByEmail() {
		try {
			String srcFileName = GolfDatabase.getInstance(context).getDatabaseName();
			String srcFilePath = context.getDatabasePath(srcFileName).getAbsolutePath();

			String dstFileName = ".material";
			String dstFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/yourgolf/"+dstFileName;

			FileUtil.encryptFile(srcFilePath, dstFilePath);
			
			final Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("file/*");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(Intent.EXTRA_SUBJECT, "round status");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.mail_your_golf)});
			intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + dstFilePath));
			intent.putExtra(Intent.EXTRA_TEXT, "");
			
			return intent;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    public String getContentMail(){
        String info, account, version;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        account = pref.getString(context.getString(R.string.yourgolf_account_username_key), "");
        if(GolfApplication.isPuma())
            version = GolfApplication.getVersionName()+"p";
        else
            version = GolfApplication.getVersionName();

        TimeZone tz = TimeZone.getDefault();

        info = "Account ID (Email): " + account + "\n"
                + "App Version: " + version + "\n"
                + "OS Version: " + Build.VERSION.RELEASE + "\n"
                + "Device: " + getDeviceName() +"\n"
                + "TimeStamp: " + System.currentTimeMillis()+ "\n"
                + "TimeZone: " + tz.getDisplayName() + " " + tz.getDisplayName(false, TimeZone.SHORT,  Locale.getDefault()) + "\n\n";
        return info;
    }

    public String getContentMail(String keySearch){
        String info, account, version;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        account = pref.getString(context.getString(R.string.yourgolf_account_username_key), "");
        if(GolfApplication.isPuma())
            version = GolfApplication.getVersionName()+"p";
        else
            version =GolfApplication.getVersionName();

        TimeZone tz = TimeZone.getDefault();

        info = "Account ID (Email): " + account + "\n"
                + "App Version: " + version + "\n"
                + "OS Version: " + Build.VERSION.RELEASE + "\n"
                + "Device: " + getDeviceName() +"\n"
                + "TimeStamp: " + System.currentTimeMillis() + "\n"
                + "TimeZone: " + tz.getDisplayName() + " " + tz.getDisplayName(false, TimeZone.SHORT,  Locale.getDefault()) + "\n"
                + "Search Keywords: " + keySearch + "\n\n";
        return info;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        YgoLog.i("ContextUtil", "getDeviceName manuf" + manufacturer + "; model: " + model);
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


}
