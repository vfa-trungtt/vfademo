package com.imanoweb.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DayContainerView extends LinearLayout {

	Paint paint;
	float height = 50;
	float width = 50;
	
	public DayContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		intLayout();
	}

	public DayContainerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		intLayout();
	}
	
	public void intLayout(){
		paint = new Paint();
		paint.setColor(Color.GRAY);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(changed){
			height 	= getHeight()/6;
			width	= getWidth()/7;
			invalidate();
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
	}

}
