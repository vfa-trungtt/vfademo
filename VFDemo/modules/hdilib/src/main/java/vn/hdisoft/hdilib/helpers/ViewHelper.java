package vn.hdisoft.hdilib.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

public final class ViewHelper {

	
	public ViewHelper() {
	}

	
	@SuppressWarnings("unchecked")
	public static <V extends View> V findView(View view, int viewId) {
		return (V) view.findViewById(viewId);
	}

	
	@SuppressWarnings("unchecked")
	public static <V extends View> V findView(Activity activity, int viewId) {
		return (V) activity.findViewById(viewId);
	}

	public static void setOnClick(Activity activity, int id, OnClickListener onclick) {
		View v = findView(activity, id);
		if(v != null){
			v.setOnClickListener(onclick);
		}
	}

	public static void setOnClick(View view, int id, OnClickListener onclick) {
		View v = findView(view, id);
		if(v != null){
			v.setOnClickListener(onclick);
		}
	}
	
	public static void setText(View v, int id, int stringId) {
		((TextView) v.findViewById(id)).setText(stringId);
	}

	
	public static void setText(View v, int id, CharSequence text) {
		TextView txv = (TextView)v.findViewById(id);
		if(txv != null){
			txv.setText(text);
		}
	}
	
	public static void setText(Activity activity, int id, CharSequence text) {
		TextView txv = findView(activity, id);
		if(txv !=null){
			txv.setText(text);
		}
	}
	
	public static void setText(View v, int id, Spanned text) {
		TextView txv = (TextView)v.findViewById(id);
		if(txv !=null){
			txv.setText(text);
		}
	}
	
	public static String getText(View v, int id) {
		TextView tv = (TextView)v.findViewById(id);
		if(tv !=null){
			return tv.getText().toString().trim();
		}
		return "";
	}
	
	public static void setViewVisible(View v, int id, int visibility) {
		View view = v.findViewById(id);
		if(view != null){
			view.setVisibility(visibility);
		}		
	}

	
	public static void setImage(View v, int id, int drawableId) {
		((ImageView) v.findViewById(id)).setImageResource(drawableId);
	}

	public static void setImage(View v, int id, Bitmap bmp) {
		((ImageView) v.findViewById(id)).setImageBitmap(bmp);
	}

	
	public static void setImage(View v, int id, Drawable drawable) {
		
		((ImageView) v.findViewById(id)).setImageDrawable(drawable);
	}

	
	public static void setColor(View v, int id, int color) {
		((TextView) v.findViewById(id)).setTextColor(color);
	}
	
	public static void closeKeyboard(Context context, View v){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	public static void showKeyboard(Context context, View v){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		v.requestFocusFromTouch();
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static int getPixelFromDp(int dp){
		return 0;
	}

	public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

	public static float xxdpiTosp(Context context, float dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//		float px = dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
		float px = dp * (displayMetrics.xdpi / 442.451f);
		return px;
	}

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

	public static float getTextSizeFromPx(Context context, float sp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//		LogUtils.debug("dpi:"+displayMetrics.densityDpi);
//		float px = dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
		float px = sp * (displayMetrics.xdpi / 442.451f);
		return px;
	}

    public static View getViewFromLayoutResId(Context context, int layoutId){
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(layoutId, null);
        return v;
    }
}
