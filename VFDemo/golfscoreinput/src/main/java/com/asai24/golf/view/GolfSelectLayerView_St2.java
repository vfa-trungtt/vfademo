package com.asai24.golf.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.asai24.golf.GolfApplication;

import com.asai24.golf.inputscore.R;
import com.asai24.golf.utils.BitmapUtil;

public class GolfSelectLayerView_St2 extends RelativeLayout {

    private int mHeight = 0;
	private Bitmap mBitmapNavRight;

	public GolfSelectLayerView_St2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.selectplayers_edit_st2, this);

		float density = getResources().getDisplayMetrics().density;
        if(GolfApplication.isPuma())
            mBitmapNavRight = BitmapUtil.decodeSampledBitmapFromResource2(getResources(), R.drawable.nav_background_right, (int)(100 * density), (int)(50 * density));
        else
            mBitmapNavRight = BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.nav_background_right, (int)(100 * density), (int)(50 * density));

        //CanNC - Support Puma - 2013-12-21
        if (GolfApplication.isPuma() == false) {
		    ((LinearLayout) findViewById(R.id.step_bar_layout)).setBackgroundDrawable(new BitmapDrawable(mBitmapNavRight));
        }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mHeight == 0)
			mHeight = heightMeasureSpec;

		super.onMeasure(widthMeasureSpec, mHeight);

	}

	public void destroy(){
		if(mBitmapNavRight != null){
			mBitmapNavRight.recycle();
			mBitmapNavRight = null;
		}
	}
}
