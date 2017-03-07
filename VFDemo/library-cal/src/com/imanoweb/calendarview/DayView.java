package com.imanoweb.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class DayView extends TextView {
	
	Paint paint;
	Paint paintGrid;
	Paint paintBitmap;
	
    private Date date;
    private List<DayDecorator> decorators;
    private int selectedColor;
    
    private int Color1 = Color.TRANSPARENT;
    private int Color2 = Color.TRANSPARENT;
    
    private Rect colorRect1;
    private Rect bmpSrcRect1;
    private Rect bmpDstRect1;        
    private Bitmap bitmap1st;
    private Bitmap bitmap1stLarge;
    
    private Rect colorRect2;
    private Rect bmpSrcRect2;
    private Rect bmpDstRect2;
    private Bitmap bitmapsnd;
    private Bitmap bitmapsndLarge;
    
    private int colorWidth = 10;
    ////////
    private int countNumber;
    private RectF countRect;
    
    private Rect textBounds = new Rect();
    private boolean isSmallSize;
//    private int smallHeight = 0;
//    private int largeHeight = 0;
    
    public DayView(Context context) {
        this(context, null, 0);
        initLayout();
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initLayout();        
    }
    
//    
    public void setBitmap(Bitmap bmp,int color){
    	if(countNumber == 0){
//    		bitmap1st = bmp;
        	if(bmp == null){
        		Log.e("TrungTT", "NULLLL IMAGE");
        	}else{
        		bitmap1st = bmp;
//        		bitmap1st = getResizedBitmapByHeight(bmp, 100);
//        		bitmap1stLarge = getResizedBitmapByHeight(bmp, 200);
        		
        		bmpSrcRect1 = new Rect(0, 0, bitmap1st.getWidth(), bitmap1st.getHeight());
//        		colorRect1 	= new Rect(0, 0, bitmap1st.getWidth(), bitmap1st.getHeight());
        		Color1 		= color;
//        		getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), textBounds);
//            	bmpDstRect1.top		= textBounds.height() + 10;
//        		bitmap1stLarge = getResizedBitmapByHeight(bitmap1st, bmpDstRect1.height());
        	}
    	}
    	if(countNumber == 1){
//    		bitmapsnd = bmp;
        	if(bmp == null){
        		Log.e("TrungTT", "NULLLL IMAGE");
        	}else{
//        		bitmapsnd = getResizedBitmapByHeight(bmp, 100);
        		bitmapsnd = bmp;
        		bmpSrcRect2 = new Rect(0, 0, bitmapsnd.getWidth(), bitmapsnd.getHeight());
//        		colorRect2 = new Rect(0, 0, bitmapsnd.getWidth(), bitmapsnd.getHeight());
        		Color2 		= color;
//        		bitmapsndLarge = getResizedBitmapByHeight(bitmapsnd, bmpDstRect2.height());
        	}
        	
    	}
    	
    	countNumber++;
//    	invalidate();
    }
    
    boolean needAnimation = true;
    
    public void setBitmap(Bitmap bmp){
    	if(bitmap1st != null && bitmapsnd != null){
    		needAnimation = false;
    	}
    	
    	if(countNumber == 0){
        	if(bmp == null){
        		Log.e("TrungTT", "NULLLL IMAGE");        		
        	}else{
        		bitmap1st = bmp;
        		bmpSrcRect1 = new Rect(0, 0, bitmap1st.getWidth(), bitmap1st.getHeight());
        	}
    	}
    	if(countNumber == 1){
        	if(bmp == null){
        		Log.e("TrungTT", "NULLLL IMAGE");
        	}else{
        		bitmapsnd = bmp;
        		bmpSrcRect2 = new Rect(0, 0, bitmapsnd.getWidth(), bitmapsnd.getHeight());
        	}
    	}
    	
    	countNumber++;
    	if(needAnimation){
    		paintBitmap.setAlpha(50);
    		faceInBitmap();
    	}
//    	postInvalidate();
    }
    
    public void setBitmap1st(Bitmap bmp){
    	if(bitmap1st != null){
    		needAnimation = false;
    	}else{
    		paintBitmap.setAlpha(0);
    	}
    	
    	if(bmp == null){
    		Log.e("TrungTT", "NULLLL IMAGE");        		
    	}else{
    		bitmap1st = bmp;
    		bmpSrcRect1 = new Rect(0, 0, bitmap1st.getWidth(), bitmap1st.getHeight());    		
    	}
    	    	    	
    	if(needAnimation){
    		faceInBitmap();
    	}
    	
    }
    
    public void setBitmap2nd(Bitmap bmp){
    	if(bitmapsnd != null){
    		needAnimation = false;
    	}else{
    		paintBitmap.setAlpha(0);
    	}
    	
    	if(bmp == null){
    		Log.e("TrungTT", "NULLLL IMAGE");        		
    	}else{
    		bitmapsnd = bmp;
    		bmpSrcRect2 = new Rect(0, 0, bitmapsnd.getWidth(), bitmapsnd.getHeight());    		
    	}
    	    	    	
    	if(needAnimation){
    		faceInBitmap();
    	}
    	
    }
    
    private void faceInBitmap(){
    	Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long last = System.currentTimeMillis();
				long duration = 0;
				int alpha = 0;
				postInvalidate();
				
				while(duration < 500){
					long current = System.currentTimeMillis();
					duration = current - last;
					alpha += 40;
										
					paintBitmap.setAlpha(alpha);
					postInvalidate();
					try {
						
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				paintBitmap.setAlpha(255);
				postInvalidate();
				
			}
		});
    	th.start();
    }
    
    public void setColor(int color){
    	if(countNumber == 0){
    		Color1 		= color;
    	}
    	if(countNumber == 1){
        	Color2 		= color;
//        	countNumber = 0;
    	}
    	
    	countNumber++;
    }
    
    public void setColor1st(int color){
    	Color1 		= color;    	
    }
    
    public void setColor2nd(int color){
    	Color2 		= color;    	
    }
    
    public void setCountNumber(int value){
    	countNumber = value;    	
//    	invalidate();
    }

    @SuppressLint("NewApi") public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (isInEditMode())
                return;
        }
        initLayout();
    }
    
    private void initLayout(){
    	paintGrid = new Paint();
    	paintGrid.setStyle(Style.STROKE);
    	paintGrid.setColor(Color.GRAY);
    	
    	paintBitmap = new Paint();
    	
    	paint = new Paint();
    	
        paint.setColor(Color.parseColor("#ff726b"));
        selectedColor = Color.parseColor("#ff726b");
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3.5f);
        paint.setAntiAlias(true);
        
        countRect = new RectF(60, 2, getWidth()-6, 48);
        
        bmpDstRect1 = new Rect(2, textBounds.bottom + 4, getWidth() - 8, getHeight()- 10);
        bmpDstRect2 = new Rect(2, bmpDstRect1.bottom + 1, getWidth() - 8, getHeight()- 10);
        
        colorRect1 = new Rect();
        colorRect2 = new Rect();
        
