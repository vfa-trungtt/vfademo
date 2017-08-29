package com.asai24.golf.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asai24.golf.Constant;
import com.asai24.golf.GolfApplication;

import com.asai24.golf.db.GolfDatabase;
import com.asai24.golf.db.PlayerCursor;
import com.asai24.golf.inputscore.R;
import com.asai24.golf.utils.DateUtil;
import com.asai24.golf.utils.MathUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Distance {
	public static final double YARD_PER_METER = 1.0936133;
	public static final double METER_PER_YARD = 0.9144;

	private static final int CONNECT_TIMEOUT = 5000;
	private static final int TIMEOUT = 30000;

    private static final String[] JCOM_MODELS = {"LGT01", "LGS01"};

	public enum MEMBER_STATUS{
		FREE,
		SCORE_MONTHLY,
		TV_MONTHLY,
		TV_ANNUAL,
		SCORE_CAMPAIGN,
		TV_MONTHLY_CAMPAIGN,
		SCORE_AND_TV_CAMPAIGN
	}
	public static final void SetHeader(Context context,boolean backdisplay,boolean editdisplay,String titlestring)
	{
		try{
			View view = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
			Button mBack=(Button) view.findViewById(R.id.top_back_btn);
			mBack.setText(context.getString(R.string.back_to_score_enter));
			Button mEdit=(Button) view.findViewById(R.id.top_edit);
			mEdit.setText(context.getString(R.string.edit));
			TextView mTvTitle=(TextView) view.findViewById(R.id.top_title);
			mTvTitle.setText(titlestring);
			if(!backdisplay)
				mBack.setVisibility(View.GONE);
			if(!editdisplay)
				mEdit.setVisibility(View.GONE);
		}
		catch (Exception e) {
		}
	}
	//Using for set title of header
	public static final void SetHeaderTitle(Context context, String titlestring)
	{
		try{

			View view = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
			TextView mTvTitle=(TextView) view.findViewById(R.id.top_title);
			mTvTitle.setText(titlestring);

		}
		catch (Exception e) {
		}
	}

    public static boolean isSmallDevice(Context context) {

        boolean isSmallSize = context.getResources().getBoolean(R.bool.is_small_size);
        if (isSmallSize) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            if (metrics.densityDpi < 200) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

	public static enum Unit {
		METER, YARD,
	}

	public static int convertMeterToYard(double meter) {
		return (int) (meter * YARD_PER_METER);
	}

	public static double convertYardToMeterDouble(double yard) {
		return yard * METER_PER_YARD;
	}

	public static int convertMeterToYard(int meter) {
		return (int) MathUtil.RoundNumber(meter * YARD_PER_METER);
	}

	public static int convertYardToMeter(int yard) {
		return (int) MathUtil.RoundNumber(yard * METER_PER_YARD);
	}
	public  static void SetPreferHistory( Context context, boolean value)
	{
		SharedPreferences mPrefer =PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = mPrefer.edit();
		editor.putBoolean(context.getString(R.string.pref_score_detail_history), value);
		editor.commit();
	}
	public  static boolean GetPreferHistory( Context context)
	{
		SharedPreferences mPrefer =PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefer.getBoolean(context.getString(R.string.pref_score_detail_history), false);
	}
	public static void saveYourGolfAccountToken(Context context, String token){
//		final SharedPreferences pref = GolfApplication.getPreferences();
//		 pref.edit().putString(context.getString(R.string.yourgolf_account_auth_token_key), token).commit();
	}

	/**
	 * Check login yourgolf account
	 * @param context
	 * @return
	 */
	public  static boolean checkLoginYourgolfAccount( Context context)
	{
//		final SharedPreferences pref = GolfApplication.getPreferences();
//		final String yourgolfToken = pref.getString(context.getString(R.string.yourgolf_account_auth_token_key), "");
//		if(yourgolfToken==null || yourgolfToken.equals("")){	//YourGolfアカウント未設定
//			return false;
//		}
		return true;
	}
	public  static String getAuthTokenLogin( Context context){
//		final SharedPreferences pref = GolfApplication.getPreferences();
//		return pref.getString(context.getString(R.string.yourgolf_account_auth_token_key), "").trim();
        return "";
	}
	/**
	 * Check login gora account
	 */
	public static boolean checkLoginGoraAccount(Context context){
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		final String goraToken = pref.getString(context.getString(R.string.gora_account_auth_token_key), "");
		if(goraToken == null || goraToken.equals("")){
			return false;
		}
		return true;
	}
	/**
	 * Check oob account
	 * @return
	 */
	public  static boolean checkOobAccount()
	{

		//check oobgolf account
//		final String username = GolfApplication.getOobUserName();
//		final String password = GolfApplication.getOobPassword();
//		if (username.length() == 0 || password.length() == 0) {
//			return false;
//		}
		return true;
	}

	/**
	 * Set save profile account
	 *
	 * @param context
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @param birthday
	 */
	public  static void setPreferProfile( Context context, String firstName, String lastName,
												String gender, String birthday, String idServer)
	{
		SharedPreferences mPrefer =PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = mPrefer.edit();
		editor.putString(context.getString(R.string.pref_profile_firstname), firstName);
		editor.putString(context.getString(R.string.pref_profile_lastname), lastName);
		editor.putString(context.getString(R.string.pref_profile_gender), gender);
		editor.putString(context.getString(R.string.pref_profile_birthday), birthday);
		editor.putString(context.getString(R.string.pref_profile_id_server), idServer);
		editor.commit();

        if(null != idServer && !idServer.equals("")){
            GolfDatabase db = GolfDatabase.getInstance(context);
            PlayerCursor c = db.getOwner();
            if(null != c && c.getCount() > 0){
                db.updatePlayerServer(c.getId(), idServer);
                c.close();
            }
        }
	}

    public  static void setPreferProfile( Context context, String firstName, String lastName,
                                          String gender, String birthday, String country, String prefecture, String paymentCouponSegment, int memberStatus, String purchase_status)
    {
        if (firstName == null) {
            firstName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        if (gender == null) {
            gender = "";
        }
        if (birthday == null) {
            birthday = "";
        }
        if (country == null) {
            country = "";
        }
        if (prefecture == null) {
            prefecture = "";
        }
        if (paymentCouponSegment == null) {
            paymentCouponSegment = "";
        }

        SharedPreferences mPrefer =PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = mPrefer.edit();
        editor.putString(context.getString(R.string.pref_profile_firstname), firstName);
        editor.putString(context.getString(R.string.pref_profile_lastname), lastName);
        editor.putString(context.getString(R.string.pref_profile_gender), gender);
        editor.putString(context.getString(R.string.pref_profile_birthday), birthday);
        editor.putString(context.getString(R.string.pref_profile_country), country);
        editor.putString(context.getString(R.string.pref_profile_prefecture), prefecture);
        editor.putString(context.getString(R.string.pref_payment_coupon_segement), paymentCouponSegment);
        editor.putInt(context.getString(R.string.pref_profile_member_status), memberStatus);
		editor.putString(context.getString(R.string.pref_payment_purchase_status),purchase_status);
        editor.commit();

    }

	public static void savePurchaseStatus(Context context,String purchase_status){
		SharedPreferences mPrefer =PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = mPrefer.edit();
		editor.putString(context.getString(R.string.pref_payment_purchase_status),purchase_status);
		editor.commit();
	}


    public static void clearUserInfo(Context context) {
        SharedPreferences mPrefer = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = mPrefer.edit();
        editor.putString(context.getString(R.string.pref_profile_firstname), "");
        editor.putString(context.getString(R.string.pref_profile_lastname), "");
        editor.putString(context.getString(R.string.pref_profile_gender), "");
        editor.putString(context.getString(R.string.pref_profile_birthday), "");
        editor.putString(context.getString(R.string.pref_profile_country), "");
        editor.putString(context.getString(R.string.pref_profile_prefecture), "");
        editor.putString(context.getString(R.string.pref_payment_coupon_segement), "");
        editor.putString(context.getString(R.string.pref_profile_id_server), "");
		editor.putString(context.getString(R.string.pref_payment_purchase_status),"");
        editor.commit();
    }

	public static String GetProfileItem(Context context,String key)
	{
		SharedPreferences mPrefer = PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefer.getString(key, "");
	}

    public static boolean isUserInJapan(Context context) {

        SharedPreferences mPrefer = PreferenceManager.getDefaultSharedPreferences(context);
        String country = mPrefer.getString(context.getString(R.string.pref_profile_country), "");
        if (context.getString(R.string.country_japan_value).equals(country)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDonNotShowGolfDayMessage(Context context) {

        SharedPreferences mPrefer = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefer.getBoolean(context.getString(R.string.user_setting_no_show_golf_day_message), false);
    }

    public static void markDonNotShowGolfDayMessage(Context context) {

        SharedPreferences mPrefer =PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = mPrefer.edit();
        editor.putBoolean(context.getString(R.string.user_setting_no_show_golf_day_message), true);
        editor.commit();
    }

    public static int getMemberStatus(Context context)
    {
        SharedPreferences mPrefer = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefer.getInt(context.getString(R.string.pref_profile_member_status), 0);
    }
    public static String getMemberType(Context context){
		SharedPreferences mPrefer = PreferenceManager.getDefaultSharedPreferences(context);
		return mPrefer.getString(context.getString(R.string.pref_payment_purchase_status), Constant.PURCHASE_STATUS_NULL);

	}
	public static MEMBER_STATUS getMemberType(Context context,String scoreExpiredDate,String tvExpiredDate)
	{
		SharedPreferences mPrefer = PreferenceManager.getDefaultSharedPreferences(context);
		String result = mPrefer.getString(context.getString(R.string.pref_payment_purchase_status), Constant.PURCHASE_STATUS_NULL);
		MEMBER_STATUS memberType = MEMBER_STATUS.FREE;
		if(result.contains("GN005")) {
			memberType = MEMBER_STATUS.TV_ANNUAL;
		}else if(result.contains("GN003") && result.contains("GN002")){
			if(result.contains("GN003")){
				memberType =  MEMBER_STATUS.TV_MONTHLY;
			}else {
				memberType =  MEMBER_STATUS.SCORE_MONTHLY;
			}
			if(!TextUtils.isEmpty(tvExpiredDate) && Constant.isSupportTvTrialCampaign){
				Date mTvExpired = DateUtil.pareStringToDate(tvExpiredDate);
				// If tvexpired is valid, so user is tv campaign user.
				if(DateUtil.getCurrentDate().before(mTvExpired)){
					memberType = MEMBER_STATUS.TV_MONTHLY_CAMPAIGN;
				}
			}
			if(!TextUtils.isEmpty(scoreExpiredDate) && Constant.isSupportScoreTrialCampaign) {
				Date mScoreExpired = DateUtil.pareStringToDate(scoreExpiredDate);
				// If tvexpired is valid, so user is tv campaign user.
				if (DateUtil.getCurrentDate().before(mScoreExpired)) {
					if(memberType == MEMBER_STATUS.TV_MONTHLY_CAMPAIGN){
						memberType = MEMBER_STATUS.SCORE_AND_TV_CAMPAIGN;
					}else{
						memberType = MEMBER_STATUS.SCORE_CAMPAIGN;
					}
				}
			}
		}else if(result.contains("GN003")){
			memberType =  MEMBER_STATUS.TV_MONTHLY;
			if(!TextUtils.isEmpty(tvExpiredDate) && Constant.isSupportTvTrialCampaign){
				Date mTvExpired = DateUtil.pareStringToDate(tvExpiredDate);
				// If tvexpired is valid, so user is tv campaign user.
				if(DateUtil.getCurrentDate().before(mTvExpired)){
					memberType = MEMBER_STATUS.TV_MONTHLY_CAMPAIGN;
				}
			}
		}else if(result.contains("GN002")) {
			memberType =  MEMBER_STATUS.SCORE_MONTHLY;
			if(!TextUtils.isEmpty(scoreExpiredDate) && Constant.isSupportScoreTrialCampaign) {
				Date mScoreExpired = DateUtil.pareStringToDate(scoreExpiredDate);
				// If tvexpired is valid, so user is tv campaign user.
				if (DateUtil.getCurrentDate().before(mScoreExpired)) {
					memberType = MEMBER_STATUS.SCORE_CAMPAIGN;
				}
			}
		}else if(result.contains(Constant.PURCHASE_STATUS_NULL)) {
			memberType =  MEMBER_STATUS.FREE;
		}

		return memberType;
	}

	/**
     * Recycle the ImageView within Bitmap
     * @param iv the ImageView needs to be recycled
     */
	public static void recycleImageView(ImageView iv)
	{
		Drawable drawable = iv.getDrawable();
		if (drawable instanceof StateListDrawable) {
			Drawable bitmapDrawable = ((StateListDrawable)drawable).getCurrent();
			if(bitmapDrawable instanceof BitmapDrawable) {
				Bitmap bitmap = ((BitmapDrawable) bitmapDrawable).getBitmap();
				bitmap.recycle();
				bitmap = null;
				iv.setImageBitmap(null);
				iv.setImageDrawable(null);
			}
		}
	}

	/**
	 * write File to internal storage
	 *
	 * @param context
	 * @param fileName
	 * @param data
	 *
	 * @return boolean : true is write file success
	 *
	 */
	public static synchronized boolean writeFileToInternal(Context context,
												String fileName,
												Object data){
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(data);
			os.close();

			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
		catch (IOException e) {
			return false;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * get object data from file
	 *
	 *  @param context
	 *  @param fileName
	 *
	 * @return object from file
	 */
	public static Object getObjFromInternalFile(Context context, String fileName) {
		try {
			InputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			Object result = is.readObject();
			is.close();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Delete cache total history file
	 *
	 * @param mContext
	 * @param fileName
	 */
	public static void deleteFileFromInternal(Context mContext, String fileName) {
		try {
			mContext.deleteFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setDefaultClub(Context mContext) {

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

		String[] clubNames = mContext.getResources().getStringArray(R.array.club_name);
		String[] clubNamesDef = mContext.getResources().getStringArray(R.array.club_name_default);

		HashMap<String, String> hashCLubNameDef = new HashMap<String, String>();
		for (String clubNameDef : clubNamesDef) {
			hashCLubNameDef.put(clubNameDef, clubNameDef);
		}

		Editor editor = pref.edit();

		for (String clubName : clubNames) {
			if(hashCLubNameDef.containsKey(clubName)) {
				editor.putBoolean(clubName, true);
			}
			else {
				editor.putBoolean(clubName, false);
			}
			editor.commit();
		}
	}

	public static int countClubSelected(Context mContext) {
		int count = 0;
		String[] clubNames = mContext.getResources().getStringArray(R.array.club_name);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		for (String clubName : clubNames) {
			if(pref.getBoolean(clubName, false)){
				count++;
			}
		}

		return count;
	}

    public static boolean checkFirstOpenApp(Context context){
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(Constant.FLAG_OPEN_WALKTHROUGH, false);
    }

    public static boolean isJcomDevice() {
        String model = Build.MODEL;
        // YgoLog.d(TAG, "isJcomDevice: " + model);
        if (model == null) {
            return false;
        }
        return Arrays.asList(JCOM_MODELS).contains(model);
    }
}
