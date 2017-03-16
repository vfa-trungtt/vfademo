package vfa.vfdemo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.fragments.drawing.DrawObject;


public class BaseDrawView extends FrameLayout {

    public Paint paintDraw;

    private int gridCellSize = 20;

    int colorFill   = Color.BLACK;
    int colorStroke = Color.BLUE;
    int colorBG     = Color.WHITE;

    public float startX;
    public float startY;

    public float endX;
    public float endY;

    Bitmap bitmapDraw;
    DrawObject currentObject;
    List<DrawObject> listDrawing = new ArrayList<>();

    public BaseDrawView(@NonNull Context context) {
        super(context);
        initDrawView();
    }

    public BaseDrawView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDrawView();
    }

    public BaseDrawView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDrawView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(DrawObject obj:listDrawing){
            canvas.drawRect(obj.rectBound,paintDraw);
        }
        drawCurrentObject(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                startDraw();
                break;

            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                postInvalidate();


                break;

            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                endDraw();
                break;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    private void startDraw(){
        currentObject = new DrawObject();
        currentObject.rectBound = new RectF(startX,startY,endX,endY);
        postInvalidate();
    }

    private void endDraw(){
        listDrawing.add(currentObject);
        currentObject = new DrawObject();
        postInvalidate();
    }

    private void initDrawView(){
        setBackgroundColor(colorBG);

        paintDraw = new Paint();
        paintDraw.setAntiAlias(true);
        paintDraw.setColor(colorStroke);
        paintDraw.setStyle(Paint.Style.STROKE);

//        bitmapDraw = Bitmap.(100,100);
        currentObject = new DrawObject();
    }

    public void drawCurrentObject(Canvas canvas){
        if(currentObject == null) return;

        if(endX < startX){
            currentObject.rectBound = new RectF(endX,startY,startX,endY);
        }else if(endY < startY){
            currentObject.rectBound = new RectF(startX,endY,endX,startY);
        }else {
            currentObject.rectBound = new RectF(startX,startY,endX,endY);
        }

        canvas.drawRect(currentObject.rectBound,paintDraw);

//        postInvalidate();

    }

    public void clearDrawing(){
        listDrawing.clear();
        currentObject = null;
        postInvalidate();
    }

    public void drawGrid(){

    }

    public void drawFreeHand(){

    }

    public void drawRectangle(PointF leftTop,PointF righBottom){

    }

    public void drawLine(PointF p1,PointF p2){

    }

    public void drawCircle(PointF center,float radius){

    }

    public void drawSquare(PointF center,float size){

    }

    public void drawTriangle(PointF p1,PointF p2,PointF p3){

    }

    public void drawBitmap(Bitmap bmp,PointF topLeft){

    }

    public void drawText(String text, RectF bound){

    }

    public void saveCanvas(String imagePath){

    }

}