//        Color1 = Color.YELLOW;
//        Color2 = Color.CYAN;
        
        setPadding(5, 0, 0, 0);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textsize_day));

//        setTextSize(getResources().getDimension(R.dimen.textsize_day));
    }

    public void bind(Date date, List<DayDecorator> decorators) {
        this.date = date;
        this.decorators = decorators;

        final SimpleDateFormat df = new SimpleDateFormat("d");
        int day = Integer.parseInt(df.format(date));
        setText(String.valueOf(day));
    }

    public void decorate() {
        //Set custom decorators
        if (null != decorators) {
            for (DayDecorator decorator : decorators) {
                decorator.decorate(this);
            }
        }
    }

    public Date getDate() {
        return date;
    }
    
    private int lastHeight = 0;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
    		int bottom) {
    	// TODO Auto-generated method stub
    	super.onLayout(changed, left, top, right, bottom);
    	if(changed){
    		
    		getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), textBounds);
    		bmpDstRect1.top		= textBounds.height() + 10 + 3;
    		bmpDstRect1.right 	= getWidth();
    		bmpDstRect1.bottom	= (getHeight() - bmpDstRect1.top)/2 + bmpDstRect1.top;
    		
    		colorRect1.top 		= bmpDstRect1.top;
    		colorRect1.left		= 0;
    		colorRect1.right 	= colorRect1.left + colorWidth;
    		colorRect1.bottom 	= bmpDstRect1.bottom;
    		
    		
    		bmpDstRect2.top 	= bmpDstRect1.bottom;
    		bmpDstRect2.right 	= getWidth();
    		bmpDstRect2.bottom	= getHeight(); 
    		
    		colorRect2.top 		= bmpDstRect2.top;
    		colorRect2.left		= 0;
    		colorRect2.right 	= colorRect2.left + colorWidth;
    		colorRect2.bottom 	= bmpDstRect2.bottom;
    		
    		getPaint().getTextBounds("2", 0, 1, textBounds);
    		countRect.left	= getWidth() - textBounds.width()*3 - 10;
    		countRect.right	= getWidth() - 6;
    		countRect.bottom= countRect.top + textBounds.height() + 5;
    		if(getHeight()*5 > getContext().getResources().getDisplayMetrics().heightPixels/2){
    			isSmallSize = false;
    		}else{
    			isSmallSize = true;
    		}
    		
    	}
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	paint.setStyle(Style.STROKE);
    	getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), textBounds);
    	
    	bmpDstRect1.top		= textBounds.height() + 10 + 2;
    	
    	paint.setColor(Color1);
		paint.setStyle(Style.FILL);
		
		drawOtayori1(canvas);
    	drawOtayori2(canvas);
    	
		
    	if(countNumber >= 3){
    		paint.setColor(selectedColor);
        	paint.setStyle(Style.FILL);
        	canvas.drawRoundRect(countRect, 5, 5, paint);
        	
    		paint.setColor(Color.WHITE);
        	paint.setTextSize(getTextSize() - 5);
        	String text = countNumber + "件";
        	float w = paint.measureText(text);
        	Rect countBounds = new Rect();
        	paint.getTextBounds(text, 0, text.length(), countBounds);
        	canvas.drawText(text, countRect.right - w - 5, countRect.centerY() + countBounds.height()/2, paint);
    	}
    	
    	paint.setStyle(Style.STROKE);
    	if(isSelected()){
    		paint.setColor(selectedColor);
    	}else{
    		paint.setColor(Color.TRANSPARENT);
    	}
    	
    	
    	canvas.drawRect(2, 2, getWidth() -2, getHeight() - 2, paint);
    	canvas.drawRect(0, 0, getWidth() , getHeight() , paintGrid);
    	super.onDraw(canvas);
    }
    
    private void drawOtayori1(Canvas canvas){
    	
    	if(countNumber >= 1) {
    		
    		canvas.drawRect(bmpDstRect1, paint);
    		if(bitmap1st != null){
        		colorRect1.top = bmpDstRect1.top;
        		if(isSmallSize){
        			canvas.drawBitmap(bitmap1st, bmpSrcRect1, bmpDstRect1, paintBitmap);
        		}else{
        			bitmap1stLarge = getResizedBitmapByHeight(bitmap1st, bmpDstRect1.height());
        			canvas.drawBitmap(bitmap1stLarge, bmpDstRect1.left, bmpDstRect1.top, paintBitmap);
        		}
        	}else{
//        		canvas.drawRect(bmpDstRect1, paint);
        	}
    		canvas.drawRect(colorRect1, paint);
    	}
    	
    	
    	
    	
    }
    
    private void drawOtayori2(Canvas canvas){
    	
    	if(countNumber >=2) {
    		paint.setColor(Color2);
    		paint.setStyle(Style.FILL);
    		
    		
        	canvas.drawRect(bmpDstRect2, paint);
        	if(bitmapsnd != null){
        		if(isSmallSize){
        			canvas.drawBitmap(bitmapsnd, bmpSrcRect2, bmpDstRect2, paintBitmap);
        		}else{
        			bitmapsndLarge = getResizedBitmapByHeight(bitmapsnd, bmpDstRect2.height());
        			canvas.drawBitmap(bitmapsndLarge, bmpDstRect2.left, bmpDstRect2.top, paintBitmap);
        		}
        	}else{
//        		canvas.drawRect(bmpDstRect2, paint);
        	}
        	canvas.drawRect(colorRect2, paint);
    	}
    	
    	
    	
    }
    
    @Override
    protected void onDetachedFromWindow() {
    	// TODO Auto-generated method stub
    	super.onDetachedFromWindow();
//    @Override
//    protected void onDetachedFromWindow() {
//    	// TODO Auto-generated method stub
//    	super.onDetachedFromWindow();
//    	if(bitmap1st != null){
//    		bitmap1st.recycle();
//    		bitmap1st = null;    		
//    	}
//    	if(bitmapsnd != null){
//    		bitmapsnd.recycle();
//    		bitmapsnd = null;    		
//    	}
    }
    
