package com.asai24.golf.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

//import com.loopj.android.image.SmartImageView;

public class CleanUpUtil {

	public static final void cleanupView(View view) {
		try{
//            YgoLog.i("CleanUpUtil","cleanupView");
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageDrawable(null);
            } else if (view instanceof ImageView) {
                /*ThuNA 2013/03/11 ADD-S*/
                ((ImageView) view).setImageDrawable(null);
                ((ImageView) view).setImageBitmap(null);
                ((ImageView) view).setBackgroundDrawable(null);
                recycle((ImageView)view);
                //recycle(((ImageView) view));
                /*ThuNA 2013/03/11 ADD-S*/
//            } else if (view instanceof SmartImageView) {
                ((ImageView) view).setImageDrawable(null);
                /*ThuNA 2013/03/11 ADD-S*/
                //recycle(((ImageView) view));
                /*ThuNA 2013/03/11 ADD-S*/
            } else if (view instanceof SeekBar) {
                final SeekBar sb = (SeekBar) view;
                sb.setProgressDrawable(null);
                sb.setThumb(null);
            }
//            if (view!=null && view.getBackground() != null) {
//
//                ((BitmapDrawable)view.getBackground()).getBitmap().recycle();
//                view.getBackground().setCallback(null);
//                view.setBackgroundDrawable(null);
//                view=null;
//            }

            if (view != null) {
                view.setBackgroundDrawable(null);
                // CongVC 2012-08-31
                if (view.getBackground() != null) {
                    view.getBackground().setCallback(null);
                }

            }
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int size = vg.getChildCount();
                for (int i = 0; i < size; i++) {
                    cleanupView(vg.getChildAt(i));
                }
            }
        }catch(Exception e){}
	}
	public static final void cleanupView1(View view) {
		try{
//            YgoLog.i("CleanUpUtil","cleanupView1");
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageDrawable(null);
            } else if (view instanceof ImageView) {
                /*ThuNA 2013/03/11 ADD-S*/
                recycle(((ImageView) view));
                /*ThuNA 2013/03/11 ADD-S*/
//            } else if (view instanceof SmartImageView) {
                /*ThuNA 2013/03/11 ADD-S*/
//                recycle(((ImageView) view));
                /*ThuNA 2013/03/11 ADD-S*/
            } else if (view instanceof SeekBar) {
                final SeekBar sb = (SeekBar) view;
                sb.setProgressDrawable(null);
                sb.setThumb(null);
            }

//            if (view!=null && view.getBackground() != null) {
//
//                ((BitmapDrawable)view.getBackground()).getBitmap().recycle();
//                view.getBackground().setCallback(null);
//                view.setBackgroundDrawable(null);
//                view=null;
//            }

            if (view != null) {
                view.setBackgroundDrawable(null);

                // CongVC 2012-08-31
                if (view.getBackground() != null) {
                    view.getBackground().setCallback(null);
                }
            }
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int size = vg.getChildCount();
                for (int i = 0; i < size; i++) {
                    cleanupView(vg.getChildAt(i));
                }
            } else {
                view = null;
            }
        }catch(Exception e){}
	}
	public static final void cleanupView2(View view) {
		Drawable drawble = view.getBackground();
		if(drawble != null && drawble instanceof BitmapDrawable){
			Bitmap bitmap = ((BitmapDrawable) drawble).getBitmap();
			if (bitmap != null)
				bitmap.recycle();
			bitmap = null;
			YgoLog.d("CleanUpUtil", "++++++++++ Recycled 2 ++++++++++++++");
		}
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			int size = vg.getChildCount();
			for (int i = 0; i < size; i++) {
				cleanupView(vg.getChildAt(i));
			}
		} else {
			view = null;
		}
	}
	/*ThuNA 2013/03/11 ADD-S*/
	// Add method to recycle a image view
	private static void recycle(ImageView iv) {
		Drawable drawable = iv.getDrawable();
		if (drawable instanceof BitmapDrawable) {

			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			iv.setImageBitmap(null);
			iv.setImageDrawable(null);
			//iv.postInvalidate();
			
			if (bitmap != null)
				bitmap.recycle();
			bitmap = null;		
		}
		
	}
	/*ThuNA 2013/03/11 ADD-S*/

}
