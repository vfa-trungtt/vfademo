package com.asai24.golf.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;


import com.asai24.golf.inputscore.ActivitySearchCourse;
import com.asai24.golf.utils.YgoLog;
import com.google.android.maps.MapView;

public class TapControlledMapView extends MapView implements OnGestureListener {

    private GestureDetector gd;    
    private OnSingleTapListener singleTapListener;
    private int curZoomLevel;
    private volatile boolean isStart = false;
    private volatile boolean isUp = false;
    private Context context;
    private CountDownTimer countDownTimer;
    private boolean isPressTwoFinger;
    private PointF point = new PointF(0.0f,0.0f);
	public TapControlledMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupGestures();
        this.context = context;
    }

    public TapControlledMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupGestures();
        this.context = context;
    }

    public TapControlledMapView(Context context, String apiKey) {
        super(context, apiKey);
        setupGestures();
        this.context = context;
    }
    
    private void setupGestures() {
    	gd = new GestureDetector(this);
        
        //set the on Double tap listener  
        gd.setOnDoubleTapListener(new OnDoubleTapListener() {

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (singleTapListener != null) {
					return singleTapListener.onSingleTap(e);
				} else {
					return false;
				}
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				isPressTwoFinger = true;
				point.set(0.0f, 0.0f);
				TapControlledMapView.this.getController().zoomInFixing((int) e.getX(), (int) e.getY());
				return false;
			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}
        	
        });
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (this.gd.onTouchEvent(ev)) {
			return true;
		} else {
			switch(ev.getAction()){
				case MotionEvent.ACTION_DOWN:
					isUp = false;
					if(ev.getPointerCount() > 1){
						isPressTwoFinger = true;
					}
					Log.d("THUNA", "onDown: isStart " + isStart);
					break;
				case MotionEvent.ACTION_MOVE:
					Log.d("THUNA", "On move");
					Log.d("THUNA", "pointer count is" + ev.getPointerCount());
					if(ev.getPointerCount() > 1){
						isPressTwoFinger = true;
						point.set(0.0f, 0.0f);
					}else{
						if(isPressTwoFinger){ // Lift a finger
							if(point.x == 0.0f && point.y == 0.0f){
								point.x = ev.getX();
								point.y = ev.getY();
							}
						}
					}
					Log.d("THUNA", "onMove: isStart " + isStart);
					break;
				case MotionEvent.ACTION_UP:
					Log.d("THUNA", "point.x =" + point.x + "point.y = " + point.y);
					if(point.x != 0.0f && point.y != 0.0f){
						float x = ev.getX();
						float y = ev.getY();
						if(Math.abs(x - point.x) > 10.0f || Math.abs(y - point.y) > 10.0f){
							isPressTwoFinger = false;
						}else{
							isPressTwoFinger = true;
						}
					}
					if(isPressTwoFinger == false){
						isStart = true;
					}
					point.set(0.0f, 0.0f);
					Log.d("THUNA", "onUp: isStart " + isStart);
					isUp = true;
					break;
			}
			Log.d("THUNA", "ev.getAction()= " + ev.getAction());
			return super.onTouchEvent(ev);
		}
			
	}
	
	public void setOnSingleTapListener(OnSingleTapListener singleTapListener) {
		this.singleTapListener = singleTapListener;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		curZoomLevel = getZoomLevel();
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
//			Log.d("THUNA", "Scroll");
//			Log.d("THUNA", "Distance X" + distanceX + "DIstanceY" + distanceY);
			if(countDownTimer != null){
				countDownTimer.cancel();
				countDownTimer = null;
			}
			
			countDownTimer = new CountDownTimer(1500, 100) {
				@Override
				public void onTick(long millisUntilFinished) {
					
				}
				
				@Override
				public void onFinish() {
					YgoLog.i("TapControlledMapView", "onFinish: isStart " + isStart);
					if(isStart && isUp){
						isStart = false;
						isPressTwoFinger = false;
						// Call api to load club object
						((ActivitySearchCourse) context).updateSearchClub();
					}
				}
			};
			countDownTimer.start();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		return false;
	}

}