//    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
//	    int width = bm.getWidth();
//	    int height = bm.getHeight();
//	    float scaleWidth = ((float) newWidth) / width;
//	    float scaleHeight = ((float) newHeight) / height;
//	    // CREATE A MATRIX FOR THE MANIPULATION
//	    Matrix matrix = new Matrix();
//	    // RESIZE THE BIT MAP
//	    matrix.postScale(scaleWidth, scaleHeight);
//
//	    // "RECREATE" THE NEW BITMAP
//	    Bitmap resizedBitmap = Bitmap.createBitmap(
//	        bm, 0, 0, width, height, matrix, false);
//	    bm.recycle();
//	    return resizedBitmap;
//	}
//    
//    public Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
//	    int width = bm.getWidth();
//	    int height = bm.getHeight();
//	    int newHeight = bm.getHeight()*newWidth/bm.getWidth();
//	    
////	    int newHeight = bm.getHeight()/newWidth/bm.getWidth();
//	    
//	    float scaleWidth = ((float) newWidth) / width;
//	    float scaleHeight = ((float) newHeight) / height;
//	    // CREATE A MATRIX FOR THE MANIPULATION
//	    Matrix matrix = new Matrix();
//	    // RESIZE THE BIT MAP
//	    matrix.postScale(scaleWidth, scaleHeight);
//
//	    // "RECREATE" THE NEW BITMAP
//	    Bitmap resizedBitmap = Bitmap.createBitmap(
//	        bm, 0, 0, width, height, matrix, false);
//	    bm.recycle();
//	    return resizedBitmap;
//	}
    public Bitmap getResizedBitmapByHeight(Bitmap bm, int newHeight) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    int newWidth = bm.getWidth()*newHeight/bm.getHeight();
	    
//	    int newHeight = bm.getHeight()/newWidth/bm.getWidth();
	    
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(
	        bm, 0, 0, width, height, matrix, false);
//	    bm.recycle();
	    return resizedBitmap;
	}
    
    public void cleanData(){
//    	Color1 = Color.TRANSPARENT;
//        Color2 = Color.TRANSPARENT;
//    	if(bitmap1st != null){
//    		bitmap1st.recycle();
//    		bitmap1st = null;
//    	}
//    	if(bitmap1stLarge != null){
//    		bitmap1stLarge.recycle();
//    		bitmap1stLarge = null;
//    	}
//    	
//    	if(bitmapsnd != null){
//    		bitmapsnd.recycle();
//    		bitmapsnd = null;
//    	}
//    	if(bitmapsndLarge != null){
//    		bitmapsndLarge.recycle();
//    		bitmapsndLarge = null;
//    	}
    	
    }
}